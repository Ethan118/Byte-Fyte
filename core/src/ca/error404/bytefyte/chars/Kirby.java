package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.Globals;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.MultiHit;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.TMap;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;

import javax.sound.sampled.AudioFormat;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Kirby extends Character {
    private float yOffset = 12.5f;

    private boolean hasHovered = false;
    private float flyAcceleration = 0f;

    public Kirby(TMap screen, Vector2 spawnPoint, Controller controller, int playernumber) {
        super(screen, spawnPoint, controller, playernumber, "kirby", "KIRBY", 0.7f, 0.8f);
        weight = 0.8f;
        maxJumps = 10;
        manualSpriteOffset = new Vector2(100, yOffset);
        projectilesOnScreen = new ArrayList<>(1);
    }

    /*
    * Pre: Delta time
    * Post: Updates character
    * */
    public void update(float deltaTime) {
        super.update(deltaTime);
//        Ensuring he respawns
        if (knockedOff && stockCount != 0) {
            knockedOff = false;
        }

//        Allowing the animation of up b to play
        if (animState == AnimationState.SPECIAL_U && vel.y < 0) {
            lockAnim = false;
        }

//        Hovering for up b
        if (animState == AnimationState.SPECIAL_U) {
            specialUp();
            hasHovered = true;
            manualSpriteOffset.y = 40f;
        } else {
            manualSpriteOffset.y = yOffset;
        }

//        Sets cooldown for side b
        if (attackState == AttackState.SPECIAL && moveState == MovementState.WALK) {
            animDuration = 0.075f;
        }

    }

//    All abilities.  Will add colliders or move shy guy as applicable
    @Override
    void basicNeutral() {
        resetControls();
    }

    @Override
    void basicSide() {
        resetControls();
    }

    @Override
    void basicUp() {
        resetControls();
    }

    @Override
    void basicDown() {
        resetControls();
    }

    @Override
    void dashAttack() {
        resetControls();
    }

    @Override
    void smashSide() {
        resetControls();
    }

    @Override
    void smashUp() {
        resetControls();
    }

    @Override
    void smashDown() {
        resetControls();
    }

    @Override
    void specialNeutral() {
        resetControls();
    }

    @Override
    void specialSide() {
        resetControls();
    }

    @Override
    void specialUp() {

//        Hovers him for a frame
        if (animDuration == 0) {
            if (!hasHovered && !grounded) {
                vel.y = -3;
            }

            flyAcceleration = 0;
            hasHovered = true;
            animDuration = 1.8f;
        }

//        Exponentially flies him up after the initial frame
        vel.y = (flyAcceleration * flyAcceleration);
        flyAcceleration += 0.02;

//        Gets direction being faced
        if (moveVector.x > deadzone) {
            facingLeft = false;
        } else if (moveVector.x < -deadzone) {
            facingLeft = true;
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
        resetControls();
    }

    @Override
    void airForward() {
    }

    @Override
    void airBack() {
    }

    @Override
    void airUp() {
        resetControls();
    }

    @Override
    void airDown() {
        resetControls();
    }

}
