package games.fatsquirrel.entities;

import games.fatsquirrel.core.XY;

public class BadPlant extends Entity {

    private static final int START_ENERGY = -100;

    public BadPlant(int id, XY startPos) {
        super(id, START_ENERGY, startPos);
    }

    @Override
    public int getStartEnergy() {
        return START_ENERGY;
    }

}
