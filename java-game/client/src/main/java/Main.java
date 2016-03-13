import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Config config = ConfigFactory.load("akka.conf")
                .withValue("akka.remote.netty.tcp.port", ConfigValueFactory.fromAnyRef(0));

        ActorSystem system = ActorSystem.create("game", config);
        ActorRef player = system.actorOf(Props.create(PlayerActor.class), "player");
        player.tell("JoinGame", ActorRef.noSender());

        Thread.sleep(10 * 1000);
        player.tell("StartGame", ActorRef.noSender());

//        ActorRef mediator = DistributedPubSub.get(system).mediator();
//        mediator.tell(new DistributedPubSubMediator.Publish("content", "Hi"), ActorRef.noSender());
    }

}
