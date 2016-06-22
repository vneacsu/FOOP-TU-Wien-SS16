package at.foop16.player.service;

import akka.actor.ActorRef;
import at.foop16.model.Maze;

import java.util.List;

public interface GameStateListener {

    void onGameServerDown();

    void onPlayerConnected();

    void onGameReady(Maze maze, List<ActorRef> players);

    void onPlayerLeftGame(ActorRef player);
}
