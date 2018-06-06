package games.fatsquirrel.core;

import games.fatsquirrel.entities.*;

public interface EntityContext {

    XY getSize();

    void tryMove(MiniSquirrel miniSquirrel, XY moveDirection);

    void tryMove(GoodBeast goodBeast, XY moveDirection);

    void tryMove(BadBeast badBeast, XY moveDirection);

    void tryMove(MasterSquirrel masterSquirrel, XY moveDirection);

    PlayerEntity nearestPlayerEntity(XY pos);

    void kill(Entity entity);

    void killAndReplace(Entity entity);

    EntityType getEntityType(XY xy);

    void spawnMiniSquirrel();

    void implode(MiniSquirrel miniSquirrel, int impactRadius);

    public Entity getEntity(XY xy);

    XY getAwayfromPosition(Entity entity, XY position);

    XY getToPosition(Entity entity, XY position);

}

