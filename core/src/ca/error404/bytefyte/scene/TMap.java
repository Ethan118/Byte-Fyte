package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.HUD;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.*;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.constants.Globals;
import ca.error404.bytefyte.constants.ScreenSizes;
import ca.error404.bytefyte.objects.BattleCam;
import ca.error404.bytefyte.tools.CutscenePlayer;
import ca.error404.bytefyte.tools.WorldContactListener;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
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
    private final BattleCam gamecam;
    private final OrthographicCamera bgCam;
    private Vector2 bgPos = new Vector2(-1920 / 2f, -1080 / 2f);
    private Vector2 scrollVector;
    private final Viewport viewport;

    private final Main game;
    private final HUD hud;

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    private final MapProperties mProp;
    private final World world;
    private final Box2DDebugRenderer b2dr;

    private int playersAlive;

    private Texture background;

    CutscenePlayer videoPlayer = new CutscenePlayer("delivery dance");

    private String[] characters;
    private CharacterSelect characterSelect;

    public TMap(String[] characters, CharacterSelect characterSelect, Main game, TiledMap map, Vector2 scrollVector, Texture background) {
        this.game = game;
        this.characters = characters;

        this.characterSelect = characterSelect;

        gamecam = new BattleCam();
        bgCam = new OrthographicCamera(1920, 1080);
        this.scrollVector = scrollVector;
        this.background = background;

        viewport = new FitViewport(Main.WIDTH / Main.PPM, Main.HEIGHT / Main.PPM, gamecam);

        this.map = map;
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

        for (MapObject object: map.getLayers().get("Spawn Points").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            int i = (int) object.getProperties().get("player");
            Character chara;
            if (characters[i-1] != null) {
                if (characters[i-1].equalsIgnoreCase("Master Chief")) {
                    try {
                        chara = new MasterChief(this, new Vector2(rect.getX(), rect.getY()), Main.controllers.get(i - 1), i);
                    } catch (Exception e) {
                        chara = new MasterChief(this, new Vector2(rect.getX(), rect.getY()), null, i);

                    }
                    chara.facingLeft = (boolean) object.getProperties().get("left");
                    chara.respawnPos = new Vector2(pos.x / Main.PPM, pos.y / Main.PPM);

                } else if (characters[i-1].equalsIgnoreCase("Shy Guy")) {
                    try {
                        chara = new ShyGuy(this, new Vector2(rect.getX(), rect.getY()), Main.controllers.get(i - 1), i);
                    } catch (Exception e) {
                        chara = new ShyGuy(this, new Vector2(rect.getX(), rect.getY()), null, i);
                    }
                    chara.facingLeft = (boolean) object.getProperties().get("left");
                    chara.respawnPos = new Vector2(pos.x / Main.PPM, pos.y / Main.PPM);
                } else if (characters[i-1].equalsIgnoreCase("Kirby")) {
                    try {
                        chara = new Kirby(this, new Vector2(rect.getX(), rect.getY()), Main.controllers.get(i - 1), i);
                    } catch (Exception e) {
                        chara = new Kirby(this, new Vector2(rect.getX(), rect.getY()), null, i);
                    }
                    chara.facingLeft = (boolean) object.getProperties().get("left");
                    chara.respawnPos = new Vector2(pos.x / Main.PPM, pos.y / Main.PPM);
                }

            }
        }

        float width = (mProp.get("width", Integer.class) * mProp.get("tilewidth", Integer.class)) / Main.PPM;
        float height = (mProp.get("height", Integer.class) * mProp.get("tileheight", Integer.class)) / Main.PPM;

        gamecam.max = new Vector2(width, height);

        world.setContactListener(new WorldContactListener());

        hud = new HUD();
    }

    @Override
    public void show() {

    }

    public void update(float deltaTime) {
        int i = 0;
        playersAlive = 0;
        for (Character character: Main.players) {
            i ++;
            if (character != null) {
                if (character.dead) {
                    Main.players.set(i, null);
                } else {
                    playersAlive += 1;
                }
            }
        }

        if (playersAlive == 1) {
            System.out.println("True");
            for (int j = 0; j < 4; j++) {
                if (characterSelect.cursors[j] != null) {
                    characterSelect.characters[j] = null;
                    characterSelect.charsSelected[j] = false;
                }
            }
            game.setScreen(characterSelect);
        }

        bgPos.x += scrollVector.x * deltaTime;
        bgPos.y += scrollVector.y * deltaTime;

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

        drawBackground();

        renderer.render();

        game.batch.begin();
        game.batch.setProjectionMatrix(gamecam.combined);

        if (videoPlayer.isPlaying()) {
            videoPlayer.draw(game.batch);
        }

        viewport.apply();

        for (GameObject obj : Main.gameObjects) obj.draw(game.batch);
        game.batch.end();

        b2dr.render(world, gamecam.combined);

        hud.draw();
    }

    public void drawBackground() {
        game.batch.begin();
        game.batch.setProjectionMatrix(bgCam.combined);
        float w = (bgCam.viewportHeight / background.getHeight()) * background.getWidth();

        if (bgPos.x <= -(w + (1920 / 2f))) {
            bgPos.x += w;
        } else if (bgPos.x >= (w + (1920 / 2f))) {
            bgPos.x -= w;
        }

        if (bgPos.y <= -(1080 - (-1080 / 2f))) {
            bgPos.y += bgCam.viewportHeight;
        } else if (bgPos.y >= (1080 - (-1080 / 2f))) {
            bgPos.y -= bgCam.viewportHeight;
        }

        float x = bgPos.x;

        while (x > -(bgCam.viewportWidth)) {
            x -= w;
        }

        while (x < bgCam.viewportWidth) {
            game.batch.draw(background, x, bgPos.y, w, bgCam.viewportHeight);
            game.batch.draw(background, x, bgPos.y + bgCam.viewportHeight, w, bgCam.viewportHeight);
            game.batch.draw(background, x, bgPos.y - bgCam.viewportHeight, w, bgCam.viewportHeight);
            game.batch.draw(background, x, bgPos.y - bgCam.viewportHeight * 2, w, bgCam.viewportHeight);

            x += w;
        }
        game.batch.end();
    }

    // updates screen size
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        gamecam.update();
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
        renderer.dispose();
        map.dispose();


    }

//  Gets the world
    public World getWorld() {
        return world;
    }

}
