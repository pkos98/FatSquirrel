package de.hsa.games.fatsquirrel.entities;

import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.util.XYSupport;

public class MiniSquirrel extends PlayerEntity {
    private static int START_ENERGY = 0;
    private MasterSquirrel patron;

    public MiniSquirrel(int id, XY startPos, MasterSquirrel patron) {
        super(id, START_ENERGY, startPos);
        this.patron = patron;
        getPatron();
    }

    @Override
    public int getStartEnergy() {
        return START_ENERGY;
    }

    @Override
    public XY nextStep(EntityContext entityContext) {
        XY oldPos = getPosition();
        entityContext.tryMove(this, XYSupport.getRandomMoveVector());
        // Loses 1 energy point each step
        if (oldPos != getPosition())
            updateEnergy(-1);
        return getPosition();
    }

    public MasterSquirrel getPatron() {
        return patron;
    }
}
