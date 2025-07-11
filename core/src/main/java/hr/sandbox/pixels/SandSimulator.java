package hr.sandbox.pixels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SandSimulator {

    private int width;
    private int height;
    private Pixmap pixmap;
    private boolean[][] sand;

    public SandSimulator(int width, int height) {
        this.width = width;
        this.height = height;
        sand = new boolean[width][height];
        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
    }
    private void drawSand() {
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        pixmap.setColor(Color.YELLOW);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (sand[x][y]) {
                    pixmap.drawPixel(x, y);
                }
            }
        }
    }
    public void dispose(){
        pixmap.dispose();

    }
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        pixmap.dispose();
        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        boolean[][] newSand = new boolean[width][height];
        for (int x = 0; x < Math.min(sand.length, width); x++) {
            System.arraycopy(sand[x], 0, newSand[x], 0, Math.min(sand[0].length, height));
        }
        sand = newSand;
    }
    private void handleInput() {
        if (Gdx.input.isTouched()) {
            int mouseX = Gdx.input.getX();
            int mouseY = Gdx.input.getY();
            if (mouseX >= 0 && mouseX < width && mouseY >= 0 && mouseY < height) {
                sand[mouseX][mouseY] = true;
            }

        }
    }
    public void update() {
        handleInput();
        updateGravity();
        updateHorizontally();
        drawSand();
    }
    private void updateGravity() {
        for (int y = height -2; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (sand[x][y] && !sand[x][y + 1]) {
                    sand[x][y] = false;
                    sand[x][y + 1] = true;
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
                if (sand[x][y] && sand[x][y + 1]) {
                    if (!sand[x-1][y] && !sand[x + 1][y]) {
                        int direction = rand.nextBoolean() ? -1 : 1;
                        sand[x][y] = false;
                        sand[x + direction][y] = true;
                    }
                    else if (!sand[x-1][y] && sand[x + 1][y] && (!sand[x-1][y+1] || !sand[x+1][y+1])) {
                        sand[x][y] = false;
                        sand[x -1][y] = true;
                    }
                    else if (sand[x-1][y] && !sand[x + 1][y] && (!sand[x-1][y+1] || !sand[x+1][y+1])) {
                        sand[x][y] = false;
                        sand[x + 1][y] = true;
                    }
                }
            }
        }
    }

    public Pixmap getPixmap() {
        return pixmap;
    }
}
