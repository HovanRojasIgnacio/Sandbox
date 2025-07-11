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

    public Materials[][] materialGrid;
    public List<Simulator> simulatorList;

    public GameEngine(int width, int height) {
        materialGrid = new Materials[width][height];
        this.width = width;
        this.height = height;
        Arrays.stream(materialGrid).forEach(m -> Arrays.fill(m, Materials.empty));
        simulatorList = new ArrayList<>();
        simulatorList.add(new SandSimulator(materialGrid,width, height));
    }

    public void simulate() {
        for(Simulator simulator : simulatorList) {
            materialGrid = simulator.simulate(materialGrid);

        }
    }

    public void dispose(){
        simulatorList.forEach(Simulator::dispose);
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        Materials[][] newMaterialGrid = new Materials[width][height];

        for (int x = 0; x < Math.min(materialGrid.length, width); x++) {
            System.arraycopy(materialGrid[x], 0, newMaterialGrid[x], 0, Math.min(materialGrid[0].length, height));
        }
        materialGrid = newMaterialGrid;
    }

    public List<Pixmap> getPixmap() {
        return simulatorList.stream().map(Simulator::getPixmap).collect(Collectors.toList());
    }

    public void printGrid() {
        for (int x = 0; x < Math.min(materialGrid.length, width); x++) {
            for (int y = 0; y < Math.min(materialGrid[0].length, height); y++) {
                System.out.print(materialGrid[x][y]+" ");
            }
            System.out.println();
        }

    }

}
