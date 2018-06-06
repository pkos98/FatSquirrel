package de.hsa.games.fatsquirrel.core;

public class BoardConfig {

    private XY size;
    private int wallCount;
    private int numberWalls, numberGoodBeasts, numberGoodPlants, numberBadPlants, numberBadBeasts;

    public BoardConfig(XY size, int wallCount) {
        this.size = size;
        this.wallCount = wallCount;
        numberWalls = 10;
        numberBadPlants = 5;
        numberBadBeasts = 5;
        numberGoodPlants = 5;
        numberGoodBeasts = 5;
    }

    public XY getSize() {
        return size;
    }

    public int getWallCount() {
        return wallCount;
    }

    public int getNumberWalls() {
        return numberWalls;
    }

    public int getNumberGoodBeasts() {
        return numberGoodBeasts;
    }

    public int getNumberGoodPlants() {
        return numberGoodPlants;
    }

    public int getNumberBadPlants() {
        return numberBadPlants;
    }

    public int getNumberBadBeasts() {
        return numberBadBeasts;
    }
}
