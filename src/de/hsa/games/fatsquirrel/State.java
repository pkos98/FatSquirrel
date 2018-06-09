package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.EntityType;
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

    public FlattenedBoard flattenedBoard() {
        return board.flatten();
    }

    public void setInput(GameCommandType command) {
        input = command;
    }
}
