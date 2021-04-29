package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.BattleMap;
import com.badlogic.gdx.controllers.Controller;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Mario extends Character {
    private boolean resetLuigi;
    private Luigi luigi;

    public Vector2 followPoint;

    private Vector2 leftFollow = new Vector2(0.2f, 0);
    private Vector2 rightFollow = new Vector2(-0.2f, 0);

    public Mario(BattleMap screen, Vector2 spawnPoint, Controller controller, int playerNumber) {
        super(screen, spawnPoint, controller, playerNumber, "marioluigi", "MARIO & LUIGI", 1, 1);

        manualSpriteOffset = new Vector2(250, 250);
        projectilesOnScreen = new ArrayList<>(1);

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
        sideB.setFrameDuration(1/30f);

        nair.setFrameDuration(1/30f);
        dair.setFrameDuration(1/30f);
        fair.setFrameDuration(1/30f);
        bair.setFrameDuration(1/30f);
        uair.setFrameDuration(1/30f);

        upSmash.setFrameDuration(1/30f);
        downSmash.setFrameDuration(1/30f);
        sideSmash.setFrameDuration(1/30f);

        dashAttack.setFrameDuration(1/30f);

        followPoint = rightFollow;

        luigi = new Luigi(screen, followPoint, controller, playerNumber, this);
        resetLuigi = true;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (facingLeft) {
            followPoint = pos.cpy().add(leftFollow);
        } else {
            followPoint = pos.cpy().add(rightFollow);
        }

        if (resetLuigi) {
            resetLuigi();
        }
    }

    @Override
    public void die() {
        super.die();
        resetLuigi = true;
    }

    private void resetLuigi() {
        luigi.goToPos = followPoint;
        resetLuigi = false;
    }

    @Override
    void basicNeutral() {
        if (facingLeft) {
            new Projectile(this, new Vector2(-0.1f, 0f), new Vector2(-5, 0), 0, 0, 10, 2f, 2f, 0.15f, "fireball", "sprites/marioluigi.atlas", 0, 0.4f);
        } else {
            new Projectile(this, new Vector2(0.1f, 0f), new Vector2(5, 0), 0, 0, 10, 2f, 2f, 0.15f, "fireball", "sprites/marioluigi.atlas", 0, 0.4f);
        }
    }

    @Override
    void basicSide() {
        if (facingLeft) {
            new Projectile(this, new Vector2(-0.1f, 0f), new Vector2(-5, 0), 0, 0, 10, 2f, 2f, 0.15f, "fireball", "sprites/marioluigi.atlas", 20f/30f, 0.4f);
        } else {
            new Projectile(this, new Vector2(0.1f, 0f), new Vector2(5, 0), 0, 0, 10, 2f, 2f, 0.15f, "fireball", "sprites/marioluigi.atlas", 20f/30f, 0.4f);
        }
    }

    @Override
    void basicUp() {

    }

    @Override
    void basicDown() {

    }

    @Override
    void dashAttack() {

    }

    @Override
    void smashSide() {
        if (facingLeft) {
            new Projectile(this, new Vector2(-0.1f, -0.07f), new Vector2(-5, 0), 0, 0, 10, 4f, 5f, 0.25f, "red-shell", "sprites/marioluigi.atlas", 10 / 30f, spriteScale);
        } else {
            new Projectile(this, new Vector2(0.1f, -0.07f), new Vector2(5, 0), 0, 0, 10, 4f, 5f, 0.25f, "red-shell", "sprites/marioluigi.atlas", 10 / 30f, spriteScale);
        }
    }

    @Override
    void smashUp() {

    }

    @Override
    void smashDown() {
        new Collider(new Vector2(20, 0), 65, 40, this, 2f, 2f, 0.25f, 0);
    }

    @Override
    void specialNeutral() {
        new Collider(new Vector2(25, -5), 30, 40, this, 2f, 2f, 0.25f, 79f/30f);
    }

    @Override
    void specialSide() {

    }

    @Override
    void specialUp() {

    }

    @Override
    void specialDown() {

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
