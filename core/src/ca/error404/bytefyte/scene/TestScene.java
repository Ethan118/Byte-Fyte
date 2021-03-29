package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.HUD;
import ca.error404.bytefyte.chars.DeathWall;
import ca.error404.bytefyte.chars.ShyGuy;
import ca.error404.bytefyte.chars.Wall;
import ca.error404.bytefyte.constants.Globals;
import ca.error404.bytefyte.constants.ScreenSizes;
import ca.error404.bytefyte.objects.BattleCam;
import ca.error404.bytefyte.tools.CutscenePlayer;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private final HUD hud;

    public Character player;

    private final World world;
    private final Box2DDebugRenderer b2dr;

    CutscenePlayer videoPlayer = new CutscenePlayer("delivery dance");

    public TestScene(Main game) {
        // sets up variables
        this.game = game;
        cam = new BattleCam();
        viewport = new FitViewport(Main.WIDTH / Main.PPM, Main.HEIGHT / Main.PPM, cam);
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        if (Main.controllers.size > 0) {
            player = new ShyGuy(this, new Vector2(-38, 150), Main.controllers.get(0), 1);
            new ShyGuy(this, new Vector2(38, 150),  null, 2);
        } else {
            player = new ShyGuy(this, new Vector2(-38, 150), null, 1);
            new ShyGuy(this, new Vector2(38, 150), null, 2);
        }

        player.facingLeft = false;

        world.setContactListener(new WorldContactListener());

        new Wall(0, -30, 100, 10, this);
        new Wall(-75, 65, 20, 20, this);
        new Wall(75, 65, 20, 20, this);
        new DeathWall(0, -400, 1000, 10, this);
        new DeathWall(0, 500, 1000, 10, this);
        new DeathWall(-225, 0, 10, 1000, this);
        new DeathWall(225, 0, 10, 1000, this);

        // plays a song so I can hear things
        game.music = game.newSong();
        game.music.setVolume(Main.musicVolume / 10f);
        game.music.play();

        hud = new HUD(game.batch);
    }

    // function is called in between constructor and first render; is required for the Screen class
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
        if (videoPlayer.isPlaying()) {
            videoPlayer.draw(game.batch);
        }
        viewport.apply();
        for (GameObject obj : Main.gameObjects) obj.draw(game.batch);
        hud.draw(game.batch);
        game.batch.end();

        if (!videoPlayer.isPlaying()) {
            b2dr.render(world, cam.combined);
        }

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void update(float deltaTime) {
        // game.music looping
        if (game.music.getPosition() >= game.songLoopEnd) {
            game.music.setPosition((float) (game.music.getPosition() - (game.songLoopEnd - game.songLoopStart)));
        }

        // stop video if playing
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            videoPlayer.stop();
        }

        // Set game.music volume
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            Main.musicVolume = Main.musicVolume < 10 ? Main.musicVolume + 1 : 10;

            // Writes data to the settings file
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

            // Writes data to the settings file
            File settings = new File(Globals.workingDirectory + "settings.ini");

            try {
                Wini ini = new Wini(settings);
                ini.add("Settings", "music volume", Main.musicVolume);
                ini.store();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Adjusts screen size, then writes screen size to settings file
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

        game.music.setVolume(Main.musicVolume / 10f);

        // toggles fullscreen
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0), ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1));
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }

        // Scene Switching test
        if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            game.music.stop();
            System.out.println(game.music);
            game.setScreen(new TestScene2(game));
        }

        // start video if not playing
        if (Gdx.input.isKeyJustPressed(Input.Keys.P) && !videoPlayer.isPlaying()) {
            videoPlayer.play();
            game.music.pause();
        } else if (!videoPlayer.isPlaying()) {
            // update all objects and physics objects
            world.step(1 / 60f, 6, 2);
            for (GameObject obj : Main.gameObjects) {
                if (obj.remove) {
                    world.destroyBody(obj.b2body);
                    Main.objectsToRemove.add(obj);
                }
                obj.update(deltaTime);
            }

            hud.update(deltaTime);

            // Manage which game objects are active
            Main.gameObjects.addAll(Main.objectsToAdd);
            Main.gameObjects.removeAll(Main.objectsToRemove);
            Main.objectsToAdd.clear();
            Main.objectsToRemove.clear();

            if (!game.music.isPlaying()) {
                game.music.play();
            }
        }

        // clear all controller inputs
        Set<Controller> keys = Main.recentButtons.keySet();
        for (Controller key : keys) {
            Main.recentButtons.get(key).clear();
        }
    }

    // updates screen size
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        cam.update();
    }

    @Override
    public void pause() {
        game.music.pause();
    }

    @Override
    public void resume() {
        game.music.play();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public World getWorld() {
        return world;
    }
}
