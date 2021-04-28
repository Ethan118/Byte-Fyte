package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.BattleMap;
import ca.error404.bytefyte.scene.PlayRoom;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class MasterChief extends Character {

    public MasterChief(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playernumber) {
        this(screen, spawnPoint, controller, playernumber, 0);
    }

    public MasterChief(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playernumber, int stamina) {
        super(screen, spawnPoint, controller, playernumber, "masterchief", "MASTER CHIEF", stamina);
        weight = 1.2f;
        walkSpeed = 0.9f;
        runSpeed = 1.8f;
        manualSpriteOffset = new Vector2(1100, 350);

        projectilesOnScreen = new ArrayList<>(1);

        walk.setFrameDuration(0.02f);
    }

    /*
    * Pre: Delta time
    * Post: Updates character
    * */
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (animState == AnimationState.SPECIAL_D ) {
            canDownB = false;
        } else if (grounded) {
            canDownB = true;
        }
        if (grounded && (animState == AnimationState.AIR_F || animState == AnimationState.AIR_B)) {
            moveVector = new Vector2(0, 0);
        }
        if ((upB.getKeyFrameIndex(elapsedTime) >= 12 && upB.getKeyFrameIndex(elapsedTime) <= 15) && animState == AnimationState.SPECIAL_U) {
            vel.y = 12;
        }
        if (downB.getKeyFrameIndex(elapsedTime) >= 20 && downB.getKeyFrameIndex(elapsedTime) <= 23 && animState == AnimationState.SPECIAL_D && projectilesOnScreen.isEmpty()) {
            projectilesOnScreen.add(new Projectile(this, new Vector2(0, 0), new Vector2(0, -5), 0, 0f, 20, 3, 20, 1, "laser", "sprites/masterchief.atlas", 0));
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
        if (facingLeft) {
            new Projectile(this, new Vector2( -0.25f, 0.01f), new Vector2(-6, 0), 0, 0f, 20, 0.25f, 2.4f, 0, "laser", "sprites/masterchief.atlas", 0);
        } else {
            new Projectile(this, new Vector2(0.25f, 0.01f), new Vector2(6, 0), 0, 0f, 20, 0.25f, 2.4f, 0, "laser", "sprites/masterchief.atlas", 0);

        }
        resetControls();
    }

    @Override
    void specialSide() {
        if (facingLeft) {
            new Projectile(this, new Vector2(-0.2f, 0.152f), new Vector2(-4f, 0), 0, 0f, 20, 3, 13, 1, "bazooka", "sprites/masterchief.atlas", 0.5f);
        } else {
            new Projectile(this, new Vector2(0.2f, 0.15f), new Vector2(4f, 0), 0, 0f, 20, 3, 13, 1, "bazooka", "sprites/masterchief.atlas", 0.5f);

        }
        resetControls();    }

    @Override
    void specialUp() {
//        Logic in update loop
    }

    @Override
    void specialDown() {
        if (canDownB) {
            vel.y = 4.75f;
//        friction = 0;
            if (facingLeft) {
                vel.x = -5;
            } else {
                vel.x = 5;
            }
        }
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
        if (facingLeft) {
            new Projectile(this, new Vector2(-0.2f, 0.1f), new Vector2(-6, 0), 0, 0f, 20, 3, 12, 1, "orb", "sprites/masterchief.atlas", 0.3f);
        } else {
            new Projectile(this, new Vector2(0.2f, 0.1f), new Vector2(6, 0), 0, 0f, 20, 3, 12, 1, "orb", "sprites/masterchief.atlas", 0.3f);

        }
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
