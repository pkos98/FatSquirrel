package de.hsa.games.fatsquirrel.util.ui.console;

import de.hsa.games.fatsquirrel.Command;
import de.hsa.games.fatsquirrel.util.ui.ScanException;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Arrays;

public class CommandScanner {

    protected BufferedReader inputReader;
    protected PrintStream outputStream;
    protected CommandTypeInfo[] commandTypeInfos;

    public CommandScanner(CommandTypeInfo[] info, BufferedReader reader, PrintStream stream) {
        this.commandTypeInfos = info;
        this.inputReader = reader;
        this.outputStream = stream;
    }

    /**
     * Scans the next command from stdin
     *
     * @return
     * @throws ScanException
     */
    public Command next() throws ScanException {
        String input = "";
        try {
            input = inputReader.readLine();
            if (input == "")
                throw new ScanException();
            String[] tokens = input.split(" "); // TODO: maybe...
            String name = tokens[0];
            for (int i = 0; i < commandTypeInfos.length; i++) {
                CommandTypeInfo iterCommandType = commandTypeInfos[i];
                String iterCommandName = iterCommandType.getName().toLowerCase();
                if (!iterCommandName.equals(name))
                    continue;
                String[] paramTokens = tokens.length == 1 ?
                        new String[0] : Arrays.copyOfRange(tokens, 1, tokens.length - 1);
                Object[] parsedParams = null;
                try {
                    parsedParams = parseAndVerifyInput(iterCommandType, paramTokens);
                } catch (ScanException ex) {
                    outputStream.println("Illegal input. Please enter valid input.");
                    return next();
                }
                return new Command(iterCommandType, parsedParams);
            }
        } catch (Exception ex) {
            throw new ScanException();
        }
        return null;
    }

    private Object[] parseAndVerifyInput(CommandTypeInfo commandType, String... tokens) throws ScanException {
        try {
            Object[] paramValues = new Object[commandType.getParamTypes().length];
            for (int i = 0; i < commandType.getParamTypes().length; i++) {
                Class<?> iterParam = commandType.getParamTypes()[i];
                String iterToken = tokens[i];
                if (iterParam != int.class) {
                    paramValues[i] = iterToken;
                    continue;
                }
                // If its an integer
                Integer intToken = Integer.parseInt(iterToken);
                if (intToken < 0)
                    throw new ScanException();
                paramValues[i] = Integer.parseInt(iterToken);
            }
            return paramValues;
        } catch (Exception ex) {
            throw new ScanException();
        }
    }
}
