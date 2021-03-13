package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class Character extends Sprite {
    public World world;
    public Body b2body;

    public Controller controller;
    public float deadzone;

    public Vector2 moveVector = new Vector2();

    public Vector2 goToPos;
    public Vector2 prevGoToPos = Vector2.Zero;

    public Vector2 pos = new Vector2();
    public Vector2 prevPos = Vector2.Zero;

    public Vector2 vel = new Vector2();
    public Vector2 prevVel = Vector2.Zero;

    public float walkSpeed = 1f;
    public float walkAcc = 20;
    public float dashSpeed = 2;
    public float runSpeed = 2;
    public float maxSpeed;
    public boolean running = false;

    private boolean facingRight = true;
    public boolean grounded = false;

    public float jumpPower = 3;
    public boolean jumping = false;
    public int maxJumps = 1;
    public int jumpsLeft;

    public float friction = -7f;

    public float downGravity = 20;
    public float upGravity = 10;

    public float airSpeed = 1f;
    public float airAcc = 25;

    public float fallSpeed = -3;
    public float fastFallSpeed = -20;
    public float maxFallSpeed;

    public float weight = 1f;

    public int hitboxScale = 18;
    public int spriteScale = 15;

    private float elapsedTime = 0f;

    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> walk;
    private final Animation<TextureRegion> run;

    private final Animation<TextureRegion> jump;
    private final Animation<TextureRegion> fall;
    private final Animation<TextureRegion> hit;

    private final Animation<TextureRegion> neutralAttack;
    private final Animation<TextureRegion> upTilt;
    private final Animation<TextureRegion> downTilt;
    private final Animation<TextureRegion> sideTilt;

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

    public Character(TestScene screen, Vector2 spawnPoint, Controller controller) {
        this.world = screen.getWorld();
        this.controller = controller;

        mState = MovementState.IDLE;
        prevMState = MovementState.IDLE;

        goToPos = new Vector2(spawnPoint.x / Main.PPM, spawnPoint.y / Main.PPM);

        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("sprites/shyguy.atlas"));

        idle = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_idle"), Animation.PlayMode.LOOP);
        walk = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("shyguy_walk"), Animation.PlayMode.LOOP);
        run = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_run"), Animation.PlayMode.LOOP);

        jump = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_jump"), Animation.PlayMode.LOOP);
        fall = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_fall"), Animation.PlayMode.LOOP);
        hit = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_hit"), Animation.PlayMode.LOOP);

        neutralAttack = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_neutral"), Animation.PlayMode.LOOP);
        sideTilt = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_stilt"), Animation.PlayMode.LOOP);
        upTilt = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_utilt"), Animation.PlayMode.LOOP);
        downTilt = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_dtilt"), Animation.PlayMode.LOOP);

        TextureRegion sprite = idle.getKeyFrame(elapsedTime, true);
        setRegion(sprite);

        defineChar();

        setBounds(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y - getHeight() / 2), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
        setRegion(sprite);
    }

    public void defineChar() {
        // loads collision box
        BodyDef bdef = new BodyDef();
        bdef.position.set(goToPos.x, goToPos.y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox((float) getRegionWidth() / hitboxScale / 2 / Main.PPM,(float) getRegionHeight() / hitboxScale / 2 / Main.PPM);

        fdef.shape = shape;
        fdef.filter.categoryBits = Tags.PLAYER_BIT;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);

        // loads feet trigger
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2((float) -getRegionWidth() / hitboxScale / 2.2f / Main.PPM, -getRegionHeight() / (hitboxScale * 1.9f) / Main.PPM), new Vector2((float) getRegionWidth() / hitboxScale / 2.2f / Main.PPM, -getRegionHeight() / (hitboxScale * 1.9f) / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = feet;
        fdef.filter.categoryBits = Tags.PLAYER_FEET_BIT;
        b2body.createFixture(fdef).setUserData(this);

        // loads head trigger
        EdgeShape head = new EdgeShape();
        head.set(new Vector2((float) -getRegionWidth() / hitboxScale / 2.2f / Main.PPM, getRegionHeight() / (hitboxScale * 1.9f) / Main.PPM), new Vector2((float) getRegionWidth() / hitboxScale / 2.2f / Main.PPM, getRegionHeight() / (hitboxScale * 1.9f) / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = head;
        fdef.filter.categoryBits = Tags.PLAYER_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    // grounds the player
    public void ground() {
        vel.y = 0;
        jumpsLeft = maxJumps;
        grounded = true;
    }

    private void handleInput() {
        moveVector.set(0f, 0f);

        if (controller != null) {
            moveVector.x = Math.abs(controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS)) >= deadzone ? controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) : 0;
            moveVector.y = Math.abs(controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS)) >= deadzone ? controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) : 0;

            moveVector.y = Main.contains(Main.recentButtons.get(controller), ControllerButtons.X) || Main.contains(Main.recentButtons.get(Main.controllers.get(0)), ControllerButtons.Y) ? 1 : 0;
        } else {
            moveVector.x += Gdx.input.isKeyPressed(Keys.MOVE_RIGHT) ? 1 : 0;
            moveVector.x -= Gdx.input.isKeyPressed(Keys.MOVE_LEFT) ? 1 : 0;
            moveVector.y += Gdx.input.isKeyPressed(Keys.MOVE_UP) ? 1 : 0;
            moveVector.y -= Gdx.input.isKeyPressed(Keys.MOVE_DOWN) ? 1 : 0;

            running = Gdx.input.isKeyPressed(Keys.RUN);
            jumping = Gdx.input.isKeyJustPressed(Keys.JUMP);
        }
    }

    public void update(float deltaTime) {
        // set variables
        prevVel = vel;
        prevPos.set(pos);
        pos.set(b2body.getPosition());

        // Teleport Player
        if (prevGoToPos != goToPos) {
            b2body.setTransform(goToPos, 0f);
        }
        prevGoToPos = goToPos;

        handleInput();

        if (grounded) {
            maxSpeed = running ? runSpeed : walkSpeed;
        } else {
            maxSpeed = airSpeed;
        }

        if (Math.abs(vel.x) <= maxSpeed) {
            if (grounded) {
                if (!(moveVector.x < 0 && vel.x > 0) && !(moveVector.x > 0 && vel.x < 0)) {
                    vel.x += (walkAcc / weight) * deltaTime * moveVector.x;
                }
            } else {
                vel.x += (airAcc / weight) * moveVector.x * deltaTime;
            }
        }

        applyFriction(deltaTime);

        if (moveVector.y < 0) {
            maxFallSpeed = fastFallSpeed;
        } else {
            maxFallSpeed = fallSpeed;
        }

        if (vel.y > 0 && !grounded) {
            vel.y -= upGravity * deltaTime;
        } else if (vel.y > maxFallSpeed && !grounded) {
            vel.y -= downGravity * deltaTime;
        }

        // jumping
        if (jumping && jumpsLeft > 0) {
            if (vel.y <= 0) {
                vel.y = jumpPower;
            } else {
                vel.y += jumpPower;
            }

            if (!grounded) jumpsLeft -= 1;
            grounded = false;
        }

        // grounds player if y position hasn't changed in a while because sometimes
        // the game doesn't register the player landing on the ground
        if (pos.y == prevPos.y && vel.y < fastFallSpeed && !grounded) {
            ground();
        }

        b2body.setLinearVelocity(vel);
        setRegion(getFrame(deltaTime));
        setBounds(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 0.01f, (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
    }

    // friction + gravity
    public void applyFriction(float deltaTime) {
        if (Math.abs(vel.x) > 0.1f) {
            vel.x += Math.signum(vel.x) * friction * deltaTime;
        } else {
            vel.x = 0;
        }
    }

    public MovementState getState() {
        if (vel.y > 0 && !grounded) {
            return MovementState.JUMP;
        } else if (vel.y <= 0 && !grounded) {
            return MovementState.FALL;
        } else if (Math.abs(b2body.getLinearVelocity().x) > 0.1 && walkSpeed * 1.5f > Math.abs(b2body.getLinearVelocity().x)) {
            return MovementState.WALK;
        } else if (Math.abs(b2body.getLinearVelocity().x) >= walkSpeed * 1.5) {
            return MovementState.RUN;
        }

        return MovementState.IDLE;
    }

    public TextureRegion getFrame(float deltaTime) {
        elapsedTime += deltaTime;
        mState = getState();

        TextureRegion region;
        switch (mState) {
            case WALK:
                region = walk.getKeyFrame(elapsedTime, true);
                break;
            case RUN:
                region = run.getKeyFrame(elapsedTime, true);
                break;
            case JUMP:
                region = jump.getKeyFrame(elapsedTime, true);
                break;
            case FALL:
                region = fall.getKeyFrame(elapsedTime, true);
                break;
            case IDLE:
            default:
                region = idle.getKeyFrame(elapsedTime, true);
                break;
        }

        // Decide which direction to face
        if (grounded) {
            if ((vel.x > 0) && !region.isFlipX()) {
                region.flip(true, false);
                facingRight = false;
            } else if ((vel.x < 0) && region.isFlipX()) {
                region.flip(true, false);
                facingRight = true;
            } else {
                if (!facingRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if (facingRight && region.isFlipX()) {
                    region.flip(true, false);
                }
            }
        } else {
            if (!facingRight && !region.isFlipX()) {
                region.flip(true, false);
            } else if (facingRight && region.isFlipX()) {
                region.flip(true, false);
            }
        }

        elapsedTime = mState == prevMState ? elapsedTime + deltaTime: 0;
        prevMState = mState;
        return region;
    }

    public void setPos(int x, int y) {
        goToPos = new Vector2(x / Main.PPM, y / Main.PPM);
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
