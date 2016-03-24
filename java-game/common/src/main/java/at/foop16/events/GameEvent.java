package at.foop16.events;

import java.io.Serializable;

public interface GameEvent extends Serializable {

    void accept(GameEventVisitor visitor);

}
