package at.foop16.server;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import at.foop16.events.AwaitNewGameEvent;
import at.foop16.events.PlayerConnectedEvent;

import static akka.cluster.ClusterEvent.initialStateAsEvents;

public class GameServerActor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    Cluster cluster = Cluster.get(getContext().system());

    @Override
    public void preStart() {
        cluster.subscribe(getSelf(), initialStateAsEvents(), ClusterEvent.MemberEvent.class);
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
            log.info("Player requested new game for {} players", ((AwaitNewGameEvent) msg).getNumPlayers());
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
}
