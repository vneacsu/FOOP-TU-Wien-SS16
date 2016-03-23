package at.foop16.player.service;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import at.foop16.events.AwaitNewGameEvent;
import at.foop16.events.PlayerConnectedEvent;
import com.google.common.base.Preconditions;

public class GamePlayerActor extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef gameServer;

    private GameStateListener gameStateListener;

    public GamePlayerActor(GameStateListener gameStateListener) {
        Preconditions.checkNotNull(gameStateListener);

        this.gameStateListener = gameStateListener;
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof PlayerConnectedEvent) {
            log.info("Player connected to game server");

            this.gameServer = getSender();

            gameStateListener.onPlayerConnected();
        } else if (msg instanceof AwaitNewGameEvent) {
            gameServer.tell(msg, getSelf());
        } else {
            unhandled(msg);
        }

        // TODO: handle game server failures/removal with watch
    }
}