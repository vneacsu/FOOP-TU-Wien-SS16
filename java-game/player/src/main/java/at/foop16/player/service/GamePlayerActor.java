package at.foop16.player.service;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import at.foop16.events.*;
import com.google.common.base.Preconditions;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static akka.cluster.ClusterEvent.initialStateAsEvents;

public class GamePlayerActor extends UntypedActor implements GameEventVisitor {

    private static final FiniteDuration GAME_SERVER_TIMEOUT = FiniteDuration.apply(10, TimeUnit.SECONDS);

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final Cluster cluster = Cluster.get(getContext().system());
    private final GameStateListener gameStateListener;

    @Nullable
    private ActorRef gameServer = null;

    public GamePlayerActor(GameStateListener gameStateListener) {
        Preconditions.checkNotNull(gameStateListener);

        this.gameStateListener = gameStateListener;
    }

    @Override
    public void preStart() {
        cluster.subscribe(getSelf(), initialStateAsEvents(), MemberUp.class);
    }

    @Override
    public void postStop() {
        cluster.unsubscribe(getSelf());
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof Terminated) {
            handleTerminated((Terminated) msg);
        } else if (msg instanceof MemberUp) {
            handleMemberUp(((MemberUp) msg).member());
        } else if (msg instanceof GameEvent) {
            ((GameEvent) msg).accept(this);
        } else {
            unhandled(msg);
        }
    }

    private void handleTerminated(Terminated msg) {
        log.info("Received terminated event for {}", msg.actor());

        if (gameServer != null && gameServer.equals(msg.actor())) {
            log.error("Game server is DOWN");

            gameServer = null;
            gameStateListener.onGameServerDown();
        } else {
            log.warning("Player {} is DOWN and left the game", msg.actor());
            gameStateListener.onPlayerLeftGame(msg.actor());
        }
    }

    private void handleMemberUp(Member member) {
        if (!member.hasRole("game-server")) return;

        log.info("Found game server actor at address {}", member.address());

        Future<ActorRef> actorRefFuture = context()
                .actorSelection(member.address() + "/user/game-server")
                .resolveOne(GAME_SERVER_TIMEOUT);

        tryConnectToGameServer(actorRefFuture);
    }

    private void tryConnectToGameServer(Future<ActorRef> actorRefFuture) {
        try {
            gameServer = Await.result(actorRefFuture, GAME_SERVER_TIMEOUT);
            log.info("Player connected to game server");

            context().watch(gameServer);
            gameStateListener.onPlayerConnected();

        } catch (Exception e) {
            log.error("Player failed to connect to game server: {}", e);
            gameStateListener.onGameServerDown();
        }
    }

    @Override
    public void onAwaitNewGameEvent(AwaitNewGameEvent event) {
        Preconditions.checkNotNull(gameServer, "Game server is DOWN");
        gameServer.tell(event, getSelf());
    }

    @Override
    public void onGameReadyEvent(GameReadyEvent event) {
        log.info("Game ready to start");

        event.getPlayers().stream()
                .filter(it -> !it.equals(getSelf()))
                .forEach(it -> getContext().watch(it));

        gameStateListener.onGameReady(event.getPlayers());
    }

    @Override
    public void onLeaveActiveGameEvent(LeaveActiveGameEvent event) {
        log.info("Player {} left the game", getSender());

        gameStateListener.onPlayerLeftGame(getSender());
    }
}