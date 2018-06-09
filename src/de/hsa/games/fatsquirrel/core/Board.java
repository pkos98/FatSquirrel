package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.entities.*;
import de.hsa.games.fatsquirrel.util.XYSupport;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<Entity> entities;
    private int entityCounter;
    private BoardConfig boardConfig;
    private FlattenedBoard boardCache;
    private int width, height;

    public Board(BoardConfig boardConfig) {
        this.boardConfig = boardConfig;
        width = boardConfig.getSize().getX();
        height = boardConfig.getSize().getY();
        int boardSize = boardConfig.getSize().getX() * boardConfig.getSize().getY();
        entities = new ArrayList<>(boardSize);
        initBoard();
    }

    public BoardConfig getBoardConfig() {
        return boardConfig;
    }

    public XY getSize() {
        return boardConfig.getSize();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getEntityCounter() {
        return entityCounter;
    }

    public void insertEntity(Entity entity) {
        entities.add(entity);
        entityCounter++;
    }

    public void deleteEntity(Entity entity) {
        entities.remove(entity);
        entityCounter--;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public FlattenedBoard flatten() {
        if (boardCache == null)
            boardCache = new FlattenedBoard(this);
        return boardCache;
    }

    public void insertEntity(Entity entity) {
        entities[entity.getPosition().getX()][entity.getPosition().getY()] = entity;
    }

    public Entity[][] getEntities() {
        return entities;
    }

    public Entity getEntity(XY location) {
        return null;
    }

    public void deleteEntity(Entity entity) {
        entities[entity.getPosition().getX()][entity.getPosition().getY()] = null;
    }
}