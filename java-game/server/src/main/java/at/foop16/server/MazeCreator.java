package at.foop16.server;

import at.foop16.model.Maze;
import at.foop16.model.fields.CheeseField;
import at.foop16.model.fields.Field;
import at.foop16.model.fields.FreeField;
import at.foop16.model.fields.WallField;

public class MazeCreator {

    public static MazeCreator INSTANCE = new MazeCreator();

    private static final int MAZE_SIZE = 7;

    private MazeCreator() {
    }

    public Maze createMaze() {
        final String mazePattern =
                "" +
                        "fffwwff" + "\n" +
                        "fffffff" + "\n" +
                        "fffwfff" + "\n" +
                        "fffwfff" + "\n" +
                        "fffcfff" + "\n" +
                        "ffwwwff" + "\n" +
                        "fwwwwff";


        Field[][] fields = new Field[MAZE_SIZE][MAZE_SIZE];

        int rowCnt = 0;
        for (String row : mazePattern.split("\n")) {

            int colCnt = 0;
            for (char charField : row.toCharArray()) {
                Field field = mapCharField(charField);
                fields[rowCnt][colCnt] = field;

                colCnt++;
            }

            rowCnt++;
        }

        return new Maze(fields);
    }

    private Field mapCharField(char charField) {
        switch (charField) {
            case 'f':
                return new FreeField();
            case 'w':
                return new WallField();
            case 'c':
                return new CheeseField();
            default:
                throw new IllegalArgumentException("Unmapped char");
        }
    }
}
