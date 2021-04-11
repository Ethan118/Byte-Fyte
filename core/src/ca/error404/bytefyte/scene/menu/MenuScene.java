package ca.error404.bytefyte.scene.menu;


import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.LoadBattleMap;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScene implements Screen {

    //delairing variables
    ShapeRenderer shapeRenderer;
    private Texture background = new Texture("sprites/menu/main_bg.png");
    private Vector2 bgPos = new Vector2(0, 0);
    private int speed = -100;
    private final Main game;

    protected Viewport viewport;
    private Camera cam;

    private Vector2 cursorPos;
    private Vector2 cursor2Pos;

    // menuscene function
    public MenuScene(Main game) {
        this.game = game;
        game.cursors.clear();
        game.buttons.clear();

        shapeRenderer = new ShapeRenderer();

        cam = new OrthographicCamera(1920, 1080);
        cam.position.set(960, 540, cam.position.z);
        cam.update();
        viewport = new FitViewport(1920, 1080, cam);

        cursorPos = new Vector2(Main.WIDTH / 2f, Main.HEIGHT / 2f);
        cursor2Pos = new Vector2(Main.WIDTH / 2f, Main.HEIGHT / 2f);

        new MenuCursor(cursorPos, null, game, new Vector2(1234, 675), new Vector2(45, 45), new Rectangle());

        try {
            new MenuCursor(cursor2Pos, Main.controllers.get(0), game, new Vector2(1234, 675), new Vector2(45, 45), new Rectangle());
        } catch (Exception e) {
            new MenuCursor(cursor2Pos, null, game, new Vector2(1234, 675), new Vector2(45, 45), new Rectangle());
        }

        game.buttons.add(new Button(new Rectangle(), new Vector2(300, 400), new Vector2(100, 50), game) { public void click() {
            game.setScreen(new LoadBattleMap("Russia", game, new Vector2(0, 0)));
        }});

        game.music = game.newSong("menu");
        game.music.setVolume(Main.musicVolume / 10f);
        game.music.play();
    }

    @Override
    public void show() {

    }

    @Override
    //render function
    public void render(float delta) {
        //drawing things
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);
        game.batch.begin();

        drawBackground();

        for (MenuCursor cursor : Main.cursors) { cursor.draw(game.batch); }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (MenuCursor cursor : Main.cursors) { shapeRenderer.rect(cursor.cursorRect.getX(), cursor.cursorRect.getY(), cursor.cursorRect.getWidth(), cursor.cursorRect.getHeight()); }

        for (Button button : game.buttons) { shapeRenderer.rect(button.buttonRect.getX(), button.buttonRect.getY(), button.buttonRect.getWidth(), button.buttonRect.getHeight()); }

        game.batch.end();
        shapeRenderer.end();
    }

    public void drawBackground() {
        float w = (cam.viewportHeight / background.getHeight()) * background.getWidth();

        if (bgPos.x <= -(w + (1920 / 2f))) {
            bgPos.x += w;
        } else if (bgPos.x >= (w + (1920 / 2f))) {
            bgPos.x -= w;
        }

        if (bgPos.y <= -(1080 - (-1080 / 2f))) {
            bgPos.y += cam.viewportHeight;
        } else if (bgPos.y >= (1080 - (-1080 / 2f))) {
            bgPos.y -= cam.viewportHeight;
        }

        float x = bgPos.x;

        while (x > -(cam.viewportWidth)) {
            x -= w;
        }

        while (x < cam.viewportWidth) {
            game.batch.draw(background, x, bgPos.y, w, cam.viewportHeight);
            game.batch.draw(background, x, bgPos.y + cam.viewportHeight, w, cam.viewportHeight);
            game.batch.draw(background, x, bgPos.y - cam.viewportHeight, w, cam.viewportHeight);
            game.batch.draw(background, x, bgPos.y - cam.viewportHeight * 2, w, cam.viewportHeight);

            x += w;
        }
    }

    public void update(float deltaTime) {
        bgPos.x += speed * deltaTime;
        bgPos.y += speed * deltaTime;
        if (game.music.getPosition() >= game.songLoopEnd) {
            game.music.setPosition((float) (game.music.getPosition() - (game.songLoopEnd - game.songLoopStart)));
        }

        for (MenuCursor cursor : Main.cursors) { cursor.update(deltaTime); }
        for (Button button : game.buttons) { button.update(); }
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
