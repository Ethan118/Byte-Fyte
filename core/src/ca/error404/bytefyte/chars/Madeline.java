package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.HairPoint;
import ca.error404.bytefyte.objects.Laser;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.PlayRoom;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;

public class Madeline extends Character {
//    initialize variables
    Badeline badeline;

    Vector2 leftOffset = new Vector2(17f, 5.5f);
    Vector2 rightOffset = new Vector2(15f, 5.5f);

    private LinkedList<HairPoint> hairPoints = new LinkedList<>();
    private LinkedList<HairPoint> bgPoints = new LinkedList<>();
    private Vector2 followPoint;
    private Vector2 followOffset;

    private boolean resetHair;

    public float badelineMeter = 0;
    public final float badelineMaxMeter = 100;
    public boolean badelineActive = false;

    private final Color oneDash = new Color(172 / 255f, 50 / 255f, 50 / 255f, 1);
    private final Color twoDashes = new Color(255 / 255f, 109 / 255f, 239 / 255f, 1);
    private final Color emptyDashes = new Color(68 / 255f, 183 / 255f, 255 / 255f, 1);
    private Color currentColor;

    public boolean charging = false;

    private float moveCooldown;

    private int maxDashes = 1;
    private int currentDash = maxDashes;

    public Madeline(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playernumber) {
        this(screen, spawnPoint, controller, playernumber, 0);
    }

    /**
     * Constructor
     * @param screen
     * @param spawnPoint
     * @param controller
     * @param playerNumber
     * @param stamina
     * pre: the screen madeline is on, spawn point, controller, player number, health
     * post: instantiates a madeline instance
     */
    public Madeline(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playerNumber, int stamina) {
//        calls parent constructor
        super(screen, spawnPoint, controller, playerNumber, "madeline", "MADELINE", 0.4f, 0.5f, stamina);
        manualSpriteOffset = rightOffset;

        weight = 1.1f;

//        reset animations
        TextureAtlas textureAtlas = Main.manager.get(String.format("sprites/%s.atlas", charname), TextureAtlas.class);

        sideTilt = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        upTilt = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        downTilt = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);

