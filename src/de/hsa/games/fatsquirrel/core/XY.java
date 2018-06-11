package de.hsa.games.fatsquirrel.core;

import java.util.Objects;
import java.util.Random;

public final class XY {

    public static final XY ZERO_ZERO = new XY(0, 0);
    public static final XY RIGHT = new XY(1, 0);
    public static final XY LEFT = new XY(-1, 0);
    public static final XY UP = new XY(0, -1);
    public static final XY DOWN = new XY(0, 1);
    public static final XY RIGHT_UP = new XY(1, -1);
    public static final XY RIGHT_DOWN = new XY(1, 1);
    public static final XY LEFT_UP = new XY(-1, -1);
    public static final XY LEFT_DOWN = new XY(-1, 1);
    private static Random random = new Random();
    private final int x, y;

    public XY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double distanceFrom(XY xy) {
        return Math.sqrt(Math.pow(xy.getX() - x, 2) + Math.pow(xy.getY() - y, 2));
    }

    @Override
    public String toString() {
        return "X: " + x + " | Y:" + y;
    }

    public double convertDistance(XY b) {
        if (b.getX() > b.getY())
            return b.getX();
        else
            return b.getY();
    }


    public XY reduceVector(XY vector) {
        return new XY(x - vector.getX(), y - vector.getY());
    }

    public XY distanceFromXY(XY b) {
        int x = getX() - b.getX();
        int y = getY() - b.getY();
        return new XY(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof XY))
            return false;
        else if (obj == this)
            return true;
        XY xy = (XY) obj;
        return getX() == xy.getX() && getY() == xy.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
