package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ShyGuy extends Character{
    public ShyGuy(TestScene screen, Vector2 spawnPoint, Controller controller) {
        super(screen, spawnPoint, controller);
    }


    @Override
    void basicNeutral() {
        System.out.println("Basic Neutral");
    }

    @Override
    void basicSide() {
        System.out.println("Basic Side");
    }

    @Override
    void basicUp() {
        System.out.println("Basic Up");
    }

    @Override
    void basicDown() {
        System.out.println("Basic Down");
    }

    @Override
    void dashAttack() {
        System.out.println("Dash Attack");

    }

    @Override
    void smashNeutral() {
        System.out.println("Smash Neutral");

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
    void airLeft() {
        System.out.println("Air Left");

    }

    @Override
    void airRight() {
        System.out.println("Air Right");

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
