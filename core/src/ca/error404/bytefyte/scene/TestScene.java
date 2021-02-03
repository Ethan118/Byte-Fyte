package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TestScene implements Screen {
    private Main main;
    private OrthographicCamera gameCamera;
    private Viewport viewport;

    Texture icon;

    public TestScene(Main main) {
        this.main = main;
        gameCamera = new OrthographicCamera();
        viewport = new FitViewport(1980, 1080, gameCamera);

        icon = new Texture("/Users/ethanahn/IdeaProjects/Byte-Fyte/core/assets/icons/mac.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.begin();
        main.batch.draw(icon, 0, 0);
        main.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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

    }
}
