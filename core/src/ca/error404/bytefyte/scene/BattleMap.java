package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.HUD;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.chars.*;
import ca.error404.bytefyte.scene.menu.CharacterSelect;
import ca.error404.bytefyte.tools.CutscenePlayer;
import ca.error404.bytefyte.tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Set;

// Battlemap class to create a map to fight on
public class BattleMap extends PlayRoom {

//    Initializing variables
    private Vector2 bgPos = new Vector2(-1920 / 2f, -1080 / 2f);
    public static ArrayList<Integer> positions = new ArrayList<>();
    private int numOfPlayers = 0;

    private final HUD hud;

    private int playersAlive;

    private Texture background;

    public static ArrayList<Character> alive;

    CutscenePlayer videoPlayer = new CutscenePlayer("delivery dance");

    private String[] characters;
    private CharacterSelect characterSelect;


    /*
    * Constructor
    * Pre: Values for parameters
    * Post: A new battlemap
    * */
    public BattleMap(Main game, TiledMap map, Vector2 scrollVector, Texture background) {

//        Calling the super() method and setting variables
        super(game, map, scrollVector, background);
        this.background = background;

        alive = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            alive.add(null);
        }

        Vector2 pos = Vector2.Zero;

//        Respawn points
        for (MapObject object: map.getLayers().get("Respawn Point").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            pos = new Vector2(rect.getX(), rect.getY());
        }

        gamecam.position.set(pos.x / Main.PPM, pos.y / Main.PPM, 0);

//        Ground objects
        for (MapObject object: map.getLayers().get("Ground").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Wall((int)(rect.getX() + rect.getWidth()/2), (int)(rect.getY() + rect.getHeight()/2),rect.getWidth() / 2f, rect.getHeight() / 2f, this);
        }

//        Barrier objects
        for (MapObject object: map.getLayers().get("Death Barrier").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new DeathWall((int)(rect.getX() + rect.getWidth()/2), (int)(rect.getY() + rect.getHeight()/2),rect.getWidth() / 2f, rect.getHeight() / 2f, this);
        }

//        Spawn points
        for (MapObject object: map.getLayers().get("Spawn Points").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            int i = (int) object.getProperties().get("player");

//            Detects characters to be added based on character list and spawns them
            Character chara;
            if (CharacterSelect.characters[i-1] != null) {

//                For regular fighting
                if (!Main.stamina) {
                    if (CharacterSelect.characters[i - 1].equalsIgnoreCase("masterchief")) {
                        try {
                            chara = new MasterChief(this, new Vector2(rect.getX(), rect.getY()), Main.controllers[i - 1], i);
                        } catch (Exception e) {
                            chara = new MasterChief(this, new Vector2(rect.getX(), rect.getY()), null, i);
                        }
                    } else if (CharacterSelect.characters[i - 1].equalsIgnoreCase("shyguy")) {
                        try {
                            chara = new ShyGuy(this, new Vector2(rect.getX(), rect.getY()), Main.controllers[i - 1], i);
                        } catch (Exception e) {
                            chara = new ShyGuy(this, new Vector2(rect.getX(), rect.getY()), null, i);
                        }
                    } else if (CharacterSelect.characters[i - 1].equalsIgnoreCase("madeline")) {
                        try {
                            chara = new Madeline(this, new Vector2(rect.getX(), rect.getY()), Main.controllers[i - 1], i);
                        } catch (Exception e) {
                            chara = new Madeline(this, new Vector2(rect.getX(), rect.getY()), null, i);
                        }
                    } else if (CharacterSelect.characters[i-1].equalsIgnoreCase("marioluigi")) {
                    try {
                        chara = new Mario(this, new Vector2(rect.getX(), rect.getY()), Main.controllers[i-1], i);
                    } catch (Exception e) {
                        chara = new Mario(this, new Vector2(rect.getX(), rect.getY()), null, i);
                    }
                }else if (CharacterSelect.characters[i-1].equalsIgnoreCase("sans")){
                    try {
                        chara = new Sans(this, new Vector2(rect.getX(), rect.getY()), Main.controllers[i - 1], i);
                    } catch (Exception e) {
                        chara = new Sans(this, new Vector2(rect.getX(), rect.getY()), null, i);
                        }
                } else {
                    try {
                        chara = new Kirby(this, new Vector2(rect.getX(), rect.getY()), Main.controllers[i-1], i);
                    } catch (Exception e) {
                        chara = new Kirby(this, new Vector2(rect.getX(), rect.getY()), null, i);}
                    }

//                In stamina mode
                } else {
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
                }

//                Sets the character index of the list of characters to that character, initialize them
                alive.set(i - 1, chara);
                chara.facingLeft = (boolean) object.getProperties().get("left");
                chara.respawnPos = new Vector2(pos.x / Main.PPM, pos.y / Main.PPM);
            }
        }

//        Initialize more variables
        float width = (mProp.get("width", Integer.class) * mProp.get("tilewidth", Integer.class)) / Main.PPM;
        float height = (mProp.get("height", Integer.class) * mProp.get("tileheight", Integer.class)) / Main.PPM;

