package de.hsa.games.fatsquirrel.entities;

import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.util.XYSupport;

public class GoodBeast extends Character {

    private static final int START_ENERGY = 200;
    private int nextStepCounter = 0;

    public GoodBeast(int id, XY startPos) {
        super(id, START_ENERGY, startPos);
    }

    @Override
    public int getStartEnergy() {
        return START_ENERGY;
    }

    public XY nextStep(EntityContext context) {
        if (++nextStepCounter % 4 != 0) {
            return getPosition();
        } else {
            context.tryMove(this, botApi(this, context));
            return botApi(this, context);
        }
    }

    public XY botApi(GoodBeast goodBeast, EntityContext context) {
        XY botapi;
        for (int IterX = goodBeast.getPosition().getX() - 6; IterX < goodBeast.getPosition().getX() + 6; IterX++) {
            for (int IterY = goodBeast.getPosition().getY() - 6; IterY < goodBeast.getPosition().getY() + 6; IterY++) {
                if (IterX >= 0 && IterY >= 0 && IterX < context.getSize().getX() && IterY < context.getSize().getY()) { //kann ich hier irgendwie die Größe des Spielfeldes abfragen?
                    XY iter = new XY(IterX, IterY);
                    Entity entity = context.getEntity(iter);
                    if (entity instanceof MasterSquirrel || entity instanceof MiniSquirrel) {
                        int x, y;
                        if (goodBeast.getPosition().getX() - entity.getPosition().getX() > 0)
                            x = 1;
                        else if (goodBeast.getPosition().getX() - entity.getPosition().getX() == 0)
                            x = 0;
                        else
                            x = -1;

                        if (goodBeast.getPosition().getY() - entity.getPosition().getY() > 0)
                            y = 1;
                        else if (goodBeast.getPosition().getY() - entity.getPosition().getY() == 0)
                            y = 0;
                        else
                            y = -1;
                        botapi = new XY(x, y);
                        return botapi;
                    } else continue;
                } else continue;
            }
        }
        return XYSupport.getRandomMoveVector();
    }
}