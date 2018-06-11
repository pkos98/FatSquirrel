import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.entities.*;
import de.hsa.games.fatsquirrel.util.BoardConfigProvider;
import de.hsa.games.fatsquirrel.util.PropertyBoardConfigProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


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
    public void testCollisionsOfMasterSquirrel() {
        int startEnergy = 100;
        MasterSquirrel ms = new HandOperatedMasterSquirrel(Entity.ID_AUTO_GENERATE, startEnergy, new XY(1, 1));
        board.insertEntity(ms);
        List<Entity> collidingEntities = new LinkedList<>();
        collidingEntities.add(new BadPlant(Entity.ID_AUTO_GENERATE, new XY(2, 1)));
        collidingEntities.add(new GoodPlant(Entity.ID_AUTO_GENERATE, new XY(3, 1)));
        collidingEntities.add(new GoodBeast(Entity.ID_AUTO_GENERATE, new XY(4, 1)));
        collidingEntities.add(new BadBeast(Entity.ID_AUTO_GENERATE, new XY(5, 1)));
        Entity iterEntity = null;
        int expectedEnergy = startEnergy;
        for (int i = 0; i < collidingEntities.size(); i++) {
            if (i > 0)
                board.deleteEntity(iterEntity);
            iterEntity = collidingEntities.get(i);
            board.insertEntity(iterEntity);
            flattenedBoard.tryMove(ms, XY.RIGHT);
            XY expectedPos = new XY(i + 2, 1);
            expectedEnergy += iterEntity.getEnergy();
            assertEquals(ms.getPosition(), expectedPos);
            assertEquals(ms.getEnergy(), expectedEnergy);
        }
    }

}
