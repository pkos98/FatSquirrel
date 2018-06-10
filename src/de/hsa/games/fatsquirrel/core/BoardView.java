package de.hsa.games.fatsquirrel.core;

/**
 * Interface defining a 2D view of the board
 */
public interface BoardView {

    EntityType getEntityType(int x, int y);
    EntityType getEntityType(XY xy);

    XY getSize();

}
