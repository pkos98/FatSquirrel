package de.hsa.games.fatsquirrel.botapi;

public class OutOfViewException extends RuntimeException {

    public OutOfViewException(String mag) {
        super(mag);
    }
}
