package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.entities.*;

public enum EntityType {

    GOOD_BEAST,
    BAD_BEAST,
    GOOD_PLANT,
    BAD_PLANT,
    WALL,
    MASTER_SQUIRREL,
    MINI_SQUIRREL,
    MASTER_SQUIRREL_BOT,
    MINI_SQUIRREL_BOT;


    public static EntityType fromEntity(Entity entity) {
        if (entity instanceof GoodBeast)
            return EntityType.GOOD_BEAST;
        else if (entity instanceof BadBeast)
            return EntityType.BAD_BEAST;
        else if (entity instanceof GoodPlant)
            return EntityType.GOOD_PLANT;
        else if (entity instanceof BadPlant)
            return EntityType.BAD_PLANT;
        else if (entity instanceof Wall)
            return EntityType.WALL;
        else if (entity instanceof MasterSquirrel)
            return EntityType.MASTER_SQUIRREL;
        else if (entity instanceof MiniSquirrel)
            return EntityType.MINI_SQUIRREL;
        else if (entity instanceof MasterSquirrelBot)
            return EntityType.MASTER_SQUIRREL_BOT;
        else
            return null;        //is equal to none
    }
}