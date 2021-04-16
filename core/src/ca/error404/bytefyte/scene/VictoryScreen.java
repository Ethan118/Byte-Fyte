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

public class VictoryScreen implements Screen {
    private final Main game;
    BitmapFont font = new BitmapFont();

    private Texture checkBox = new Texture(Gdx.files.internal("sprites/ready1.png"));
    private Texture checkMark = new Texture(Gdx.files.internal("sprites/checkmark.png"));

    private Button readyButton;
    private boolean[] ready;
    public String[] characters;
    private MenuCursor[] cursors;

    private int numOfPlayers = 0;
    public VictoryScreen(Main game, String[] characters, MenuCursor[] cursors) {
        this.game = game;
        this.characters = characters;
        this.cursors = cursors;

        for (String character: characters) {
            if (character != null) {
                numOfPlayers += 1;
            }
        }
        ready = new boolean[numOfPlayers];

        readyButton = new Button(cursors, new Rectangle(), new Vector2(0, 0), new Vector2(8000, 8000));
    }

    public void update(float deltaTime) {
        for (MenuCursor cursor: cursors) {
            if (cursor != null) {
                cursor.update(deltaTime);
            }
        }
        if (readyButton.isClicked() != 0) {
            System.out.println("tr");
            ready[readyButton.isClicked() - 1] = true;
        }

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0.5f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

//        if (Main.players.size() == 1) {
//            game.batch.draw(checkBox, 500, 500);
//        } else if (Main.players.size() == 2) {
//            game.batch.draw(checkBox, 250, 500);
//            game.batch.draw(checkBox, 750, 500);
//        } else if (Main.players.size() == 3) {
//            game.batch.draw(checkBox, 250, 500);
//            game.batch.draw(checkBox, 500, 500);
//            game.batch.draw(checkBox, 750, 500);
//        } else if (Main.players.size() == 4) {
//            game.batch.draw(checkBox, 200, 500);
//            game.batch.draw(checkBox, 400, 500);
//            game.batch.draw(checkBox, 600, 500);
//            game.batch.draw(checkBox, 800, 500);
//        }
//
        for (MenuCursor cursor: cursors) {
            if (cursor != null) {
                game.batch.draw(cursor.cursorImage, cursor.cursorPos.x, cursor.cursorPos.y);
            }
        }
        for (int i = 0; i < numOfPlayers; i++) {
            game.batch.draw(checkBox, ((float)900 / numOfPlayers) * (i + 1), 500);
            if (ready[i] == true) {
                game.batch.draw(checkMark, ((float)900 / numOfPlayers) * (i + 1), 500);
            }
        }

        font.draw(game.batch, "Shy Guy", 204, 440);
        font.draw(game.batch, "Master Chief", 404, 440);
        font.draw(game.batch, "Kirby", 604, 440);

        game.batch.end();
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
    }
}
