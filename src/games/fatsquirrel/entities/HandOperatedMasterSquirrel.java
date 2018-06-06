package games.fatsquirrel.entities;

import games.fatsquirrel.console.GameCommandType;
import games.fatsquirrel.core.EntityContext;
import games.fatsquirrel.core.XY;
import games.fatsquirrel.util.XYSupport;

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