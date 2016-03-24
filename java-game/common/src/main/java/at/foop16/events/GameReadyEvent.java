package at.foop16.events;

import akka.actor.ActorRef;

import java.util.Collections;
import java.util.List;

public class GameReadyEvent implements GameEvent {

    private final List<ActorRef> players;

    public GameReadyEvent(List<ActorRef> players) {
        this.players = players;
    }

    public List<ActorRef> getPlayers() {
        return Collections.unmodifiableList(players);
    }
}
