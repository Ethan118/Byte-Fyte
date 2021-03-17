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

public class ShyGuy extends Character{

    public ShyGuy(TestScene screen, Vector2 spawnPoint, Controller controller) {
        super(screen, spawnPoint, controller);
        manualSpriteOffset = new Vector2(2200, 300);
    }

    public void update(float deltaTime) {
        super.update(deltaTime);

        if (animState == AnimationState.SPECIAL_U && vel.y < 0) {
            lockAnim = false;
        }
    }

    @Override
    void basicNeutral() {
        System.out.println("Basic Neutral");
        Collider hitBox = new Collider(new Vector2(20 / Main.PPM, 0 / Main.PPM), 5, 30, this, 2f, 5f, 0.25f);
        colliders.add(hitBox);
        moveVector = new Vector2(0, moveVector.y);
    }

    @Override
    void basicSide() {
        System.out.println("Basic Side");
        Collider hitBox = new Collider(new Vector2(20 / Main.PPM, 0 / Main.PPM), 25, 30, this, 3f, 7f, 0.25f);
        colliders.add(hitBox);
        moveVector = new Vector2(0, moveVector.y);
    }

    @Override
    void basicUp() {
        System.out.println("Basic Up");
        Collider hitBox = new Collider(new Vector2(0 / Main.PPM, 20 / Main.PPM), 30, 5, this, 3f, 7f, 0.25f);
        colliders.add(hitBox);
        moveVector = new Vector2(0, moveVector.y);
    }

    @Override
    void basicDown() {
        System.out.println("Basic Down");
        Collider hitBox = new Collider(new Vector2(0 / Main.PPM, -10 / Main.PPM), 40, 20, this, 3f, 6f, 0.25f);
        colliders.add(hitBox);
        moveVector = new Vector2(0, moveVector.y);
    }

    @Override
    void dashAttack() {
        System.out.println("Dash Attack");
        moveVector = new Vector2(0, moveVector.y);

    }

    @Override
    void smashSide() {
        System.out.println("Smash Side");
        moveVector = new Vector2(0, moveVector.y);

    }

    @Override
    void smashUp() {
        System.out.println("Smash Up");

    }

    @Override
    void smashDown() {
        System.out.println("Smash Down");

    }

    @Override
    void specialNeutral() {
        System.out.println("Special Neutral");
        moveVector = new Vector2(0, moveVector.y);
    }

    @Override
    void specialSide() {
        System.out.println("Special Side");
        moveVector = new Vector2(0, moveVector.y);
    }

    @Override
    void specialUp() {
        System.out.println("Special Up");
        vel.y = jumpPower + 3;
        jumpsLeft = 0;
    }

    @Override
    void specialDown() {
        System.out.println("Special Down");
        moveVector = new Vector2(0, moveVector.y);
    }

    @Override
    void ultimate() {
        System.out.println("Ultimate");
    }

    @Override
    void airNeutral() {
        System.out.println("Air Neutral");
        Collider hitBox = new Collider(new Vector2(0, 0), 40, 40, this, 4f, 5f, 0.25f);
        colliders.add(hitBox);
    }

    @Override
    void airForward() {
        System.out.println("Air Forward");
    }

    @Override
    void airBack() {
        System.out.println("Air Backward");
        Collider hitBox = new Collider(new Vector2(-20 / Main.PPM, 0), 20, 20, this, 3f, 4f, 0.25f);
        colliders.add(hitBox);
    }

    @Override
    void airUp() {
        System.out.println("Air Up");
        Collider hitBox = new Collider(new Vector2(0, 20 / Main.PPM), 40, 20, this, 4f, 4f, 0.5f);
        colliders.add(hitBox);
    }

    @Override
    void airDown() {
        System.out.println("Air Down");

    }
}
