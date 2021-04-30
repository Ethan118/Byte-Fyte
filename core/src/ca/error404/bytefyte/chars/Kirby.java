package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.PlayRoom;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;


/*
 * Pre: kirby selected
 * Post: creates the kirby class
 * */
public class Kirby extends Character {
    private float yOffset = 12f;

    private boolean hasHovered = false;
    private float flyAcceleration = 0f;
    private float lowFriction = -4f;
    private float defaultFriction = -7f;
    private float defaultGravity;
    private float defaultFall;
    private float defaultMaxFall;
    private Collider upBCollider;

    private boolean rock;

    public Kirby(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playernumber) {
        this(screen, spawnPoint, controller, playernumber, 0);
    }

    /*constructor
     * Pre: kirby selected
     * Post: initializes the kirby variables
     * */
    public Kirby(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playernumber, int stamina) {
        super(screen, spawnPoint, controller, playernumber, "kirby", "KIRBY", 0.7f, 0.8f, stamina);
        weight = 0.8f;
        maxJumps = 10;
        defaultGravity = downGravity;
        defaultFall = fallSpeed;
        manualSpriteOffset = new Vector2(100, yOffset);
        projectilesOnScreen = new ArrayList<>(1);

        jump.setPlayMode(Animation.PlayMode.NORMAL);
        downB.setPlayMode(Animation.PlayMode.NORMAL);
        sideB.setPlayMode(Animation.PlayMode.NORMAL);
    }

    /*
    * Pre: Delta time
    * Post: Updates character
    * */
    public void update(float deltaTime) {
        if (rock) {
            downGravity = 50f;
            fallSpeed = -50f;
            lockAnim = true;
            if (vel.y > 0) {
                vel.y = 0;
            }

            //handle's input for rock state
            handleInput();
            if (attackState == AttackState.SPECIAL && moveVector.y < 0 || hasBeenHit) {
                rock = false;
            } else {
                resetControls();
            }
        } else {
            downGravity = defaultGravity;
            fallSpeed = defaultFall;
        }

        super.update(deltaTime);

        friction = defaultFriction;

//        Allowing the animation of up b to play
        if (animState == AnimationState.SPECIAL_U && vel.y < 0) {
            lockAnim = false;
        }

//        Hovering for up b
        if (animState == AnimationState.SPECIAL_U) {
            specialUp();
            hasHovered = true;
        } else if (animState == AnimationState.DASH) {
            friction = lowFriction;
        }

        if (animDuration <= 1/60f || animState == AnimationState.HIT) {
            manualSpriteOffset.y = yOffset;
        }

//        Sets cooldown for side b
        if (attackState == AttackState.SPECIAL && moveState == MovementState.WALK) {
            animDuration = 0.075f;
        }

    }

    public TextureRegion checkFacing(TextureRegion region) {


        // Decide which direction to face
        if (grounded && attackAnimation == null) {
            if ((vel.x > 0) && !region.isFlipX()) {
                region.flip(true, false);
                facingLeft = false;
            } else if ((vel.x < 0) && region.isFlipX()) {
                region.flip(true, false);
                facingLeft = true;
            } else {
                if (!facingLeft && !region.isFlipX()) {
                    region.flip(true, false);
                } else if (facingLeft && region.isFlipX()) {
                    region.flip(true, false);
                }
            }
        } else if (animState == AnimationState.SPECIAL_S) {
            if ((vel.x > 0) && !region.isFlipX()) {
                region.flip(true, false);
                facingLeft = false;
            } else if ((vel.x < 0) && region.isFlipX()) {
                region.flip(true, false);
                facingLeft = true;
            } else {
                if (!facingLeft && !region.isFlipX()) {
                    region.flip(true, false);
                } else if (facingLeft && region.isFlipX()) {
                    region.flip(true, false);
                }
            }
        }
        else {
            if (!facingLeft && !region.isFlipX()) {
                region.flip(true, false);
            } else if (facingLeft && region.isFlipX()) {
                region.flip(true, false);
            }
        }

        return region;
    }

//    All abilities.  Will add colliders or move kirby as applicable
    @Override
    void basicNeutral() {

        new Collider(new Vector2(25, 0), 20, 35, this, 2f, 4f, 0.25f, 0);

        resetControls();
    }
    @Override
    void basicSide() {
        new Collider(new Vector2(15, 0), 40, 20, this, 2f, 4f, 0.25f, 0);

        resetControls();
    }
    @Override
    void basicUp() {
        new Collider(new Vector2(5, 15), 25, 15, this, 2f, 4f, 0.25f, 0);

        resetControls();
    }

