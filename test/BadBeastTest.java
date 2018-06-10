import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.entities.BadBeast;
import de.hsa.games.fatsquirrel.entities.Entity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.any;

public class BadBeastTest {
    private static int xyCounter;
    private BadBeast badBeast;
    private EntityContext context;

    @Before
    public void setup() {
        badBeast = new BadBeast(Entity.ID_AUTO_GENERATE, new XY(0, 0));
        context = Mockito.mock(EntityContext.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                xyCounter++;
                badBeast.setPosition(new XY(xyCounter, xyCounter));
                return null;
            }
        }).when(context).tryMove(eq(badBeast), any(XY.class));
    }

    @Test
    public void testNextStepShouldMoveEvery4thStep() {
        int times = 10;
        for (int i = 0; i < times; i++) {
            XY pos = badBeast.getPosition();
            for (int j = 0; j < 4; j++) {
                badBeast.nextStep(context);
            }
            assertEquals(badBeast.getPosition(), new XY(xyCounter, xyCounter));
        }
    }

    @Test
    public void testBiteIncrementsBiteCounter() {
        int times = 100;
        for (int i = 0; i < times; i++)
            badBeast.bite();
        assertEquals(badBeast.getBiteCounter(), times);
    }

}
