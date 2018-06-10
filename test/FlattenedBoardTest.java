import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.entities.*;
import de.hsa.games.fatsquirrel.util.BoardConfigProvider;
import de.hsa.games.fatsquirrel.util.PropertyBoardConfigProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class FlattenedBoardTest {
    private FlattenedBoard flattenedBoard;
    private Board board;

    @Before
    public void setup() {
        BoardConfigProvider provider = new PropertyBoardConfigProvider("properties/test.properties");
        board = new Board(new BoardConfig(provider));
        flattenedBoard = new FlattenedBoard(board);
    }

    @Test
    public void testCollisionOfMasterSquirrelWithBadPlant() {
        int startEnergy = 100;
        MasterSquirrel ms = new HandOperatedMasterSquirrel(Entity.ID_AUTO_GENERATE, startEnergy, new XY(0, 0));
        BadPlant badPlant = new BadPlant(Entity.ID_AUTO_GENERATE, new XY(1, 0));
        board.insertEntity(ms);
        board.insertEntity(badPlant);
        flattenedBoard.tryMove(ms, XY.RIGHT);
        // Assert the instance respawned somewhere else
        assertNotEquals(badPlant.getPosition(), new XY(1, 0));
        assertEquals(ms.getEnergy(), startEnergy + badPlant.getEnergy());
    }

    @Test
    public void testCollisionOfMasterSquirrelWithGoodPlant() {
        int startEnergy = 100;
        MasterSquirrel ms = new HandOperatedMasterSquirrel(Entity.ID_AUTO_GENERATE, startEnergy, new XY(0, 0));
        GoodPlant goodPlant = new GoodPlant(Entity.ID_AUTO_GENERATE, new XY(1, 0));
        board.insertEntity(ms);
        board.insertEntity(goodPlant);
        flattenedBoard.tryMove(ms, XY.RIGHT);
        // Assert the instance respawned somewhere else
        assertNotEquals(goodPlant.getPosition(), new XY(1, 0));
        assertEquals(ms.getEnergy(), startEnergy + goodPlant.getEnergy());
    }

}
