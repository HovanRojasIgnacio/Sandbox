package hr.sandbox.pixels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WaterSimulator extends AbstractSimulator{

    public WaterSimulator(GameEngine.Materials[][] grid, int width, int height) {
        super(grid, width, height);
    }

    public void dispose(){
    }


    public GameEngine.Materials[][] simulate(GameEngine.Materials[][] grid) {
        this.grid = grid;
        updateGravity();
        updateHorizontally();
        return grid;
    }
    @Override
    protected void updateGravity() {
        for (int y = height -2; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (grid[x][y]== GameEngine.Materials.water && grid[x][y].isMoreDenseThan(grid[x][y + 1].getDensity())) {
                    grid[x][y] = grid[x][y + 1];
                    grid[x][y + 1] = GameEngine.Materials.water;
                }
            }
        }
    }
    @Override
    protected void updateHorizontally() {
        Random rand = new Random();

        List<Integer> horizontalPositions = new ArrayList<>();
        for (int x =  width-2; x >0; x--) {
            horizontalPositions.add(x);
        }
        Collections.shuffle(horizontalPositions);
        for (int y = height -2; y >= 0; y--) {
            for (int x: horizontalPositions) {
                if (grid[x][y] == GameEngine.Materials.water && !grid[x][y + 1].equals(GameEngine.Materials.empty)) {
                    if (!grid[x-1][y].isSolid() && !grid[x+1][y].isSolid()) {
                        int direction = rand.nextBoolean() ? -1 : 1;
                        grid[x][y] =  grid[x + direction][y];
                        grid[x + direction][y] = GameEngine.Materials.water;
                    }

                }
            }
        }
    }


    public void resize(int width, int height) {
        this.width = width;
        this.height = height;


    }

}
