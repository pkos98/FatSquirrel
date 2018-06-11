import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.entities.BadBeast;
import de.hsa.games.fatsquirrel.entities.Entity;
import de.hsa.games.fatsquirrel.entities.GoodBeast;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.any;

public class GoodBeastTest {
    private static int xyCounter;
    private GoodBeast goodBeast;
    private EntityContext context;

    @Before
    public void setup() {
        goodBeast = new GoodBeast(Entity.ID_AUTO_GENERATE, new XY(0, 0));
        context = Mockito.mock(EntityContext.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                xyCounter++;
                goodBeast.setPosition(new XY(xyCounter, xyCounter));
                return null;
            }
        }).when(context).tryMove(eq(goodBeast), any(XY.class));
    }

    @Test
    public void testNextStepShouldMoveEvery4thStep() {
        int times = 10;
        for (int i = 0; i < times; i++) {
            XY pos = goodBeast.getPosition();
            for (int j = 0; j < 4; j++) {
                goodBeast.nextStep(context);
            }
            assertEquals(goodBeast.getPosition(), new XY(xyCounter, xyCounter));
        }
    }

}
