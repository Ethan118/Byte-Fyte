package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.objects.BattleCam;
import ca.error404.bytefyte.ui.PlayerHealth;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

public class PlayRoom implements Screen {
//    initializing variables
    protected final BattleCam gamecam;
    protected final OrthographicCamera bgCam;
    protected final Texture background;
    protected Vector2 bgPos = new Vector2(-1920 / 2f, -1080 / 2f);
    protected Vector2 scrollVector;
    protected final Viewport viewport;
    protected static ArrayList<Integer> positions = new ArrayList<>();
    protected int numOfPlayers = 0;

    protected final Main game;

    protected final TiledMap map;
    protected final OrthogonalTiledMapRenderer renderer;

    public final MapProperties mProp;
    public final World world;
    protected final Box2DDebugRenderer b2dr;
    protected Random rand = new Random();

    /**
     * @param game
     * @param map
     * @param scrollVector
     * @param background
     * pre: reference to a game, map reference, scroll vector, background
     * post: instantiates a instance of play room
     */
    public PlayRoom(Main game, TiledMap map, Vector2 scrollVector, Texture background) {
        this.game = game;
        game.batch = new SpriteBatch();

        PlayerHealth.nerds = rand.nextInt(100);

//        creates necessary cameras
        gamecam = new BattleCam();
        bgCam = new OrthographicCamera(1920, 1080);
        this.scrollVector = scrollVector;
        this.background = background;

        viewport = new FitViewport(Main.WIDTH / Main.PPM, Main.HEIGHT / Main.PPM, gamecam);

//        creates a new tile map renderer
        this.map = map;
        renderer = new OrthogonalTiledMapRenderer(map, 1/Main.PPM);

        mProp = map.getProperties();

//        creates a new physics world
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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

    //  Gets the world
    public World getWorld() {
        return world;
    }
}
