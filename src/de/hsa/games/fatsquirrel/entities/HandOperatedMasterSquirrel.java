package de.hsa.games.fatsquirrel.entities;

import de.hsa.games.fatsquirrel.ui.console.GameCommandType;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.util.XYSupport;

public class HandOperatedMasterSquirrel extends MasterSquirrel {

    private GameCommandType input;
	public HandOperatedMasterSquirrel(int id, int startEnergy,XY startPos) {
		super(id, startEnergy, startPos);
	}
	
	@Override
	public String toString() {
	    return "HandOperatedMasterSquirrel";
	}

    public void setInput(GameCommandType input) {
        this.input = input;
    }

    public GameCommandType getInput() {
        return input;
    }

    @Override
    public XY nextStep(EntityContext entityContext) {
        if (input == null)
            return getPosition();

        if (isParalyzed(true))
            return getPosition();
        entityContext.tryMove(this, XYSupport.convertFromGameCommand(input));
        return getPosition();
    }

}