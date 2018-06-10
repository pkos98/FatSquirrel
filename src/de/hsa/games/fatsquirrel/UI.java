package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.ui.console.GameCommandType;

/**
 * Abstraction defining the basic functionality of an UI
 */
public interface UI {

    GameCommandType getCommand();

    void render(BoardView view);

    void setContext(EntityContext flattenedBoard);

    void resetCommand();
}
