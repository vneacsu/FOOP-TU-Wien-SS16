package at.foop16.player.service;

public interface GameStateListener {

    default void onPlayerConnected() {
    }
}
