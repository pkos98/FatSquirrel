import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.entities.Entity;
import de.hsa.games.fatsquirrel.entities.Wall;
import de.hsa.games.fatsquirrel.util.BoardConfigProvider;
import de.hsa.games.fatsquirrel.util.PropertyBoardConfigProvider;
import javafx.collections.ListChangeListener;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoardTest {
    int initialWalls;
    private Board board;
    private Wall[] walls = new Wall[3];
    private int callbackCounter;

    @Before
    public void setup() {
        BoardConfigProvider propertyConfigProvider = new PropertyBoardConfigProvider("properties/test.properties");
        BoardConfig config = new BoardConfig(propertyConfigProvider);
        board = new Board(config);
        initialWalls = board.getHeight() * 2 + board.getWidth() * 2 - 1;
        walls[0] = new Wall(Entity.ID_AUTO_GENERATE, new XY(1, 1));
        walls[1] = new Wall(Entity.ID_AUTO_GENERATE, new XY(1, 2));
        walls[2] = new Wall(Entity.ID_AUTO_GENERATE, new XY(1, 3));
        board.getObservableList().addListener(new ListChangeListener<Entity>() {
            @Override
            public void onChanged(Change<? extends Entity> c) {
                while (c.next()) {
                    if (c.wasAdded())
                        callbackCounter++;
                    else if (c.wasRemoved())
                        callbackCounter++;
                }
            }
        });
        board.insertEntity(walls[0]);
        board.insertEntity(walls[1]);
        board.insertEntity(walls[2]);
    }

    @Test
    public void testInsertEntity() {
        List<Entity> actualList = board.getEntities();
        assertTrue(actualList.containsAll(Arrays.asList(walls)));
        assertEquals(walls.length + initialWalls, actualList.size());
        assertEquals(callbackCounter, walls.length);
    }

    @Test
    public void testDeleteEntity() {
        for (int i = 0; i < walls.length; i++)
            board.deleteEntity(walls[i]);
        assertEquals(initialWalls, board.getEntities().size());
        assertEquals(walls.length * 2, callbackCounter);
    }

}
