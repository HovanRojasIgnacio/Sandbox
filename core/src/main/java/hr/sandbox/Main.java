package hr.sandbox;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import hr.sandbox.pixels.GameEngine;

public class Main extends ApplicationAdapter {

    private int width;
    private int height;

    public Texture texture;
    public SpriteBatch batch;
    private GameEngine gameEngine;

    private Stage stage;
    private Skin skin;
    private Table uiTable;

    private boolean isLeftMouseDown = false;
    private boolean isRightMouseDown = false;
    private int currentMouseX = 0;
    private int currentMouseY = 0;

    @Override
    public void create() {
        Gdx.graphics.setVSync(false);
        Gdx.graphics.setForegroundFPS(200);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        // --- Scene2D UI Setup ---
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        uiTable = new Table(skin);
        uiTable.top().left();

        // Create buttons for each material
        addMaterialButton("Sand", GameEngine.Materials.sand);
        addMaterialButton("Wood", GameEngine.Materials.wood);
        addMaterialButton("Water", GameEngine.Materials.water);
        addMaterialButton("Oil", GameEngine.Materials.oil);
        addMaterialButton("Empty", GameEngine.Materials.empty);
        uiTable.pack();
        uiTable.setSize(uiTable.getPrefWidth(), uiTable.getPrefHeight());
        float screenPadding = 10;
        uiTable.setPosition(screenPadding, height - uiTable.getHeight() - screenPadding);
        stage.addActor(uiTable);

        InputAdapter gameInputAdapter = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (stage.hit(screenX, screenY, true) != null) {
                    return false; // UI handled it
                }

                currentMouseX = screenX;
                currentMouseY = screenY;
                if (button == Input.Buttons.LEFT) {
                    isLeftMouseDown = true;
                } else if (button == Input.Buttons.RIGHT) {
                    isRightMouseDown = true;
                }
                gameEngine.handleTouchInput(screenX, screenY, button);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                // Reset mouse state when button is released
                if (button == Input.Buttons.LEFT) {
                    isLeftMouseDown = false;
                } else if (button == Input.Buttons.RIGHT) {
                    isRightMouseDown = false;
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                currentMouseX = screenX;
                currentMouseY = screenY;
                return true;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                currentMouseX = screenX;
                currentMouseY = screenY;
                return false;
            }
        };

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(gameInputAdapter);
        Gdx.input.setInputProcessor(multiplexer);

        gameEngine = new GameEngine(width, height);
        texture = new Texture(width, height, Pixmap.Format.RGBA8888);
        batch = new SpriteBatch();

    }
    private void addMaterialButton(String text, GameEngine.Materials material) {
        TextButton button = new TextButton(text, skin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameEngine.setSelectedMaterial(material);
            }
        });
        uiTable.add(button).pad(5).width(60).height(40);
    }
    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            gameEngine.printGrid();
        }
        Gdx.app.log("FPS", String.valueOf(Gdx.graphics.getFramesPerSecond()));
        if (isLeftMouseDown || isRightMouseDown) {

            if (isLeftMouseDown) {
                gameEngine.handleTouchInput(currentMouseX, currentMouseY, Input.Buttons.LEFT);
            } else if (isRightMouseDown) {
                gameEngine.handleTouchInput(currentMouseX, currentMouseY, Input.Buttons.RIGHT);
            }
        }
        gameEngine.simulate();

        texture.draw(gameEngine.getPixmap(), 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texture, 0, 0, width, height);
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        gameEngine.dispose();
        texture.dispose();
        batch.dispose();
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        texture.dispose();
        gameEngine.resize(width,height);
        texture = new Texture(width, height, Pixmap.Format.RGBA8888);
        stage.getViewport().update(width, height, true);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }
}
