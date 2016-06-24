package at.foop16.model;

import at.foop16.model.fields.Field;
import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Maze implements Serializable {

    private final Field[][] fields;
    private final List<Mouse> mice;

    private final int rowCnt;
    private final int colCnt;

    private Maze(Field[][] fields, List<Mouse> mice) {
        this.fields = fields;
        this.mice = ImmutableList.copyOf(mice);

        rowCnt = fields.length;
        colCnt = fields[0].length;
    }

    public static Maze of(Field[][] fields) {
        return new Maze(fields, Collections.emptyList());
    }

    public Maze withMice(List<Mouse> mice) {
        return new Maze(fields, mice);
    }

    public Field getField(int row, int col) {
        return fields[row][col];
    }

    public List<Mouse> getMice() {
        return mice;
    }

    public int getRowCnt() {
        return rowCnt;
    }

    public int getColCnt() {
        return colCnt;
    }
}
