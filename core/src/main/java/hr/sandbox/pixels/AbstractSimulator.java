package hr.sandbox.pixels;

public abstract class AbstractSimulator implements Simulator{

    protected int width;
    protected int height;
    protected GameEngine.Materials[][] grid;

    public AbstractSimulator(GameEngine.Materials[][] grid, int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = grid;
    }

    public GameEngine.Materials[][] simulate(GameEngine.Materials[][] grid) {
        this.grid = grid;
        updateGravity();
        updateHorizontally();

        return grid;
    }

    protected abstract void updateHorizontally();

    protected abstract void updateGravity();
}
