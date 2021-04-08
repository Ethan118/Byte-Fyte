package ca.error404.bytefyte.scene;



// imports
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
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



public class MenuScene implements Screen {

    //delairing variables
    ShapeRenderer shapeRenderer;
    BitmapFont font = new BitmapFont();
    private final Main game;

    private Texture cursorImage = new Texture(Gdx.files.internal("sprites/cursor.png"));

    private Vector2 cursorPos;

    private MenuCursor cursor;

    private Button playButton;
    // menuscene function
    public MenuScene(Main game) {
        this.game = game;
        //font used
        cursorPos = new Vector2(Main.WIDTH / 2f, Main.HEIGHT / 2f);

        try {
            cursor = new MenuCursor(cursorPos, Main.controllers.get(0), cursorImage, game, new Vector2(1234, 675), new Vector2(45, 45), new Rectangle());
        } catch (Exception e) {
            cursor = new MenuCursor(cursorPos, null, cursorImage, game, new Vector2(1234, 675), new Vector2(45, 45), new Rectangle());

        }

        playButton = new Button(cursor, new Rectangle(), new Vector2(300, 400), new Vector2(100, 50));
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

        game.batch.draw(cursor.cursorImage, cursor.cursorPos.x, cursor.cursorPos.y);

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.rect(cursor.cursorRect.getX(), cursor.cursorRect.getY(), cursor.cursorRect.getWidth(), cursor.cursorRect.getHeight());

        shapeRenderer.rect(playButton.buttonRect.getX(), playButton.buttonRect.getY(), playButton.buttonRect.getWidth(), playButton.buttonRect.getHeight());
        //text drawing
        font.draw(game.batch, "Welcome to Byte fyte!", Gdx.graphics.getWidth()*.4f, Gdx.graphics.getHeight() * .85f);
        font.draw(game.batch, "Options", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .3f);
        font.draw(game.batch, "Story", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .4f);
        font.draw(game.batch, "Leaderboard", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .5f);
        font.draw(game.batch, "Fyte!", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .6f);
        game.batch.end();
        shapeRenderer.end();
    }

    public void update(float deltaTime) {
        cursor.update(deltaTime);

        if (playButton.isClicked(cursor.controller)) {
            game.setScreen(new LoadTMap("Halberd", game, new Vector2(-350, 0)));
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
