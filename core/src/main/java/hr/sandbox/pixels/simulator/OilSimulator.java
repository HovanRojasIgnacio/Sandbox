package hr.sandbox.pixels.simulator;

import hr.sandbox.pixels.GameEngine;

import java.util.List;
import java.util.Random;

public class OilSimulator extends AbstractSimulator{

    public OilSimulator(GameEngine.Materials[][] grid, int width, int height) {
        super(grid, width, height);
    }

    @Override
    protected void update() {
        Random rand = new Random();

        List<Integer> horizontalPositions = shuffle();
        for (int y = height -2; y >= 0; y--) {
            for (int x: horizontalPositions) {
                if (grid[x][y]== GameEngine.Materials.oil && grid[x][y].isMoreDenseThan(grid[x][y + 1].getDensity())) {
                    grid[x][y] = grid[x][y + 1];
                    grid[x][y + 1] = GameEngine.Materials.oil;
                }else if (grid[x][y] == GameEngine.Materials.oil && !grid[x][y + 1].equals(GameEngine.Materials.empty)) {
                    if (!grid[x-1][y].isSolid() && !grid[x+1][y].isSolid()) {
                        int direction = rand.nextBoolean() ? -1 : 1;
                        grid[x][y] =  grid[x + direction][y];
                        grid[x + direction][y] = GameEngine.Materials.oil;
                    }

                }
            }
        }
    }

    public void dispose(){
    }



    public void resize(int width, int height) {
        this.width = width;
        this.height = height;


    }

    @Override
    public GameEngine.Materials getMaterial() {
        return GameEngine.Materials.oil;
    }

}
