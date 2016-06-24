package at.foop16.events;

import at.foop16.model.Mouse;

public class MouseMoveEvent implements GameEvent {

    private final Mouse mouse;

    public MouseMoveEvent(Mouse mouse) {
        this.mouse = mouse;
    }

    public Mouse getMouse() {
        return mouse;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.onMouseMovedEvent(this);
    }
}