        upB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("dash"), Animation.PlayMode.LOOP);
        downB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("dash"), Animation.PlayMode.LOOP);
        sideB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("dash"), Animation.PlayMode.LOOP);

        nair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        dair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        fair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        bair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        uair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);

        upSmash = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        downSmash = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        sideSmash = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);

        dashAttack = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        hit = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("madeline_hit"), Animation.PlayMode.NORMAL);

        idle.setFrameDuration(1/30f);
        walk.setFrameDuration(1/60f);
        jump.setFrameDuration(1/30f);
        jump.setPlayMode(Animation.PlayMode.NORMAL);
        fall.setFrameDuration(1/30f);

        projectilesOnScreen = new ArrayList<>(1);

        resetHair = true;

        createHair();
    }

    public void update (float delta) {
//        countdown cooldown counter if above 0
        if (moveCooldown > 0) {
            moveCooldown -= delta;
        } else {
            moveCooldown = 0;
        }

//        if knocked off reset madeline
        if (knockedOff) {
            badelineMeter = 0;
            currentDash = 1;
            knockedOff = false;
        }

//        if charging increase the meter, lock the animation and check if the user must stop charging
        if (charging) {
            badelineMeter = Math.min(badelineMeter + (delta * 2), badelineMaxMeter);


            lockAnim = true;
            handleInput();
            if (attackState == AttackState.SPECIAL) {
                if (moveVector.isZero()) {
                    specialNeutral();
                }
            }

            if (!moveVector.isZero() || (attackState != AttackState.NONE && attackState != AttackState.SPECIAL) || jumping || hasBeenHit) {
                charging = false;
            }
        }

        super.update(delta);
        afterUpB = false;

//        reset the hair
        if (resetHair) {
            resetHair();
        }

//        reset the current amount of dashes
        if (grounded) {
            currentDash = maxDashes;
        }

//        spawn a badeline if the meter reaches max
        if (badelineMeter == badelineMaxMeter) {
            badelineActive = true;
            charging = false;
            badeline = new Badeline(this);
        } else if (!vel.isZero() && !badelineActive) {
            badelineMeter = Math.min(badelineMeter + (delta / 2), badelineMaxMeter);
        }

//        if badeline is active gain an extra dash countdown dashes and check to destroy badeline
        if (badelineActive) {
            maxDashes = 2;

            badelineMeter = Math.max(badelineMeter - delta * 3, 0);
            if (animState == AnimationState.SPECIAL_N) {
                animState = AnimationState.IDLE;
            }

            if (badelineMeter <= 0) {
                maxDashes = 1;
                badelineActive = false;
                Main.badeline.remove(badeline);
                badeline.destroy();
            }
        }

//        set manual sprite offset based on direction facing
        if (facingLeft) {
            manualSpriteOffset = leftOffset;
        } else {
            manualSpriteOffset = rightOffset;
        }

//        change hair color based on amount of dashes
        switch (currentDash) {
            case 0:
                currentColor = emptyDashes;
                break;
            case 1:
                currentColor = oneDash;
                break;
            case 2:
                currentColor = twoDashes;
                break;
        }

//        set follow points for hair
        findFollow();

//        simulate hair
        simulateHair(delta);
    }

    /**
     * pre:
     * post: sets the follow offset based on state
     */
    void findFollow() {
        switch (animState) {
            case BASIC_N:
            case BASIC_S:
            case BASIC_U:
            case BASIC_D:
            case SMASH_S:
            case SMASH_U:
            case SMASH_D:
            case AIR_N:
            case AIR_F:
            case AIR_B:
            case AIR_U:
            case AIR_D:
                followOffset = new Vector2(-0.065f, 0.14f);
                break;
            case RUN:
                followOffset = new Vector2(-0.04f, 0.14f);
                break;
            case FALL:
            case JUMP:
                followOffset = new Vector2(-0.04f, 0.16f);
                break;
            case SPECIAL_S:
            case SPECIAL_U:
            case SPECIAL_D:
                followOffset = new Vector2(-0.075f, 0.14f);
                break;
            default:
                followOffset = new Vector2(-0.015f, 0.14f);

        }
    }

    /**
     * pre:
     * post: creates all hair points
     */
    void createHair() {
        for (int i = 0; i < 6; i++) {
            hairPoints.add(new HairPoint(this, Math.max(3, Math.min(6, 6 - i)), "hair", charname));
            bgPoints.add(new HairPoint(this, Math.max(4.5f, Math.min(7.5f, 7.5f - i)), "hair", charname));
        }
    }

    /**
     * @param delta
     * pre: the amount of time between frames
     * post: change follow point based on facing and simulate hair
     */
    void simulateHair(float delta) {
        if (facingLeft) {
            followPoint = pos.cpy().add(new Vector2(followOffset.x, followOffset.y));
        } else {
            followPoint = pos.cpy().add(new Vector2(followOffset.x * -1, followOffset.y));
        }

//        loop through all hair points and set the follow points to the hair point in front of it
        int i = 0;
        for (HairPoint h : hairPoints) {
            if (i == 0) {
                h.pos = followPoint;
            }

            h.targetPos = followPoint;
            h.update(delta);
            followPoint = h.pos;

            i++;
        }

//        same for the hair outline
        if (facingLeft) {
            followPoint = pos.cpy().add(new Vector2(followOffset.x, followOffset.y));
        } else {
            followPoint = pos.cpy().add(new Vector2(followOffset.x * -1, followOffset.y));
        }
        i = 0;
        for (HairPoint h : bgPoints) {
            if (i == 0) {
                h.pos = followPoint;
            }

            h.targetPos = followPoint;
            h.update(delta);
            followPoint = h.pos;

            i++;
        }
    }

    /**
     * @param batch
     * pre: the batch used to draw
     * post: draw hair
     */
    public void drawHair(SpriteBatch batch) {
        if (!charging) {
            for (HairPoint h : bgPoints) {
                h.setColor(Color.BLACK);
                h.draw(batch);
            }

            for (HairPoint h : hairPoints) {
                h.setColor(currentColor);
                h.draw(batch);
            }
        }
    }

    /**
     * pre:
     * post: reset hair
     */
    public void resetHair() {
        for (HairPoint h : bgPoints) {
            h.pos = pos.cpy();
            h.targetPos = pos.cpy();
        }

        for (HairPoint h : hairPoints) {
            h.pos = pos.cpy();
            h.targetPos = pos.cpy();
        }

        resetHair = false;
    }

