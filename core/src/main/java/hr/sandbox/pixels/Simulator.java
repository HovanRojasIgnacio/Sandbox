package hr.sandbox.pixels;

import com.badlogic.gdx.graphics.Pixmap;

public interface Simulator {

    GameEngine.Materials[][] simulate(GameEngine.Materials[][] grid);
    void dispose();

    Pixmap getPixmap();
}
