package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.HUD;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.chars.*;
import ca.error404.bytefyte.chars.bosses.Petey;
import ca.error404.bytefyte.objects.BattleCam;
import ca.error404.bytefyte.scene.menu.CharacterSelect;
import ca.error404.bytefyte.tools.CutscenePlayer;
import ca.error404.bytefyte.tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Set;

public class BossRoom extends PlayRoom {
    private final BattleCam gamecam;
    private final OrthographicCamera bgCam;
    private Vector2 bgPos = new Vector2(-1920 / 2f, -1080 / 2f);
    private Vector2 scrollVector;
    private final Viewport viewport;
    public static ArrayList<Integer> positions = new ArrayList<>();
    private int numOfPlayers = 0;

    private final Main game;
    private final HUD hud;

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    private final MapProperties mProp;
    private final World world;
    private final Box2DDebugRenderer b2dr;

    private int playersAlive;

    private Texture background;

    public static ArrayList<Character> alive = new ArrayList<>();

    CutscenePlayer videoPlayer = new CutscenePlayer("delivery dance");

    private String[] characters;
    private CharacterSelect characterSelect;

    public BossRoom(Main game, TiledMap map, Vector2 scrollVector, Texture background) {
        super(game, map, scrollVector, background);
        this.game = game;
        game.batch = new SpriteBatch();

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

        gamecam.position.set(pos.x / Main.PPM, pos.y / Main.PPM, 0);

        for (MapObject object: map.getLayers().get("Ground").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Wall((int)(rect.getX() + rect.getWidth()/2), (int)(rect.getY() + rect.getHeight()/2),rect.getWidth() / 2f, rect.getHeight() / 2f, this);
        }

        for (MapObject object: map.getLayers().get("Death Barrier").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new DeathWall((int)(rect.getX() + rect.getWidth()/2), (int)(rect.getY() + rect.getHeight()/2),rect.getWidth() / 2f, rect.getHeight() / 2f, this);
        }

        for (MapObject object: map.getLayers().get("Boss Point").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            switch ((String) object.getProperties().get("boss name")) {
                case "Petey":
                default:
                    new Petey(this, new Vector2(rect.getX(), rect.getY()), game);
            }
        }

        for (MapObject object: map.getLayers().get("Spawn Point").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            int i = 1;

            Character chara;
            if (CharacterSelect.characters[i-1] != null) {
                if (CharacterSelect.characters[i - 1].equalsIgnoreCase("masterchief")) {
                    try {
                        chara = new MasterChief(this, new Vector2(rect.getX(), rect.getY()), Main.controllers[i - 1], i, 300);
                    } catch (Exception e) {
                        chara = new MasterChief(this, new Vector2(rect.getX(), rect.getY()), null, i, 300);
                    }
                } else if (CharacterSelect.characters[i - 1].equalsIgnoreCase("shyguy")) {
                    try {
                        chara = new ShyGuy(this, new Vector2(rect.getX(), rect.getY()), Main.controllers[i - 1], i, 300);
                    } catch (Exception e) {
                        chara = new ShyGuy(this, new Vector2(rect.getX(), rect.getY()), null, i, 300);
                    }
                } else if (CharacterSelect.characters[i - 1].equalsIgnoreCase("madeline")) {
                    try {
                        chara = new Madeline(this, new Vector2(rect.getX(), rect.getY()), Main.controllers[i - 1], i, 300);
                    } catch (Exception e) {
                        chara = new Madeline(this, new Vector2(rect.getX(), rect.getY()), null, i, 300);
                    }
                } else if (CharacterSelect.characters[i - 1].equalsIgnoreCase("sans")) {
                    try {
                        chara = new Sans(this, new Vector2(rect.getX(), rect.getY()), Main.controllers[i - 1], i, 300);
                    } catch (Exception e) {
                        chara = new Sans(this, new Vector2(rect.getX(), rect.getY()), null, i, 300);
                    }
                } else if (CharacterSelect.characters[i - 1].equalsIgnoreCase("marioluigi")) {
                    try {
                        chara = new Mario(this, new Vector2(rect.getX(), rect.getY()), Main.controllers[i - 1], i, 300);
                    } catch (Exception e) {
                        chara = new Mario(this, new Vector2(rect.getX(), rect.getY()), null, i, 300);
                    }
                } else {
                    try {
                        chara = new Kirby(this, new Vector2(rect.getX(), rect.getY()), Main.controllers[i - 1], i, 300);
                    } catch (Exception e) {
                        chara = new Kirby(this, new Vector2(rect.getX(), rect.getY()), null, i, 300);
                    }
                }

                chara.facingLeft = false;
                chara.respawnPos = new Vector2(pos.x / Main.PPM, pos.y / Main.PPM);
            }
        }

        float width = (mProp.get("width", Integer.class) * mProp.get("tilewidth", Integer.class)) / Main.PPM;
        float height = (mProp.get("height", Integer.class) * mProp.get("tileheight", Integer.class)) / Main.PPM;

        gamecam.max = new Vector2(width, height);

        world.setContactListener(new WorldContactListener());

        hud = new HUD();
        gamecam.scale = gamecam.scale * 2.5f;
        alive.addAll(Main.players);
    }

