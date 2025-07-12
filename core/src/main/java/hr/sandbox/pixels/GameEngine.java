package hr.sandbox.pixels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameEngine {

    public enum Materials{
        water(1.0, Color.BLUE,false),
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
    public GameEngine(int width, int height) {
        grid = new Materials[width][height];
        this.width = width;
        this.height = height;
        Arrays.stream(grid).forEach(m -> Arrays.fill(m, Materials.empty));
        simulatorList = new ArrayList<>();
        simulatorList.add(new SandSimulator(grid,width, height));
        simulatorList.add(new WaterSimulator(grid,width, height));
    }
    private void handleInput() {
        if (Gdx.input.isTouched()) {
            int mouseX = Gdx.input.getX();
            int mouseY = Gdx.input.getY();

            if (mouseX >= 1 && mouseX < width-1 && mouseY >= 0 && mouseY < height) {
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    grid[mouseX][mouseY] = Materials.sand;
                } else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    grid[mouseX][mouseY] = Materials.water;
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        grid[x][y] = Materials.empty;
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
        handleInput();
        for(Simulator simulator : simulatorList) {
            grid = simulator.simulate(grid);
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
