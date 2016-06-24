package at.foop16.events;

import akka.actor.ActorRef;
import at.foop16.model.Maze;
import at.foop16.model.Mouse;

import java.util.Collections;
import java.util.List;

public class GameReadyEvent implements GameEvent {

    private final List<ActorRef> players;
    private final Maze maze;
    private final List<Mouse> mice;

    public GameReadyEvent(List<ActorRef> players, Maze maze, List<Mouse> mice) {
        this.players = players;
        this.maze = maze;
        this.mice = mice;
    }

    public List<ActorRef> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Maze getMaze() {
        return maze;
    }

    public List<Mouse> getMice() {
        return mice;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.onGameReadyEvent(this);
    }
}
