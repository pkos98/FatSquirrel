package games.fatsquirrel;

import games.fatsquirrel.console.GameCommandType;
import games.fatsquirrel.core.Board;
import games.fatsquirrel.core.FlattenedBoard;
import games.fatsquirrel.entities.*;
import games.fatsquirrel.entities.Character;

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
