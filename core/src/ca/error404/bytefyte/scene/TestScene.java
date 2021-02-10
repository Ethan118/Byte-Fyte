package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.tools.CutscenePlayer;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.TestChar;
import ca.error404.bytefyte.tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Set;

public class TestScene implements Screen {
    private Main game;
    private OrthographicCamera cam;
    private Viewport viewport;

    private World world;
    private Box2DDebugRenderer b2dr;
    private Body b2body;

    private TestChar player;

    private Music music;

    Texture icon;
    CutscenePlayer videoPlayer = new CutscenePlayer("test 2");

    public TestScene(Main game) {
        // sets up variables
        this.game = game;
        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH / Main.PPM, Main.HEIGHT / Main.PPM, cam);
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        player = new TestChar(this);

        icon = new Texture("icons/mac.png");

        world.setContactListener(new WorldContactListener());

        // creates rectangle for player to stand on (TEMP)
        BodyDef bdef = new BodyDef();
        bdef.position.set(0, -30 / Main.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(100 / Main.PPM,10 / Main.PPM);
        fdef.friction = 0;
        fdef.filter.categoryBits = Tags.GROUND_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef);

        // plays a song so I can hear things
        game.songFromSeries("other");
        music = game.manager.get("audio/music/" + Main.songName + ".wav", Music.class);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        update(deltaTime);

        // draw everything to the screen
        viewport.apply();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        if (videoPlayer.isPlaying()) {
            videoPlayer.draw(game.batch);
        }
        game.batch.end();


        if (!videoPlayer.isPlaying()) {
            b2dr.render(world, cam.combined);
        }
    }

    public void update(float deltaTime) {
        // music looping
        if (music.getPosition() >= game.songLoopEnd) {
            music.setPosition((float) (music.getPosition() - (game.songLoopEnd - game.songLoopStart)));
        }

        // stop video if playing
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            videoPlayer.stop();
        }

        // start video if not playing
        if (Gdx.input.isKeyJustPressed(Input.Keys.P) && !videoPlayer.isPlaying()) {
            videoPlayer.play();
            music.pause();
        } else if (!videoPlayer.isPlaying()) {
            // update all objects and physics objects
            player.update(deltaTime);
            world.step(1 / 60f, 6, 2);
            if (!music.isPlaying()) {
                music.play();
            }
        }

        // clear all controller inputs
        Set<Controller> keys = Main.recentButtons.keySet();
        for (Controller key : keys) {
            Main.recentButtons.get(key).clear();
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
