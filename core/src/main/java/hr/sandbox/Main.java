package hr.sandbox;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import hr.sandbox.pixels.GameEngine;

public class Main extends ApplicationAdapter {

    private int width;
    private int height;

    public Texture texture;
    public SpriteBatch batch;

    private GameEngine gameEngine;

    @Override
    public void create() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        gameEngine = new GameEngine(width, height);
        texture = new Texture(width, height, Pixmap.Format.RGBA8888);
        batch = new SpriteBatch();

    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            gameEngine.printGrid();
        }
        gameEngine.simulate();
        texture.draw(gameEngine.getPixmap(), 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texture, 0, 0, width, height);
        batch.end();
    }

    @Override
    public void dispose() {
        gameEngine.dispose();
        texture.dispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        texture.dispose();
        gameEngine.resize(width,height);
        texture = new Texture(width, height, Pixmap.Format.RGBA8888);

        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }
}
