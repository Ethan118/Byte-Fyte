package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.chars.DeathWall;
import ca.error404.bytefyte.chars.Wall;
import ca.error404.bytefyte.constants.Globals;
import ca.error404.bytefyte.constants.ScreenSizes;
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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class TestScene implements Screen {
    private final Main game;
    private final OrthographicCamera cam;
    private final Viewport viewport;

    private final World world;
    private final Box2DDebugRenderer b2dr;

    public final TestChar player;

    private Music music;

    CutscenePlayer videoPlayer = new CutscenePlayer("test 2");

    public TestScene(Main game) {
        // sets up variables
        this.game = game;
        cam = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH / Main.PPM, Main.HEIGHT / Main.PPM, cam);
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        player = new TestChar(this);

        world.setContactListener(new WorldContactListener());

        new Wall(0, -30, 100, 10, this);
        new DeathWall(0, -200, 1000, 10, this);
        new DeathWall(0, 200, 1000, 10, this);
        new DeathWall(-225, 0, 10, 1000, this);
        new DeathWall(225, 0, 10, 1000, this);

        // plays a song so I can hear things
        music = game.newSong();
        music.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        update(deltaTime);

        // draw everything to the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.draw(game.batch);
        if (videoPlayer.isPlaying()) {
            videoPlayer.draw(game.batch);
        }
        game.batch.end();

        if (!videoPlayer.isPlaying()) {
            //b2dr.render(world, cam.combined);
        }
    }

    public void update(float deltaTime) {
        // music looping
        if (music.getPosition() >= game.songLoopEnd) {
            System.out.println(music.getPosition());
            music.setPosition((float) (music.getPosition() - (game.songLoopEnd - game.songLoopStart)));
            System.out.println(music.getPosition());
        }

        // stop video if playing
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            videoPlayer.stop();
        }

        // Set music volume
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            Main.musicVolume = Main.musicVolume < 10 ? Main.musicVolume + 1 : 10;

            File settings = new File(Globals.workingDirectory + "settings.ini");

            try {
                Wini ini = new Wini(settings);
                ini.add("Settings", "music volume", Main.musicVolume);
                ini.store();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            Main.musicVolume = Main.musicVolume > 0 ? Main.musicVolume - 1 : 0;

            File settings = new File(Globals.workingDirectory + "settings.ini");

            try {
                Wini ini = new Wini(settings);
                ini.add("Settings", "music volume", Main.musicVolume);
                ini.store();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
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
        }

        music.setVolume(Main.musicVolume / 10f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0), ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1));
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }

        // start video if not playing
        if (Gdx.input.isKeyJustPressed(Input.Keys.P) && !videoPlayer.isPlaying()) {
            videoPlayer.play();
            music.pause();
        } else if (!videoPlayer.isPlaying()) {
            // update all objects and physics objects
            world.step(1 / 60f, 6, 2);
            player.update(deltaTime);
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
        cam.update();
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
