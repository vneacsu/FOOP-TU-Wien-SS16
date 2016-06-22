package at.foop16.events;

public class AwaitNewGameEvent implements GameEvent {
    private final int numPlayers;

    public AwaitNewGameEvent(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.onAwaitNewGameEvent(this);
    }
}
