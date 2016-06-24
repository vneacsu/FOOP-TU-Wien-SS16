package at.foop16.model;

import at.foop16.model.fields.Field;
import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Maze implements Serializable {

    private final Field[][] fields;

    private final int rowCnt;
    private final int colCnt;

    private Maze(Field[][] fields) {
        this.fields = fields;

        rowCnt = fields.length;
        colCnt = fields[0].length;
    }

    public static Maze of(Field[][] fields) {
        return new Maze(fields);
    }

    public Field getField(int row, int col) {
        return fields[row][col];
    }

    public int getRowCnt() {
        return rowCnt;
    }

    public int getColCnt() {
        return colCnt;
    }

    public boolean isValid(Position position) {
        int posRow = position.getRow();
        int posCol = position.getCol();

        return 0 <= posRow && posRow < rowCnt &&
                0 <= posCol && posCol < colCnt &&
                getField(posRow, posCol).canWalkIn();
    }
}
