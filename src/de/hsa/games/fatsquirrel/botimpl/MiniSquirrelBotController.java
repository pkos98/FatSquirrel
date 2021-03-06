package de.hsa.games.fatsquirrel.botimpl;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.core.EntityType;
import de.hsa.games.fatsquirrel.core.XY;

import java.util.logging.Logger;

import static de.hsa.games.fatsquirrel.botimpl.BotHelper.moveToNearestGoodEntity;

public class MiniSquirrelBotController implements BotController {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final BotHelper botHelper;

    public MiniSquirrelBotController(BotHelper botHelper) {
        this.botHelper = botHelper;
    }

    @Override
    public void nextStep(ControllerContext view) {
        int impactRadius = 5;
        boolean shouldImplode = implodeCondition(view, impactRadius);

        if (shouldImplode) {
            logger.info("MINI_SQUIRREL_BOT implode!");
            view.implode(impactRadius);
        }

        XY move = moveToNearestGoodEntity(view);
        if (view.getEnergy() < 300) {
            view.move(move);
        } else {
            move = view.directionOfMaster();
            view.move(move);
        }
    }

    private boolean implodeCondition(ControllerContext view, int impactRadius) {
        int counterToImplode = 0;
        boolean shouldImpode = false;

        int startX = view.locate().getX() - (impactRadius - 1) / 2;
        int startY = view.locate().getY() - (impactRadius - 1) / 2;
        int stopX = view.locate().getX() + (impactRadius - 1) / 2;
        int stopY = view.locate().getY() + (impactRadius - 1) / 2;

        // Begrenzung setzen
        if (startX < 0)
            startX = 0;
        if (startY < 0)
            startY = 0;
        if (stopX > view.getViewUpperRight().getX())
            stopX = view.getViewUpperRight().getX();
        if (stopY > view.getViewUpperRight().getY())
            stopY = view.getViewUpperRight().getY();

        for (int x = startX; x < stopX; x++) {
            for (int y = startY; y < stopY; y++) {
                try {
                    EntityType checkEntity = view.getEntityAt(new XY(x, y));
                    if (checkEntity == EntityType.GOOD_BEAST || checkEntity == EntityType.GOOD_PLANT) {
                        counterToImplode++;
                    }
                } catch (OutOfViewException e) {
                    logger.finer("No Entity in the searchVector");
                }
            }
        }
        if (counterToImplode >= 1)
            shouldImpode = true;
        return shouldImpode;
    }

}