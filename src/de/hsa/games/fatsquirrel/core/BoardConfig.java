package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.util.BoardConfigProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * The configuration of the board
 */
public class BoardConfig {

    private XY size;
    private int wallCount;
    private int playerMode;
    private int gameDuration;
    private int gameRounds;
    private int waitingTimeBeast;
    private int fps;
    private String mainBotPath, secondaryBotPath;
    private Map<EntityType, Integer> amountByEntityType;
    private int stepCounter;

    /**
     * Create a new BoardConfig instance
     */
    public BoardConfig(BoardConfigProvider configProvider) {
        amountByEntityType = configProvider.getEntityDistribution();
        playerMode = configProvider.getSettingAsInt("playerMode");
        fps = configProvider.getSettingAsInt("fps");
        gameDuration = configProvider.getSettingAsInt("gameDuration");
        gameRounds = configProvider.getSettingAsInt("gameRounds");
        waitingTimeBeast = configProvider.getSettingAsInt("waitingTimeBeast");
        mainBotPath = configProvider.getSetting("mainBotPath");
        secondaryBotPath = configProvider.getSetting("secondaryBotPath");
        stepCounter = configProvider.getSettingAsInt("stepCounter");
        int width = configProvider.getSettingAsInt("width");
        int height = configProvider.getSettingAsInt("height");
        size = new XY(width, height);
    }

    public int getFps() {
        return fps;
    }

    public int getGameDuration() {
        return gameDuration;
    }

    public int getGameRounds() {
        return gameRounds;
    }

    public int getWaitingTimeBeast() {
        return waitingTimeBeast;
    }

    public String getMainBotPath() {
        return mainBotPath;
    }

    public String getSecondaryBotPath() {
        return secondaryBotPath;
    }

    public int getPlayerMode() {
        return playerMode;
    }

    public int getStepCounter() {
        return stepCounter;
    }

    /**
     * Get a Map containing the amount of entities to spawn by its EntityType
     */
    public Map<EntityType, Integer> getAmountByEntityType() {
        return amountByEntityType;
    }

    public XY getSize() {
        return size;
    }

    public int getWallCount() {
        return wallCount;
    }

}
