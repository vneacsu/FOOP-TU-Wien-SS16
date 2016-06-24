package at.foop16.model;

import java.io.Serializable;

public class Position implements Serializable {

    private final int row;
    private final int col;

    private Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Position of(int row, int col) {
        return new Position(row, col);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
