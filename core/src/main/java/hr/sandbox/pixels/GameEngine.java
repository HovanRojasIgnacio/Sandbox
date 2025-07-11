package hr.sandbox.pixels;

import com.badlogic.gdx.graphics.Pixmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameEngine {


    public enum Materials{
        water, sand, empty
    }

    private int width;
    private int height;

    public Materials[][] grid;
    public List<Simulator> simulatorList;

    public GameEngine(int width, int height) {
        grid = new Materials[width][height];
        this.width = width;
        this.height = height;
        Arrays.stream(grid).forEach(m -> Arrays.fill(m, Materials.empty));
        simulatorList = new ArrayList<>();
        simulatorList.add(new SandSimulator(grid,width, height));
    }

    public void simulate() {
        for(Simulator simulator : simulatorList) {
            grid = simulator.simulate(grid);

        }
    }

    public void dispose(){
        simulatorList.forEach(Simulator::dispose);
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        Materials[][] newMaterialGrid = new Materials[width][height];

        for (int x = 0; x < Math.min(grid.length, width); x++) {
            System.arraycopy(grid[x], 0, newMaterialGrid[x], 0, Math.min(grid[0].length, height));
        }
        grid = newMaterialGrid;
    }

    public List<Pixmap> getPixmap() {
        return simulatorList.stream().map(Simulator::getPixmap).collect(Collectors.toList());
    }

    public void printGrid() {
        for (int y = 0; y < Math.min(grid[0].length, height); y++) {
            for (int x = 0; x < Math.min(grid.length, width); x++) {
                System.out.print(grid[x][y] + " ");
            }
            System.out.println();
        }
    }

}
