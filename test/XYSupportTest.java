import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.util.XYSupport;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class XYSupportTest {

    @Test
    public void testGetRandomMoveVectorShouldDistributeEvenly() {
        int tries = 1000;
        int treshold = 10;
        Map<XY, Integer> occurrenceByDirection = new HashMap<>();
        occurrenceByDirection.put(XY.DOWN, new Integer(0));
        occurrenceByDirection.put(XY.UP, new Integer(0));
        occurrenceByDirection.put(XY.RIGHT, new Integer(0));
        occurrenceByDirection.put(XY.LEFT, new Integer(0));
        occurrenceByDirection.put(XY.LEFT_DOWN, new Integer(0));
        occurrenceByDirection.put(XY.LEFT_UP, new Integer(0));
        occurrenceByDirection.put(XY.RIGHT_UP, new Integer(0));
        occurrenceByDirection.put(XY.RIGHT_DOWN, new Integer(0));
        occurrenceByDirection.put(new XY(0, 0), new Integer(0));
        for (int i = 0; i < tries; i++) {
            XY randomVector = XYSupport.getRandomMoveVector();
            int occurrence = occurrenceByDirection.get(randomVector);
            occurrenceByDirection.put(randomVector, new Integer(++occurrence));
        }

        for (XY key : occurrenceByDirection.keySet()) {
            if (occurrenceByDirection.get(key) < treshold)
                TestCase.fail();
        }
    }

}
