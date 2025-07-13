package hr.sandbox.pixels.simulator;

import hr.sandbox.pixels.GameEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        update();
        return grid;
    }

    protected abstract void update();

    protected List<Integer> shuffle(){
        List<Integer> horizontalPositions = new ArrayList<>();
        for (int x =  width-2; x >0; x--) {
            horizontalPositions.add(x);
        }
        Collections.shuffle(horizontalPositions);
        return horizontalPositions;
    }
}
