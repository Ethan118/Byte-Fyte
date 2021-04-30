package ca.error404.bytefyte.scene.menu;


import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Set;

/*
 * Pre: game instance
 * Post: creates the menuscene screen
 * */
public class MenuScene implements Screen {

    //delairing variables
    protected ShapeRenderer shapeRenderer;
    protected Texture background;
    protected Vector2 bgPos = new Vector2(0, 0);
    protected int xSpeed = -100;
    protected int ySpeed = -100;
    protected final Main game;

    protected Viewport viewport;
    protected Camera cam;

    /*
    * Constructor
    * Pre: Game instance
    * Post: New MenuScene
    * */
    public MenuScene(Main game) {
        this.game = game;
    }


    /*
     * Pre: None
     * Post: Initializes variables
     * */
    @Override
    public void show() {
        Main.cursors.clear();
        Main.buttons.clear();

        // sets up variables
        shapeRenderer = new ShapeRenderer();
        cam = new OrthographicCamera(1920, 1080);
        cam.position.set(960, 540, cam.position.z);
        cam.update();
        viewport = new FitViewport(1920, 1080, cam);
    }

    @Override
    /*
     * Pre: game instance
     * Post: handles the rendering for the screen
     * */
    public void render(float delta) {
        //drawing things
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

        game.batch.end();

        for (int i=0; i < Main.transitions.size(); i++) Main.transitions.get(i).draw();
    }

    /*
     * Pre: game instance
     * Post: creates the background
     * */
    public void drawBackground() {
        float w = background.getWidth();
        float h = background.getHeight();

        if (bgPos.x <= -(w + (1920 / 2f))) {
            bgPos.x += w;
        } else if (bgPos.x >= (w + (1920 / 2f))) {
            bgPos.x -= w;
        }

        if (bgPos.y <= -(1080 - (-1080 / 2f))) {
            bgPos.y += h;
        } else if (bgPos.y >= (1080 - (-1080 / 2f))) {
            bgPos.y -= h;
        }

        float x = bgPos.x;

        while (x > -(cam.viewportWidth)) {
            x -= w;
        }

        //draws the background
        while (x < cam.viewportWidth) {
            game.batch.draw(background, x, bgPos.y);
            game.batch.draw(background, x, bgPos.y + h);
            game.batch.draw(background, x, bgPos.y - h);
            game.batch.draw(background, x, bgPos.y - h * 2);
            game.batch.draw(background, x, bgPos.y + h * 2);

            x += w;
        }
    }

    /*
     * Pre: game instance
     * Post: handles the update function
     * */
    public void update(float deltaTime) {
        bgPos.x += xSpeed * deltaTime;
        bgPos.y += ySpeed * deltaTime;


        // changes the music
        if (game.music != null) {
            if (game.music.getPosition() >= game.songLoopEnd) {
                game.music.setPosition((float) (game.music.getPosition() - (game.songLoopEnd - game.songLoopStart)));
            }
        }

        for (MenuCursor cursor : Main.cursors) { cursor.update(deltaTime); }
        for (int i=0; i < game.buttons.size(); i++) { game.buttons.get(i).update(); }

        // clear all controller inputs
        Set<Controller> keys = Main.recentButtons.keySet();
        for (Controller key : keys) {
            Main.recentButtons.get(key).clear();
        }

        for (int i=0; i < Main.transitions.size(); i++) Main.transitions.get(i).update(deltaTime);
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

    /*
    * Pre: None
    * Post: Disposes of all applicable assets
    * */
    @Override
    public void dispose() {
        game.batch.dispose();
        shapeRenderer.dispose();
    }
}
