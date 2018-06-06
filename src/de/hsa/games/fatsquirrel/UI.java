package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.ui.console.GameCommandType;
import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.EntityContext;

public interface UI {

    GameCommandType getCommand();

    void render(BoardView view);

    void setContext(EntityContext flattenedBoard);

    void resetCommand();
}
