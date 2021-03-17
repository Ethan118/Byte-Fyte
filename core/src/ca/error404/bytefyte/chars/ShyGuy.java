package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import java.util.Arrays;

public class ShyGuy extends Character {

    public ShyGuy(TestScene screen, Vector2 spawnPoint, Controller controller) {
        super(screen, spawnPoint, controller);
        manualSpriteOffset = new Vector2(2200, 300);
    }

    private int hovertimer = 2;
    private int timer = 2;
    private boolean hasHovered = false;

    public void update(float deltaTime) {
        super.update(deltaTime);
        System.out.println(hasHovered);

        if (grounded) {
            hasHovered = false;
            duration = 0;
        }
        if (animState == AnimationState.SPECIAL_U && vel.y < 0) {
            lockAnim = false;
        }

        if (animState == AnimationState.SPECIAL_U) {
            specialUp();
            hasHovered = true;
        }
    }
private float duration = 0f;

    @Override
    void basicNeutral() {
        Collider hitBox = new Collider(new Vector2(20, 0), 5, 30, this, 2f, 5f, 0.25f);
        colliders.add(hitBox);
        moveVector = new Vector2(0, 0);
    }

    @Override
    void basicSide() {
        Collider hitBox = new Collider(new Vector2(20, 0), 25, 30, this, 3f, 7f, 0.25f);
        colliders.add(hitBox);
        moveVector = new Vector2(0, 0);
    }

    @Override
    void basicUp() {
        Collider hitBox = new Collider(new Vector2(0, 20), 30, 5, this, 3f, 7f, 0.25f);
        colliders.add(hitBox);
        moveVector = new Vector2(0, 0);
    }

    @Override
    void basicDown() {
        Collider hitBox = new Collider(new Vector2(0, -10), 40, 20, this, 3f, 6f, 0.25f);
        colliders.add(hitBox);
        moveVector = new Vector2(0, 0);
    }

    @Override
    void dashAttack() {
        moveVector = new Vector2(0, 0);
    }

    @Override
    void smashSide() {
        moveVector = new Vector2(0, 0);
    }

    @Override
    void smashUp() {
        moveVector = new Vector2(0, 0);
    }

    @Override
    void smashDown() {
        moveVector = new Vector2(0, 0);
    }

    @Override
    void specialNeutral() {
        moveVector = new Vector2(0, 0);
    }

    @Override
    void specialSide() {
        moveVector = new Vector2(0, 0);
    }

    @Override
    void specialUp() {
        if (animDuration == 0) {
            if (!hasHovered) {
                vel.y = -3;
                duration = 0;
                hasHovered = true;
            }
            animDuration = 1.4f;


        }
        vel.y = (duration * duration);
        duration += 0.02;



//        vel.y = jumpPower + 3;
//        jumpsLeft = 0;
    }

    @Override
    void specialDown() {
        animDuration = 7;
        lockAnim = true;
        moveVector = new Vector2(0, 0);
    }

    @Override
    void ultimate() {
    }

    @Override
    void airNeutral() {
        Collider hitBox = new Collider(new Vector2(0, 0), 40, 40, this, 4f, 5f, 0.25f);
        colliders.add(hitBox);
    }

    @Override
    void airForward() {
    }

    @Override
    void airBack() {
        Collider hitBox = new Collider(new Vector2(-20, 0), 20, 20, this, 3f, 4f, 0.25f);
        colliders.add(hitBox);
    }

    @Override
    void airUp() {
        Collider hitBox = new Collider(new Vector2(0, 20), 40, 20, this, 4f, 4f, 0.5f);
        colliders.add(hitBox);
    }

    @Override
    void airDown() {
    }
}
