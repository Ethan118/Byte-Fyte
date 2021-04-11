package ca.error404.bytefyte.scene;


import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class MainMenu implements Screen {

    //delairing variables
    ShapeRenderer shapeRenderer;
    BitmapFont font = new BitmapFont();
    private final Main game;

    private Texture cursorImage = new Texture(Gdx.files.internal("sprites/cursor.png"));

    private Vector2[] cursorPos = new Vector2[4];

    private boolean keyboardUsed = false;

    private Button playButton;
    private Button settingsButton;


    public MenuCursor[] cursors = new MenuCursor[4];

    // menuscene function
    public MainMenu(Main game) {
        this.game = game;

        for (int i = 0; i < cursorPos.length; i++) {
            cursorPos[i] = new Vector2(Main.WIDTH * (i + 1)/1.5f, Main.HEIGHT / 2f);
        }

        for (int i = 0; i < cursors.length; i++) {
            try {
                cursors[i] = new MenuCursor(cursorPos[i], Main.controllers.get(i), cursorImage, game, new Vector2(1234, 675), new Vector2(45, 45), new Rectangle());
            } catch (Exception e) {
                if (!keyboardUsed) {
                    cursors[i] = new MenuCursor(cursorPos[i], null, cursorImage, game, new Vector2(1234, 675), new Vector2(45, 45), new Rectangle());
                    keyboardUsed = true;
                } else {
                    cursors[i] = null;
                }
            }
        }

        playButton = new Button(cursors, new Rectangle(), new Vector2(300, 400), new Vector2(100, 50));
        settingsButton = new Button(cursors, new Rectangle(), new Vector2(300, 325), new Vector2(100, 50));

    }

    @Override
    public void show() {

    }

    @Override
    //render function
    public void render(float delta) {
        //drawing things
        update(delta);
        Gdx.gl.glClearColor(0, 0.5f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (MenuCursor cursor: cursors) {
            if (cursor != null) {
                game.batch.draw(cursor.cursorImage, cursor.cursorPos.x, cursor.cursorPos.y);
                shapeRenderer.rect(cursor.cursorRect.getX(), cursor.cursorRect.getY(), cursor.cursorRect.getWidth(), cursor.cursorRect.getHeight());
            }
        }

        shapeRenderer.rect(playButton.buttonRect.getX(), playButton.buttonRect.getY(), playButton.buttonRect.getWidth(), playButton.buttonRect.getHeight());
        shapeRenderer.rect(settingsButton.buttonRect.getX(), settingsButton.buttonRect.getY(), settingsButton.buttonRect.getWidth(), settingsButton.buttonRect.getHeight());

        //text drawing
        font.draw(game.batch, "Welcome to Byte fyte!", Gdx.graphics.getWidth()*.4f, Gdx.graphics.getHeight() * .85f);
        font.draw(game.batch, "Options", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .3f);
        font.draw(game.batch, "Story", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .4f);
        font.draw(game.batch, "Settings", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .5f);
        font.draw(game.batch, "Fyte!", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .6f);
        game.batch.end();
        shapeRenderer.end();
    }

    public void update(float deltaTime) {
        for (MenuCursor cursor: cursors) {
            if (cursor != null) {
                cursor.update(deltaTime);
            }
        }

        if (playButton.isClicked() != 0) {
            game.setScreen(new CharacterSelect(game, cursors));
        } else if (settingsButton.isClicked() != 0) {
            game.setScreen(new SettingsMenu(game, cursors, this));
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.batch.dispose();
        shapeRenderer.dispose();
    }




}
