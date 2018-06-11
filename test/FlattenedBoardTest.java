import de.hsa.games.fatsquirrel.core.*;
import de.hsa.games.fatsquirrel.entities.*;
import de.hsa.games.fatsquirrel.util.BoardConfigProvider;
import de.hsa.games.fatsquirrel.util.PropertyBoardConfigProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static javafx.scene.input.KeyCode.M;
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
        collidingEntities.add(new Wall(Entity.ID_AUTO_GENERATE, new XY(6, 1)));
        Entity iterEntity = null;
        int expectedEnergy = startEnergy;
        for (int i = 0; i < collidingEntities.size(); i++) {
            if (i > 0)
                board.deleteEntity(iterEntity);
            iterEntity = collidingEntities.get(i);
            board.insertEntity(iterEntity);
            flattenedBoard.tryMove(ms, XY.RIGHT);
            if (EntityType.fromEntity(iterEntity) == EntityType.WALL) {
                assertEquals(true, ms.isParalyzed(false));
                assertEquals(new XY(5, 1), ms.getPosition());
                continue;
            }
            XY expectedPos = new XY(i + 2, 1);
            expectedEnergy += iterEntity.getEnergy();
            assertEquals(ms.getPosition(), expectedPos);
            assertEquals(ms.getEnergy(), expectedEnergy);

        }
    }

    @Test
    public void testCollisionOfBadBeastWithMS() {
        int startEnergy = 100;
        BadBeast badBeast = new BadBeast(Entity.ID_AUTO_GENERATE, new XY(1, 1));
        MasterSquirrel ms = new HandOperatedMasterSquirrel(Entity.ID_AUTO_GENERATE, startEnergy, new XY(2, 1));
        board.insertEntity(badBeast);
        board.insertEntity(ms);
        flattenedBoard.tryMove(badBeast, XY.RIGHT);
        assertEquals(1, badBeast.getBiteCounter());
        assertEquals(badBeast.getStartEnergy(), badBeast.getEnergy());
        assertEquals(new XY(1, 1), badBeast.getPosition());
        assertEquals(new XY(2, 1), ms.getPosition());
    }

    public void testCollisionOfMiniSquirrel() {
        int startEnergy = 100;


        List<Entity> collidingEntities = new LinkedList<>();
        collidingEntities.add(new BadPlant(Entity.ID_AUTO_GENERATE, new XY(2, 1)));
        collidingEntities.add(new GoodPlant(Entity.ID_AUTO_GENERATE, new XY(3, 1)));
        collidingEntities.add(new GoodBeast(Entity.ID_AUTO_GENERATE, new XY(4, 1)));
        collidingEntities.add(new BadBeast(Entity.ID_AUTO_GENERATE, new XY(5, 1)));
        collidingEntities.add(new Wall(Entity.ID_AUTO_GENERATE, new XY(6, 1)));
        collidingEntities.add(new HandOperatedMasterSquirrel(Entity.ID_AUTO_GENERATE, startEnergy, new XY(7, 1)));
        collidingEntities.add(new HandOperatedMasterSquirrel(Entity.ID_AUTO_GENERATE, startEnergy, new XY(8, 1)));
        MiniSquirrel ms = new MiniSquirrel(Entity.ID_AUTO_GENERATE, new XY(1, 1), (MasterSquirrel) collidingEntities.get(5));
        board.insertEntity(ms);
        Entity iterEntity = null;
        int expectedEnergy = startEnergy;
        for (int i = 0; i < 6; i++) {
            if (i > 0)
                board.deleteEntity(iterEntity);
            iterEntity = collidingEntities.get(i);
            board.insertEntity(iterEntity);
            flattenedBoard.tryMove(ms, XY.RIGHT);
            if (EntityType.fromEntity(iterEntity) == EntityType.WALL) {
                assertEquals(true, ms.isParalyzed(false));
                assertEquals(new XY(5, 1), ms.getPosition());
                continue;
            }
            XY expectedPos = new XY(i + 2, 1);
            expectedEnergy += iterEntity.getEnergy();
            assertEquals(ms.getPosition(), expectedPos);
            assertEquals(ms.getEnergy(), expectedEnergy);
        }
    }
}
