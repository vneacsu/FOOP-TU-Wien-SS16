import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class Main {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("game", ConfigFactory.load("akka.conf"));
        system.actorOf(Props.create(ObserverActor.class), "observer");
    }

}
