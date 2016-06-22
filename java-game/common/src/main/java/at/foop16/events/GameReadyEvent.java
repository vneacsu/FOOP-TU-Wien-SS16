package at.foop16.events;

import akka.actor.ActorRef;
import at.foop16.model.Maze;

import java.util.Collections;
import java.util.List;

public class GameReadyEvent implements GameEvent {

    private final List<ActorRef> players;
    private final Maze maze;

    public GameReadyEvent(List<ActorRef> players, Maze maze) {
        this.players = players;
        this.maze = maze;
    }

    public List<ActorRef> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Maze getMaze() {
        return maze;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.onGameReadyEvent(this);
    }
}
