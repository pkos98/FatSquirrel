package games.fatsquirrel.core;

public enum MoveCommand {
    LEFT,
    RIGHT,
    UP,
    DOWN,
    LEFT_UP,
    LEFT_DOWN,
    RIGHT_UP,
    RIGHT_DOWN;

    public XY toXY() {
        switch (this) {

            case LEFT:
                return new XY(-1, 0);
            case RIGHT:
                return new XY(1, 0);
            case UP:
                return new XY(0, 1);
            case DOWN:
                return new XY(0, -1);
            case LEFT_UP:
                return new XY(-1, 1);
            case LEFT_DOWN:
                return new XY(-1, -1);
            case RIGHT_UP:
                return new XY(1, 1);
            case RIGHT_DOWN:
                return new XY(1, -1);
        }
        return null;
    }

}
