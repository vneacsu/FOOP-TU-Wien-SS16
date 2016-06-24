package at.foop16.model;

public class Mouse {

    private final int id;
    private final int row;
    private final int col;

    private Mouse(int id, int row, int col) {
        this.id = id;
        this.row = row;
        this.col = col;
    }

    public static Mouse of(int id, int row, int col) {
        return new Mouse(id, row, col);
    }

    public Mouse withPosition(int row, int col) {
        return new Mouse(id, row, col);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
