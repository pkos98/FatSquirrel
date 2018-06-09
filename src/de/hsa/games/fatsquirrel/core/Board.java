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

    public boolean isInBoardRange(XY pos) {
        int x = pos.getX();
        int y = pos.getY();
        return !((x < 0 || x >= getWidth()) ||
                (y < 0 || y >= getHeight()));
    }

    private void initBoard() {
        XY pos;
        // Instantiate upper & lower boundary walls
        for (int x = 0; x < getWidth(); x++) {
            entities.add(new Wall(Entity.ID_AUTO_GENERATE, new XY(x, 0)));
            entities.add(new Wall(Entity.ID_AUTO_GENERATE, new XY(x, getHeight() - 1)));
        }
        // Instantiate upper & lower boundary walls
        for (int y = 1; y < getHeight(); y++) {
            entities.add(new Wall(Entity.ID_AUTO_GENERATE, new XY(0, y)));
            entities.add(new Wall(Entity.ID_AUTO_GENERATE, new XY(getWidth() - 1, y)));
        }
        for (int i = 0; i < getBoardConfig().getNumberGoodBeasts(); i++) {
            pos = XYSupport.getRandomEmptyPosition(getWidth(), getHeight(), flatten());
            entities.add(new GoodBeast(Entity.ID_AUTO_GENERATE, pos));
        }
        for (int i = 0; i < getBoardConfig().getNumberGoodPlants(); i++) {
            pos = XYSupport.getRandomEmptyPosition(getWidth(), getHeight(), flatten());
            entities.add(new GoodPlant(Entity.ID_AUTO_GENERATE, pos));
        }
        for (int i = 0; i < getBoardConfig().getNumberBadBeasts(); i++) {
            pos = XYSupport.getRandomEmptyPosition(getWidth(), getHeight(), flatten());
            entities.add(new BadBeast(Entity.ID_AUTO_GENERATE, pos));
        }
        for (int i = 0; i < getBoardConfig().getNumberBadPlants(); i++) {
            pos = XYSupport.getRandomEmptyPosition(getWidth(), getHeight(), flatten());
            entities.add(new BadPlant(Entity.ID_AUTO_GENERATE, pos));
        }
        pos = XYSupport.getRandomEmptyPosition(getWidth(), getHeight(), flatten());
        entities.add(new HandOperatedMasterSquirrel(Entity.ID_AUTO_GENERATE, 100, pos));
    }
}