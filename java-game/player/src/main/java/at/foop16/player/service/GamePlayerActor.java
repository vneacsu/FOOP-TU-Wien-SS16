package at.foop16.player.service;

import akka.actor.ActorRef;
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
        if (msg instanceof GameEvent) {
            ((GameEvent) msg).accept(this);
        } else {
            unhandled(msg);
        }

        // TODO: handle game server failures/removal with watch
    }

    @Override
    public void visitPlayerConnectedEvent(PlayerConnectedEvent event) {
        log.info("Player connected to game server");

        this.gameServer = getSender();

        gameStateListener.onPlayerConnected();
    }

    @Override
    public void visitAwaitNewGameEvent(AwaitNewGameEvent event) {
        gameServer.tell(event, getSelf());
    }

    @Override
    public void visitGameReadyEvent(GameReadyEvent event) {
        log.info("Game ready to start");

        gameStateListener.onGameReady(event.getPlayers());
    }
}