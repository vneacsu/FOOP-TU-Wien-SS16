package at.foop16.events;

public interface GameEventVisitor {

    default void onAwaitNewGameEvent(AwaitNewGameEvent event) {
        throw new UnsupportedOperationException(
                "Operation not supported! You must either implement " +
                        "it or make sure it is never called!");
    }

    default void onGameReadyEvent(GameReadyEvent event) {
        throw new UnsupportedOperationException(
                "Operation not supported! You must either implement " +
                        "it or make sure it is never called!");
    }

    default void onLeaveActiveGameEvent(LeaveActiveGameEvent event) {
        throw new UnsupportedOperationException(
                "Operation not supported! You must either implement " +
                        "it or make sure it is never called!");
    }

    default void onMouseMovedEvent(MouseMoveEvent event) {
        throw new UnsupportedOperationException(
                "Operation not supported! You must either implement " +
                        "it or make sure it is never called!");
    }
}
