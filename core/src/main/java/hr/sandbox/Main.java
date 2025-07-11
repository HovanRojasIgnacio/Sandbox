package hr.sandbox;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import hr.sandbox.pixels.SandSimulator;

public class Main extends ApplicationAdapter {

    private int width;
    private int height;

    public Texture texture;
    public SpriteBatch batch;

    private SandSimulator sandSimulator;

    public final static int SCALE = 4;
    @Override
    public void create() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        sandSimulator = new SandSimulator(width, height);
        texture = new Texture(width, height, Pixmap.Format.RGBA8888);
        batch = new SpriteBatch();

    }

    @Override
    public void render() {
        sandSimulator.update();

        texture.draw(sandSimulator.getPixmap(), 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texture, 0, 0, width, height);
        batch.end();
    }







    @Override
    public void dispose() {
        sandSimulator.dispose();
        texture.dispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        // Dispose old buffers
        texture.dispose();
        sandSimulator.resize(width,height);
        // Create new buffers
        texture = new Texture(width, height, Pixmap.Format.RGBA8888);

        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }
}
