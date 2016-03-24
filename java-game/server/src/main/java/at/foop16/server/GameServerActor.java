package at.foop16.server;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import at.foop16.events.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static akka.cluster.ClusterEvent.initialStateAsEvents;

public class GameServerActor extends UntypedActor implements GameEventVisitor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    Cluster cluster = Cluster.get(getContext().system());

    private Map<Integer, List<ActorRef>> waitingPlayersMap = new HashMap<>();

    @Override
    public void preStart() {
        cluster.subscribe(getSelf(), initialStateAsEvents(), ClusterEvent.MemberUp.class);
    }

    @Override
    public void postStop() {
        cluster.unsubscribe(getSelf());
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof Terminated) {
            handleTerminated((Terminated) msg);
        } else if (msg instanceof MemberUp) {
            handleMemberUp(((MemberUp) msg).member());
        } else if (msg instanceof GameEvent) {
            ((GameEvent) msg).accept(this);
        } else {
            unhandled(msg);
        }

        // TODO: handle member failures/removal
    }

    private void handleTerminated(Terminated msg) {
        log.warning("Player {} is DOWN!", msg.actor());

        for (List<ActorRef> players : waitingPlayersMap.values()) {
            if (players.contains(msg.actor())) {
                players.remove(msg.actor());
            }
        }
    }

    private void handleMemberUp(Member member) {
        if (!member.hasRole("game-player")) return;

        context().actorSelection(member.address() + "/user/player")
                .tell(new PlayerConnectedEvent(), getSelf());
    }

    @Override
    public void visitAwaitNewGameEvent(AwaitNewGameEvent event) {
        log.info("Player requested new game for {} players", event.getNumPlayers());

        getContext().watch(getSender());

        updateWaitingPlayersMap(event);

        if (enoughPlayersForGameOf(event.getNumPlayers())) {
            notifyGameReadyFor(event.getNumPlayers());
        }
    }

    private void updateWaitingPlayersMap(AwaitNewGameEvent event) {
        List<ActorRef> players = waitingPlayersMap.getOrDefault(
                event.getNumPlayers(), new ArrayList<>());

        players.add(getSender());

        waitingPlayersMap.put(event.getNumPlayers(), players);
    }

    private boolean enoughPlayersForGameOf(int numPlayers) {
        return numPlayers == waitingPlayersMap.get(numPlayers).size();
    }

    private void notifyGameReadyFor(int numPlayers) {
        log.info("Notifying game ready for {} players", numPlayers);

        List<ActorRef> players = waitingPlayersMap.remove(numPlayers);

        GameReadyEvent event = new GameReadyEvent(players);

        players.forEach(it -> {
            getContext().unwatch(it);

            it.tell(event, getSelf());
        });
    }
}
