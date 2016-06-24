package at.foop16.player.service;

import akka.actor.ActorRef;
import at.foop16.model.Maze;
import at.foop16.model.Mouse;

import java.util.List;

public interface GameStateListener {

    void onGameServerDown();

    void onPlayerConnected();

    void onGameReady(Maze maze, List<Mouse> mice, List<ActorRef> players);

    void onPlayerLeftGame(ActorRef player);
}
