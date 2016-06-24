package at.foop16.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Mouse implements Serializable {

    private final int id;
    private final Position position;
    private final MoveStrategy moveStrategy;

    private static final Random random = new Random();
    private static final List<MoveStrategy> moveStrategies = Arrays.asList(
            pos -> Position.of(pos.getRow() - 1, pos.getCol()),
            pos -> Position.of(pos.getRow() + 1, pos.getCol()),
            pos -> Position.of(pos.getRow(), pos.getCol() - 1),
            pos -> Position.of(pos.getRow(), pos.getCol() + 1)
    );

    private Mouse(int id, Position position, MoveStrategy moveStrategy) {
        this.id = id;
        this.position = position;
        this.moveStrategy = moveStrategy;
    }

    private static MoveStrategy randomMoveStrategy() {
        return moveStrategies.get(random.nextInt(moveStrategies.size()));
    }

    public static Mouse of(int id, Position position) {
        return new Mouse(id, position, randomMoveStrategy());
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public Mouse moveInMaze(Maze maze) {
        MoveStrategy nextMoveStrategy = moveStrategy;
        Position nextPosition = nextMoveStrategy.nextPosition(position);

        while (!maze.isValid(nextPosition)) {
            nextMoveStrategy = randomMoveStrategy();
            nextPosition = nextMoveStrategy.nextPosition(position);
        }

        return new Mouse(id, nextPosition, nextMoveStrategy);
    }

}
