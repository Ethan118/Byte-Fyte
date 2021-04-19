package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.menu.CharacterSelect;
import ca.error404.bytefyte.scene.menu.MenuScene;
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

public class VictoryScreen extends MenuScene {
    private final Main game;
    BitmapFont font = new BitmapFont();
    private boolean keyboardUsed = false;

    private Texture checkBox = new Texture(Gdx.files.internal("sprites/ready1.png"));
    private Texture checkMark = new Texture(Gdx.files.internal("sprites/checkmark.png"));

    private Button readyButton;
    private boolean[] ready;
    public String[] characters;

    private int numOfPlayers = 0;
    public VictoryScreen(Main game, String[] characters) {
        super(game);
        this.game = game;

    }

    @Override
    public void show() {
        this.characters = CharacterSelect.characters;

        for (int i = 0; i < 4; i ++) {
            if (Main.controllers[i] != null) {
                new MenuCursor(new Vector2(0, 0), Main.controllers[i], game);
            } else if (!keyboardUsed){
                new MenuCursor(new Vector2(0, 0), null, game);
                keyboardUsed = true;
            }
        }

        for (String character: characters) {
            if (character != null) {
                numOfPlayers += 1;
            }
        }
        ready = new boolean[numOfPlayers];
        readyButton = new Button(new Vector2(0, 0), game, new Texture[] { new Texture("sprites/readyButton.png") }) {
            public void click(){
                ready[cursor.getID()] = true;
        }};
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);
        game.batch.begin();

        drawBackground();

        for (Button button : Main.buttons) { button.draw(game.batch); }

        for (MenuCursor cursor : Main.cursors) { cursor.draw(game.batch); }

        if (Main.debug) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            for (MenuCursor cursor : Main.cursors) {
                shapeRenderer.rect(cursor.rect.getX(), cursor.rect.getY(), cursor.rect.getWidth(), cursor.rect.getHeight());
            }

            for (Button button : game.buttons) {
                shapeRenderer.rect(button.buttonRect.getX(), button.buttonRect.getY(), button.buttonRect.getWidth(), button.buttonRect.getHeight());
            }

            shapeRenderer.end();
        }
        for (int i=0; i < Main.transitions.size(); i++) Main.transitions.get(i).draw();

        for (int i = 0; i < numOfPlayers; i++) {
            game.batch.draw(checkBox, ((float)900 / numOfPlayers) * (i + 1), 500);
            if (ready[i]) {
                game.batch.draw(checkMark, ((float)900 / numOfPlayers) * (i + 1), 500);
            }
        }

        game.batch.end();

        if (checkReady()) {
            new ScreenWipe(new CharacterSelect(game), game);
        }

    }

    @Override
    public void resize(int width, int height) {

    }
    private boolean checkReady() {
        for (boolean playerIsReady: ready) {
            if (!playerIsReady) {
                return false;
            }
        }
        return true;
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
