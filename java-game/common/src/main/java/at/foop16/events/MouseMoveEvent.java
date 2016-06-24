package at.foop16.events;

public class MouseMoveEvent implements GameEvent {
    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.onMouseMovedEvent(this);
    }
}
