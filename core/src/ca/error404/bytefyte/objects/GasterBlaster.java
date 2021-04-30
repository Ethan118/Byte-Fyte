package ca.error404.bytefyte.objects;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

// Gaster blaster class
public class GasterBlaster extends GameObject {

//    Instantiating variables
    private Character parent;
    private float speed;
    private Laser laser;
    public Vector2 pos = new Vector2(0, 0);
    public Vector2 targetPos;
    private TextureAtlas textureAtlas;
    private Animation<TextureRegion> animation;
    private float elapsedTime = 0;
    private float spriteScale;
    private TextureRegion sprite;
    private Vector2 angle;
    private boolean laserHasSpawned = false;
    private Vector2 offset = new Vector2(0, 0);
    private Music arriveSFX;
    private Music fireSFX;
    private boolean arriveHasPlayed = false;



    /*
    * Constructor
    * Pre: Inputs for parameters
    * Post: A Gaster Blaster that can fire a laser
    * */
    public GasterBlaster(Character parent, float speed, Laser laser) {
//        Assigning parameters to variables
        this.parent = parent;
        this.laser = laser;
        this.speed = speed;
        this.targetPos = parent.pos.cpy();
        this.spriteScale = parent.spriteScale;

        this.textureAtlas = Main.manager.get("sprites/sans.atlas", TextureAtlas.class);
        animation = new Animation<TextureRegion>(100f, textureAtlas.findRegions("gb_normal"), Animation.PlayMode.NORMAL);
        sprite = animation.getKeyFrame(elapsedTime, true);

        setBounds(parent.pos.x - (getWidth() / 2), parent.pos.y - (getHeight() / 2), getRegionWidth() / spriteScale / Main.PPM, getRegionHeight() / spriteScale / Main.PPM);
        setRegion(sprite);
        setOriginCenter();

//        Setting the angle
        if (parent.controller != null) {
            this.angle = parent.rStick.cpy();
        } else {
            this.angle = parent.moveVector.cpy();
        }
        setRotation(angle.angleDeg() + 90);

//        Instantiating sound effects
        arriveSFX = Gdx.audio.newMusic(Gdx.files.internal("audio/sound effects/gasterBlasterArrive.wav"));
        arriveSFX.setLooping(false);
        arriveSFX.setVolume(Main.sfxVolume / 10f);

        fireSFX = Gdx.audio.newMusic(Gdx.files.internal("audio/sound effects/gasterBlasterFire.wav"));
        fireSFX.setLooping(false);
        fireSFX.setVolume(Main.sfxVolume / 10f);

    }

    /*
    * Pre: Delta Time
    * Post: Updates Gaster Blaster
    * */
    @Override
    public void update(float delta) {

//        Moves the Gaster Blaster to the target position
        pos.lerp(targetPos, speed * delta);

//        Updates the laser offset
        offset.y = pos.y - parent.pos.cpy().y;
        offset.x = (float) ((pos.x - parent.pos.cpy().x) + (sprite.getRegionWidth()/0.8 / Main.PPM)/4 );

//        Checks if the Gaster Blaster has arrived at the target location
        if (pos.dst(targetPos) < 0.1) {

//            If it has, the animation is changed and the elapsed time is counted
            animation = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("gb_fire"), Animation.PlayMode.NORMAL);
            elapsedTime += delta;

//            If the laser has not already spawned
            if (!laserHasSpawned) {

//                Play the sound effect and create a laser
                fireSFX.play();

                laser = new Laser(parent, offset, angle, 100, 2.5f, 4, 0.5f, 0.1f, 1f, "gb_laser", "sprites/sans.atlas", 0.8f);

//                The laser has now been spawned
                laserHasSpawned = true;


            }

//            If the laser has been on thescreen for a second (duration), destroy the gaster blaster
            if (elapsedTime > 1) {
                destroy();
            }

//        If the arrive sound effect has not already played, play it
        } else if (!arriveHasPlayed) {
            arriveSFX.play();
            arriveHasPlayed = true;
        }

//        If the has has spawned, remove this instance of the gaster blaster from the gameobjects list, and
//        re-add it to change the draw order
        if (laserHasSpawned) {
            Main.gameObjects.remove(this);
            Main.gameObjects.add(this);
        }

//        Set the sprite to draw, and draw it
        sprite = animation.getKeyFrame(elapsedTime, true);

        setBounds(pos.x - (getWidth() / 2), pos.y - (getHeight() / 2), getRegionWidth() / spriteScale / Main.PPM, getRegionHeight() / spriteScale / Main.PPM);
        setRegion(sprite);
        setOriginCenter();
    }

    /*
    * Pre: Destroy method in the game objects class
    * Post: Destroys and disposes of all assets in this instance of Gaster Blaster
    * */
    @Override
    public void destroy() {

//        Disposes of everything in this instance of Gaster Blaster
        super.destroy();
        arriveSFX.dispose();
    }
}
