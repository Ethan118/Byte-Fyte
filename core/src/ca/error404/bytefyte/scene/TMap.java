package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.HUD;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.chars.DeathWall;
import ca.error404.bytefyte.chars.ShyGuy;
import ca.error404.bytefyte.chars.Wall;
import ca.error404.bytefyte.constants.Globals;
import ca.error404.bytefyte.constants.ScreenSizes;
import ca.error404.bytefyte.objects.BattleCam;
import ca.error404.bytefyte.tools.CutscenePlayer;
import ca.error404.bytefyte.tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class TMap implements Screen {
    private BattleCam gamecam;
    private Viewport viewport;

    private final Main game;
    private final HUD hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private MapProperties mProp;
    private World world;
    private Box2DDebugRenderer b2dr;

    CutscenePlayer videoPlayer = new CutscenePlayer("delivery dance");


    public TMap(String mapName, Main game) {
        this.game = game;

        gamecam = new BattleCam();

        viewport = new FitViewport(Main.WIDTH / Main.PPM, Main.HEIGHT/ Main.PPM, gamecam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load(mapName + ".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/Main.PPM);

        mProp = map.getProperties();


        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        Vector2 pos = Vector2.Zero;

        for (MapObject object: map.getLayers().get("Respawn Point").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            pos = new Vector2(rect.getX(), rect.getY());
        }

        gamecam.position.set(pos.x / Main.PPM, pos.y / Main.PPM, 0);

        for (MapObject object: map.getLayers().get("Ground").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Wall((int)(rect.getX() + rect.getWidth()/2), (int)(rect.getY() + rect.getHeight()/2),rect.getWidth() / 2f, rect.getHeight() / 2f, this);
        }

        for (MapObject object: map.getLayers().get("Death Barrier").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new DeathWall((int)(rect.getX() + rect.getWidth()/2), (int)(rect.getY() + rect.getHeight()/2),rect.getWidth() / 2f, rect.getHeight() / 2f, this);
        }

        int i = 0;
        for (MapObject object: map.getLayers().get("Spawn Points").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            Character chara;
            try {
                chara = new ShyGuy(this, new Vector2(rect.getX(), rect.getY()), Main.controllers.get(i), (int) object.getProperties().get("player"));
                i++;
            } catch (Exception e) {
                chara = new ShyGuy(this, new Vector2(rect.getX(), rect.getY()), null, (int) object.getProperties().get("player"));
            }
            chara.respawnPos = new Vector2(pos.x / Main.PPM, pos.y / Main.PPM);
        }

        float width = (mProp.get("width", Integer.class) * mProp.get("tilewidth", Integer.class)) / Main.PPM;
        float height = (mProp.get("height", Integer.class) * mProp.get("tileheight", Integer.class)) / Main.PPM;

        gamecam.max = new Vector2(width, height);

        world.setContactListener(new WorldContactListener());

        // plays a song so I can hear things
        game.music = game.newSong();
        game.music.setVolume(Main.musicVolume / 10f);
        game.music.play();

        hud = new HUD();
    }

    @Override
    public void show() {

    }

    public void update(float deltaTime) {

        gamecam.update();
        renderer.setView(gamecam);

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

            // Manage which game objects are active
            Main.gameObjects.addAll(Main.objectsToAdd);
            Main.gameObjects.removeAll(Main.objectsToRemove);
            Main.objectsToAdd.clear();
            Main.objectsToRemove.clear();

            if (!game.music.isPlaying()) {
                game.music.play();
            }

            hud.update(deltaTime);
        }

        // clear all controller inputs
        Set<Controller> keys = Main.recentButtons.keySet();
        for (Controller key : keys) {
            Main.recentButtons.get(key).clear();
        }

    }
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        game.batch.setProjectionMatrix(gamecam.combined);

        viewport.apply();

        game.batch.begin();

        if (videoPlayer.isPlaying()) {
            videoPlayer.draw(game.batch);
        }

        for (GameObject obj : Main.gameObjects) obj.draw(game.batch);
        game.batch.end();

        b2dr.render(world, gamecam.combined);

        hud.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        gamecam.update();
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
