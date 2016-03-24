package at.foop16.events;

public interface GameEventVisitor {

    default void visitPlayerConnectedEvent(PlayerConnectedEvent event) {
        throw new UnsupportedOperationException(
                "Operation not supported! You must either implement " +
                        "it or make sure it is never called!");
    }

    default void visitAwaitNewGameEvent(AwaitNewGameEvent event) {
        throw new UnsupportedOperationException(
                "Operation not supported! You must either implement " +
                        "it or make sure it is never called!");
    }

    default void visitGameReadyEvent(GameReadyEvent event) {
        throw new UnsupportedOperationException(
                "Operation not supported! You must either implement " +
                        "it or make sure it is never called!");
    }

}
