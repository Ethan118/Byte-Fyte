package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Character extends Sprite {
    private enum AttackState {
        BASIC,
        SPECIAL,
        SMASH,
        ULTIMATE
    }

    private enum MovementState {
        IDLE,
        RUN,
        WALK,
        JUMP,
        FALL
    }

    private enum DirectionInput {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        IDLE
    }

    private float ultMeter;

    private AttackState aState;
    private AttackState prevAState;

    private MovementState mState;
    private MovementState prevMState;

    private DirectionInput direction;

    private Vector2 vel;
    private Vector2 pos;
    private float deltaTime;

    private World world;
    private Body b2Body;

    private int speed;
    private int jumpPower;

    private int gravity;
    private int friction;

    private float scale;

    private boolean grounded;

    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> walk;
    private Animation<TextureRegion> run;
    private Animation<TextureRegion> jump;
    private Animation<TextureRegion> fall;

    public Character(TestScene scene, Vector2 spawn) {

    }

    private void update() {

    }

    private void render() {

    }

    public void handleAttacks() {

//        Ultimate checker
        if (aState == AttackState.ULTIMATE && ultMeter >= 100) {
            ultimate();
        }

//        Idle attacks
        if (mState == MovementState.IDLE && direction == DirectionInput.IDLE) {
            if (aState == AttackState.BASIC) {
                basicNeutral();
            } else if (aState == AttackState.SMASH) {
                smashNeutral();
            } else if (aState == AttackState.SPECIAL) {
                specialNeutral();
            }


        } else if (direction == DirectionInput.RIGHT || direction == DirectionInput.LEFT) {
            if (aState == AttackState.SPECIAL) {
                specialSide();
            }

//                Up attacks
        } else if (direction == DirectionInput.UP) {
            if (aState == AttackState.SPECIAL) {
                specialUp();
            }

//                Down attacks
        } else if (direction == DirectionInput.DOWN) {
            if (aState == AttackState.SPECIAL) {
                specialDown();
            }


//            Any attacks while not in air
        } else if (mState != MovementState.FALL && mState != MovementState.JUMP) {

//            Side attacks
            if (direction == DirectionInput.RIGHT || direction == DirectionInput.LEFT) {
                if (aState == AttackState.BASIC) {
                    basicSide();
                } else if (aState == AttackState.SMASH) {
                    smashSide();
                } else if (aState == AttackState.SPECIAL) {
                    specialSide();
                }

//                Up attacks
            } else if (direction == DirectionInput.UP) {
                if (aState == AttackState.BASIC) {
                    basicUp();
                } else if (aState == AttackState.SMASH) {
                    smashUp();
                } else if (aState == AttackState.SPECIAL) {
                    specialUp();
                }

//                Down attacks
            } else if (direction == DirectionInput.DOWN) {
                if (aState == AttackState.BASIC) {
                    basicDown();
                } else if (aState == AttackState.SMASH) {
                    smashDown();
                } else if (aState == AttackState.SPECIAL) {
                    specialDown();
                }
            }

//        Air attacks
        } else if (mState == MovementState.FALL || mState == MovementState.JUMP) {
            if (aState == AttackState.BASIC || aState == AttackState.SMASH) {
                if (direction == DirectionInput.IDLE) {
                    airNeutral();
                } else if (direction == DirectionInput.LEFT) {
                    airLeft();
                } else if (direction == DirectionInput.RIGHT) {
                    airRight();
                } else if (direction == DirectionInput.UP) {
                    airUp();
                } else if (direction == DirectionInput.DOWN) {
                    airDown();
                }
            }
        } else if (mState == MovementState.RUN && aState == AttackState.BASIC) {
            dashAttack();
        }
    }

//    Basic Attacks
    abstract void basicNeutral();

    abstract void basicSide();

    abstract void basicUp();

    abstract void basicDown();

    abstract void dashAttack();

//    Smash Attacks
    abstract void smashNeutral();

    abstract void smashSide();

    abstract void smashUp();

    abstract void smashDown();


//    Special Attacks
    abstract void specialNeutral();

    abstract void specialSide();

    abstract void specialUp();

    abstract void specialDown();

    abstract void ultimate();


//    Air Attacks
    abstract void airNeutral();

    abstract void airLeft();

    abstract void airRight();

    abstract void airUp();

    abstract void airDown();


}
