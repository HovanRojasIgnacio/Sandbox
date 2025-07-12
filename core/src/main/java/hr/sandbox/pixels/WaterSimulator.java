package hr.sandbox.pixels;

import java.util.List;
import java.util.Random;

public class WaterSimulator extends AbstractSimulator{

    public WaterSimulator(GameEngine.Materials[][] grid, int width, int height) {
        super(grid, width, height);
    }

    @Override
    protected void update() {
        Random rand = new Random();

        List<Integer> horizontalPositions = shuffle();
        for (int y = height -2; y >= 0; y--) {
            for (int x: horizontalPositions) {
                if (grid[x][y]== GameEngine.Materials.water && grid[x][y].isMoreDenseThan(grid[x][y + 1].getDensity())) {
                    grid[x][y] = grid[x][y + 1];
                    grid[x][y + 1] = GameEngine.Materials.water;
                }else if (grid[x][y] == GameEngine.Materials.water && !grid[x][y + 1].equals(GameEngine.Materials.empty)) {
                    if (!grid[x-1][y].isSolid() && !grid[x+1][y].isSolid()) {
                        int direction = rand.nextBoolean() ? -1 : 1;
                        grid[x][y] =  grid[x + direction][y];
                        grid[x + direction][y] = GameEngine.Materials.water;
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

}
