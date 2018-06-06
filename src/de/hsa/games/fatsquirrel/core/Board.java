package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.entities.Entity;

public class Board {

    private Entity[][] entities;

    public BoardConfig getBoardConfig() {
        return boardConfig;
    }

    private BoardConfig boardConfig;
    private FlattenedBoard boardCache;

    public Board(BoardConfig config) {
        this.boardConfig = config;
        this.entities = new Entity[config.getSize().getX()][config.getSize().getY()];
    }

    public XY getSize() {
        return boardConfig.getSize();
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

    public Entity getEntity (XY location){
        return null;
    }

    public void deleteEntity(Entity entity){
        entities[entity.getPosition().getX()][entity.getPosition().getY()] = null;
    }
}
