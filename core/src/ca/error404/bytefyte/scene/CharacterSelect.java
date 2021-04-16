package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CharacterSelect implements Screen {
    private final Main game;
    BitmapFont font = new BitmapFont();

    public boolean[] charsSelected = new boolean[4];

    public MenuCursor[] cursors;

    ShapeRenderer shapeRenderer;
    private Button shyGuyButton;
    private Button masterChiefButton;
    private Button kirbyButton;

    public String[] characters = {null, null, null, null};

    // menuscene function
    public CharacterSelect(Main game, MenuCursor[] cursors) {
        this.game = game;

        this.cursors = cursors;


        shyGuyButton = new Button(cursors, new Rectangle(), new Vector2(200, 400), new Vector2(150, 50));
        masterChiefButton = new Button(cursors, new Rectangle(), new Vector2(400, 400), new Vector2(150, 50));
        kirbyButton = new Button(cursors, new Rectangle(), new Vector2(600, 400), new Vector2(150, 50));


    }

    public void update(float deltaTime) {
        System.out.println("O");
        for (MenuCursor cursor: cursors) {
            if (cursor != null) {
                cursor.update(deltaTime);
            }
        }

        for (String character: characters) {
            System.out.println(character);
        }

        for (int i = 0; i < cursors.length; i++) {
            if (masterChiefButton.isClicked() == i + 1) {
                characters[i] = "Master Chief";
            } else if (shyGuyButton.isClicked() == i + 1) {
                characters[i] = "Shy Guy";
            } else if (kirbyButton.isClicked() == i + 1) {
                characters[i] = "Kirby";
            }
        }


        for (int i = 0; i < characters.length; i++) {
            if(cursors[i] != null) {
                if (characters[i] != null) {
                    charsSelected[i] = true;
                } else {
                    charsSelected[i] = false;
                }
            }
        }

        if (checkChars()) {
            System.out.println(characters);
            game.setScreen(new MapSelect(game, cursors, characters, this));
        }

    }

    private boolean checkChars() {
        for (int i = 0; i < cursors.length; i++) {
            if (cursors[i] != null) {
                if(!charsSelected[i]) {
                    return false;
                }
            }
        }
        return true;
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
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (MenuCursor cursor: cursors) {
            if (cursor != null) {
                game.batch.draw(cursor.cursorImage, cursor.cursorPos.x, cursor.cursorPos.y);
                shapeRenderer.rect(cursor.cursorRect.getX(), cursor.cursorRect.getY(), cursor.cursorRect.getWidth(), cursor.cursorRect.getHeight());
            }
        }

        shapeRenderer.rect(shyGuyButton.buttonRect.getX(), shyGuyButton.buttonRect.getY(), shyGuyButton.buttonRect.getWidth(), shyGuyButton.buttonRect.getHeight());
        shapeRenderer.rect(masterChiefButton.buttonRect.getX(), masterChiefButton.buttonRect.getY(), masterChiefButton.buttonRect.getWidth(), masterChiefButton.buttonRect.getHeight());
        shapeRenderer.rect(kirbyButton.buttonRect.getX(), kirbyButton.buttonRect.getY(), kirbyButton.buttonRect.getWidth(), kirbyButton.buttonRect.getHeight());

        font.draw(game.batch, "Shy Guy", 204, 440);
        font.draw(game.batch, "Master Chief", 404, 440);
        font.draw(game.batch, "Kirby", 604, 440);

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
