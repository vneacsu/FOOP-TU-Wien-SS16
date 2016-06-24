package at.foop16;

import akka.actor.ActorRef;

public class ActorUtil {

    private ActorUtil() {
    }

    public static int getId(ActorRef item) {
        return item.path().uid();
    }

}