//    if badeline is active spawn projectiles otherwise spawn a collider
    @Override
    void basicNeutral() {
        if (!badelineActive) {
            new Collider(new Vector2(20, 0), 10, 25, this, 2f, 2f, 0.25f, 0);
        } else {
            if (moveCooldown == 0) {
                moveCooldown = 1f;

                // badeline projectile
                Vector2 dir = moveVector.cpy();

                if (facingLeft) {
                    if (dir.isZero()) {
                        dir.x = -1;
                    }
                    new Projectile(this, new Vector2(0.5f, 0.3f), dir.cpy().scl(5), 0, 0, 10, 5f, 10f, 0.5f, "projectile", "sprites/madeline.atlas", 0, 0.4f);
                } else {
                    if (dir.isZero()) {
                        dir.x = 1;
                    }
                    new Projectile(this, new Vector2(-0.5f, 0.3f), dir.cpy().scl(5), 0, 0, 10, 5f, 10f, 0.5f, "projectile", "sprites/madeline.atlas", 0, 0.4f);
                }
            }
        }
    }

    @Override
    void basicSide() {
        basicNeutral();
    }

    @Override
    void basicUp() {
        basicNeutral();
    }

    @Override
    void basicDown() {
        basicNeutral();
    }

    @Override
    void dashAttack() {
    }

//    if badeline is active spawn lasers
    @Override
    void smashSide() {
        if (badelineActive && moveCooldown == 0) {
            moveCooldown = 1f;

            if (controller != null) {
                if (facingLeft) {
                    new Laser(this, new Vector2(0.3f, 0.3f), rStick.cpy(), 100, 5, 10, 0.5f, 53f / 30f, 74f / 30f, "beam", "sprites/madeline.atlas", 0.4f);
                } else {
                    new Laser(this, new Vector2(-0.3f, 0.3f), rStick.cpy(), 100, 5, 10, 0.5f, 53f / 30f, 74f / 30f, "beam", "sprites/madeline.atlas", 0.4f);
                }
            } else {
                if (facingLeft) {
                    new Laser(this, new Vector2(0.3f, 0.3f), moveVector.cpy(), 100, 5, 10, 0.5f, 53f / 30f, 74f / 30f, "beam", "sprites/madeline.atlas", 0.4f);
                } else {
                    new Laser(this, new Vector2(-0.3f, 0.3f), moveVector.cpy(), 100, 5, 10, 0.5f, 53f / 30f, 74f / 30f, "beam", "sprites/madeline.atlas", 0.4f);
                }
            }
        } else {
            basicNeutral();
        }
    }

    @Override
    void smashUp() {
        smashSide();
    }

    @Override
    void smashDown() {
        smashSide();
    }

//    toggle charging
    @Override
    void specialNeutral() {
        if (!badelineActive) {
            charging = !charging;
            resetControls();
        }
    }

//    dash in the move vector direction
    @Override
    void specialSide() {
        if (currentDash > 0) {
            if (moveVector.y > 0) {
                grounded = false;
            }

            new Collider(new Vector2(0, 0), 30, 30, this, 2f, 5.2f, 0.25f, 0);

            if (!badelineActive) {
                badelineMeter = Math.min(badelineMeter + 1, badelineMaxMeter);
            }

            currentDash -= 1;

            animDuration = 0.1f;
            Vector2 dir = moveVector;
            vel.set(dir.scl(7));

            resetControls();
        }
    }

    @Override
    void specialUp() {
        specialSide();
    }

    @Override
    void specialDown() {
        specialSide();
    }

    @Override
    void ultimate() {

    }

    @Override
    void airNeutral() {
        basicNeutral();
    }

    @Override
    void airForward() {
        basicNeutral();
    }

    @Override
    void airBack() {
        basicNeutral();
    }

    @Override
    void airUp() {
        basicNeutral();
    }

    @Override
    void airDown() {
        basicNeutral();
    }

//    reset hair if dies
    @Override
    public void die() {
        super.die();
        resetHair = true;
    }
}
