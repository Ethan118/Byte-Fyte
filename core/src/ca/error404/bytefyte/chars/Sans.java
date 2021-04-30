package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.GasterBlaster;
import ca.error404.bytefyte.objects.Laser;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.PlayRoom;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

// Class for Sans.  Extends character to create a new character.
public class Sans extends Character{

//    Instantiating all variables
    private Music healSFX;
    private boolean upBStart = true;
    private boolean upBEnd = false;
    private boolean usingUpB = false;
    private boolean sit = false;
    private boolean usingDownB = false;
    private float moveCooldown;
    private boolean usingDTilt = false;
    private GasterBlaster gasterBlaster;
    private Laser laser;

    private TextureAtlas textureAtlas = Main.manager.get(String.format("sprites/%s.atlas", charname), TextureAtlas.class);
    private boolean hasTeleported = false;

    /*
    * Constructor
    * Pre: Inputted parameters
    * Post: A new instance of the character Sans that is playable by the user
    * */
    public Sans(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playernumber) {

//        Calling the other constructor
        this(screen, spawnPoint, controller, playernumber, 0);
    }

    public Sans(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playerNumber, int stamina) {
//        Calling the character constructor
        super(screen, spawnPoint, controller, playerNumber, "sans", "SANS", 0.8f, 0.9f, stamina);

//        Assigning variables values
        upB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("up_b_start"), Animation.PlayMode.NORMAL);

        weight = 0.9f;
        walkSpeed = 1.1f;
        runSpeed = 2.2f;
        manualSpriteOffset = new Vector2(50, 15f);

        projectilesOnScreen = new ArrayList<>(1);


        walk.setFrameDuration(0.02f);

//        Preparing sound effects
        healSFX = Gdx.audio.newMusic(Gdx.files.internal("audio/sound effects/fullRestore.wav"));
        healSFX.setLooping(false);
        healSFX.setVolume(5);
    }

    /*
     * Pre: Delta time
     * Post: Updates character
     * */
    public void update(float deltaTime) {

//        If there is a move cooldown, reduce it by the elapsed time
        if (moveCooldown > 0) {
            moveCooldown -= deltaTime;

//        Otherwise, the move cooldown is 0
        } else {
            moveCooldown = 0;
        }

//        If the user is in the sit animation, lock their animation
        if (sit) {
            lockAnim = true;
            if (vel.y > 0) {
                vel.y = 0;
            }

            handleInput();

//            If they get hit or recast the move, unlock them, take them out of the sit animation
            if (attackState == AttackState.SPECIAL && moveVector.y == 0 || hasBeenHit) {
                sit = false;
            } else {
                resetControls();
            }
        }

//        Call the update from the character class
        super.update(deltaTime);

//        If they are eliminated, dispose of the sound effects
        if (dead) {
            healSFX.dispose();
        }

//        Set the animation of their up b to the beginning or end of the animation depending on situation
        if (upBStart) {
            upB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("up_b_start"), Animation.PlayMode.NORMAL);
        } else {
            upB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("up_b_end"), Animation.PlayMode.NORMAL);
        }

//        If trying to use up b
        if (animState == AnimationState.SPECIAL_U) {

//            If they arent currently using up b, use it
            if (!usingUpB) {
                animDuration = 0.6f;
                usingUpB = true;
            }
        }

//        If using up b
        if (usingUpB) {
            animState = AnimationState.SPECIAL_U;

//            If in the correct time and frame, teleport the player upwards
            if (animDuration > 0.41) {
                upBStart = true;
                upBEnd = false;
                if (upB.getKeyFrameIndex(elapsedTime) == 7) {
                    b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y + 0.67f, 0);
                    grounded = false;
                }

//            Otherwise, if they are still in the up b animation, play the ending animation
            } else if (animDuration > 0.01) {
                upBStart = false;
                upBEnd = true;

//            Otherwise, end the up b
            } else if (animDuration == 0){
                usingUpB = false;
            }
        }

