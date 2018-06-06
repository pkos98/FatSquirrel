package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.entities.*;
import de.hsa.games.fatsquirrel.util.XYSupport;

import java.util.logging.Logger;

public class FlattenedBoard implements EntityContext, BoardView {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Board board;
    private Entity[][] cells;
    private MasterSquirrel masterSquirrelCache = null;

    @Override
    public EntityType getEntityType(int x, int y) {
        return getEntityType(new XY(x, y));
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
        if (nextEntity instanceof Wall) {
            miniSquirrel.updateEnergy(nextEntity.getEnergy());
            miniSquirrel.paralyze();
            return;
        } else if (nextEntity instanceof MiniSquirrel &&
                miniSquirrel.getPatron() != ((MiniSquirrel) nextEntity).getPatron()) {
            kill(miniSquirrel);
        } else if (nextEntity instanceof MasterSquirrel &&
                miniSquirrel.getPatron() != nextEntity) {
            kill(miniSquirrel);
        } else if (nextEntity instanceof GoodPlant ||
                nextEntity instanceof BadPlant) {
            miniSquirrel.updateEnergy(nextEntity.getEnergy());
            killAndReplace(nextEntity);
        } else if (nextEntity instanceof GoodBeast ||
                nextEntity instanceof BadBeast) {
            miniSquirrel.updateEnergy(nextEntity.getEnergy());
            killAndReplace(nextEntity);
        }
        move(miniSquirrel, nextPosition);
    }

    @Override
    public void tryMove(GoodBeast goodBeast, XY moveDirection) {
        XY nextPosition = new XY(goodBeast.getPosition().getX() + moveDirection.getX(),
                goodBeast.getPosition().getY() + moveDirection.getY());
        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];
        if (nextEntity instanceof Wall) {
            move(goodBeast, goodBeast.getPosition());
        }
        else if (nextEntity instanceof MiniSquirrel ||
                nextEntity instanceof MasterSquirrel) {
            nextEntity.updateEnergy(goodBeast.getEnergy());
            killAndReplace(goodBeast);
        } else if (nextEntity == null)
            move(goodBeast, nextPosition);
        else
            return;
    }

    @Override
    public void tryMove(BadBeast badBeast, XY moveDirection) {
        XY nextPosition = new XY(badBeast.getPosition().getX() + moveDirection.getX(),
                badBeast.getPosition().getY() + moveDirection.getY());
        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];
        if (nextEntity instanceof Wall)
            return;
        else if (nextEntity instanceof MiniSquirrel || nextEntity instanceof MasterSquirrel) {
            nextEntity.updateEnergy(badBeast.getEnergy());
            killAndReplace(badBeast);
        } else if (nextEntity == null)
            move(badBeast, nextPosition);
        else
            return;
    }

    @Override
    public void tryMove(MasterSquirrel masterSquirrel, XY moveDirection) {
        XY nextPosition = XYSupport.add(masterSquirrel.getPosition(), moveDirection);
        if (nextPosition.getX() >= board.getBoardConfig().getSize().getX() || nextPosition.getX() < 0)
            return;
        else if (nextPosition.getY() >= board.getBoardConfig().getSize().getY() || nextPosition.getY() < 0)
            return;

        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];
        if (nextEntity instanceof Wall) {
            masterSquirrel.updateEnergy(nextEntity.getEnergy());
            masterSquirrel.paralyze();
            return;
        } else if (nextEntity instanceof MasterSquirrel) {
            return;
        } else if (nextEntity != null) {
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
                Entity currentCell = cells[x][y];
                if (!(currentCell instanceof PlayerEntity))
                    continue;

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
    public EntityType getEntityType(XY xy) {
        return EntityType.fromEntity(cells[xy.getX()][xy.getY()]);
    }

    @Override
    public void spawnMiniSquirrel() {
        // 1. MasterSquirrel finden
        logger.info("MiniSquirrel spawned");
        MasterSquirrel masterSquirrel = findMasterSquirrel();
        XY randomPos = XYSupport.getRandomEmptyPosition(board.getSize().getX(), board.getSize().getY(), this);
        MiniSquirrel miniSquirrel = new MiniSquirrel(42, randomPos, masterSquirrel);
        masterSquirrel.addMiniSquirrel(miniSquirrel);

    }

    @Override
    public void implode(MiniSquirrel miniSquirrel, int impactRadius) {
        XY location = miniSquirrel.getPosition();
        for (int iterX = location.getX() - impactRadius; iterX <= location.getX() + impactRadius; iterX++) {
            for (int iterY = location.getX() - impactRadius; iterY <= location.getX() + impactRadius; iterY++) {
                Entity entity = this.board.getEntity(new XY(iterX, iterX));
                XY XYdistance = new XY(iterX, iterX);
                double distance = XYdistance.convertDistance(XYdistance.distanceFromXY(entity.getPosition()));
                double impactArea = impactRadius * impactRadius * Math.PI;
                double energyLoss = 200 * (miniSquirrel.getEnergy() / impactArea) * (1 - distance / impactRadius);

                if (entity instanceof MiniSquirrel) {
                    if (((MiniSquirrel) entity).getPatron() == miniSquirrel.getPatron())
                        break;
                    else {
                        entity.updateEnergy(-(int) energyLoss);
                        miniSquirrel.getPatron().updateEnergy((int) energyLoss);
                        if (entity.getEnergy() <= 0)
                            killAndReplace(entity);
                    }
                }

                if (entity instanceof MasterSquirrel) {
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
                } else if (entity instanceof GoodBeast || entity instanceof GoodPlant) {
                    entity.updateEnergy(-(int) energyLoss);
                    miniSquirrel.getPatron().updateEnergy((int) energyLoss);
                    if (entity.getEnergy() <= 0)
                        killAndReplace(entity);

                } else if (entity instanceof BadPlant || entity instanceof BadBeast) {
                    entity.updateEnergy(-(int) energyLoss);
                    if (entity.getEnergy() <= 0)
                        killAndReplace(entity);
                }
            }
        }
    }

    public FlattenedBoard(Board board) {
        this.board = board;
        cells = board.getEntities();
        createEntities();
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
        cells[pos.getX()][pos.getY()] = new MasterSquirrelBot(Entity.ID_AUTO_GENERATE, 100, pos);
    }

    private MasterSquirrel findMasterSquirrel() {
        if (masterSquirrelCache != null)
            return masterSquirrelCache;
        for (int x = 0; x < board.getSize().getX(); x++) {
            for (int y = 0; y < board.getSize().getY(); y++) {
                Entity iterCell = cells[x][y];
                if (!(iterCell instanceof MasterSquirrel))
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

    public Entity getEntity(XY xy) {
        return cells[xy.getX()][xy.getY()];
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
        else if(entity.getPosition().getY() - position.getY() <= 0)
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
