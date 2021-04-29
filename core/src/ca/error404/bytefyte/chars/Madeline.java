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

    public Madeline(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playerNumber, int stamina) {
        super(screen, spawnPoint, controller, playerNumber, "madeline", "MADELINE", 0.4f, 0.5f, stamina);
        manualSpriteOffset = rightOffset;

        weight = 1.1f;

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
        if (moveCooldown > 0) {
            moveCooldown -= delta;
        } else {
            moveCooldown = 0;
        }

        if (knockedOff) {
            badelineMeter = 0;
            currentDash = 1;
            knockedOff = false;
        }

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

        if (resetHair) {
            resetHair();
        }

        if (grounded) {
            currentDash = maxDashes;
        }

        if (badelineMeter == badelineMaxMeter) {
            badelineActive = true;
            charging = false;
            badeline = new Badeline(this);
        } else if (!vel.isZero() && !badelineActive) {
            badelineMeter = Math.min(badelineMeter + (delta / 2), badelineMaxMeter);
        }

        if (badelineActive) {
            maxDashes = 2;

            badelineMeter = Math.max(badelineMeter - delta * 3, 0);
            if (animState == AnimationState.SPECIAL_N) {
                animState = AnimationState.IDLE;
            }

            if (badelineMeter <= 0) {
                maxDashes = 1;
                badelineActive = false;
                badeline.destroy();
            }
        }

        if (facingLeft) {
            manualSpriteOffset = leftOffset;
        } else {
            manualSpriteOffset = rightOffset;
        }

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

        findFollow();

        simulateHair(delta);
    }

    void findFollow() {
        switch (animState) {
            case BASIC_N:
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

    void createHair() {
        for (int i = 0; i < 6; i++) {
            hairPoints.add(new HairPoint(this, Math.max(3, Math.min(6, 6 - i)), "hair", charname));
            bgPoints.add(new HairPoint(this, Math.max(4.5f, Math.min(7.5f, 7.5f - i)), "hair", charname));
        }
    }

    void simulateHair(float delta) {
        if (facingLeft) {
            followPoint = pos.cpy().add(new Vector2(followOffset.x, followOffset.y));
        } else {
            followPoint = pos.cpy().add(new Vector2(followOffset.x * -1, followOffset.y));
        }

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

    @Override
    void specialNeutral() {
        if (!badelineActive) {
            charging = !charging;
            resetControls();
        }
    }

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

    @Override
    void airDown() {

    }

    @Override
    public void die() {
        super.die();
        resetHair = true;
    }
}
