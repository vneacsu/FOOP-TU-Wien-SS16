package at.foop16.model;

import java.io.Serializable;

public interface MoveStrategy extends Serializable {

    Position nextPosition(Position position);

}
