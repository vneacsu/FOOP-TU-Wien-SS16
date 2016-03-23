package at.foop16.player.service;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GamePlayerActor extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final ActorRef mediator = DistributedPubSub.get(getContext().system()).mediator();

    private Cancellable waitingForPlayers;
    private final UUID uuid;

    public GamePlayerActor() {
        uuid = UUID.randomUUID();
        mediator.tell(new DistributedPubSubMediator.Subscribe("content", getSelf()), getSelf());
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof String) {
            log.info("Got: {}", msg);

            if (msg.equals("JoinGame")) {
                waitingForPlayers = getContext().system().scheduler().schedule(
                        Duration.create(0, TimeUnit.MILLISECONDS),
                        Duration.create(5, TimeUnit.SECONDS),
                        mediator,
                        new DistributedPubSubMediator.Publish("content", uuid + " I'm ready to play"),
                        getContext().system().dispatcher(),
                        getSelf());
            } else if (msg.equals("StartGame")) {
                waitingForPlayers.cancel();
            }

        } else if (msg instanceof DistributedPubSubMediator.SubscribeAck)
            log.info("subscribing");
        else {
            unhandled(msg);
        }

    }
}