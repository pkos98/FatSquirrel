package de.hsa.games.fatsquirrel.botapi;
import de.hsa.games.fatsquirrel.core.XY;

public interface BotControllerFactory {
    BotController createMasterBotController();
    BotController createMiniBotController();
}