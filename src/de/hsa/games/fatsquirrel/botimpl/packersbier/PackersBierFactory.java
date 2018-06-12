package de.hsa.games.fatsquirrel.botimpl.packersbier;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.botimpl.BotHelper;
import de.hsa.games.fatsquirrel.core.EntityType;

public class PackersBierFactory implements BotControllerFactory {

    @Override
    public BotController createMasterBotController() {
        return new PackersBiersController(EntityType.MASTER_SQUIRREL_BOT);
    }

    @Override
    public BotController createMiniBotController() {
        return new PackersBiersController(EntityType.MINI_SQUIRREL);
    }
}