    @Override
    void basicDown() {
        new Collider(new Vector2(0, -15), 40, 15, this, 2f, 4f, 0.25f, 0);

        resetControls();
    }

    @Override
    void dashAttack() {
        new Collider(new Vector2(20, 0), 35, 15, this, 2f, 4f, 0.25f, 0.1f);

        resetControls();
    }

    @Override
    void smashSide() {
        new Collider(new Vector2(25, 0), 35, 30, this, 4f, 5f, 0.25f, 0.1f);

        resetControls();
    }

    @Override
    void smashUp() {
        new Collider(new Vector2(5, 15), 40, 20, this, 2f, 4f, 0.25f, 0);

        resetControls();
    }
    @Override
    void smashDown() {
        new Collider(new Vector2(0, -15), 40, 15, this, 2f, 4f, 0.25f, 0);

        resetControls();
    }

    @Override
    void specialNeutral() {
        new Collider(new Vector2(25, 0), 50, 40, this, 3f, 4f, 0.25f, 1f);

        resetControls();
    }

    @Override
    void specialSide() {
        new Collider(new Vector2(0, 5), 40, 60, this, 2f, 4f, 0.25f, 0);
        animDuration = 1f;

        vel.set(new Vector2(7 * moveVector.x, 3));
        resetControls();
    }

    @Override
    void specialUp() {
        manualSpriteOffset.y = 40f;

//        Hovers him for a frame
        if (animDuration == 0) {
            if (!hasHovered && !grounded) {
                vel.y = -3;
            }

            flyAcceleration = 0;
            hasHovered = true;
            animDuration = 1.8f;
        }

        upBCollider = new Collider(new Vector2(0, -20), 15, 30, this, 3f, 0.25f, 0, 0);
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
        new Collider(new Vector2(0, 0), 30, 30, this, 2f, 4f, 0.5f, 13/60f, 2f);
        resetControls();
        rock = true;
    }

    @Override
    void ultimate() {
    }

    @Override
    void airNeutral() {
        new Collider(new Vector2(0, 0), 40, 20, this, 2f, 4f, 0.25f, 0);

        resetControls();
    }
    @Override
    void airForward() {
        new Collider(new Vector2(10, 5), 25, 40, this, 2f, 4f, 0.25f, 0);

        resetControls();
    }
    @Override
    void airBack() {
//      Create a projectile based on the user direction
        if (!facingLeft) {
            new Projectile(this, new Vector2(-0.2f, 0.1f), new Vector2(-4, 0), 0, 0, (float) Double.POSITIVE_INFINITY, 2f, 7f, 0.25f, "beam_shot", "sprites/kirby.atlas", 3f / 60f, spriteScale);
        } else {
            new Projectile(this, new Vector2(0.2f, 0.1f), new Vector2(4, 0), 0, 0, (float) Double.POSITIVE_INFINITY, 2f, 7f, 0.25f, "beam_shot", "sprites/kirby.atlas", 3f / 60f, spriteScale);
        }

        resetControls();
    }

    @Override
    void airUp() {
        new Collider(new Vector2(0, 15), 30, 30, this, 2f, 4f, 0.25f, 0);

        resetControls();
    }
    @Override
    void airDown() {
        new Collider(new Vector2(5, -15), 40, -30, this, 2f, 4f, 0.25f, 0);

        resetControls();
    }

    @Override
    public void die() {
        super.die();
        rock = false;
    }
}
