package de.hsa.games.fatsquirrel.entities;

import de.hsa.games.fatsquirrel.core.XY;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public abstract class MasterSquirrel extends PlayerEntity {

    private String name = "tester";
    private static int START_ENERGY = 0;
    private List<MiniSquirrel> miniSquirrels = new LinkedList<>();
    private Logger logger = Logger.getLogger(this.getClass().getName());


    public MasterSquirrel(int id, int startEnergy, XY startPos) {
        super(id, startEnergy, startPos);
    }

    public void addMiniSquirrel(MiniSquirrel miniSquirrel) {
        miniSquirrels.add(miniSquirrel);
    }

    @Override
    public int getStartEnergy() {
        return START_ENERGY;
    }

    @Override
    public String toString() {
        return "HAND_OPERATED_MASTER_SQUIRREL";
    }

    public boolean isPatronOf(MiniSquirrel miniSquirrel) {
        return miniSquirrels.contains(miniSquirrel);
    }

    public String getName(){
        return name;
    }

}
