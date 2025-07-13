package hr.sandbox.pixels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import hr.sandbox.pixels.simulator.*;

import java.util.*;

public class GameEngine {

    public enum Materials{
        water(1.0, Color.BLUE,false),
        wood(Double.MAX_VALUE, Color.BROWN,true),
        oil(0.8, Color.GRAY,false),
        sand(2.0, Color.YELLOW,true),
        empty(0.0, Color.BLACK,false);

        private final double density;
        private final Color color;
        private final boolean isSolid;

        Materials(double density, Color color, boolean isSolid) {
            this.density = density;
            this.color = color;
            this.isSolid = isSolid;
        }

        public double getDensity() {
            return density;
        }
        public Color getColor() {
            return color;
        }
        public boolean isSolid() {
            return isSolid;
        }

        public boolean isMoreDenseThan(double density) {
            return this.density > density;
        }
    }

    private int width;
    private int height;

    public Materials[][] grid;
    public List<Simulator> simulatorList;
    private Pixmap gamePixmap;

    private Materials selectedMaterial = Materials.sand;

    public int brushSize = 1;

    private Map<Materials, Boolean> currentMaterialsOnScreen;

    public GameEngine(int width, int height) {
        grid = new Materials[width][height];
        this.width = width;
        this.height = height;
        Arrays.stream(grid).forEach(m -> Arrays.fill(m, Materials.empty));
        currentMaterialsOnScreen = new HashMap<>();
        simulatorList = new ArrayList<>();
        simulatorList.add(new SandSimulator(grid,width, height));
        simulatorList.add(new WaterSimulator(grid,width, height));
        simulatorList.add(new OilSimulator(grid,width, height));
        simulatorList.add(new WoodSimulator(grid,width, height));
        resetMaterials();
    }

    private void resetMaterials() {
        currentMaterialsOnScreen.put(Materials.empty,true);
        for (Simulator simulator : simulatorList) {
            currentMaterialsOnScreen.put(simulator.getMaterial(),false);
        }
    }

    public void setSelectedMaterial(Materials material) {
        selectedMaterial = material;
    }

    public void handleTouchInput(int screenX, int screenY, int button) {
        if (screenX >= 1 && screenX < width-1 && screenY >= 0 && screenY < height) {
            if (button == Input.Buttons.LEFT) {
                spawnMaterial(screenX, screenY);
            }else if(button == Input.Buttons.RIGHT) {
                brushSize = brushSize + 1;
                if(brushSize > 4) {
                    brushSize = 1;
                }
            }
        }
    }

    private void spawnMaterial(int centerX, int centerY) {
        int radius = brushSize - 1;
        int radiusSquared = radius * radius;

        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int y = centerY - radius; y <= centerY + radius; y++) {
                if (x >= 0 && x < width && y >= 0 && y < height) {
                    int dx = x - centerX;
                    int dy = y - centerY;
                    int distanceSquared = (dx * dx) + (dy * dy);

                    if (distanceSquared <= radiusSquared) {
                        if (grid[x][y] != Materials.wood) {
                            grid[x][y] = selectedMaterial;
                            currentMaterialsOnScreen.put(selectedMaterial,true);
                        }
                    }
                }
            }
        }
    }

    private void drawGridToPixmap() {
        gamePixmap.setColor(Materials.empty.getColor());
        gamePixmap.fill();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y] != Materials.empty) {
                    gamePixmap.setColor(grid[x][y].getColor());
                    gamePixmap.drawPixel(x, y);
                }
            }
        }
    }

    public void simulate() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    grid[x][y] = Materials.empty;
                }
            }
            resetMaterials();
        }
        for(Simulator simulator : simulatorList) {
            if(currentMaterialsOnScreen.get(simulator.getMaterial())) {
                grid = simulator.simulate(grid);
            }
        }
        drawGridToPixmap();

    }

    public void dispose(){
        simulatorList.forEach(Simulator::dispose);
        gamePixmap.dispose();

    }

    public void resize(int width, int height) {
        if (gamePixmap != null) {
            gamePixmap.dispose();
        }

        this.width = width;
        this.height = height;

        Materials[][] newMaterialGrid = new Materials[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newMaterialGrid[x][y] = Materials.empty;
            }
        }

        for (int x = 0; x < Math.min(grid.length, width); x++) {
            System.arraycopy(grid[x], 0, newMaterialGrid[x], 0, Math.min(grid[0].length, height));
        }
        this.grid = newMaterialGrid;
        gamePixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for (Simulator simulator : simulatorList) {
            simulator.resize(width, height);
        }
    }

    public Pixmap getPixmap() {
        return gamePixmap;
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
