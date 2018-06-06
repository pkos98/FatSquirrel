package de.hsa.games.fatsquirrel.ui.console;

import de.hsa.games.fatsquirrel.Command;
import de.hsa.games.fatsquirrel.UI;
import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.EntityType;

public class ConsoleUI implements UI {

    private String FIELD_SEPERATOR = "|";
    private EntityContext context;
    private GameCommandProcessor processor;
    private boolean alreadyStartedProcessor;

    public ConsoleUI() {
        processor = new GameCommandProcessor(context);
    }

    @Override
    public GameCommandType getCommand() {
        if (!alreadyStartedProcessor) {
            alreadyStartedProcessor = true;
            processor.process();
        }

        Command newCommand = processor.getCommmandBuffer();
        processor.resetCommandBuffer();
        if (newCommand != null)
            return (GameCommandType) newCommand.getCommandType();
        else
            return null;
    }

    @Override
    public void render(BoardView view) {
        String result = "";
        for (int y = 0; y < view.getSize().getY(); y++) {
            for (int x = 0; x < view.getSize().getX(); x++) {
                EntityType iterField = view.getEntityType(x, y);
                if (iterField == null)
                    result += "__";
                else
                    result += toString(iterField) + FIELD_SEPERATOR;
            }
            result += System.lineSeparator();
        }
        System.out.print(result);
    }

    private String toString(EntityType entity) {
        switch (entity) {
            case GOOD_BEAST:
                return "GB";
            case BAD_BEAST:
                return "BB";
            case GOOD_PLANT:
                return "GP";
            case BAD_PLANT:
                return "BP";
            case WALL:
                return "WA";
            case MASTER_SQUIRREL:
                return "MS";
            case MINI_SQUIRREL:
                return "mS";
            default:
                return null;
        }
    }

    @Override
    public void setContext(EntityContext context) {
        this.context = context;
    }

    @Override
    public void resetCommand() {
    }

}
