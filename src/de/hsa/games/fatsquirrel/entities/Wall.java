package de.hsa.games.fatsquirrel.entities;

import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;

public class Wall extends Entity {

    private static final int START_ENERGY = -10;

    public Wall(int id, XY startPos) {
        super(id, START_ENERGY, startPos);
    }

    @Override
    public int getStartEnergy() {
        return START_ENERGY;
    }

    public XY nextStep(EntityContext context) {
        return getPosition();
    }

}
