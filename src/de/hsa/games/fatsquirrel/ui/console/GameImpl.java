package de.hsa.games.fatsquirrel.ui.console;

import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.State;
import de.hsa.games.fatsquirrel.UI;

import java.util.logging.Logger;

public class GameImpl extends Game {

    private Logger logger = Logger.getLogger(getClass().getName());

    public GameImpl(State state, UI ui) {
        super(state, ui);
        initProxy();
        ui.setContext(state.flattenedBoard());
    }

    private void initProxy() {
        int width = state.flattenedBoard().getSize().getX();
        int height = state.flattenedBoard().getSize().getY();
    }

    @Override
    protected void render() {
        ui.render(state.flattenedBoard());
    }

    @Override
    protected void processInput() {
        state.setInput(ui.getCommand());
        ui.resetCommand();
    }

}