    @Override
    public void show() {

    }

    public void update(float deltaTime) {
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

        // update all objects and physics objects
        world.step(1 / 60f, 6, 2);
        for (int j = 0; j < Main.gameObjects.size(); j++) {
            if (Main.gameObjects.get(j).remove) {
                try {
                    world.destroyBody(Main.gameObjects.get(j).b2body);
                } catch (Exception ignored) {}
                Main.objectsToRemove.add(Main.gameObjects.get(j));
            } else {
                Main.gameObjects.get(j).update(deltaTime);
            }
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

        for (Character chara : Main.players) {
            if (chara.pos.x < -(chara.getWidth())) {
                chara.b2body.setTransform(chara.pos.x + (mProp.get("width", Integer.class) * mProp.get("tilewidth", Integer.class)) / Main.PPM, chara.b2body.getPosition().y, chara.b2body.getTransform().getRotation());
            } else if (chara.pos.x > (mProp.get("width", Integer.class) * mProp.get("tilewidth", Integer.class)) / Main.PPM) {
                chara.b2body.setTransform(chara.pos.x - (mProp.get("width", Integer.class) * mProp.get("tilewidth", Integer.class)) / Main.PPM, chara.b2body.getPosition().y, chara.b2body.getTransform().getRotation());
            }
        }

        for (Luigi chara : Main.luigis) {
            if (chara.pos.x < -(chara.getWidth())) {
                chara.b2body.setTransform(chara.pos.x + (mProp.get("width", Integer.class) * mProp.get("tilewidth", Integer.class)) / Main.PPM, chara.b2body.getPosition().y, chara.b2body.getTransform().getRotation());
            } else if (chara.pos.x > (mProp.get("width", Integer.class) * mProp.get("tilewidth", Integer.class)) / Main.PPM) {
                chara.b2body.setTransform(chara.pos.x - (mProp.get("width", Integer.class) * mProp.get("tilewidth", Integer.class)) / Main.PPM, chara.b2body.getPosition().y, chara.b2body.getTransform().getRotation());
            }
        }

        // clear all controller inputs
        Set<Controller> keys = Main.recentButtons.keySet();
        for (Controller key : keys) {
            Main.recentButtons.get(key).clear();
        }

        for (int j=0; j < Main.transitions.size(); j++) Main.transitions.get(j).update(deltaTime);
    }

    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        game.batch.setShader(GrayscaleShader.grayscaleShader);
//        renderer.getBatch().setShader(GrayscaleShader.grayscaleShader);

        drawBackground();

        renderer.render();
//        game.batch.setShader(null);
//        renderer.getBatch().setShader(null);

        game.batch.begin();
        game.batch.setProjectionMatrix(gamecam.combined);

        if (videoPlayer.isPlaying()) {
            videoPlayer.draw(game.batch);
        }

        viewport.apply();

        for (GameObject obj : Main.gameObjects) {
            try {
                Madeline madeline = (Madeline) obj;
                madeline.drawHair(game.batch);
            } catch (Exception ignore) {

            }

            obj.draw(game.batch);
        }

        game.batch.end();

        if (Main.debug) {
            b2dr.render(world, gamecam.combined);
        }

        hud.draw();

        for (int i=0; i < Main.transitions.size(); i++) Main.transitions.get(i).draw();
    }

    public void drawBackground() {
        game.batch.begin();
        game.batch.setProjectionMatrix(bgCam.combined);
        float h;
        float w;

        if (background.getHeight() <= background.getWidth()) {
            h = bgCam.viewportHeight;
            w = (bgCam.viewportHeight / background.getHeight()) * background.getWidth();
        } else {
            h = (bgCam.viewportWidth / background.getWidth()) * background.getHeight();
            w = bgCam.viewportWidth;
        }

        if (bgPos.x <= -(w + (1920 / 2f))) {
            bgPos.x += w;
        } else if (bgPos.x >= (w + (1920 / 2f))) {
            bgPos.x -= w;
        }

        if (bgPos.y <= -(h - (-1080 / 2f))) {
            bgPos.y += h;
        } else if (bgPos.y >= (h - (-1080 / 2f))) {
            bgPos.y -= h;
        }

        float x = bgPos.x;

        while (x > -(bgCam.viewportWidth)) {
            x -= w;
        }

        while (x < bgCam.viewportWidth) {
            game.batch.draw(background, x, bgPos.y, w, h);
            game.batch.draw(background, x, bgPos.y + h, w, h);
            game.batch.draw(background, x, bgPos.y - h, w, h);
            game.batch.draw(background, x, bgPos.y - h * 2, w, h);

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
