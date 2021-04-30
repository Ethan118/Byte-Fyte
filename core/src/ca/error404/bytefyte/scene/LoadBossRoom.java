package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.menu.CharacterSelect;
import ca.error404.bytefyte.ui.ShowSongName;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

// A class to load the bossRoom
public class LoadBossRoom implements Screen {

//    Initializing variables
    OrthographicCamera cam;
    Main game;
    Viewport viewport;
    Vector2 scrollVector;
    float rotation = 0;
    int rotateSpeed = 180;
    String tmap;
    String tileset;
    String background;
    String series;
    String mapName;
    Texture loadTex;
    Texture loadTexSpin;
    TiledMap map;
    TmxMapLoader mapLoader = new TmxMapLoader();

    private float minLoadTime = 10f;

    /*
    * Constructor
    * Pre: Inputs for all parameters
    * Post: A new load boss room instance
    * */
    public LoadBossRoom(String tmap, String tileset, String background, Main game, Vector2 scrollVector, String series) {

//        Setting all variables
        this.game = game;
        this.scrollVector = scrollVector;
        game.music = this.game.music;
        this.tmap = tmap;
        this.tileset = tileset;
        this.background = background;
        this.series = series;

        Main.bill = false;
    }

    /*
    * Pre: None
    * Post: Initializes variables, cameras, viewports, etc.
    * */
    @Override
    public void show() {

//        Sets all necessary variables
        Main.buttons.clear();
        cam = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, cam);

        Main.manager.load(String.format("sprites/single player/%s.png", tileset), Texture.class);
        Main.manager.finishLoading();
        Main.manager.load(String.format("sprites/single player/%s.png", background), Texture.class);
        Main.manager.load("sprites/battleUI.atlas", TextureAtlas.class);

//         Convert String Array to List
        List<String> characters = new ArrayList<>();

        for (String charname : CharacterSelect.characters) {

            if (!characters.contains(charname) && charname != null) {
                Main.manager.load(String.format("sprites/%s.atlas", charname), TextureAtlas.class);
            }

            characters.add(charname);
        }

//        Loads boss
        Main.manager.load("sprites/enemies/petey piranha.atlas", TextureAtlas.class);

//        If shy guy is selected, load his songs
        if (characters.contains("shyguy")) {
            for (int i = 0; i < 24; i++) {
                Main.audioManager.load(String.format("audio/sound effects/shysongs/shyguy_song_%d.wav", i + 1), Sound.class);
            }
        }

//        Assigning variables
        mapName = tmap;
        tmap = String.format("sprites/single player/%s.tmx", tmap);

        mapLoader.getDependencies(tmap, Gdx.files.internal(tmap), null);
        mapLoader.loadAsync(Main.manager, tmap, Gdx.files.internal(tmap), null);
        map = mapLoader.loadSync(Main.manager, tmap, Gdx.files.internal(tmap), null);

//         plays a song so I can hear things
        if (!Main.internalSongName.equalsIgnoreCase(series) && game.music != null) {
            game.music.stop();
        }

//        Playing music
        game.music = game.newSong(series);
        game.music.setVolume(Main.musicVolume / 10f);
        game.music.play();

        loadTex = new Texture("sprites/load.png");
        loadTexSpin = new Texture("sprites/loadSpin.png");
    }

    /*
    * Pre: Delta time
    * Post: Renders all images
    * */
    @Override
    public void render(float delta) {

//        Counts down load time
        minLoadTime -= delta;

//        If the load time is done, set the screen, dispose of necessary assets
        if (Main.manager.update() && minLoadTime <= 0) {
            loadTex.dispose();
            loadTexSpin.dispose();
            new ShowSongName();
            game.setScreen(new BossRoom(game, map, scrollVector, Main.manager.get(String.format("sprites/single player/%s.png", background), Texture.class)));
        }

//        Looping game music
        if (game.music.getPosition() >= game.songLoopEnd) {
            game.music.setPosition((float) (game.music.getPosition() - (game.songLoopEnd - game.songLoopStart)));
        }

//        Setting rotation if above 360
        if (Math.abs(rotation) >= 360) {
            rotation -= 360;
        }

//        Rotates the loading sprite
        rotation += delta * rotateSpeed;

//        Clearing screen and redrawing textures
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(new TextureRegion(loadTex), (1920 / 2f) - (loadTex.getWidth()) - 52.5f, -(1080 /2f) + 50);
        game.batch.draw(new TextureRegion(loadTexSpin), (1920 / 2f) - (loadTexSpin.getWidth()) - 20f, -(1080 /2f) + 20, (float) loadTexSpin.getWidth() / 2f, (float) loadTexSpin.getHeight() / 2f, (float) loadTexSpin.getWidth(), (float) loadTexSpin.getHeight(), 1f, 1f, rotation);
        game.batch.end();
    }

    /*
    * Pre: Width and height
    * Post: Updates screen size
    * */
    @Override
    public void resize(int width, int height) {

//        Updates the viewport to match screen size
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