        gamecam.max = new Vector2(width, height);

        world.setContactListener(new WorldContactListener());

        hud = new HUD();

        for (String character: CharacterSelect.characters) {
            if (character != null) {
                numOfPlayers += 1;
            }
        }


        for (int i = 0; i < numOfPlayers; i++) {
            positions.add(9);
        }

    }

    @Override
    public void show() {

    }

    /*
    * Pre: Delta Time
    * Post: Updates the map
    * */
    public void update(float deltaTime) {

//        Checks if players are dead, if they are, set them to null, count number of players, update rank
        int i = -1;
        playersAlive = 0;
        for (int k = 0; k < Main.players.size(); k++) {
            i ++;
            Main.players.get(k).rank = Main.players.size();
            if (Main.players.get(k) != null) {
                if (Main.players.get(k).dead) {
                    Main.players.set(i, null);
                } else {
                    playersAlive += 1;
                }
            }
        }

//        What to do if there is only one player left
        if (playersAlive == 1) {
            for (Character character : Main.players) {
                i++;
                if (character != null) {
                    character.rank = 1;
                }
            }

//            Remove all assets, switch screen
            Main.uiToAdd.clear();
            Main.ui.clear();
            Main.uiToRemove.clear();
            Main.players.clear();
            new ScreenWipe(new VictoryScreen(game), game);
        }

//        Update camera and background
        bgPos.x += scrollVector.x * deltaTime;
        bgPos.y += scrollVector.y * deltaTime;

        gamecam.update();
        renderer.setView(gamecam);

//        Loops song
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

        // clear all controller inputs
        Set<Controller> keys = Main.recentButtons.keySet();
        for (Controller key : keys) {
            Main.recentButtons.get(key).clear();
        }

        for (int j=0; j < Main.transitions.size(); j++) Main.transitions.get(j).update(deltaTime);
    }


    /*
    * Pre: Delta Time
    * Post: Renders all components
    * */
    public void render(float delta) {

//        Clears screen and renders
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        drawBackground();

        renderer.render();


        game.batch.begin();
        game.batch.setProjectionMatrix(gamecam.combined);

//        Plays video
        if (videoPlayer.isPlaying()) {
            videoPlayer.draw(game.batch);
        }

        viewport.apply();

//        Draws madeline hair
        for (GameObject obj : Main.gameObjects) {
            try {
                Madeline madeline = (Madeline) obj;
                madeline.drawHair(game.batch);
            } catch (Exception ignore) {

            }

            obj.draw(game.batch);
        }

        game.batch.end();

//        Debug renderer
        if (Main.debug) {
            b2dr.render(world, gamecam.combined);
        }

//        Draw hud and transitions
        hud.draw();

        for (int i=0; i < Main.transitions.size(); i++) Main.transitions.get(i).draw();
    }

    /*
    * Pre: None
    * Post: Draws the background
    * */
    public void drawBackground() {
        game.batch.begin();
        game.batch.setProjectionMatrix(bgCam.combined);
        float h;
        float w;

//        Sets width and height
        if (background.getHeight() <= background.getWidth()) {
            h = bgCam.viewportHeight;
            w = (bgCam.viewportHeight / background.getHeight()) * background.getWidth();
        } else {
            h = (bgCam.viewportWidth / background.getWidth()) * background.getHeight();
            w = bgCam.viewportWidth;
        }

//        Sets x
        if (bgPos.x <= -(w + (1920 / 2f))) {
            bgPos.x += w;
        } else if (bgPos.x >= (w + (1920 / 2f))) {
            bgPos.x -= w;
        }

//        Sets y
        if (bgPos.y <= -(h - (-1080 / 2f))) {
            bgPos.y += h;
        } else if (bgPos.y >= (h - (-1080 / 2f))) {
            bgPos.y -= h;
        }

//        Adjusts x
        float x = bgPos.x;

        while (x > -(bgCam.viewportWidth)) {
            x -= w;
        }

//        Draws background
        while (x < bgCam.viewportWidth) {
            game.batch.draw(background, x, bgPos.y, w, h);
            game.batch.draw(background, x, bgPos.y + h, w, h);
            game.batch.draw(background, x, bgPos.y - h, w, h);
            game.batch.draw(background, x, bgPos.y - h * 2, w, h);

            x += w;
        }
        game.batch.end();
    }

    /*
    * Pre: Width and height
    * Post: Updates screen size
    * */
    @Override
    public void resize(int width, int height) {

//        Update screen size
        viewport.update(width, height);
        gamecam.update();
    }

    /*
     * Pre: None
     * Post: Pauses music
     * */
    @Override
    public void pause() {
        game.music.pause();
    }

    /*
     * Pre: None
     * Post: Resumes music
     * */
    @Override
    public void resume() {
        game.music.play();
    }

    /*
     * Pre: None
     * Post: Hides screen
     * */
    @Override
    public void hide() {

    }

    /*
     * Pre: None
     * Post: Disposes of all assets
     * */
    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        renderer.dispose();
        map.dispose();


    }

}
