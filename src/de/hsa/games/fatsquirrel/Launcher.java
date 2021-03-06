package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.ui.console.ConsoleUI;
import de.hsa.games.fatsquirrel.ui.console.GameImpl;
import de.hsa.games.fatsquirrel.ui.fxui.FxUI;
import de.hsa.games.fatsquirrel.util.BoardConfigProvider;
import de.hsa.games.fatsquirrel.util.PropertyBoardConfigProvider;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Application main entry-point which starts the game & ui
 */
public class Launcher extends Application {

    private final static XY BOARD_SIZE = new XY(40, 40);
    private final static int WALL_COUNT = 10;
    private static BoardConfigProvider propertyConfigProvider =
            new PropertyBoardConfigProvider("properties/default.properties");

    /**
     * Entry point which starts the game
     * @param args --gui or --single-thread/--console for the corresponding ui
     */
    public static void main(String[] args) {
        if (containsArgument(args, "gui") || containsArgument(args, "--gui")) {
            Application.launch(new String[]{args[0]});
            return;
        }
        BoardConfigProvider configProvider = new PropertyBoardConfigProvider("properties/default.properties");
        Game game = new GameImpl(new State(new Board(new BoardConfig(configProvider))), new ConsoleUI());
        if (containsArgument(args, "single-thread") || containsArgument(args, "--single-thread"))
            startGameSingleThreaded(game);
        else
            startGameMultiThreaded(game);
    }

    private static void startGameSingleThreaded(Game game) {
        game.run();
    }

    private static void startGameMultiThreaded(Game game) {
        Timer timer = new Timer();
        int intervall = (int) ((1f / game.getFPS()) * 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.runMultiThreaded();
            }
        }, 100, intervall);
    }

    private static boolean containsArgument(String[] args, String arg) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(arg))
                return true;
        }
        return false;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FxUI fxUI = FxUI.createInstance(BOARD_SIZE);
        primaryStage.setTitle("Diligent Squirrel");
        primaryStage.setScene(fxUI);
        primaryStage.setOnCloseRequest(x -> System.exit(-1));
        primaryStage.show();
        Game game = new GameImpl(new State(new Board(new BoardConfig(propertyConfigProvider))), fxUI);
        startGameMultiThreaded(game);
    }

}
