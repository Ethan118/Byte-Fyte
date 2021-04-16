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

    private MenuCursor[] cursors;

    ShapeRenderer shapeRenderer;
    private Button trainingRoom;
    private Button halberd;
    private String[] characters;
    private CharacterSelect characterSelect;

    // menuscene function
    public MapSelect(Main game, MenuCursor[] cursors, String[] characters, CharacterSelect characterSelect) {
        this.game = game;
        this.characters = characters;

        this.cursors = cursors;

        this.characterSelect = characterSelect;

        trainingRoom = new Button(cursors, new Rectangle(), new Vector2(400, 250), new Vector2(150, 50));
        halberd = new Button(cursors, new Rectangle(), new Vector2(730, 250), new Vector2(150, 50));


    }

    public void update(float deltaTime) {
        for (MenuCursor cursor: cursors) {
            if (cursor != null) {
                cursor.update(deltaTime);
            }
        }

        if (halberd.isClicked() != 0) {
            game.setScreen(new LoadTMap("Halberd", game, new Vector2(-350, 0), characters, characterSelect));
        }

        else if (trainingRoom.isClicked() != 0) {
            game.setScreen(new LoadTMap("Training Room", game, new Vector2(-350, 0), characters, characterSelect));
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

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (MenuCursor cursor: cursors) {
            if (cursor != null) {
                game.batch.draw(cursor.cursorImage, cursor.cursorPos.x, cursor.cursorPos.y);
                shapeRenderer.rect(cursor.cursorRect.getX(), cursor.cursorRect.getY(), cursor.cursorRect.getWidth(), cursor.cursorRect.getHeight());
            }
        }


        shapeRenderer.rect(trainingRoom.buttonRect.getX(), trainingRoom.buttonRect.getY(), trainingRoom.buttonRect.getWidth(), trainingRoom.buttonRect.getHeight());
        shapeRenderer.rect(halberd.buttonRect.getX(), halberd.buttonRect.getY(), halberd.buttonRect.getWidth(), halberd.buttonRect.getHeight());

        font.draw(game.batch, "Training Room", 415, 285);
        font.draw(game.batch, "Halberd", 753, 285);
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
