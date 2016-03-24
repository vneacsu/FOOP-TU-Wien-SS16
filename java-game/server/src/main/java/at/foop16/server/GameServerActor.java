package at.foop16.server;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import at.foop16.events.AwaitNewGameEvent;
import at.foop16.events.GameReadyEvent;
import at.foop16.events.PlayerConnectedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static akka.cluster.ClusterEvent.initialStateAsEvents;

public class GameServerActor extends UntypedActor {

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
        if (msg instanceof MemberUp) {
            handleMemberUp(((MemberUp) msg).member());
        } else if (msg instanceof AwaitNewGameEvent) {
            handleAwaitNewGame((AwaitNewGameEvent) msg);
        } else {
            unhandled(msg);
        }

        // TODO: handle member failures/removal
//        if (message instanceof ClusterEvent.MemberUp) {
//            ClusterEvent.MemberUp mUp = (ClusterEvent.MemberUp) message;
//            log.info("Member is Up: {}", mUp.member());
//
//        } else if (message instanceof ClusterEvent.UnreachableMember) {
//            ClusterEvent.UnreachableMember mUnreachable = (ClusterEvent.UnreachableMember) message;
//            log.info("Member detected as unreachable: {}", mUnreachable.member());
//
//        } else if (message instanceof ClusterEvent.MemberRemoved) {
//            ClusterEvent.MemberRemoved mRemoved = (ClusterEvent.MemberRemoved) message;
//            log.info("Member is Removed: {}", mRemoved.member());
//
//        } else if (message instanceof ClusterEvent.MemberEvent) {
//            // ignore
//            unhandled(message);
//        } else {
//            unhandled(message);
//        }
    }

    private void handleMemberUp(Member member) {
        if (!member.hasRole("game-player")) return;

        context().actorSelection(member.address() + "/user/player")
                .tell(new PlayerConnectedEvent(), getSelf());
    }

    private void handleAwaitNewGame(AwaitNewGameEvent msg) {
        log.info("Player requested new game for {} players", msg.getNumPlayers());

        updateWaitingPlayersMap(msg);

        if (enoughPlayersForGameOf(msg.getNumPlayers())) {
            notifyGameReadyFor(msg.getNumPlayers());
        }
    }

    private void updateWaitingPlayersMap(AwaitNewGameEvent msg) {
        List<ActorRef> players = waitingPlayersMap.getOrDefault(
                msg.getNumPlayers(), new ArrayList<>());

        players.add(getSender());

        waitingPlayersMap.put(msg.getNumPlayers(), players);
    }

    private boolean enoughPlayersForGameOf(int numPlayers) {
        return numPlayers == waitingPlayersMap.get(numPlayers).size();
    }

    private void notifyGameReadyFor(int numPlayers) {
        log.info("Notifying game ready for {} players", numPlayers);

        List<ActorRef> players = waitingPlayersMap.remove(numPlayers);

        GameReadyEvent event = new GameReadyEvent(players);

        players.forEach(it -> it.tell(event, getSelf()));
    }
}
