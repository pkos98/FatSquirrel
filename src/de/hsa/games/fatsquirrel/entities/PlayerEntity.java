package de.hsa.games.fatsquirrel.entities;

import de.hsa.games.fatsquirrel.core.XY;

import java.util.logging.Logger;

public abstract class PlayerEntity extends Character {

    private static final int START_ENERGY = 0;
    private final int PARALYZED_STEPS = 3;
    private boolean isParalyzed;
    private int paralyzeCounter = 0;
    private Logger logger = Logger.getLogger(getClass().getName());

    public PlayerEntity(int id, int startEnergy, XY startPos) {
        super(id, startEnergy, startPos);
    }

    public void paralyze() {

        isParalyzed = true;
        logger.info(this + " is paralyzed!");
    }

    public int getParalyzeCounter() {
        return paralyzeCounter;
    }

    protected boolean isParalyzed(boolean incrementCounter) {
        if (isParalyzed == false)
            return false;
        if (incrementCounter)
            paralyzeCounter++;
        if (paralyzeCounter == PARALYZED_STEPS) {
            isParalyzed = false;
            paralyzeCounter = 0;
            return false;
        }
        logger.info(this + " is still paralyzed!" + (PARALYZED_STEPS - paralyzeCounter) + " steps left!");
        return true;
    }

}
