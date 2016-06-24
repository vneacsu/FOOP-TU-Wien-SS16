package at.foop16.model;

public class Mouse {

    private final int id;
    private final Position position;

    private Mouse(int id, Position position) {
        this.id = id;
        this.position = position;
    }

    public static Mouse of(int id, Position position) {
        return new Mouse(id, position);
    }

    public Mouse withPosition(Position position) {
        return new Mouse(id, position);
    }

    public Position getPosition() {
        return position;
    }

}
