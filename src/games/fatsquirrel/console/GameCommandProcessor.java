package games.fatsquirrel.console;

import games.fatsquirrel.Command;
import games.fatsquirrel.core.EntityContext;
import games.fatsquirrel.core.EntityType;
import games.fatsquirrel.core.XY;
import games.fatsquirrel.util.ui.console.CommandScanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GameCommandProcessor {

    private final CommandScanner scanner;
    private final EntityContext context;
    private final PrintStream outputStream;

    private Command commandBuffer;

    GameCommandProcessor(EntityContext context) {
        outputStream = new PrintStream(System.out);
        scanner = new CommandScanner(GameCommandType.values(), new BufferedReader(new InputStreamReader(System.in)),
                outputStream);
        this.context = context;
    }

    /**
     * Executes a given command using reflection
     * Convention: Name of executing method = Command-name
     */
    public Command process() {
        while (true) {
            try {
                commandBuffer = scanner.next();
                String methodName = commandBuffer.getCommandType().getName().toLowerCase();
                Method method = this.getClass().getMethod(methodName, commandBuffer.getCommandType().getParamTypes());
                try {
                    method.invoke(this, commandBuffer.getParamValues());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return commandBuffer;
            } catch (Exception e) {
                outputStream.println("Invalid input. Please try again. Type help for help");
                continue;
            }
        }
    }

    public void spawnMini() {
        context.spawnMiniSquirrel();
    }

    public void help() {
        GameCommandType[] values = GameCommandType.values();
        for (int i = 0; i < values.length; i++) {
            GameCommandType iterValue = values[i];
            outputStream.println(iterValue.getName() + ": " + iterValue.getHelpText());
        }
    }

    public void exit() {
        System.exit(0);
    }

    public void all() {
        for (int x = 0; x < context.getSize().getX(); x++) {
            for (int y = 0; y < context.getSize().getY(); y++) {
                XY pos = new XY(x, y);
                EntityType iterCell = context.getEntityType(pos);
                outputStream.println(iterCell.name() + ": " + pos);
            }
        }
    }

    public void left() {
    }

    public void up() {
    }

    public void down() {
    }

    public void right() {
    }

    public void master_energy() {
        // TODO: ...
    }

    public void spawn_mini() {
        context.spawnMiniSquirrel();
    }

    public Command getCommmandBuffer() {
        return commandBuffer;
    }

    public void resetCommandBuffer() {
        commandBuffer = null;
    }
}
