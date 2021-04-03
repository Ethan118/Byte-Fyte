package ca.error404.bytefyte.scene;



// imports
import ca.error404.bytefyte.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class MenuScene implements Screen {

    //delairing variables
    ShapeRenderer shapeRenderer;
    BitmapFont font;
    private final Main game;



    // menuscene function
    public MenuScene(Main game) {
        this.game = game;

    }

    @Override
    public void show() {

    }

    @Override
    //render function
    public void render(float delta) {


        //font used
        BitmapFont font = new BitmapFont();

        //drawing things
        Gdx.gl.glClearColor(0, 0.5f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        //text drawing
        font.draw(game.batch, "Welcome to Byte fyte!", Gdx.graphics.getWidth()*.4f, Gdx.graphics.getHeight() * .85f);
        font.draw(game.batch, "Options", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .3f);
        font.draw(game.batch, "Story", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .4f);
        font.draw(game.batch, "Leaderboard", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .5f);
        font.draw(game.batch, "Account", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .6f);
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

    }
}
