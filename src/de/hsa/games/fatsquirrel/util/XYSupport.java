package de.hsa.games.fatsquirrel.util;

import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.ui.console.GameCommandType;

import java.util.Random;

public class XYSupport {

    private static final Random random = new Random();

    public static XY getRandomMoveVector() {
        int deltaX = -1 + random.nextInt(3);
        int deltaY = -1 + random.nextInt(3);
        return new XY(deltaX, deltaY);
    }

    public static XY getRandomEmptyPosition(int width, int heigth, EntityContext context) {
        XY randomPos = getRandomPosition(width, heigth);
        while (context.getEntityType(randomPos) != null)
            randomPos = getRandomPosition(width, heigth);
        return randomPos;
    }

    public static XY add(XY a, XY b) {
        return new XY(a.getX() + b.getX(), a.getY() + b.getY());
    }

    public static XY convertFromGameCommand(GameCommandType command) {
        switch (command) {

            case LEFT:
                return new XY(-1, 0);
            case UP:
                return new XY(0, 1);
            case DOWN:
                return new XY(0, -1);
            case RIGHT:
                return new XY(1, 0);
            default:
                return new XY(0, 0);
        }
    }

    public static boolean isInRange(XY m, XY lL, XY uR) {
        return (m.getX() >= lL.getX()) && (m.getX() <= uR.getX()) && (m.getY() >= lL.getY()) && (m.getY() <= uR.getY());
    }

    public static boolean isInRange(XY start, XY target, int viewDistance) {
        if (Math.abs(start.getX() - target.getX()) > (viewDistance - 1) / 2) {
            return false;
        } else if (Math.abs(start.getY() - target.getY()) > (viewDistance - 1) / 2) {
            return false;
        }

        return true;
    }

    public static XY decreaseDistance(XY start, XY target) {
        int xDiff = target.getX() - start.getX();
        int yDiff = target.getY() - start.getY();
        int moveX, moveY;

        moveX = Integer.compare(xDiff, 0);
        moveY = Integer.compare(yDiff, 0);

        return new XY(moveX, moveY);
    }

    public static XY assignMoveVector(XY xy) {
        int oldX = xy.getX();
        int oldY = xy.getY();
        if (oldX == 0) {
            if (oldY == 0)
                return XY.ZERO_ZERO;
            else if (oldY < 0) {
                return XY.UP;
            } else {
                return XY.DOWN;
            }
        } else if (oldY == 0) {
            if (oldX < 0) {
                return XY.LEFT;
            } else {
                return XY.RIGHT;
            }
        } else if (oldX < 0) {
            if (oldY < 0) {
                return XY.LEFT_UP;
            } else {
                return XY.LEFT_DOWN;
            }
        } else {
            if (oldY < 0) {
                return XY.RIGHT_UP;
            } else {
                return XY.RIGHT_DOWN;
            }
        }
    }

    private static XY getRandomPosition(int width, int height) {
        Random random = new Random();
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        return new XY(x, y);
    }

}
