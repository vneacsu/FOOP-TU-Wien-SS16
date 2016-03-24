package at.foop16.player.service;

import akka.actor.ActorRef;

import java.util.List;

public interface GameStateListener {

    void onGameServerDown();

    void onPlayerConnected();

    void onGameReady(List<ActorRef> players);

    void onPlayerLeftGame(ActorRef player);
}
