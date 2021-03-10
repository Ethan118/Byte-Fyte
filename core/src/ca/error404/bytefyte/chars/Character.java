package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class Character extends Sprite {
    public enum State {IDLE, WALK, RUN, JUMP, FALL}

    public State currentState;
    public State prevState;

    public Vector2 goToPos;
    public Vector2 vel = new Vector2();
    public Vector2 pos = new Vector2();
    public Vector2 prevGoToPos = Vector2.Zero;
    public Vector2 prevVel = Vector2.Zero;
    public Vector2 prevPos = Vector2.Zero;

    public World world;
    public Body b2body;

    public int walkSpeed = 1;
    public int jumpSpeed = 2;

    public int friction = 7;
    public int upGravity = 5;
    public int fallGravity = 6;
    public int fastFall = 20;
    public float maxFastFall = -10f;

    public float turnCooldown = 0f;
    public float maxTurnCooldown = 0.1f;
    public int maxJumps = 1;
    public int jumpsLeft = 0;

    public int hitboxScale = 18;
    public int spriteScale = 15;

    public boolean grounded = false;

    private boolean facingRight = true;
    private float elapsedTime = 0f;
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> walk;
    private final Animation<TextureRegion> run;
    private final Animation<TextureRegion> jump;
    private final Animation<TextureRegion> fall;

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



    public Character(TestScene screen, Vector2 spawnPoint) {
        this.world = screen.getWorld();
        currentState = State.IDLE;
        prevState = State.IDLE;
        goToPos = new Vector2(spawnPoint.x / Main.PPM, spawnPoint.y / Main.PPM);

        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("sprites/shyguy.atlas"));

        idle = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_idle"), Animation.PlayMode.LOOP);
        walk = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("shyguy_walk"), Animation.PlayMode.LOOP);
        run = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_run"), Animation.PlayMode.LOOP);
        jump = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_jump"), Animation.PlayMode.LOOP);
        fall = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_fall"), Animation.PlayMode.LOOP);

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

    public void update(float deltaTime) {
        // set variables
        prevVel = vel;
        prevPos = new Vector2(pos.x, pos.y);
        pos = new Vector2(b2body.getPosition().x, b2body.getPosition().y);

        // Teleport Player
        if (prevGoToPos != goToPos) {
            b2body.setTransform(goToPos, 0f);
        }
        prevGoToPos = goToPos;

        boolean controllerJump = false;
        if (Main.controllers.size > 0) controllerJump = Main.contains(Main.recentButtons.get(Main.controllers.get(0)), ControllerButtons.X) || Main.contains(Main.recentButtons.get(Main.controllers.get(0)), ControllerButtons.Y);

        Vector2 moveVector = Main.leftStick();

        int running = (Gdx.input.isKeyPressed(Keys.RUN)) ? 2 : 1;
        // horizontal movement
        if (moveVector.x != 0 && Math.abs(vel.x) <= walkSpeed * running) {
            if (grounded) {
                if (((moveVector.x < 0 && vel.x > 0) || (moveVector.x > 0 && vel.x < 0)) && Math.abs(turnCooldown) >= maxTurnCooldown) {
                    vel.x += walkSpeed * deltaTime * 2 * moveVector.x;
                } else {
                    vel.x = walkSpeed * moveVector.x * running;
                }
                turnCooldown += moveVector.x * deltaTime;
            } else {
                vel.x += walkSpeed * deltaTime * 10 * moveVector.x;
            }
        }

        // fast fall if down is held
        if (moveVector.y < -0.9f && !grounded && vel.y < jumpSpeed) {
            if (vel.y > 0) {
                vel.y = 0;
            }
            vel.y -= fastFall * deltaTime;
            vel.y = Math.max(vel.y, maxFastFall);
        }

        // jumping
        if ((Gdx.input.isKeyJustPressed(Keys.JUMP) || controllerJump) && jumpsLeft > 0) {
            if (vel.y <= 0) {
                vel.y = jumpSpeed;
            } else {
                vel.y += jumpSpeed;
            }

            if (!grounded) jumpsLeft -= 1;
            grounded = false;
        }

        applyFriction(deltaTime);
        if (grounded) vel.y = 0;

        // grounds player if y position hasn't changed in a while because sometimes
        // the game doesn't register the player landing on the ground
        if (pos.y == prevPos.y && vel.y <= maxFastFall * 1.1 && !grounded) {
            ground();
        }

        b2body.setLinearVelocity(vel);
        setRegion(getFrame(deltaTime));
        setBounds(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 0.01f, (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
    }

    // friction + gravity
    public void applyFriction(float deltaTime) {
        if (vel.x > 0.1) {
            vel.x -= friction * deltaTime;
        } else if (vel.x < -0.1) {
            vel.x += friction * deltaTime;
        } else {
            vel.x = 0;
        }

        if (vel.y > 0) {
            vel.y -= upGravity * deltaTime ;
        } else if (vel.y <= 0) {
            vel.y -= fallGravity * deltaTime;
        }
    }

    public State getState() {
        if (vel.y > 0 && !grounded) {
            return State.JUMP;
        } else if (vel.y <= 0 && !grounded) {
            return State.FALL;
        } else if (Math.abs(b2body.getLinearVelocity().x) > 0.1 && walkSpeed * 1.5f > Math.abs(b2body.getLinearVelocity().x)) {
            return State.WALK;
        } else if (Math.abs(b2body.getLinearVelocity().x) >= walkSpeed * 1.5) {
            return State.RUN;
        }

        return State.IDLE;
    }

    public TextureRegion getFrame(float deltaTime) {
        elapsedTime += deltaTime;
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
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

        if (grounded) {
            if ((b2body.getLinearVelocity().x > 0 || !facingRight) && !region.isFlipX()) {
                region.flip(true, false);
                facingRight = false;
            } else if ((b2body.getLinearVelocity().x < 0 || facingRight) && region.isFlipX()) {
                region.flip(true, false);
                facingRight = true;
            }
        } else {
            if (!facingRight && !region.isFlipX()) {
                region.flip(true, false);
            } else if (facingRight && region.isFlipX()) {
                region.flip(true, false);
            }
        }

        elapsedTime = currentState == prevState ? elapsedTime + deltaTime: 0;
        prevState = currentState;
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
