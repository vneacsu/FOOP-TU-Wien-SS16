package at.foop16.model;

import at.foop16.model.fields.Field;

import java.io.Serializable;

public class Maze implements Serializable {

    private final Field[][] fields;

    private final int rowCnt;
    private final int colCnt;

    public Maze(Field[][] fields) {
        this.fields = fields;

        rowCnt = fields.length;
        colCnt = fields[0].length;
    }

    public int getRowCnt() {
        return rowCnt;
    }

    public int getColCnt() {
        return colCnt;
    }

    public Field getField(int row, int col) {
        return fields[row][col];
    }
}
