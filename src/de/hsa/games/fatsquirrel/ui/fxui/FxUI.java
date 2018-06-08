package de.hsa.games.fatsquirrel.ui.fxui;

import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.EntityType;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.ui.console.GameCommandType;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.logging.Logger;

public class FxUI extends Scene implements de.hsa.games.fatsquirrel.UI {

    private static final int CELL_SIZE = 15;
    private final Canvas boardCanvas;
    private final Label msgLabel;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private EntityContext context;
    private GameCommandType command;

    public FxUI(Parent parent, Canvas boardCanvas, Label msgLabel) {
        super(parent);
        this.boardCanvas = boardCanvas;
        this.msgLabel = msgLabel;
        this.setOnKeyPressed(this::handleOnKeyPressed);
    }

    public static FxUI createInstance(XY boardSize) {
        Canvas boardCanvas = new Canvas(boardSize.getX() * CELL_SIZE, boardSize.getY() * CELL_SIZE);
        Label statusLabel = new Label();
        VBox top = new VBox();
        top.getChildren().add(boardCanvas);
        top.getChildren().add(statusLabel);
        statusLabel.setText("Hallo Welt");
        final FxUI fxUI = new FxUI(top, boardCanvas, statusLabel);
        return fxUI;
    }

    @Override
    public void render(final BoardView view) {
        Platform.runLater(() -> repaintBoardCanvas(view));
    }

    private void repaintBoardCanvas(BoardView view) {
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());
        drawEntities(gc, view);
    }

    private void drawEntities(GraphicsContext gc, BoardView view) {
        int width = view.getSize().getX();
        int height = view.getSize().getY();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                EntityType entityType = view.getEntityType(x, y);
                drawEntity(gc, entityType, new XY(x, y));
            }
        }
    }

    private void drawEntity(GraphicsContext gc, EntityType entityType, XY pos) {
        int xPos = pos.getX() * CELL_SIZE;
        int yPos = pos.getY() * CELL_SIZE;
        if (entityType == null) {
            gc.setFill(Color.WHITE);
            gc.fillRect(xPos, yPos, CELL_SIZE, CELL_SIZE);
            return;
        }

        switch (entityType) {
            case GOOD_BEAST:
                gc.setFill(Color.GREEN);
                gc.fillOval(xPos, yPos, CELL_SIZE, CELL_SIZE);
                break;
            case BAD_BEAST:
                gc.setFill(Color.RED);
                gc.fillOval(xPos, yPos, CELL_SIZE, CELL_SIZE);
                break;
            case GOOD_PLANT:
                gc.setFill(Color.GREEN);
                gc.fillRect(xPos, yPos, CELL_SIZE, CELL_SIZE);
                break;
            case BAD_PLANT:
                gc.setFill(Color.RED);
                gc.fillRect(xPos, yPos, CELL_SIZE, CELL_SIZE);
                break;
            case WALL:
                gc.setFill(Color.GRAY);
                gc.fillRect(xPos, yPos, CELL_SIZE, CELL_SIZE);
                break;
            case HAND_OPERATED_MASTER_SQUIRREL:
                gc.setFill(Color.GOLD);
                gc.fillOval(xPos, yPos, CELL_SIZE, CELL_SIZE);
                break;
            case MINI_SQUIRREL:
                break;
        }
    }

    public void message(final String msg) {
        Platform.runLater(() -> msgLabel.setText(msg));
    }

    @Override
    public GameCommandType getCommand() {
        return command;
    }

    private void setCommand(GameCommandType command) {
        this.command = command;
        logger.info("Command " + command + " received");
    }

    @Override
    public void setContext(EntityContext flattenedBoard) {
        context = flattenedBoard;
    }

    @Override
    public void resetCommand() {
        command = null;
    }

    private void handleOnKeyPressed(KeyEvent x) {
        KeyCode code = x.getCode();
        logger.info("Key \"" + code + "\" was pressed");
        switch (code) {
            case W:
                setCommand(GameCommandType.DOWN);
                break;
            case A:
                setCommand(GameCommandType.LEFT);
                break;
            case S:
                setCommand(GameCommandType.UP);
                break;
            case D:
                setCommand(GameCommandType.RIGHT);
                break;
            default:
                setCommand(null);
        }
    }
}