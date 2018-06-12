package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.entities.*;
import de.hsa.games.fatsquirrel.util.EntityAnnotation;
import de.hsa.games.fatsquirrel.util.XYSupport;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Board {

    private ObservableList<Entity> entities;
    private ArrayList<Entity> masterSquirrels;
    private int entityCounter;
    private BoardConfig boardConfig;
    private FlattenedBoard flattenedBoard;
    private int width, height;

    public Board(BoardConfig boardConfig) {
        this.boardConfig = boardConfig;
        width = boardConfig.getSize().getX();
        height = boardConfig.getSize().getY();
        entities = FXCollections.observableArrayList();
        masterSquirrels = new ArrayList<>();
        entities.addListener(new ListChangeListener<Entity>() {
            @Override
            public void onChanged(Change<? extends Entity> c) {
                while (c.next()) {
                    if (c.wasAdded())
                        entityCounter++;
                    else if (c.wasRemoved())
                        entityCounter--;
                }
            }
        });
        flattenedBoard = new FlattenedBoard(this);
        // Initiialization must be AFTER instantiation of FlattenedBoard
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

    /**
     * Insert an entity into the board using its position property
     *
     * @param entity The entity to insert
     */
    public void insertEntity(Entity entity) {
        entities.add(entity);
        if (EntityType.fromEntity(entity) == EntityType.MASTER_SQUIRREL_BOT ||
                EntityType.fromEntity(entity) == EntityType.HAND_OPERATED_MASTER_SQUIRREL)
            masterSquirrels.add(entity);
    }

    /**
     * Delete the entity from the board
     *
     * @param entity The entity to delete
     */
    public void deleteEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Get all entities of the board
     *
     * @return A list of all entities
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Get a 2d-representation of the board
     *
     * @return The 2d-representation of the board
     */
    public FlattenedBoard flatten() {
        return flattenedBoard;
    }

    /**
     * Proves if the given position is within the board range
     *
     * @param pos The position to prove
     * @return true if the position is in the board, else false
     */
    public boolean isInBoardRange(XY pos) {
        int x = pos.getX();
        int y = pos.getY();
        return !((x < 0 || x >= getWidth()) ||
                (y < 0 || y >= getHeight()));
    }

    /**
     * Get an observable list of all entities
     *
     * @return An observable list of all entities
     */
    public ObservableList<Entity> getObservableList() {
        return entities;
    }

    private void loadBots() {
        String mainBotPath = boardConfig.getMainBotPath();
        try {
            // MasterSquirel
            BotControllerFactory factory = (BotControllerFactory) Class.forName(mainBotPath).newInstance();
            XY pos = XYSupport.getRandomEmptyPosition(getWidth(), getHeight(), flatten());
            MasterSquirrelBot bot = new MasterSquirrelBot(Entity.ID_AUTO_GENERATE, 1000, pos, factory);
            insertEntity(bot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the board with the amount of entities defined in BoardConfig
     */
    private void initBoard() {
        Map<EntityType, Integer> map = getBoardConfig().getAmountByEntityType();
        XY pos;
        for (int i = 0; i < map.size(); i++) {
            EntityType iterType = EntityType.values()[i];
            for (int j = 0; j < map.get(iterType); j++) {
                insertEntity(instantiateEntity(i));
            }
        }
        // Instantiate upper & lower boundary walls
        for (int x = 0; x < getWidth(); x++) {
            insertEntity(new Wall(Entity.ID_AUTO_GENERATE, new XY(x, 0)));
            insertEntity(new Wall(Entity.ID_AUTO_GENERATE, new XY(x, getHeight() - 1)));
        }
        // Instantiate upper & lower boundary walls
        for (int y = 1; y < getHeight(); y++) {
            insertEntity(new Wall(Entity.ID_AUTO_GENERATE, new XY(0, y)));
            insertEntity(new Wall(Entity.ID_AUTO_GENERATE, new XY(getWidth() - 1, y)));
        }
        pos = XYSupport.getRandomEmptyPosition(getWidth(), getHeight(), flatten());
        HandOperatedMasterSquirrel ms = new HandOperatedMasterSquirrel(Entity.ID_AUTO_GENERATE, 150, pos);
        insertEntity(ms);
        loadBots();
    }

    private Entity instantiateEntity(int i) {
        Field[] fieldList = EntityType.class.getDeclaredFields();
        Field field = fieldList[i];
        EntityAnnotation t = (EntityAnnotation) field.getDeclaredAnnotations()[0];
        Class clazz = t.entity();
        try {
            XY pos = XYSupport.getRandomEmptyPosition(getWidth(), getHeight(), flatten());
            return (Entity) clazz.getConstructor(Integer.TYPE, XY.class)
                    .newInstance(Entity.ID_AUTO_GENERATE, pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Entity> getMasters() {
        return masterSquirrels;
    }
}
