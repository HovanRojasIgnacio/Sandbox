package hr.sandbox.pixels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import hr.sandbox.pixels.GameEngine.Materials;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SandSimulator implements Simulator{

    private int width;
    private int height;
    private Pixmap pixmap;
    private boolean[][] sand;
    private Materials[][] grid;

    public SandSimulator(Materials[][] grid, int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = grid;
        sand = new boolean[width][height];
        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
    }
    private void drawSand() {
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        pixmap.setColor(Color.YELLOW);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y].equals(Materials.sand)) {
                    pixmap.drawPixel(x, y);
                }
            }
        }
    }
    public void dispose(){
        pixmap.dispose();

    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            int mouseX = Gdx.input.getX();
            int mouseY = Gdx.input.getY();
            if (mouseX >= 0 && mouseX < width && mouseY >= 0 && mouseY < height) {
                grid[mouseX][mouseY] = Materials.sand;
            }

        }
    }
    public Materials[][] simulate(Materials[][] grid) {
        this.grid = grid;
        handleInput();
        updateGravity();
        updateHorizontally();
        drawSand();

        return grid;
    }
    private void updateGravity() {
        for (int y = height -2; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (grid[x][y]==Materials.sand && grid[x][y + 1] == Materials.empty ) {
                    grid[x][y] = Materials.empty;
                    grid[x][y + 1] = Materials.sand;
                } else if (grid[x][y]==Materials.sand && grid[x][y + 1] == Materials.water ) {
                    grid[x][y] = Materials.water;
                    grid[x][y + 1] = Materials.sand;
                }
            }
        }
    }

    private void updateHorizontally() {
        Random rand = new Random();

        List<Integer> horizontalPositions = new ArrayList<>();
        for (int x =  width-2; x >0; x--) {
            horizontalPositions.add(x);
        }
        Collections.shuffle(horizontalPositions);
        for (int y = height -2; y >= 0; y--) {
            for (int x: horizontalPositions) {
                if (grid[x][y]==Materials.sand && grid[x][y + 1] == Materials.empty ) {
                    if (grid[x-1][y] == Materials.empty && grid[x+1][y] == Materials.empty) {
                        int direction = rand.nextBoolean() ? -1 : 1;
                        grid[x][y] = Materials.empty;
                        grid[x + direction][y] = Materials.sand;
                    }
                    else if (grid[x-1][y] == Materials.empty && grid[x+1][y]==Materials.sand
                            && (grid[x-1][y+1] == Materials.empty || grid[x+1][y+1] == Materials.empty)) {
                        grid[x][y] = Materials.empty;
                        grid[x - 1][y] = Materials.sand;
                    }
                    else if (grid[x-1][y] == Materials.sand && grid[x+1][y] == Materials.empty
                            && (grid[x-1][y+1] == Materials.empty || grid[x+1][y+1] == Materials.empty)) {
                        grid[x][y] = Materials.empty;
                        grid[x + 1][y] = Materials.sand;
                    }
                }
            }
        }
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

}
