package de.hsa.games.fatsquirrel.ui.console;

import de.hsa.games.fatsquirrel.util.ui.console.CommandTypeInfo;

public enum GameCommandType implements CommandTypeInfo {

    HELP("help", "prints help"),
    EXIT("exit", "exit game"),
    ALL("all", " print all"),
    LEFT("left", "go left"),
    UP("up", "go up"),
    DOWN("down", "go down"),
    RIGHT("right", "go right"),
    MASTER_ENERGY("master_energy", "print master_energy"),
    SPAWN_MINI("spawn_mini", "spawn mini squirrel", int.class, int.class);

    private final String commandName;
    private final String helpText;
    private final Class<?>[] paramTypes;

    GameCommandType(String commandName, String helpText, Class<?>... paramTypes) {
        this.commandName = commandName;
        this.helpText = helpText;
        this.paramTypes = paramTypes;
    }

    @Override
    public String getName() {
        return commandName;
    }

    @Override
    public String getHelpText() {
        return helpText;
    }

    @Override
    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

}
