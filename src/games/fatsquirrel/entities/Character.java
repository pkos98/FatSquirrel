package games.fatsquirrel.entities;

import games.fatsquirrel.core.EntityContext;
import games.fatsquirrel.core.XY;

public abstract class Character extends Entity {

    public abstract XY nextStep(EntityContext context);

    public Character(int id, int startEnergy, XY startPos) {
        super(id, startEnergy, startPos);
    }

}
