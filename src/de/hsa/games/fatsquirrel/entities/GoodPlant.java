package de.hsa.games.fatsquirrel.entities;

import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;

public class GoodPlant extends Character {

    public static final int START_ENERGY = 100;

    public GoodPlant(int id, XY startPos) {
        super(id, START_ENERGY, startPos);
    }

    @Override
    public XY nextStep(EntityContext entityContext) {
        return getPosition();
    }

    @Override
    public int getStartEnergy() {
        return START_ENERGY;
    }
}
