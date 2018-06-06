package games.fatsquirrel;

import games.fatsquirrel.console.GameCommandType;
import games.fatsquirrel.core.BoardView;
import games.fatsquirrel.core.EntityContext;

public interface UI {

    GameCommandType getCommand();

    void render(BoardView view);

    void setContext(EntityContext flattenedBoard);

    void resetCommand();
}
