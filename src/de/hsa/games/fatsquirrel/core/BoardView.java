package de.hsa.games.fatsquirrel.core;

public interface BoardView {

    EntityType getEntityType(int x, int y);

    XY getSize();

}
