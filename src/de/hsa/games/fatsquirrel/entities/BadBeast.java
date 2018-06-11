package de.hsa.games.fatsquirrel.entities;

import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.util.XYSupport;

public class BadBeast extends Character {

    private static final int START_ENERGY = -150;
    private final int viewDistance = 6;
    private int biteCounter = 0;
    private int nextStepCounter = 0;
    public static final int MAX_BITES = 6;

    public BadBeast(int id, XY startPos) {
        super(id, START_ENERGY, startPos);
    }

    @Override
    public int getStartEnergy() {
        return START_ENERGY;
    }

    public int getBiteCounter() {
        return biteCounter;
    }

    public void bite() {
        biteCounter++;
    }

    @Override
    public XY nextStep(EntityContext context) {
        if (++nextStepCounter % 4 != 0) {
            return getPosition();
        } else {
            context.tryMove(this, XYSupport.getRandomMoveVector());
            return getPosition();//botApi(this, context);
        }
    }

    public XY botApi(BadBeast badBeast, EntityContext context) {
      /*  XY botapi;
        for (int IterX = badBeast.getPosition().getX() - viewDistance; IterX < badBeast.getPosition().getX() + viewDistance; IterX++) {
            for (int IterY = badBeast.getPosition().getY() - viewDistance; IterY < badBeast.getPosition().getY() + viewDistance; IterY++) {
                if (IterX >= 0 && IterY >= 0 && IterX < 40 && IterY < 40) {
                    XY iter = new XY(IterX, IterY);
                    Entity entity = context.getEntity(iter);
                    if (entity instanceof MasterSquirrel || entity instanceof MiniSquirrel) {
                        int x, y;
                        if (badBeast.getPosition().getX() - entity.getPosition().getX() > 0)
                            x = -1;
                        else if (badBeast.getPosition().getX() - entity.getPosition().getX() == 0)
                            x = 0;
                        else
                            x = 1;

                        if (badBeast.getPosition().getY() - entity.getPosition().getY() > 0)
                            y = -1;
                        else if (badBeast.getPosition().getY() - entity.getPosition().getY() == 0)
                            y = 0;
                        else
                            y = 1;
                        botapi = new XY(x, y);
                        return botapi;
                    } else continue;
                } else continue;
            }
        }
        context.tryMove(this, XYSupport.getRandomMoveVector());
        return XYSupport.getRandomMoveVector();*/
        return null;
    }
}