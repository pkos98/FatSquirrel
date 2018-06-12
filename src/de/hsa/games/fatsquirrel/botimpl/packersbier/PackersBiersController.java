package de.hsa.games.fatsquirrel.botimpl.packersbier;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.core.EntityType;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.util.XYSupport;

public class PackersBiersController implements BotController {

    private EntityType type;

    public PackersBiersController(EntityType type) {
        this.type = type;
    }

    @Override
    public void nextStep(ControllerContext context) {

        XY xy = null;

        for (int x = context.getViewLowerLeft().getX(); x <= context.getViewUpperRight().getX(); x++) {
            for (int y = context.getViewUpperRight().getY(); y <= context.getViewLowerLeft().getY(); y++) {
                XY temp = new XY(x, y);
                if (x <= 0 || x >= 40 || y >= 40 || y <= 0)
                    continue;
                EntityType tempType = context.getEntityAt(new XY(x, y));
                if (tempType == EntityType.GOOD_BEAST || tempType == EntityType.GOOD_PLANT) {
                    if (xy == null || context.locate().distanceFrom(xy) > context.locate().distanceFrom(temp)) {
                        xy = temp;
                    }
                }
            }
        }

        if (xy == null) {
            context.move(XYSupport.getRandomMoveVector());
            return;
        }

        context.move(XYSupport.getDirection(context.locate(), xy));
    }

}
