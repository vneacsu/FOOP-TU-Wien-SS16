package at.foop16.events;

public class LeaveActiveGameEvent implements GameEvent {

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.visitLeaveActiveGameEvent(this);
    }
}
