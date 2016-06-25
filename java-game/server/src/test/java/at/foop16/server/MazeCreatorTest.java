package at.foop16.server;

import at.foop16.model.Maze;
import at.foop16.model.fields.FreeField;
import at.foop16.model.fields.WallField;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class MazeCreatorTest {

    @Test
    public void basic() {
        Maze maze = MazeCreator.INSTANCE.createMaze();

        assertThat(maze.getRowCnt(), is(15));
        assertThat(maze.getColCnt(), is(15));

        assertThat(maze.getField(0, 0), instanceOf(FreeField.class));
        assertThat(maze.getField(0, 6), instanceOf(WallField.class));
    }

}