package at.foop16.server;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import at.foop16.events.AwaitNewGameEvent;
import at.foop16.events.GameEvent;
import at.foop16.events.GameEventVisitor;
import at.foop16.events.GameReadyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServerActor extends UntypedActor implements GameEventVisitor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private Map<Integer, List<ActorRef>> waitingPlayersMap = new HashMap<>();

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof Terminated) {
            handleTerminated((Terminated) msg);
        } else if (msg instanceof GameEvent) {
            ((GameEvent) msg).accept(this);
        } else {
            unhandled(msg);
        }
    }

    private void handleTerminated(Terminated msg) {
        log.warning("Player {} is DOWN!", msg.actor());

        waitingPlayersMap.values().stream()
                .filter(players -> players.contains(msg.actor()))
                .forEach(players -> players.remove(msg.actor()));
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
