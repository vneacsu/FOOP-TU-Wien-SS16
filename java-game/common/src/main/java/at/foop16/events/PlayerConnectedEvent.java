package at.foop16.events;

public class PlayerConnectedEvent implements GameEvent {

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.visitPlayerConnectedEvent(this);
    }
}
