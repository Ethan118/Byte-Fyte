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

    @Override
    void basicNeutral() {
        System.out.println("Basic Neutral");
        Collider hitBox = new Collider(new Vector2(20 / Main.PPM, 0 / Main.PPM), 5, 30, this, 3f, 1f, 5f, Vector2.X, 0.25f);
        colliders.add(hitBox);
    }

    @Override
    void basicSide() {
        System.out.println("Basic Side");
        Collider hitBox = new Collider(new Vector2(20 / Main.PPM, 0 / Main.PPM), 15, 20, this, 3f, 1f, 7f, Vector2.X, 0.25f);
        colliders.add(hitBox);
    }

    @Override
    void basicUp() {
        System.out.println("Basic Up");
        Collider hitBox = new Collider(new Vector2(0 / Main.PPM, 20 / Main.PPM), 30, 5, this, 3f, 1f, 7f, new Vector2(0, 1), 0.25f);
        colliders.add(hitBox);
    }

    @Override
    void basicDown() {
        System.out.println("Basic Down");
        Collider hitBox = new Collider(new Vector2(0 / Main.PPM, -10 / Main.PPM), 40, 20, this, 3f, 1f, 6f, new Vector2(0, 1), 0.25f);
        colliders.add(hitBox);
    }

    @Override
    void dashAttack() {
        System.out.println("Dash Attack");

    }

    @Override
    void smashSide() {
        System.out.println("Smash Side");

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

    }

    @Override
    void specialSide() {
        System.out.println("Special Side");

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

    }

    @Override
    void ultimate() {
        System.out.println("Ultimate");

    }

    @Override
    void airNeutral() {
        System.out.println("Air Neutral");

    }

    @Override
    void airForward() {
        System.out.println("Air Forward");

    }

    @Override
    void airBack() {
        System.out.println("Air Backward");

    }

    @Override
    void airUp() {
        System.out.println("Air Up");

    }

    @Override
    void airDown() {
        System.out.println("Air Down");

    }
}
