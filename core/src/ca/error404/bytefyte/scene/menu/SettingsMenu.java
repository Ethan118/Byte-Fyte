package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.Globals;
import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.constants.ScreenSizes;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public class SettingsMenu implements Screen {

    //delairing variables
    ShapeRenderer shapeRenderer;
    BitmapFont font;
    private final Main game;
    BitmapFont font = new BitmapFont();

    private MainMenu mainMenu;
    private MenuCursor[] cursors;

    ShapeRenderer shapeRenderer;
    private Button increaseScreenSize;
    private Button decreaseScreenSize;
    private Button back;

    // menuscene function
    public SettingsMenu(Main game, MenuCursor[] cursors, MainMenu mainMenu) {
        this.game = game;
        this.mainMenu = mainMenu;
        this.cursors = cursors;

        increaseScreenSize = new Button(cursors, new Rectangle(), new Vector2(650, 450), new Vector2(20, 20));
        decreaseScreenSize = new Button(cursors, new Rectangle(), new Vector2(450, 450), new Vector2(20, 20));
        back = new Button(cursors, new Rectangle(), new Vector2(0, 0), new Vector2(100, 50));


    }

    public void update(float deltaTime) {

        System.out.println(Main.controllers);

        for (MenuCursor cursor: cursors) {
            if (cursor != null) {
                cursor.update(deltaTime);
            }
        }

        if (decreaseScreenSize.isClicked() != 0 || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            ScreenSizes.screenSize = ScreenSizes.screenSize == 0 ? ScreenSizes.screenSizes.size() - 1 : ScreenSizes.screenSize - 1;
            Gdx.graphics.setWindowedMode(ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0), ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1));

            File settings = new File(Globals.workingDirectory + "settings.ini");

            try {
                Wini ini = new Wini(settings);
                ini.add("Settings", "screen size", ScreenSizes.screenSize);
                ini.store();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < cursors.length; i ++) {
                if (cursors[i] != null) {
                    cursors[i].cursorPos = new Vector2(Main.WIDTH * (i + 1)/1.5f, Main.HEIGHT / 2f);;
                    cursors[i].cursorRect.set(cursors[i].cursorPos.x, cursors[i].cursorPos.y, cursors[i].size.x, cursors[i].size.y);

                }
            }
        }

        else if (increaseScreenSize.isClicked() != 0 || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            ScreenSizes.screenSize = ScreenSizes.screenSize >= ScreenSizes.screenSizes.size() - 1 ? 0 : ScreenSizes.screenSize + 1;
            Gdx.graphics.setWindowedMode(ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0), ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1));

            File settings = new File(Globals.workingDirectory + "settings.ini");

            try {
                Wini ini = new Wini(settings);
                ini.add("Settings", "screen size", ScreenSizes.screenSize);
                ini.store();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < cursors.length; i ++) {
                if (cursors[i] != null) {
                    cursors[i].cursorPos = new Vector2(Main.WIDTH * (i + 1)/1.5f, Main.HEIGHT / 2f);;
                    cursors[i].cursorRect.set(cursors[i].cursorPos.x, cursors[i].cursorPos.y, cursors[i].size.x, cursors[i].size.y);

                }
            }
        }

        else if (back.isClicked() != 0) {
            game.setScreen(mainMenu);
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


        shapeRenderer.rect(increaseScreenSize.buttonRect.getX(), increaseScreenSize.buttonRect.getY(), increaseScreenSize.buttonRect.getWidth(), increaseScreenSize.buttonRect.getHeight());
        shapeRenderer.rect(decreaseScreenSize.buttonRect.getX(), decreaseScreenSize.buttonRect.getY(), decreaseScreenSize.buttonRect.getWidth(), decreaseScreenSize.buttonRect.getHeight());
        shapeRenderer.rect(back.buttonRect.getX(), back.buttonRect.getY(), back.buttonRect.getWidth(), back.buttonRect.getHeight());

        font.draw(game.batch, "Screen Size", 500, 500);
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
