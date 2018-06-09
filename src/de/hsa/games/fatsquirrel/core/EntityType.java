package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.entities.*;
import de.hsa.games.fatsquirrel.util.EntityAnnotation;

public enum EntityType {

    @EntityAnnotation(entity = GoodBeast.class)
    GOOD_BEAST,
    @EntityAnnotation(entity = BadBeast.class)
    BAD_BEAST,
    @EntityAnnotation(entity = GoodPlant.class)
    GOOD_PLANT,
    @EntityAnnotation(entity = BadPlant.class)
    BAD_PLANT,
    @EntityAnnotation(entity = Wall.class)
    WALL,
    @EntityAnnotation(entity = HandOperatedMasterSquirrel.class)
    HAND_OPERATED_MASTER_SQUIRREL,
    @EntityAnnotation(entity = MasterSquirrelBot.class)
    MASTER_SQUIRREL_BOT,
    @EntityAnnotation(entity = MiniSquirrelBot.class)
    MINI_SQUIRREL_BOT,
    @EntityAnnotation(entity = MiniSquirrel.class)
    MINI_SQUIRREL,
    @EntityAnnotation()
    EMPTY_FIELD;

    public static EntityType fromEntity(Entity entity) {
        // Why not to use reflection?
        // fromEntity() is very frequently used -> Perfomance
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
        else if (entity instanceof HandOperatedMasterSquirrel)
            return EntityType.HAND_OPERATED_MASTER_SQUIRREL;
        else if (entity instanceof MasterSquirrelBot)
            return EntityType.MASTER_SQUIRREL_BOT;
        else if (entity instanceof MiniSquirrelBot)
            return EntityType.MINI_SQUIRREL_BOT;
        else if (entity instanceof MiniSquirrel)
            return EntityType.MINI_SQUIRREL;
        else
            return EntityType.EMPTY_FIELD;
    }
}