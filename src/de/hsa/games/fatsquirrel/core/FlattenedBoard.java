package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.entities.*;
import de.hsa.games.fatsquirrel.util.XYSupport;

import java.util.logging.Logger;

public class FlattenedBoard implements EntityContext, BoardView {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Board board;
    private Entity[][] cells;
    private MasterSquirrel masterSquirrelCache = null;

    public FlattenedBoard(Board board) {
        this.board = board;
        cells = board.getEntities();
        createEntities();
    }

    @Override
    public Entity getEntity(XY xy) {
        return cells[xy.getX()][xy.getY()];
    }

    public Entity getEntity(int x, int y) {
        return getEntity(new XY(x, y));
    }

    @Override
    public EntityType getEntityType(XY xy) {
        return EntityType.fromEntity(getEntity(xy));
    }

    @Override
    public EntityType getEntityType(int x, int y) {
        return getEntityType(x, y);
    }

    @Override
    public XY getSize() {
        return board.getSize();
    }

    @Override
    public void tryMove(MiniSquirrel miniSquirrel, XY moveDirection) {

        XY nextPosition = new XY(miniSquirrel.getPosition().getX() + moveDirection.getX(),
                miniSquirrel.getPosition().getY() + moveDirection.getY());
        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];
        EntityType type = EntityType.fromEntity(nextEntity);
        switch (type) {

            case GOOD_BEAST:
            case BAD_BEAST:
                miniSquirrel.updateEnergy(nextEntity.getEnergy());
                killAndReplace(nextEntity);
                break;
            case GOOD_PLANT:
            case BAD_PLANT:
                miniSquirrel.updateEnergy(nextEntity.getEnergy());
                killAndReplace(nextEntity);
                break;
            case HAND_OPERATED_MASTER_SQUIRREL:
            case MASTER_SQUIRREL_BOT:
                if (!((MasterSquirrel) nextEntity).isPatronOf(miniSquirrel))
                    kill(miniSquirrel);
                break;
            case MINI_SQUIRREL:
            case MINI_SQUIRREL_BOT:
                if (miniSquirrel.getPatron() != ((MiniSquirrel) nextEntity).getPatron())
                    kill(miniSquirrel);
                break;
            case WALL:
                miniSquirrel.updateEnergy(nextEntity.getEnergy());
                miniSquirrel.paralyze();
                return;
            case EMPTY_FIELD:
                break;
        }
        move(miniSquirrel, nextPosition);
    }

    @Override
    public void tryMove(GoodBeast goodBeast, XY moveDirection) {
        XY nextPosition = new XY(goodBeast.getPosition().getX() + moveDirection.getX(),
                goodBeast.getPosition().getY() + moveDirection.getY());
        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];
        EntityType type = EntityType.fromEntity(nextEntity);
        switch (type) {
            case HAND_OPERATED_MASTER_SQUIRREL:
            case MASTER_SQUIRREL_BOT:
            case MINI_SQUIRREL:
            case MINI_SQUIRREL_BOT:
                // TODO: GoodBeast <-> BadBeast || (Bad|Good)Plant
                nextEntity.updateEnergy(goodBeast.getEnergy());
                killAndReplace(goodBeast);
                break;
            case EMPTY_FIELD:
                move(goodBeast, nextPosition);
        }
    }

    @Override
    public void tryMove(BadBeast badBeast, XY moveDirection) {
        XY nextPosition = new XY(badBeast.getPosition().getX() + moveDirection.getX(),
                badBeast.getPosition().getY() + moveDirection.getY());
        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];
        EntityType type = EntityType.fromEntity(nextEntity);
        switch (type) {
            case MINI_SQUIRREL:
            case MINI_SQUIRREL_BOT:
            case HAND_OPERATED_MASTER_SQUIRREL:
            case MASTER_SQUIRREL_BOT:
                nextEntity.updateEnergy(badBeast.getEnergy());
                killAndReplace(badBeast);
                break;
            case EMPTY_FIELD:
                move(badBeast, nextPosition);
        }
    }

    @Override
    public void tryMove(MasterSquirrel masterSquirrel, XY moveDirection) {
        XY nextPosition = XYSupport.add(masterSquirrel.getPosition(), moveDirection);
        if (nextPosition.getX() >= board.getBoardConfig().getSize().getX() || nextPosition.getX() < 0)
            return;
        else if (nextPosition.getY() >= board.getBoardConfig().getSize().getY() || nextPosition.getY() < 0)
            return;

        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];
        EntityType type = EntityType.fromEntity(nextEntity);
        switch (type) {
            case WALL:
                masterSquirrel.updateEnergy(nextEntity.getEnergy());
                masterSquirrel.paralyze();
                return;
            case MASTER_SQUIRREL_BOT:
            case HAND_OPERATED_MASTER_SQUIRREL:
                return;
            case EMPTY_FIELD:
                break;
            default:
                masterSquirrel.updateEnergy(nextEntity.getEnergy());
                killAndReplace(nextEntity);
        }
        move(masterSquirrel, nextPosition);
    }

    @Override
    public PlayerEntity nearestPlayerEntity(XY pos) {
        PlayerEntity nearestEntity = null;
        XY nearestEntityDistance = null;
        for (int x = 0; x < board.getSize().getX(); x++) {
            for (int y = 0; y < board.getSize().getY(); y++) {
                Entity currentCell = getEntity(x, y);
                EntityType type = EntityType.fromEntity(currentCell);
                switch (type) {
                    case HAND_OPERATED_MASTER_SQUIRREL:
                    case MASTER_SQUIRREL_BOT:
                        continue;
                }
                if (nearestEntity == null) {
                    nearestEntity = (PlayerEntity) currentCell;
                    nearestEntityDistance = pos.distanceFromXY(nearestEntity.getPosition());
                    continue;
                }

                PlayerEntity currentPlayerEntity = (PlayerEntity) currentCell;
                XY currentPlayerDistance = pos.distanceFromXY(currentPlayerEntity.getPosition());
                int currentPlayerDistanceSum = Math.abs(currentPlayerDistance.getX()) + Math.abs(currentPlayerDistance.getY());
                int nearestEntityDistanceSum = Math.abs(nearestEntityDistance.getX()) + Math.abs(nearestEntityDistance.getY());
                if (currentPlayerDistanceSum < nearestEntityDistanceSum) {
                    nearestEntity = currentPlayerEntity;
                    nearestEntityDistance = currentPlayerDistance;
                }
            }
        }
        return nearestEntity;
    }

    @Override
    public void kill(Entity entity) {
        board.deleteEntity((entity));
    }

    @Override
    public void killAndReplace(Entity entity) {
        // Reset energy to default level
        entity.updateEnergy(entity.getStartEnergy() - entity.getEnergy());
        XY randomPos = XYSupport.getRandomEmptyPosition(board.getSize().getX(), board.getSize().getY(), this);
        move(entity, randomPos);
    }

    @Override
    public void spawnMiniSquirrel() {
        // 1. HAND_OPERATED_MASTER_SQUIRREL finden
        logger.info("MINI_SQUIRREL spawned");
        MasterSquirrel masterSquirrel = findMasterSquirrel();
        XY randomPos = XYSupport.getRandomEmptyPosition(board.getSize().getX(), board.getSize().getY(), this);
        MiniSquirrel miniSquirrel = new MiniSquirrel(42, randomPos, masterSquirrel);
        masterSquirrel.addMiniSquirrel(miniSquirrel);

    }

    @Override
    public void implode(MiniSquirrel miniSquirrel, int impactRadius) {
        XY location = miniSquirrel.getPosition();
        for (int iterX = location.getX() - impactRadius; iterX <= location.getX() + impactRadius; iterX++) {
            innerLoop:
            for (int iterY = location.getX() - impactRadius; iterY <= location.getX() + impactRadius; iterY++) {
                Entity entity = board.getEntity(new XY(iterX, iterX));
                EntityType type = EntityType.fromEntity(entity);
                XY XYdistance = new XY(iterX, iterX);
                double distance = XYdistance.convertDistance(XYdistance.distanceFromXY(entity.getPosition()));
                double impactArea = impactRadius * impactRadius * Math.PI;
                double energyLoss = 200 * (miniSquirrel.getEnergy() / impactArea) * (1 - distance / impactRadius);

                switch (type) {
                    case MINI_SQUIRREL:
                    case MINI_SQUIRREL_BOT:
                        if (((MiniSquirrel) entity).getPatron() == miniSquirrel.getPatron())
                            break innerLoop;
                        else {
                            entity.updateEnergy(-(int) energyLoss);
                            miniSquirrel.getPatron().updateEnergy((int) energyLoss);
                            if (entity.getEnergy() <= 0)
                                killAndReplace(entity);
                        }
                        break;
                    case HAND_OPERATED_MASTER_SQUIRREL:
                    case MASTER_SQUIRREL_BOT:
                        if (entity == miniSquirrel.getPatron())
                            break;
                        else {
                            if ((int) energyLoss > entity.getEnergy())
                                entity.updateEnergy(-entity.getEnergy());
                            else {
                                entity.updateEnergy(-(int) energyLoss);
                                miniSquirrel.getPatron().updateEnergy((int) energyLoss);
                            }
                        }
                        break;
                    case GOOD_BEAST:
                    case GOOD_PLANT:
                        entity.updateEnergy(-(int) energyLoss);
                        miniSquirrel.getPatron().updateEnergy((int) energyLoss);
                        if (entity.getEnergy() <= 0)
                            killAndReplace(entity);
                        break;
                    case BAD_BEAST:
                    case BAD_PLANT:
                        entity.updateEnergy(-(int) energyLoss);
                        if (entity.getEnergy() <= 0)
                            killAndReplace(entity);
                        break;
                }
            }
        }

    }

    private void createEntities() {
        logger.info("Initializing board. Creating entities");
        int width = board.getSize().getX();
        int height = board.getSize().getY();
        for (int x = 0; x < width; x++) {
            cells[x][0] = new Wall(Entity.ID_AUTO_GENERATE, new XY(x, 0));
            cells[x][height - 1] = new Wall(Entity.ID_AUTO_GENERATE, new XY(x, height - 1));
        }
        for (int y = 1; y < height; y++) {
            cells[0][y] = new Wall(Entity.ID_AUTO_GENERATE, new XY(0, y));
            cells[width - 1][y] = new Wall(Entity.ID_AUTO_GENERATE, new XY(width - 1, y));
        }
        for (int i = 0; i < board.getBoardConfig().getNumberGoodBeasts(); i++) {
            XY pos = XYSupport.getRandomEmptyPosition(width, height, this);
            cells[pos.getX()][pos.getY()] = new GoodBeast(Entity.ID_AUTO_GENERATE, pos);
        }
        for (int i = 0; i < board.getBoardConfig().getNumberGoodPlants(); i++) {
            XY pos = XYSupport.getRandomEmptyPosition(width, height, this);
            cells[pos.getX()][pos.getY()] = new GoodPlant(Entity.ID_AUTO_GENERATE, pos);
        }
        for (int i = 0; i < board.getBoardConfig().getNumberBadBeasts(); i++) {
            XY pos = XYSupport.getRandomEmptyPosition(width, height, this);
            cells[pos.getX()][pos.getY()] = new BadBeast(Entity.ID_AUTO_GENERATE, pos);
        }
        for (int i = 0; i < board.getBoardConfig().getNumberBadPlants(); i++) {
            XY pos = XYSupport.getRandomEmptyPosition(width, height, this);
            cells[pos.getX()][pos.getY()] = new BadPlant(Entity.ID_AUTO_GENERATE, pos);
        }
        XY pos = XYSupport.getRandomEmptyPosition(width, height, this);
        cells[pos.getX()][pos.getY()] = new HandOperatedMasterSquirrel(Entity.ID_AUTO_GENERATE, 100, pos);
    }

    private MasterSquirrel findMasterSquirrel() {
        if (masterSquirrelCache != null)
            return masterSquirrelCache;
        for (int x = 0; x < board.getSize().getX(); x++) {
            for (int y = 0; y < board.getSize().getY(); y++) {
                Entity iterCell = getEntity(x, y);
                EntityType type = EntityType.fromEntity(iterCell);
                if (!((type != EntityType.MASTER_SQUIRREL_BOT) || type != EntityType.HAND_OPERATED_MASTER_SQUIRREL))
                    continue;
                masterSquirrelCache = (MasterSquirrel) iterCell;
            }
        }
        return findMasterSquirrel();
    }

    private void move(Entity entity, XY newPos) {
        cells[entity.getPosition().getX()][entity.getPosition().getY()] = null;
        cells[newPos.getX()][newPos.getY()] = entity;
        entity.setPosition(newPos);
    }

    public XY getAwayfromPosition(Entity entity, XY position) {
        int toMoveX, toMoveY;
        if (entity.getPosition().getY() - position.getY() <= 0)
            toMoveY = -1;
        else
            toMoveY = 1;

        if (entity.getPosition().getX() - position.getX() <= 0)
            toMoveX = -1;
        else
            toMoveX = 1;
        return new XY(toMoveX, toMoveY);
    }

    public XY getToPosition(Entity entity, XY position) {
        int toMoveX, toMoveY;
        if (entity.getPosition().getY() - position.getY() >= 0)
            toMoveY = -1;
        else if (entity.getPosition().getY() - position.getY() <= 0)
            toMoveY = 1;
        else
            toMoveY = 0;

        if (entity.getPosition().getX() - position.getX() >= 0)
            toMoveX = -1;
        else if (entity.getPosition().getX() - position.getX() >= 0)
            toMoveX = 1;
        else
            toMoveX = 0;
        return new XY(toMoveX, toMoveY);
    }
}
