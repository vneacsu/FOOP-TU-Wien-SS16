package at.foop16.player;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import at.foop16.player.service.GamePlayerActor;
import com.typesafe.config.ConfigFactory;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("game", ConfigFactory.load("akka.conf"));
        ActorRef player = system.actorOf(Props.create(GamePlayerActor.class), "player");
        player.tell("JoinGame", ActorRef.noSender());

        Thread.sleep(10 * 1000);
        player.tell("StartGame", ActorRef.noSender());

//        ActorRef mediator = DistributedPubSub.get(system).mediator();
//        mediator.tell(new DistributedPubSubMediator.Publish("content", "Hi"), ActorRef.noSender());
    }

}
