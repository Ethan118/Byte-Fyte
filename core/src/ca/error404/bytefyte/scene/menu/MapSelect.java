package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.LoadBattleMap;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MapSelect extends MenuScene {
    private final Main game;
    BitmapFont font = new BitmapFont();

    private MenuCursor cursor;
    private MenuCursor cursor2;

    ShapeRenderer shapeRenderer;
    private Button trainingRoom;
    private Button halberd;

    // menuscene function
    public MapSelect(Main game) {
        super(game);
        this.game = game;

        trainingRoom = new Button(new Rectangle(), new Vector2(200, 400), new Vector2(150, 50), game);
        halberd = new Button(new Rectangle(), new Vector2(450, 400), new Vector2(150, 50), game);

    }

    public void update(float deltaTime) {
        cursor.update(deltaTime);
        cursor2.update(deltaTime);

        if (halberd.isClicked()) {
            game.setScreen(new LoadBattleMap("Halberd", game, new Vector2(-350, 0)));
        }

        else if (trainingRoom.isClicked()) {
            game.setScreen(new LoadBattleMap("Training Room", game, new Vector2(-350, 0)));
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

//        game.batch.draw(cursor.cursorImage, cursor.cursorPos.x, cursor.cursorPos.y);
//        game.batch.draw(cursor2.cursorImage, cursor2.cursorPos.x, cursor2.cursorPos.y);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.rect(cursor.cursorRect.getX(), cursor.cursorRect.getY(), cursor.cursorRect.getWidth(), cursor.cursorRect.getHeight());
        shapeRenderer.rect(cursor2.cursorRect.getX(), cursor2.cursorRect.getY(), cursor2.cursorRect.getWidth(), cursor2.cursorRect.getHeight());

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
