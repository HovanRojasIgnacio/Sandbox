package hr.sandbox.pixels.simulator;

import hr.sandbox.pixels.GameEngine;

public interface Simulator {

    GameEngine.Materials[][] simulate(GameEngine.Materials[][] grid);
    void dispose();
    void resize(int width, int height);
    GameEngine.Materials getMaterial();
}
