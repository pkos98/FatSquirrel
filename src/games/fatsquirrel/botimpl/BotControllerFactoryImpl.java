package games.fatsquirrel.botimpl;

import games.fatsquirrel.botapi.BotController;
import games.fatsquirrel.botapi.BotControllerFactory;

public class BotControllerFactoryImpl implements BotControllerFactory {
    @Override
    public BotController createMasterBotController() {
        return new MasterSquirrelBotController(new BotHelper());
    }

    @Override
    public BotController createMiniBotController() {
        return new MiniSquirrelBotController(new BotHelper());
    }
}
