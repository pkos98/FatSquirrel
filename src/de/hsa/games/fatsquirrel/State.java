package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.EntityType;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.entities.Character;
import de.hsa.games.fatsquirrel.entities.Entity;
import de.hsa.games.fatsquirrel.entities.HandOperatedMasterSquirrel;
import de.hsa.games.fatsquirrel.ui.console.GameCommandType;

/**
 * Updates the states of all entities
 */
public class State {

    private Board board;
    private GameCommandType input;

    /**
     * Create a new State instance
     * @param board The board which contains access to all entities
     */
    State(Board board) {
        this.board = board;
    }

    /**
     * Update the state/position of all entities
     */
    public void update() {
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

    /**
     * Get 2D access to the board
     * @return an instance of FlattenedBoard providing 2d access to the board
     */
    public FlattenedBoard flattenedBoard() {
        return board.flatten();
    }

    /**
     * Set the input of the HandOperatedMasterSquirrel
     * @param command The input to transfer
     */
    public void setInput(GameCommandType command) {
        input = command;
    }
}
