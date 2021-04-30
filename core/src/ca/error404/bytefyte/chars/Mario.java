package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.BattleMap;
import ca.error404.bytefyte.scene.PlayRoom;
import com.badlogic.gdx.controllers.Controller;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Mario extends Character {
//    initializing variables
    private Random random = new Random();
    private final String[] badgeEffects = {"speed", "jump", "damage", "healing", "invincibility"};
    private String currentEffect;
    private float boostSpeed = 3;
    private float boostJump = 5;
    private int damageMultiplier = 1;

    private float savedWalk;
    private float savedRun;
    private float savedJump;

    private boolean resetLuigi;
    private Luigi luigi;

    public Vector2 followPoint;

    private Vector2 leftFollow = new Vector2(0.2f, 0);
    private Vector2 rightFollow = new Vector2(-0.2f, 0);

    public float moveCooldown;

    private boolean down_b = false;

    public float badgeMeter = 0;
    public final float badgeMaxMeter = 100;
    public boolean badgeActive = false;

    public Mario(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playernumber) {
        this(screen, spawnPoint, controller, playernumber, 0);
    }

    /**
     * Constructor
     * @param screen
     * @param spawnPoint
     * @param controller
     * @param playerNumber
     * pre: the screen mario is in, the spawn point, the controller, the player's number (1-4)
     * post: instantiates Mario instance
     */
    public Mario(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playerNumber, int stamina) {
//        calls parent class constructor
        super(screen, spawnPoint, controller, playerNumber, "marioluigi", "MARIO & LUIGI", 1, 1, stamina);

//        initialize variables
        savedWalk = walkSpeed;
        savedRun = runSpeed;
        savedJump = jumpPower;

        manualSpriteOffset = new Vector2(250, 252);
        projectilesOnScreen = new ArrayList<>(1);

//        setting frame durations
        idle.setFrameDuration(1/30f);
        walk.setFrameDuration(1/30f);
        run.setFrameDuration(1/30f);

        jump.setFrameDuration(1/30f);
        fall.setFrameDuration(1/30f);
        hit.setFrameDuration(1/30f);

        neutralAttack.setFrameDuration(1/30f);
        sideTilt.setFrameDuration(1/30f);
        upTilt.setFrameDuration(1/30f);
        downTilt.setFrameDuration(1/30f);

        neutralB.setFrameDuration(1/30f);
        upB.setFrameDuration(1/30f);
        downB.setFrameDuration(1/30f);
        downB.setPlayMode(Animation.PlayMode.NORMAL);
        sideB.setFrameDuration(1/30f);

        nair.setFrameDuration(1/30f);
        dair.setFrameDuration(1/30f);
        fair = fall;
        bair = fall;
        uair = fall;

        upSmash = idle;
        downSmash.setFrameDuration(1/30f);
        sideSmash.setFrameDuration(1/30f);

        dashAttack = idle;

        followPoint = rightFollow;

//        spawn luigi and reset him
        luigi = new Luigi(screen, followPoint, controller, playerNumber, this);
        resetLuigi = true;
    }

    @Override
    /**
     * pre:
     * post: handles user inputs for the character
     */
    protected void handleInput() {
        super.handleInput();

//        if the character is in the downB state disable jumping
        if (down_b) {
            jumping = false;

//            check for the special down input again
            if (attackState == AttackState.SPECIAL && moveVector.y < 0) {
                specialDown();
            }
        }
    }

    @Override
    public void update(float deltaTime) {
//        countdown the move cooldown
        if (moveCooldown > 0) {
            moveCooldown -= deltaTime;
        } else {
            moveCooldown = 0;
        }

//        if the badge is active countdown the badge meter to 0
        if (badgeActive) {
            badgeMeter = Math.max(badgeMeter - (deltaTime * 3), 0);

//            check which effect is active and apply this
            switch (currentEffect) {
                case "speed":
                    walkSpeed = boostSpeed;
                    runSpeed = boostSpeed;
                    luigi.walkSpeed = boostSpeed;
                    luigi.runSpeed = boostSpeed;
                    luigi.maxRange = 0.5f;
                    break;
                case "jump":
                    jumpPower = boostJump;
                    luigi.jumpPower = boostJump;
                    break;
                case "damage":
                    damageMultiplier = 2;
                    luigi.damageMultiplier = 2;
                    break;
                case "invincibility":
                    invinsible = true;
                    luigi.invinsible = true;
                    break;
            }

//            once the badge runs out reset all status effects
            if (badgeMeter == 0) {
                walkSpeed = savedWalk;
                runSpeed = savedRun;
                jumpPower = savedJump;

                damageMultiplier = 1;
                luigi.damageMultiplier = 1;
                luigi.maxRange = 0.2f;

                luigi.walkSpeed = luigi.savedWalk;
                luigi.runSpeed = luigi.savedRun;
                luigi.jumpPower = luigi.savedJump;

                invinsible = false;
                luigi.invinsible = false;

                badgeActive = false;
            }
        }

//        calls the parent class update function
        super.update(deltaTime);

//        checks if the upB is active and sets the y velocity to 9
        if (upB.getKeyFrameIndex(elapsedTime) == 14 && animState == AnimationState.SPECIAL_U) {
            vel.y = 9;
        }

//        changes the follow point based on the direction facing
        if (facingLeft) {
            followPoint = pos.cpy().add(leftFollow);
        } else {
            followPoint = pos.cpy().add(rightFollow);
        }

//        resets luigi
        if (resetLuigi) {
            resetLuigi();
        }

//        resets luigi if he is hiding
        resetLuigi = luigi.hide;
    }

    /**
     * pre:
     * post: overrites the death function and destroys luigi if dead
     */
    @Override
    public void die() {
        super.die();
        if (dead) {
            luigi.destroy();
        }

//        resets badge meter and luigi
        badgeMeter = 0;
        resetLuigi = true;
    }

    /**
     * pre:
     * post: reset luigi's pos, and controls
     */
    private void resetLuigi() {
        luigi.goToPos = followPoint;
        resetLuigi = false;
        luigi.resetControls();
    }

//    creates a fireball
    @Override
    void basicNeutral() {
//        creates the fireball if the cooldown is over
        if (moveCooldown == 0) {
//            adds to the badge meter if it is not active
            if (!badgeActive) {
                badgeMeter = Math.min(badgeMeter + 10, badgeMaxMeter);
            }

            if (facingLeft) {
                new Projectile(this, new Vector2(-0.1f, 0f), new Vector2(-5, 0), 0, 0, 10, 2f, 2f * damageMultiplier, 0.15f, "fireball", "sprites/marioluigi.atlas", 0, 0.4f);
            } else {
                new Projectile(this, new Vector2(0.1f, 0f), new Vector2(5, 0), 0, 0, 10, 2f, 2f * damageMultiplier, 0.15f, "fireball", "sprites/marioluigi.atlas", 0, 0.4f);
            }
        }

        resetControls();
    }

    //    creates a fireball
    @Override
    void basicSide() {
//        adds to the badge meter if it is not active
        if (!badgeActive) {
            badgeMeter = Math.min(badgeMeter + 10, badgeMaxMeter);
        }

        if (facingLeft) {
            new Projectile(this, new Vector2(-0.1f, 0f), new Vector2(-5, 0), 0, 0, 10, 2f, 2f * damageMultiplier, 0.15f, "fireball", "sprites/marioluigi.atlas", 20f/30f, 0.4f);
        } else {
            new Projectile(this, new Vector2(0.1f, 0f), new Vector2(5, 0), 0, 0, 10, 2f, 2f * damageMultiplier, 0.15f, "fireball", "sprites/marioluigi.atlas", 20f/30f, 0.4f);
        }

        resetControls();
    }

//    creates a collier above mario
    @Override
    void basicUp() {
        if (!badgeActive) {
            badgeMeter = Math.min(badgeMeter + 10, badgeMaxMeter);
        }

        new Collider(new Vector2(0, 20), 30, 20, this, 3f, 3f * damageMultiplier, 0.25f, 8f/30f, 2f/30f);

        resetControls();
    }

//    activates the badge effect
    @Override
    void basicDown() {
//        if the badge meter is full activate a random badge effect
        if (badgeMeter == badgeMaxMeter) {
            badgeActive = true;

            int i = random.nextInt(badgeEffects.length);
            currentEffect = badgeEffects[i];

            if (currentEffect.equals("healing")) {
                if (stamina) {
                    percent = Math.min(percent + 72.8f, 999);
                } else {
                    percent = Math.max(percent - 72.8f, 0);
                }
            }
        }
    }

    @Override
    void dashAttack() {

    }

//    create a shell
    @Override
    void smashSide() {
        if (!badgeActive) {
            badgeMeter = Math.min(badgeMeter + 10, badgeMaxMeter);
        }

        if (facingLeft) {
            new Projectile(this, new Vector2(-0.1f, -0.07f), new Vector2(-5, 0), 0, 0, 10, 4f , 5f * damageMultiplier, 0.25f, "red-shell", "sprites/marioluigi.atlas", 10 / 30f, spriteScale);
        } else {
            new Projectile(this, new Vector2(0.1f, -0.07f), new Vector2(5, 0), 0, 0, 10, 4f, 5f * damageMultiplier, 0.25f, "red-shell", "sprites/marioluigi.atlas", 10 / 30f, spriteScale);
        }

        resetControls();
    }

    @Override
    void smashUp() {

    }

//    create a collider
    @Override
    void smashDown() {
        if (!badgeActive) {
            badgeMeter = Math.min(badgeMeter + 10, badgeMaxMeter);
        }

        new Collider(new Vector2(30, 0), 55, 40, this, 2f, 2f * damageMultiplier, 0.25f, 0);

        //resetControls();
    }

//    create a collider
    @Override
    void specialNeutral() {
        if (!badgeActive) {
            badgeMeter = Math.min(badgeMeter + 10, badgeMaxMeter);
        }
        new Collider(new Vector2(25, -5), 30, 40, this, 2f, 2f * damageMultiplier, 0.25f, 79f/30f);

        resetControls();
    }

//    dash forward and spawn a collider
    @Override
    void specialSide() {
        if (!badgeActive) {
            badgeMeter = Math.min(badgeMeter + 10, badgeMaxMeter);
        }
        new Collider(new Vector2(0, 10), 30, 60, this, 2f, 2f * damageMultiplier, 0.25f, 0);

        animDuration = 1f;
        vel.x = 7 * moveVector.x;
        resetControls();
    }

    @Override
    void specialUp() {

    }

//    dig into the ground and become invulnerable
    @Override
    void specialDown() {
        if (!down_b) {
            if (!badgeActive) {
                badgeMeter = Math.min(badgeMeter + 10, badgeMaxMeter);
            }
            animDuration = Float.POSITIVE_INFINITY;
            new Collider(new Vector2(0, 20), 70, 80, this, 2f, 2f * damageMultiplier, 0.25f, 0, 40f / 30f);
        } else {
            animDuration = 0.1f;
        }

        down_b = !down_b;
        invinsible = !invinsible;
    }

    @Override
    void ultimate() {

    }

//    create a bomb and launch it
    @Override
    void airNeutral() {
        if (!badgeActive) {
            badgeMeter = Math.min(badgeMeter + 10, badgeMaxMeter);
        }

        if (facingLeft) {
            new Projectile(this, new Vector2(-0.1f, 0f), new Vector2(-5, 0), 0.025f, 5, 10, 2f, 2f * damageMultiplier, 0.15f, "bomb", "sprites/marioluigi.atlas", 8f/30f, 1f);
        } else {
            new Projectile(this, new Vector2(0.1f, 0f), new Vector2(5, 0), 0.025f, 5, 10, 2f, 2f * damageMultiplier, 0.15f, "bomb", "sprites/marioluigi.atlas", 8f/30f, 1f);
        }
    }

    @Override
    void airForward() {

    }

    @Override
    void airBack() {

    }

    @Override
    void airUp() {

    }

//    create a collider
    @Override
    void airDown() {
        if (!badgeActive) {
            badgeMeter = Math.min(badgeMeter + 10, badgeMaxMeter);
        }

        new Collider(new Vector2(0, -50), 30, 40, this, 3f, 3f * damageMultiplier, 0.25f, 5f/30f);
    }
}
