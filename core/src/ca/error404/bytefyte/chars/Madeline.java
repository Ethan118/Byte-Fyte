package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.Laser;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.TMap;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Madeline extends Character {
    Badeline badeline;

    Vector2 leftOffset = new Vector2(17f, 5f);
    Vector2 rightOffset = new Vector2(15f, 5f);

    private float badelineMeter = 0;
    private final float badelineMaxMeter = 100;
    private boolean badelineActive = false;

    private boolean charging = false;

    public Madeline(TMap screen, Vector2 spawnPoint, Controller controller, int playerNumber) {
        super(screen, spawnPoint, controller, playerNumber, "madeline", "MADELINE", 0.4f, 0.5f);
        manualSpriteOffset = rightOffset;

        TextureAtlas textureAtlas = Main.manager.get(String.format("sprites/%s.atlas", charname), TextureAtlas.class);

        sideTilt = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        upTilt = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        downTilt = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);

        upB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("dash"), Animation.PlayMode.LOOP);
        downB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("dash"), Animation.PlayMode.LOOP);
        sideB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("dash"), Animation.PlayMode.LOOP);

        nair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        dair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        fair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        bair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        uair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);

        upSmash = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        downSmash = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        sideSmash = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);

        dashAttack = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);

        projectilesOnScreen = new ArrayList<>(1);
    }

    public void update (float delta) {
        if (charging) {
            badelineMeter = Math.min(badelineMeter + (delta * 10), badelineMaxMeter);

            lockAnim = true;
            handleInput();
            if (attackState == AttackState.SPECIAL) {
                if (moveVector.isZero()) {
                    specialNeutral();
                }
            }

            if (!moveVector.isZero() || (attackState != AttackState.NONE && attackState != AttackState.SPECIAL) || jumping) {
                charging = false;
            }
        }

        super.update(delta);

        if (badelineMeter == badelineMaxMeter) {
            badelineActive = true;
            charging = false;
            badeline = new Badeline(this);
        } else if (!vel.isZero() && !badelineActive) {
            badelineMeter = Math.min(badelineMeter + delta, badelineMaxMeter);
        }

        if (badelineActive) {
            badelineMeter = Math.max(badelineMeter - delta, 0);

            if (badelineMeter <= 0) {
                badelineActive = false;
                badeline.destroy();
            }
        }

        if (facingLeft) {
            manualSpriteOffset = leftOffset;
        } else {
            manualSpriteOffset = rightOffset;
        }
    }

    @Override
    void basicNeutral() {
        if (!badelineActive) {
            new Collider(new Vector2(20, 0), 10, 25, this, 2f, 2f, 0.25f, 0);
        } else {
            // badeline projectile
            Vector2 dir = moveVector.cpy();

            if (facingLeft) {
                if (dir.isZero()) {
                    dir.x = -1;
                }
                new Projectile(this, new Vector2(0.5f, 0.3f), dir.cpy().scl(5), 0, 0, 10, 5f, 10f, 0.5f, "projectile", "sprites/badeline.atlas", 0, 0.4f);
            } else {
                if (dir.isZero()) {
                    dir.x = 1;
                }
                new Projectile(this, new Vector2(-0.5f, 0.3f), dir.cpy().scl(5), 0, 0, 10, 5f, 10f, 0.5f, "projectile", "sprites/badeline.atlas", 0, 0.4f);
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
        if (badelineActive) {
            if (facingLeft) {
                new Laser(this, new Vector2(0.3f, 0.3f), moveVector.cpy(), 100, 5, 10, 0.5f, 53f / 30f, 74f / 30f, "beam", "sprites/badeline.atlas", 0.4f);
            } else {
                new Laser(this, new Vector2(-0.3f, -0.3f), moveVector.cpy(), 100, 5, 10, 0.5f, 53f / 30f, 74f / 30f, "beam", "sprites/badeline.atlas", 0.4f);
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
        new Collider(new Vector2(0, 0), 30, 30, this, 2f, 5.2f, 0.25f, 0);
        badelineMeter = Math.min(badelineMeter + 5, badelineMaxMeter);

        animDuration = 1;
        Vector2 dir = moveVector;
        vel.set(dir.scl(7));

        resetControls();
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
}
