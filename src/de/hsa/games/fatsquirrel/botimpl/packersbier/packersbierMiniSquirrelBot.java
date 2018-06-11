package de.hsa.games.fatsquirrel.botimpl.packersbier;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botimpl.BotHelper;

public class packersbierMiniSquirrelBot implements BotController {

    private final BotHelper botHelper;
    private String name = "packersBier";

    @Override
    public void nextStep(ControllerContext context) {

    }

    public String getName() {
        return name;
    }

    public packersbierMiniSquirrelBot(BotHelper bothelper){
        this.botHelper = bothelper;
    }
}
