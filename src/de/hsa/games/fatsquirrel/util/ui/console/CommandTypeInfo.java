package de.hsa.games.fatsquirrel.util.ui.console;

public interface CommandTypeInfo {

    public String getName();
    public String getHelpText();

    Class<?>[] getParamTypes();

}
