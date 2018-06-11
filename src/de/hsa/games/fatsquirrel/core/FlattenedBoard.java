package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.entities.*;
import de.hsa.games.fatsquirrel.util.XYSupport;
import javafx.collections.ListChangeListener;

import java.util.logging.Logger;

/**
 * Manages collisions of all entities and provides 2d-access to the board
 */
public class FlattenedBoard implements EntityContext, BoardView {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Board board;
    private Entity[][] cells;
    private MasterSquirrel masterSquirrelCache = null;

    /**
     * Create a new instance of FlattenedBoard
     *
     * @param board The board which contains all entities
     */
    public FlattenedBoard(Board board) {
        this.board = board;
        cells = new Entity[board.getWidth()][board.getHeight()];
        board.getObservableList().addListener(new ListChangeListener<Entity>() {
            @Override
            public void onChanged(Change<? extends Entity> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        Entity iterEntity = c.getList().get(c.getFrom());
                        setCell(iterEntity, iterEntity.getPosition());
                    } else if (c.wasRemoved()) {
                        Entity iterEntity = c.getRemoved().get(0);
                        setCell(null, iterEntity.getPosition());
                    }
                }
            }
        });
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
        return getEntityType(new XY(x, y));
    }

    @Override
    public XY getSize() {
        return board.getSize();
    }

    /**
     * Defines what happens when a MiniSquirrel moves:
     * Rule...
     *
     * @param miniSquirrel  The minisquirrel which wants to move
     * @param moveDirection The direction in which the minisquirrel wants to move
     */
    @Override
    public void tryMove(MiniSquirrel miniSquirrel, XY moveDirection) {

        XY nextPosition = new XY(miniSquirrel.getPosition().getX() + moveDirection.getX(),
                miniSquirrel.getPosition().getY() + moveDirection.getY());
        if (!board.isInBoardRange(nextPosition))
            return;
        Entity nextEntity = getEntity(nextPosition.getX(), nextPosition.getY());
        EntityType type = EntityType.fromEntity(nextEntity);
        switch (type) {

            case GOOD_BEAST:
            case BAD_BEAST:
            case GOOD_PLANT:
            case BAD_PLANT:
                miniSquirrel.updateEnergy(nextEntity.getEnergy());
                killAndReplace(nextEntity);
                move(miniSquirrel, nextPosition);
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
                break;
            case EMPTY_FIELD:
                move(miniSquirrel, nextPosition);
        }
    }

    /**
     * Defines what happens when a MiniSquirrel moves:
     * Rule...
     *
     * @param goodBeast     The minisquirrel which wants to move
     * @param moveDirection The direction in which the minisquirrel wants to move
     */
    @Override
    public void tryMove(GoodBeast goodBeast, XY moveDirection) {
        XY nextPosition = new XY(goodBeast.getPosition().getX() + moveDirection.getX(),
                goodBeast.getPosition().getY() + moveDirection.getY());
        if (!board.isInBoardRange(nextPosition))
            return;
        Entity nextEntity = getEntity(nextPosition.getX(), nextPosition.getY());
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

    /**
     * Defines what happens when a MiniSquirrel moves:
     * Rule...
     *
     * @param badBeast      The minisquirrel which wants to move
     * @param moveDirection The direction in which the minisquirrel wants to move
     */
    @Override
    public void tryMove(BadBeast badBeast, XY moveDirection) {
        XY nextPosition = new XY(badBeast.getPosition().getX() + moveDirection.getX(),
                badBeast.getPosition().getY() + moveDirection.getY());
        if (!board.isInBoardRange(nextPosition))
            return;
        Entity nextEntity = getEntity(nextPosition.getX(), nextPosition.getY());
        EntityType type = EntityType.fromEntity(nextEntity);
        switch (type) {
            case MINI_SQUIRREL:
            case MINI_SQUIRREL_BOT:
            case HAND_OPERATED_MASTER_SQUIRREL:
            case MASTER_SQUIRREL_BOT:
                nextEntity.updateEnergy(badBeast.getEnergy());
                badBeast.bite();
                if (badBeast.getBiteCounter() == BadBeast.MAX_BITES)
                    killAndReplace(badBeast);
                break;
            case EMPTY_FIELD:
                move(badBeast, nextPosition);
        }
    }

    /**
     * Defines what happens when a MiniSquirrel moves:
     * Rule...
     *
     * @param masterSquirrel The minisquirrel which wants to move
     * @param moveDirection  The direction in which the minisquirrel wants to move
     */
    @Override
    public void tryMove(MasterSquirrel masterSquirrel, XY moveDirection) {
        XY nextPosition = XYSupport.add(masterSquirrel.getPosition(), moveDirection);
        if (!board.isInBoardRange(nextPosition))
            return;

        Entity nextEntity = getEntity(nextPosition.getX(), nextPosition.getY());
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

    /**
     * Finds the nearest instance of a PlayerEntity
     *
     * @param pos The position from where the nearest PlayerEntity should be found
     * @return THe PlayerEntity nearest to the given position
     */
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

    /**
     * Kills an entity by deleting it from the board
     *
     * @param entity The entity to delete
     */
    @Override
    public void kill(Entity entity) {
        board.deleteEntity((entity));
    }

    /**
     * Kills the entity and respawns an instance of the same EntityType at a random position
     */
    @Override
    public void killAndReplace(Entity entity) {
        // Reset energy to default level
        entity.updateEnergy(entity.getStartEnergy() - entity.getEnergy());
        XY randomPos = XYSupport.getRandomEmptyPosition(board.getSize().getX(), board.getSize().getY(), this);
        move(entity, randomPos);
    }

    /**
     * Spawns a mini squirrel
     */
    @Override
    public void spawnMiniSquirrel() {
        // 1. HAND_OPERATED_MASTER_SQUIRREL finden
        logger.info("MINI_SQUIRREL spawned");
        MasterSquirrel masterSquirrel = findMasterSquirrel();
        XY randomPos = XYSupport.getRandomEmptyPosition(board.getWidth(), board.getHeight(), this);
        MiniSquirrel miniSquirrel = new MiniSquirrel(42, randomPos, masterSquirrel);
        masterSquirrel.addMiniSquirrel(miniSquirrel);

    }

    @Override
    public void implode(MiniSquirrel miniSquirrel, int impactRadius) {
        XY location = miniSquirrel.getPosition();
        for (int iterX = location.getX() - impactRadius; iterX <= location.getX() + impactRadius; iterX++) {
            innerLoop:
            for (int iterY = location.getX() - impactRadius; iterY <= location.getX() + impactRadius; iterY++) {
                Entity entity = getEntity(iterX, iterY);
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

    private void setCell(Entity entity, XY pos) {
        cells[pos.getX()][pos.getY()] = entity;
    }

    private void updateCells() {
        cells = new Entity[board.getWidth()][board.getHeight()];
        for (int i = 0; i < board.getEntities().size(); i++) {
            Entity iterEntity = board.getEntities().get(i);
            cells[iterEntity.getPosition().getX()][iterEntity.getPosition().getY()] = iterEntity;
        }
    }


}
