package de.hsa.games.fatsquirrel.core;

import java.util.HashMap;
import java.util.Map;

/**
 * The configuration of the board
 */
public class BoardConfig {

    private XY size;
    private int wallCount;
    private Map<EntityType, Integer> amountByEntityType;

    /**
     * Create a new BoardConfig instance
     * @param size The board`s size where x=width and y=height
     * @param wallCount The amount of randomly distributed Walls
     */
    public BoardConfig(XY size, int wallCount) {
        this.size = size;
        this.wallCount = wallCount;
        amountByEntityType = new HashMap<>();
        amountByEntityType.put(EntityType.WALL, 10);
        amountByEntityType.put(EntityType.BAD_PLANT, 1);
        amountByEntityType.put(EntityType.BAD_BEAST, 1);
        amountByEntityType.put(EntityType.GOOD_PLANT, 1);
        amountByEntityType.put(EntityType.GOOD_BEAST, 1);
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
