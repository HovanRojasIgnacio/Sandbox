package hr.sandbox.pixels.simulator;

import hr.sandbox.pixels.GameEngine.Materials;

import java.util.List;
import java.util.Random;

public class SandSimulator extends AbstractSimulator{


    public SandSimulator(Materials[][] grid, int width, int height) {
        super(grid, width, height);
    }

    public void dispose(){

    }

    @Override
    protected void update() {
        Random rand = new Random();

        List<Integer> horizontalPositions = shuffle();
        for (int y = height -2; y >= 0; y--) {
            for (int x: horizontalPositions) {
                if (grid[x][y]==Materials.sand && !grid[x][y + 1].isSolid()) {
                    grid[x][y] = grid[x][y + 1];
                    grid[x][y + 1] = Materials.sand;
                }
                else if (grid[x][y]==Materials.sand && grid[x][y + 1].isSolid()) {
                    if (!grid[x-1][y].isSolid() && !grid[x+1][y].isSolid()) {
                        int direction = rand.nextBoolean() ? -1 : 1;
                        grid[x][y] =  grid[x + direction][y];
                        grid[x + direction][y] = Materials.sand;
                    }
                    else if (!grid[x-1][y].isSolid() && grid[x+1][y].isSolid()
                        && (!grid[x-1][y+1].isSolid() || !grid[x+1][y+1].isSolid())) {
                        grid[x][y] = grid[x - 1][y];
                        grid[x - 1][y] = Materials.sand;
                    }
                    else if (grid[x-1][y].isSolid() && !grid[x+1][y].isSolid()
                        && (!grid[x-1][y+1].isSolid() || !grid[x+1][y+1].isSolid())) {
                        grid[x][y] = grid[x + 1][y];
                        grid[x + 1][y] = Materials.sand;
                    }
                }
            }
        }
    }
    @Override
    public Materials getMaterial() {
        return Materials.sand;
    }



    public void resize(int width, int height) {
        this.width = width;
        this.height = height;


    }

}
