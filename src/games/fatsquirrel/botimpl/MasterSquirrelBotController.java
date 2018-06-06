package games.fatsquirrel.botimpl;

import games.fatsquirrel.Launcher;
import games.fatsquirrel.botapi.BotController;
import games.fatsquirrel.botapi.ControllerContext;
import games.fatsquirrel.botapi.SpawnException;
import games.fatsquirrel.core.XY;
import games.fatsquirrel.util.XYSupport;

import java.util.logging.Logger;

import static games.fatsquirrel.botimpl.BotHelper.checkSpawnField;

public class MasterSquirrelBotController implements BotController {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());
    private final BotHelper botHelper;

    private int energyToReachForSpawn = 400;

    public MasterSquirrelBotController(BotHelper botHelper) {
        this.botHelper = botHelper;
    }

    @Override
    public void nextStep(ControllerContext view) {
        XY move = botHelper.moveToNearestGoodEntity(view);
        try {
            if (view.getEnergy() < energyToReachForSpawn) {
                if (!move.equals(new XY(0,0)))
                    view.move(move);
                else
                    view.move(XYSupport.getRandomMoveVector());
            } else {
                XY spawnDirection = XYSupport.getRandomMoveVector();
                if (checkSpawnField(view, XYSupport.add(view.locate(), spawnDirection))) {
                    view.spawnMiniBot(spawnDirection, 100);
                    energyToReachForSpawn += 1000;
                }
            }
        } catch (SpawnException e) {
            logger.warning("Unable to pawn MiniSquirrel (Brain)");
        }
    }

}
