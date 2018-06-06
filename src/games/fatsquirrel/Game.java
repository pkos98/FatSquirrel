package games.fatsquirrel;

public abstract class Game {

    final int FPS = 10;
    protected UI ui;
    protected State state;

    public Game(State state, UI ui) {
        this.state = state;
        this.ui = ui;
    }

    public void run() {
        while (true) {
            render();
            processInput();
            update();
        }
    }

    public void runMultiThreaded() {
        render();
        processInput();
        update();
    }

    protected abstract void render();

    protected abstract void processInput();

    protected void update() {
        state.update();
    }

    public int getFPS() {
        return FPS;
    }
}
