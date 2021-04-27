package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.BattleMap;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Sans extends Character{

    private boolean upBStart = true;
    private boolean upBEnd = false;
    private boolean hasupB = false;

    private TextureAtlas textureAtlas = Main.manager.get(String.format("sprites/%s.atlas", charname), TextureAtlas.class);
    private boolean hasTeleported = false;

    public Sans(BattleMap screen, Vector2 spawnPoint, Controller controller, int playernumber) {
        super(screen, spawnPoint, controller, playernumber, "sans", "SANS", 0.8f, 0.9f);
        upB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("up_b_start"), Animation.PlayMode.NORMAL);

        weight = 0.9f;
        walkSpeed = 1.1f;
        runSpeed = 2.2f;
        manualSpriteOffset = new Vector2(50, 15f);

        projectilesOnScreen = new ArrayList<>(1);


        walk.setFrameDuration(0.02f);
    }

    /*
     * Pre: Delta time
     * Post: Updates character
     * */
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (upBStart) {
            upB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("up_b_start"), Animation.PlayMode.NORMAL);
        } else {
            upB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("up_b_end"), Animation.PlayMode.NORMAL);
        }

        if (animState == AnimationState.SPECIAL_U) {
            specialUp();
            if (!hasupB) {
                animDuration = 0.4f;
                hasupB = true;
            }
        }

        if (animState == AnimationState.SPECIAL_U) {
            if (!hasupB) {
                animDuration = 0.4f;
                hasupB = true;
            }
            if (animDuration < 1.9) {
                upBStart = true;
                upBEnd = false;
            } else if (animDuration < 4.1) {

            }
        }

    }

    //    All abilities.  Will add colliders or move master chief as applicable
    @Override
    void basicNeutral() {
        new Collider(new Vector2(30, 7), 25, 17, this, 2f, 5f, 0.25f, 0.4f);
        resetControls();
    }

    @Override
    void basicSide() {
        new Collider(new Vector2(35, 3), 40, 17, this, 2f, 8f, 0.25f, 0.4f);

        resetControls();
    }

    @Override
    void basicUp() {
        new Collider(new Vector2(25, 20), 35, 30, this, 2f, 4f, 0.25f, 0.1f);

        resetControls();
    }

    @Override
    void basicDown() {
        new Collider(new Vector2(20, 0), 30, 25, this, 2f, 6.5f, 0.25f, 0.3f);

        resetControls();
    }

    @Override
    void dashAttack() {
        new Collider(new Vector2(25, 0), 15, 30, this, 3f, 9f, 0.25f, 0);
        resetControls();
    }

    @Override
    void smashSide() {
        new Collider(new Vector2(40, 20), 100, 80, this, 4f, 18f, 0.5f, 0.4f);
        resetControls();
    }

    @Override
    void smashUp() {
        new Collider(new Vector2(25, 20), 35, 50, this, 3f, 10f, 0.25f, 0.1f);

        resetControls();
    }

    @Override
    void smashDown() {
        new Collider(new Vector2(35, -15), 30, 15, this, 2f, 10f, 0.25f, 0.1f);

        resetControls();
    }

    @Override
    void specialNeutral() {

    }

    @Override
    void specialSide() {
        new Collider(new Vector2(0, 5), 40, 60, this, 8f, 4f, 0.25f, 0);
        animDuration = 1f;

        vel.set(new Vector2(7 * moveVector.x, 0));
        resetControls();
    }

    @Override
    void specialUp() {
        if (animDuration > 0.2) {
            upBStart = false;
            upBEnd = true;
            if (animDuration < 0.4) {
                if (!hasTeleported) {
                    pos.y += 10;
                    hasTeleported = true;
                }
            }
        } else {
            upBStart = true;
            upBEnd = false;
        }
    }

    @Override
    void specialDown() {

        resetControls();
    }

    @Override
    void ultimate() {
    }

    @Override
    void airNeutral() {
        new Collider(new Vector2(25, -5), 15, 20, this, 2f, 5f, 0.25f, 0);

        resetControls();
    }

    @Override
    void airForward() {

    }

    @Override
    void airBack() {
        new Collider(new Vector2(-35, 15), 50, 20, this, 4f, 14f, 0.25f, 0.35f);
        resetControls();
    }

    @Override
    void airUp() {
        new Collider(new Vector2(25, 20), 35, 45, this, 2f, 7f, 0.25f, 0.1f);

        resetControls();
    }

    @Override
    void airDown() {
        new Collider(new Vector2(15, -15), 15, 25, this, 3f, 8f, 0.25f, 0.1f);

        resetControls();
    }

}
