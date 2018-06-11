package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.EntityType;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.entities.Character;
import de.hsa.games.fatsquirrel.entities.Entity;
import de.hsa.games.fatsquirrel.entities.HandOperatedMasterSquirrel;
import de.hsa.games.fatsquirrel.ui.console.GameCommandType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

/**
 * Updates the states of all entities
 */
public class State {

    private Board board;
    private BoardConfig boardConfig;
    private boolean gamePause = false;
    private HashMap<String, Integer> highScore;
    private HashMap<String, ArrayList<Integer>> moreHighScores;
    private String highScoreFile = "Properties/highscores.properties";
    private GameCommandType input;
    private int StepCounter;

    /**
     * Create a new State instance
     *
     * @param board The board which contains access to all entities
     */
    public State(Board board) {
        this.boardConfig = board.getBoardConfig();
        board = new Board(boardConfig);
        this.board = board;
        highScore = new HashMap<>();
        moreHighScores = new HashMap<>();
        StepCounter = board.getBoardConfig().getStepCounter();
    }

    /**
     * Update the state/position of all entities
     */
    public void update() {
        if (gamePause)
            return;
        if (StepCounter == 0) {
            togglePause();
            updateHighscores();
            printHighScore(); //TODO von allen
            saveHighscores();
            StepCounter = board.getBoardConfig().getStepCounter();
            board = new Board(boardConfig);
            togglePause();
        }
        StepCounter--;
        for (int i = 0; i < board.getEntities().size(); i++) {
            Entity iterEntity = board.getEntities().get(i);
            EntityType type = EntityType.fromEntity(iterEntity);
            switch (type) {
                case EMPTY_FIELD:
                    continue;
                case HAND_OPERATED_MASTER_SQUIRREL:
                    ((HandOperatedMasterSquirrel) iterEntity).setInput(input);
                    setInput(null);
                    ((Character) iterEntity).nextStep(flattenedBoard());
                    break;
                case BAD_BEAST:
                case GOOD_BEAST:
                case MASTER_SQUIRREL_BOT:
                    ((Character) iterEntity).nextStep(flattenedBoard());
                    break;
            }
        }
    }

    private void updateHighscores() {
        for (Entity entity : board.getMasters()) {
            highScore.put(entity.getName(), entity.getEnergy());

            ArrayList<Integer> buffer;
            if (moreHighScores.get(entity.getName()) != null) {
                buffer = moreHighScores.get(entity.getName());
            } else {
                buffer = new ArrayList<>();
            }

            buffer.add(entity.getEnergy());

            Collections.sort(buffer);
            Collections.reverse(buffer);

            if (buffer.size() > 3)
                buffer.subList(3, buffer.size()).clear();


            moreHighScores.put(entity.getName(), buffer);
        }
    }

    public void printHighScore() {
        System.out.println("Diese Runde zu Ende");
        for (Entity entity : board.getMasters())
            System.out.println(entity.getName() + ": Ihr Punktestand ist " + moreHighScores.get(entity.getName()));
    }

    public void saveHighscores() {
        Properties properties = new Properties();
        for (HashMap.Entry< String, ArrayList < Integer >> entries : moreHighScores.entrySet()){
            properties.setProperty(entries.getKey(), entries.getValue().toString());
        }
        try {
            properties.store(new FileOutputStream(highScoreFile), "highscores");
        } catch (IOException e) {
        }
}

    public void togglePause() {
        gamePause = !gamePause;
    }

    /**
     * Get 2D access to the board
     *
     * @return an instance of FlattenedBoard providing 2d access to the board
     */
    public FlattenedBoard flattenedBoard() {
        return board.flatten();
    }

    /**
     * Set the input of the HandOperatedMasterSquirrel
     *
     * @param command The input to transfer
     */
    public void setInput(GameCommandType command) {
        input = command;
    }


}
