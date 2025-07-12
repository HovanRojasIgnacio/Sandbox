package hr.sandbox.pixels;

public interface Simulator {

    GameEngine.Materials[][] simulate(GameEngine.Materials[][] grid);
    void dispose();
    void resize(int width, int height);
}