//        If they are trying to use their down b
        if (animState == AnimationState.SPECIAL_D) {

//            If they are not currently using it, use it and play the music for it
            if (!usingDownB) {
                animDuration = 1.75f;
                usingDownB = true;
                healSFX.play();
            }

//            If the animation is currently playing
            if (animDuration > 0) {

//                Lock the user and heal sans based on game mode
                lockAnim = true;
                if (!stamina) {
                    if (percent <= deltaTime) {
                        percent = 0;
                    } else {
                        percent -= deltaTime;
                    }
                } else {
                    if (percent + deltaTime >= 999.9) {
                        percent = 999.9f;
                    } else {
                        percent += deltaTime;
                    }
                }

//            Otherwise, end the animation, unlock user
            } else {
                usingDownB = false;
                lockAnim = false;
            }
        }

//        If using the neutral air, set an animation duration to play the animation longer
        if (animState == AnimationState.AIR_N) {
            if (animDuration == 0) {
                animDuration = 0.4f;
                airNeutral();
            }
        }

//        If using the down tilt
        if (animState == AnimationState.BASIC_D) {

//            If they aren't currently using it, allow user to use it
            if (!usingDTilt) {
                animDuration = 0.5f;
                usingDTilt = true;
            }

//            If they are in the animation, lock them
            if (animDuration > 0) {
                lockAnim = true;

//            Otherwise, unlock them
            } else {
                lockAnim = false;
                usingDTilt = false;
            }
        }



    }

    //    All abilities.  Will add colliders, projectiles, or move Sans as applicable
    @Override
    void basicNeutral() {

//        Creates three projectiles depending on direction
        if (facingLeft) {
            new Projectile(this, new Vector2(0, 0), new Vector2(5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_top", "sprites/sans.atlas", 0, 0.9f);
            new Projectile(this, new Vector2(0.0375f, 0), new Vector2(5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_mid", "sprites/sans.atlas", 0.001f, 0.9f);
            new Projectile(this, new Vector2(-0.01f, 0), new Vector2(5, 0), 0, 0f, 20, 3, 2, 1, "bone_v_bottom", "sprites/sans.atlas", 0.002f, 0.9f);

        } else {
            new Projectile(this, new Vector2(0, 0), new Vector2(-5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_top", "sprites/sans.atlas", 0, 0.9f);
            new Projectile(this, new Vector2(-0.0375f, 0), new Vector2(-5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_mid", "sprites/sans.atlas", 0.001f, 0.9f);
            new Projectile(this, new Vector2(0.01f, 0), new Vector2(-5, 0), 0, 0f, 20, 3, 2, 1, "bone_v_bottom", "sprites/sans.atlas", 0.002f, 0.9f);
        }
        resetControls();
    }

    @Override
    void basicSide() {

//        Adds three projectiles based on direction
        if (!facingLeft) {
            new Projectile(this, new Vector2(0, 0), new Vector2(5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_top", "sprites/sans.atlas", 0, 0.9f);
            new Projectile(this, new Vector2(0.0375f, 0), new Vector2(5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_mid", "sprites/sans.atlas", 0.001f, 0.9f);
            new Projectile(this, new Vector2(-0.01f, 0), new Vector2(5, 0), 0, 0f, 20, 3, 2, 1, "bone_v_bottom", "sprites/sans.atlas", 0.002f, 0.9f);

        } else {
            new Projectile(this, new Vector2(0, 0), new Vector2(-5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_top", "sprites/sans.atlas", 0, 0.9f);
            new Projectile(this, new Vector2(-0.0375f, 0), new Vector2(-5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_mid", "sprites/sans.atlas", 0.001f, 0.9f);
            new Projectile(this, new Vector2(0.01f, 0), new Vector2(-5, 0), 0, 0f, 20, 3, 2, 1, "bone_v_bottom", "sprites/sans.atlas", 0.002f, 0.9f);
        }
        resetControls();
    }

    @Override
    void basicUp() {

//        Adds projectiles to simulate "growing"
        new Projectile(this, new Vector2(0, height + 0.05f), new Vector2(0, 1), 0, 0f, 0.1f, 2, 4, 2, "bone_v_top", "sprites/sans.atlas", 0, 0.5f);
        new Projectile(this, new Vector2(0, height - 0.0375f + 0.05f), new Vector2(0, 1), 0, 0f, 0.1f, 0, 2, 0, "bone_v_mid", "sprites/sans.atlas", 0.01f, 0.5f);
        new Projectile(this, new Vector2(0, height - 0.0375f + 0.05f), new Vector2(0, 1), 0, 0f, 0.075f, 0, 0, 0, "bone_v_mid", "sprites/sans.atlas", 0.03f, 0.5f);
        new Projectile(this, new Vector2(0, height - 0.0375f + 0.05f), new Vector2(0, 1), 0, 0f, 0.05f, 0, 0, 0, "bone_v_mid", "sprites/sans.atlas", 0.05f, 0.5f);
        new Projectile(this, new Vector2(0, height - 0.0375f + 0.05f), new Vector2(0, 1), 0, 0f, 0.025f, 0, 0, 0, "bone_v_mid", "sprites/sans.atlas", 0.07f, 0.5f);

        resetControls();
    }

    @Override
    void basicDown() {

//        Adds collider
        new Collider(new Vector2(0, 0f), 75, 35, this, 2f, 5f, 0.25f, 0);

        resetControls();
    }

    @Override
    void dashAttack() {
//        Adds collider
        new Collider(new Vector2(7.5f, 0), 15, 30, this, 3f, 4f, 0.25f, 0);
        resetControls();
    }

    @Override
    void smashSide() {

//        Adds a cooldown and creates a Gaster Blaster
        if (moveCooldown == 0) {
            moveCooldown = 1f;

            gasterBlaster = new GasterBlaster(this, 10f, laser);

        }
        resetControls();
    }

    @Override
    void smashUp() {

//        Same as side smash
        smashSide();
    }

    @Override
    void smashDown() {

//        Same as side smash
        smashSide();

    }

    @Override
    void specialNeutral() {

//        Makes user sit
        sit = true;
        resetControls();
    }

    @Override
    void specialSide() {

//        Creates a new collider, adds a cooldown, and moves user
        new Collider(new Vector2(0, 5), 40, 60, this, 2f, 4f, 0.25f, 0);
        animDuration = 1f;

        vel.set(new Vector2(7 * moveVector.x, 0));
        resetControls();
    }

    @Override
    void specialUp() {
//        Code in update loop
    }

    @Override
    void specialDown() {
//        Code in update loop
        resetControls();
    }

    @Override
    void ultimate() {
    }

    @Override
    void airNeutral() {
//        Adds collider
        new Collider(new Vector2(0, 7.5f), 50, 35, this, 2f, 5f, 0.25f, 0);

        resetControls();
    }

    @Override
    void airForward() {

//        Adds a projectile based on direction
        if (facingLeft) {
            new Projectile(this, new Vector2(0, 0), new Vector2(-5, 0), 0, 0f, 20, 3, 4, 1, "bullet", "sprites/sans.atlas", 0, 0.9f);
        } else {
            new Projectile(this, new Vector2(0, 0), new Vector2(5, 0), 0, 0f, 20, 3, 4, 1, "bullet", "sprites/sans.atlas", 0, 0.9f);
        }
        resetControls();
    }

    @Override
    void airBack() {
//        Adds a projectile based on direction
        if (facingLeft) {
            new Projectile(this, new Vector2(0, 0), new Vector2(5, 0), 0, 0f, 20, 3, 4, 1, "bullet", "sprites/sans.atlas", 0, 0.9f);
        } else {
            new Projectile(this, new Vector2(0, 0), new Vector2(-5, 0), 0, 0f, 20, 3, 4, 1, "bullet", "sprites/sans.atlas", 0, 0.9f);
        }
        resetControls();
    }

    @Override
    void airUp() {
//        Adds a projectile
        new Projectile(this, new Vector2(0, 0), new Vector2(0, 5), 0, 0f, 20, 3, 4, 1, "bullet", "sprites/sans.atlas", 0, 0.9f);

        resetControls();
    }

    @Override
    void airDown() {
//        Adds a projectile
        new Projectile(this, new Vector2(0, 0), new Vector2(0, -5), 0, 0f, 20, 3, 4, 1, "bullet", "sprites/sans.atlas", 0, 0.9f);

        resetControls();
    }

}
