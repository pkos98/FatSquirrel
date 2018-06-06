package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.util.ui.console.CommandTypeInfo;

public class Command {

    private final CommandTypeInfo commandType;
    private final Object[] paramValues;

    public Command(CommandTypeInfo commandType, Object[] paramValues) {
        this.commandType = commandType;
        this.paramValues = paramValues;
    }

    public Object[] getParamValues() {
        return paramValues;
    }

    public CommandTypeInfo getCommandType() {
        return commandType;
    }

}