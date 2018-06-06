package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.entities.Character;
import de.hsa.games.fatsquirrel.entities.Entity;
import de.hsa.games.fatsquirrel.entities.HandOperatedMasterSquirrel;
import de.hsa.games.fatsquirrel.ui.console.GameCommandType;

public class State {

    private Board board;
    private GameCommandType input;

    State(Board board) {
        this.board = board;
    }

    public void update() {
        for (int x = 0; x < board.getSize().getX(); x++) {
            for (int y = 0; y < board.getSize().getY(); y++) {
                Entity iterField = board.getEntities()[x][y];
                if (iterField == null)
                    continue;
                // If NOT NULL
                if (iterField instanceof HandOperatedMasterSquirrel) {
                    ((HandOperatedMasterSquirrel) iterField).setInput(input);
                    setInput(null);
                }

                if (iterField instanceof Character)
                    ((Character) iterField).nextStep(flattenedBoard());
            }
        }
    }

    public FlattenedBoard flattenedBoard() {
        return board.flatten();
    }

    public void setInput(GameCommandType command) {
        input = command;
    }
}
