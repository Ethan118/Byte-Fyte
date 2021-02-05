package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.CutscenePlayer;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.TestChar;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

public class TestScene implements Screen {
    private Main main;
    private OrthographicCamera cam;
    private Viewport viewport;

    private World world;
    private Box2DDebugRenderer b2dr;
    private Body b2body;

    private TestChar player;

    Texture icon;
    CutscenePlayer videoPlayer = new CutscenePlayer("mario galaxy skyrim");

    public TestScene(Main main) {
        this.main = main;
        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH / Main.PPM, Main.HEIGHT / Main.PPM, cam);
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        player = new TestChar(this);

        icon = new Texture("icons/mac.png");

        BodyDef bdef = new BodyDef();
        bdef.position.set(0, -30 / Main.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(100 / Main.PPM,10 / Main.PPM);
        fdef.friction = 0;

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        update(deltaTime);

        viewport.apply();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.setProjectionMatrix(cam.combined);
        main.batch.begin();
        if (videoPlayer.isPlaying()) {
            videoPlayer.draw(main.batch);
        }
        main.batch.end();


        if (!videoPlayer.isPlaying()) {
            b2dr.render(world, cam.combined);
        }
    }

    public void update(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P) && !videoPlayer.isPlaying()) {
            videoPlayer.play();
        } else {
            player.update(deltaTime);
            world.step(1 / 60f, 6, 2);
        }
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

    public World getWorld() {
        return world;
    }
}
