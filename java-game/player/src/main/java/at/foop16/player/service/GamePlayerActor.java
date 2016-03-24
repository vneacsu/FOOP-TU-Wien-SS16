package at.foop16.player.service;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import at.foop16.events.*;
import com.google.common.base.Preconditions;

public class GamePlayerActor extends UntypedActor implements GameEventVisitor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef gameServer;

    private GameStateListener gameStateListener;

    public GamePlayerActor(GameStateListener gameStateListener) {
        Preconditions.checkNotNull(gameStateListener);

        this.gameStateListener = gameStateListener;
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof Terminated) {
            handleTerminated((Terminated) msg);
        } else if (msg instanceof GameEvent) {
            ((GameEvent) msg).accept(this);
        } else {
            unhandled(msg);
        }
    }

    private void handleTerminated(Terminated msg) {
        log.info("Received terminated event for {}", msg.actor());

        if (gameServer.equals(msg.actor())) {
            log.error("Game server is DOWN");

            gameServer = null;
            gameStateListener.onGameServerDown();
        } else {
            log.warning("Player {} is DOWN and left the game", msg.actor());

            gameStateListener.onPlayerLeftGame(msg.actor());
        }

    }

    @Override
    public void visitPlayerConnectedEvent(PlayerConnectedEvent event) {
        log.info("Player connected to game server");

        gameServer = getSender();
        getContext().watch(gameServer);

        gameStateListener.onPlayerConnected();
    }

    @Override
    public void visitAwaitNewGameEvent(AwaitNewGameEvent event) {
        gameServer.tell(event, getSelf());
    }

    @Override
    public void visitGameReadyEvent(GameReadyEvent event) {
        log.info("Game ready to start");

        event.getPlayers().stream()
                .filter(it -> !it.equals(getSelf()))
                .forEach(it -> getContext().watch(it));

        gameStateListener.onGameReady(event.getPlayers());
    }

    @Override
    public void visitLeaveActiveGameEvent(LeaveActiveGameEvent event) {
        log.info("Player {} left the game", getSender());

        gameStateListener.onPlayerLeftGame(getSender());
    }
}