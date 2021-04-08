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

public class MapSelect implements Screen {
    private final Main game;
    BitmapFont font = new BitmapFont();

    private Texture cursorImage = new Texture(Gdx.files.internal("sprites/cursor.png"));

    private Vector2 cursorPos;

    private MenuCursor cursor;
    ShapeRenderer shapeRenderer;
    private Button trainingRoom;
    private Button halberd;
    // menuscene function
    public MapSelect(Main game) {
        this.game = game;
        //font used
        cursorPos = new Vector2(Main.WIDTH / 2f, Main.HEIGHT / 2f);

        try {
            cursor = new MenuCursor(cursorPos, Main.controllers.get(0), cursorImage, game, new Vector2(1234, 675), new Vector2(45, 45), new Rectangle());
        } catch (Exception e) {
            cursor = new MenuCursor(cursorPos, null, cursorImage, game, new Vector2(1234, 675), new Vector2(45, 45), new Rectangle());
        }

        trainingRoom = new Button(cursor, new Rectangle(), new Vector2(200, 400), new Vector2(150, 50));
        halberd = new Button(cursor, new Rectangle(), new Vector2(450, 400), new Vector2(150, 50));

    }

    public void update(float deltaTime) {
        cursor.update(deltaTime);

        if (halberd.isClicked(cursor.controller)) {
            game.setScreen(new LoadTMap("Halberd", game, new Vector2(-350, 0)));
        }

        if (trainingRoom.isClicked(cursor.controller)) {
            game.setScreen(new LoadTMap("Training Room", game, new Vector2(-350, 0)));
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.batch.draw(cursor.cursorImage, cursor.cursorPos.x, cursor.cursorPos.y);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.rect(cursor.cursorRect.getX(), cursor.cursorRect.getY(), cursor.cursorRect.getWidth(), cursor.cursorRect.getHeight());

        shapeRenderer.rect(trainingRoom.buttonRect.getX(), trainingRoom.buttonRect.getY(), trainingRoom.buttonRect.getWidth(), trainingRoom.buttonRect.getHeight());
        shapeRenderer.rect(halberd.buttonRect.getX(), halberd.buttonRect.getY(), halberd.buttonRect.getWidth(), halberd.buttonRect.getHeight());

        font.draw(game.batch, "Training Room", 214, 435);
        font.draw(game.batch, "Halberd", 473, 435);
        game.batch.end();
        shapeRenderer.end();
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
