import de.hsa.games.fatsquirrel.core.XY;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class XYTest {
    XY a = new XY(1, 1);
    XY b = new XY(3, 3);

    @Before
    public void setup() {
        a = new XY(1, 1);
        b = new XY(3, 3);
    }

    @Test
    public void testEquals() {
        assertEquals(a, new XY(1, 1));
        assertEquals(a, a);
        assertEquals(b, new XY(3, 3));
        assertEquals(b, b);

        assertNotEquals(a, b);
    }

    @Test
    public void testDistanceFrom() {
        final XY testDistanceFrom1 = new XY(1, 2);
        final XY testDistanceFrom2 = new XY(6, 3);

        assertEquals(Math.sqrt(26), testDistanceFrom2.distanceFrom(testDistanceFrom1), 0.0);
        assertEquals(0.0, testDistanceFrom1.distanceFrom(testDistanceFrom1), 0.0);
    }

    @Test
    public void testSubtract() {
        XY actualResult = a.reduceVector(b);
        XY expectedResult = new XY(-2, -2);
        assertEquals(actualResult, expectedResult);

        actualResult = b.reduceVector(a);
        expectedResult = new XY(2, 2);
        assertEquals(actualResult, expectedResult);
    }

}
