package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

public abstract class Character extends Sprite {
    public World world;
    public Body b2body;

    protected ArrayList<Collider> colliders = new ArrayList<>();

    public Controller controller;
    public float deadzone = Main.deadZone;

    public float percent = 0f;
    private float stunTimer;

    public Vector2 moveVector = new Vector2();
    public Vector2 rStick = new Vector2();

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

    public boolean facingLeft = true;
    public boolean grounded = false;

    public float jumpPower = 3;
    public boolean jumping = false;
    public int maxJumps = 1;
    public int jumpsLeft;

    public float friction = -7f;

    public float downGravity = 15;
    public float upGravity = 10;

    public float airSpeed = 1f;
    public float airAcc = 25;

    public float fallSpeed = -3;
    public float fastFallSpeed = -20;
    public float maxFallSpeed;

    public float weight = 1f;

    public int hitboxScale = 18;
    public int spriteScale = 15;
    private Vector2 spriteOffset = Vector2.Zero;
    public Vector2 manualSpriteOffset = Vector2.Zero;

    protected float elapsedTime = 0f;

    protected Animation<TextureRegion> attackAnimation;
    public boolean lockAnim = false;

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

    private final Animation<TextureRegion> neutralB;
    private final Animation<TextureRegion> upB;
    private final Animation<TextureRegion> downB;
    private final Animation<TextureRegion> sideB;

    private final Animation<TextureRegion> nair;
    private final Animation<TextureRegion> dair;
    private final Animation<TextureRegion> fair;
    private final Animation<TextureRegion> bair;
    private final Animation<TextureRegion> uair;

    private final Animation<TextureRegion> upSmash;
    private final Animation<TextureRegion> downSmash;
    private final Animation<TextureRegion> sideSmash;

    private final Animation<TextureRegion> dashAttack;

    protected double moveTimer = 0;

    private boolean afterUpB = false;

    protected enum MovementState {
        IDLE,
        RUN,
        WALK,
        JUMP,
        FALL
    }

    protected enum AttackState {
        HIT,
        BASIC,
        SPECIAL,
        SMASH,
        ULTIMATE,
        NONE
    }

    protected enum AnimationState {
        IDLE,
        RUN,
        WALK,
        JUMP,
        FALL,
        HIT,
        BASIC_N,
        BASIC_S,
        BASIC_U,
        BASIC_D,
        SPECIAL_N,
        SPECIAL_S,
        SPECIAL_U,
        SPECIAL_D,
        AIR_N,
        AIR_F,
        AIR_B,
        AIR_U,
        AIR_D,
        SMASH_S,
        SMASH_U,
        SMASH_D,
        DASH,
        ULTIMATE
    }

    protected float animDuration;

    private float ultMeter = 0;

    protected AttackState attackState;
    protected final AttackState prevAttackState;

    protected MovementState moveState;
    private final MovementState prevMoveState;

    protected AnimationState animState;
    private AnimationState prevAnimState;

    public Character(TestScene screen, Vector2 spawnPoint, Controller controller) {
        this.world = screen.getWorld();
        this.controller = controller;

        attackState = AttackState.NONE;
        prevAttackState = AttackState.NONE;

        moveState = MovementState.IDLE;
        prevMoveState = MovementState.IDLE;

        animState = AnimationState.IDLE;
        prevAnimState = AnimationState.IDLE;

        goToPos = new Vector2(spawnPoint.x / Main.PPM, spawnPoint.y / Main.PPM);

        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("sprites/shyguy.atlas"));

        idle = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_idle"), Animation.PlayMode.LOOP);
        walk = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("shyguy_walk"), Animation.PlayMode.LOOP);
        run = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_run"), Animation.PlayMode.LOOP);

        jump = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_jump"), Animation.PlayMode.LOOP);
        fall = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_fall"), Animation.PlayMode.LOOP);
        hit = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_hit"), Animation.PlayMode.LOOP);

        neutralAttack = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_neutral"), Animation.PlayMode.NORMAL);
        sideTilt = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_stilt"), Animation.PlayMode.NORMAL);
        upTilt = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_utilt"), Animation.PlayMode.NORMAL);
        downTilt = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_dtilt"), Animation.PlayMode.NORMAL);

        neutralB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_neutral_b"), Animation.PlayMode.NORMAL);
        upB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_up_b"), Animation.PlayMode.LOOP);
        downB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_down_b"), Animation.PlayMode.LOOP);
        sideB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_side_b"), Animation.PlayMode.NORMAL);

        nair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_nair"), Animation.PlayMode.NORMAL);
        dair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_dair"), Animation.PlayMode.NORMAL);
        fair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_fair"), Animation.PlayMode.NORMAL);
        bair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_bair"), Animation.PlayMode.NORMAL);
        uair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_uair"), Animation.PlayMode.NORMAL);

        upSmash = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_up_smash"), Animation.PlayMode.NORMAL);
        downSmash = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_down_smash"), Animation.PlayMode.NORMAL);
        sideSmash = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_side_smash"), Animation.PlayMode.NORMAL);

        dashAttack = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("shyguy_dash_attack"), Animation.PlayMode.NORMAL);

        TextureRegion sprite = idle.getKeyFrame(elapsedTime, true);
        attackAnimation = null;
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
        fdef.filter.maskBits = Tags.GROUND_BIT | Tags.DEATH_BARRIER_BIT | Tags.ATTACK_BIT;
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
            moveVector.y = Math.abs(controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS)) >= deadzone ? -controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) : 0;

            rStick.x = Math.abs(controller.getAxis(ControllerButtons.R_STICK_HORIZONTAL_AXIS)) >= deadzone ? -controller.getAxis(ControllerButtons.R_STICK_HORIZONTAL_AXIS) : 0;
            rStick.y = Math.abs(controller.getAxis(ControllerButtons.R_STICK_VERTICAL_AXIS)) >= deadzone ? -controller.getAxis(ControllerButtons.R_STICK_VERTICAL_AXIS) : 0;

            if (!afterUpB) {
                jumping = Main.contains(Main.recentButtons.get(controller), ControllerButtons.Y) || Main.contains(Main.recentButtons.get(controller), ControllerButtons.X);
            }

            running = controller.getButton(ControllerButtons.R_BUMPER) || controller.getButton(ControllerButtons.L_BUMPER);

            if (Main.contains(Main.recentButtons.get(controller), ControllerButtons.A)) {
                if (ultMeter >= 100 && moveVector == Vector2.Zero) {
                    attackState = AttackState.ULTIMATE;
                } else {
                    attackState = AttackState.SPECIAL;
                }
            } else if (Main.contains(Main.recentButtons.get(controller), ControllerButtons.B)) {
                attackState = AttackState.BASIC;
            } else if (Math.abs(rStick.x) >= deadzone || Math.abs(rStick.y) >= deadzone) {
                attackState = AttackState.SMASH;
            } else {
                attackState = AttackState.NONE;
            }
        } else {
            moveVector.x += Gdx.input.isKeyPressed(Keys.MOVE_RIGHT) ? 1 : 0;
            moveVector.x -= Gdx.input.isKeyPressed(Keys.MOVE_LEFT) ? 1 : 0;
            moveVector.y += Gdx.input.isKeyPressed(Keys.MOVE_UP) ? 1 : 0;
            moveVector.y -= Gdx.input.isKeyPressed(Keys.MOVE_DOWN) ? 1 : 0;

            running = Gdx.input.isKeyPressed(Keys.RUN);

            if (!afterUpB) {
                jumping = Gdx.input.isKeyJustPressed(Keys.JUMP);
            }

            if (Gdx.input.isKeyJustPressed(Keys.EMPTY_METER)) ultMeter = 0;

            if (Gdx.input.isKeyJustPressed(Keys.FILL_METER)) ultMeter = 100;

            if (Gdx.input.isKeyJustPressed(Keys.SPECIAL)) {
                if (ultMeter >= 100 && moveVector == Vector2.Zero) {
                    attackState = AttackState.ULTIMATE;
                } else {
                    attackState = AttackState.SPECIAL;
                }
            } else if (Gdx.input.isKeyJustPressed(Keys.BASIC)) {
                attackState = AttackState.BASIC;
            } else if (Gdx.input.isKeyJustPressed(Keys.SMASH)) {
                attackState = AttackState.SMASH;
            } else {
                attackState = AttackState.NONE;
            }
        }
    }

    public void update(float deltaTime) {
        if (animDuration > 0) {
            animDuration -= deltaTime;
        } else {
            animDuration = 0;
        }

        if (grounded) {
            afterUpB = false;
        }

        if (animState == AnimationState.SPECIAL_U) {
            afterUpB = true;

        }

        if (moveTimer >= 0) {
            moveTimer -= deltaTime;
        }

        // set variables
        prevVel = vel;
        prevPos.set(pos);
        pos.set(b2body.getPosition());

        // Teleport Player
        if (prevGoToPos != goToPos) {
            b2body.setTransform(goToPos, 0f);
            percent = 0;
        }
        prevGoToPos = goToPos;

        if (stunTimer > 0) {
            stunTimer -= deltaTime;
        }

        if (!lockAnim && stunTimer <= 0) {
            handleInput();
        }

        if (attackAnimation == null) {
            for (Collider collider : colliders) {
                collider.destroy();
            }
            colliders.clear();
        }

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
            if (vel.y > 0) {
                vel.y = 0;
            }
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
            jumping = false;
        }

        // grounds player if y position hasn't changed in a while because sometimes
        // the game doesn't register the player landing on the ground
        if (pos.y == prevPos.y && vel.y < fastFallSpeed && !grounded) {
            ground();
        }

        if (!lockAnim) {
            if (animDuration <= 0) {
                getState();
            } else {
                lockAnim = true;
            }
        }

        for (Collider collider : colliders) {
            if (facingLeft) {
                collider.setPosition(pos, -1);
            } else {
                collider.setPosition(pos, 1);
            }
        }

        b2body.setLinearVelocity(vel);
        setRegion(getFrame(deltaTime));
        setBounds(b2body.getPosition().x + (spriteOffset.x / spriteScale / Main.PPM)  - (manualSpriteOffset.x / spriteScale / Main.PPM), b2body.getPosition().y - (manualSpriteOffset.y / spriteScale / Main.PPM) + (spriteOffset.y / spriteScale / Main.PPM), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
    }


    // friction + gravity
    public void applyFriction(float deltaTime) {
        if (Math.abs(vel.x) > 0.1f) {
            vel.x += Math.signum(vel.x) * friction * deltaTime;
        } else {
            vel.x = 0;
        }
    }

    public void getState() {
        if (vel.y > 0 && !grounded) {
            moveState = MovementState.JUMP;
            animState = AnimationState.JUMP;
        } else if (vel.y <= 0 && !grounded) {
            moveState = MovementState.FALL;
            animState = AnimationState.FALL;
        } else if (Math.abs(vel.x) > 0 && !running) {
            moveState = MovementState.WALK;
            animState = AnimationState.WALK;
        } else if (Math.abs(vel.x) > 0 && running) {
            moveState = MovementState.RUN;
            animState = AnimationState.RUN;
        } else {
            moveState = MovementState.IDLE;
            animState = AnimationState.IDLE;
        }

        if (!afterUpB) {
            // basic attacks
            if (attackState == AttackState.BASIC) {

                //tilt attacks
                if (grounded) {
                    if (moveVector.isZero()) {
                        // neutral
                        animState = AnimationState.BASIC_N;
                        basicNeutral();
                    } else if (moveVector.y > 0) {
                        // up
                        animState = AnimationState.BASIC_U;
                        basicUp();
                    } else if (moveVector.y < 0) {
                        // down
                        animState = AnimationState.BASIC_D;
                        basicDown();
                    } else if (Math.abs(moveVector.x) > 0) {
                        if (moveState == MovementState.RUN) {
                            //dash
                            animState = AnimationState.DASH;
                            dashAttack();
                        } else {
                            // side
                            animState = AnimationState.BASIC_S;
                            basicSide();
                        }
                    }

                    // air attacks
                } else {
                    if (moveVector.isZero()) {
                        // neutral
                        animState = AnimationState.AIR_N;
                        airNeutral();
                    } else if (moveVector.y > 0) {
                        // up
                        animState = AnimationState.AIR_U;
                        airUp();
                    } else if (moveVector.y < 0) {
                        // down
                        animState = AnimationState.AIR_D;
                        airDown();
                    } else if ((!facingLeft && moveVector.x >= deadzone) || (facingLeft && moveVector.x <= -deadzone)) {
                        // forward
                        animState = AnimationState.AIR_F;
                        airForward();
                    } else if ((!facingLeft && moveVector.x <= -deadzone) || (facingLeft && moveVector.x >= deadzone)) {
                        // backward
                        animState = AnimationState.AIR_B;
                        airBack();
                    }
                }
                // special attacks
            } else if (attackState == AttackState.SPECIAL) {
                if (moveVector.isZero()) {
                    //neutral
                    animState = AnimationState.SPECIAL_N;
                    specialNeutral();
                } else if (moveVector.y > 0) {
                    //up
                    animState = AnimationState.SPECIAL_U;
                    specialUp();
                } else if (moveVector.y < 0) {
                    //down
                    animState = AnimationState.SPECIAL_D;
                    specialDown();
                } else if (Math.abs(moveVector.x) > 0) {
                    //side
                    animState = AnimationState.SPECIAL_S;
                    specialSide();
                } else if (attackState == AttackState.ULTIMATE) {
                    if (ultMeter >= 100) {
                        //ult
                        animState = AnimationState.ULTIMATE;
                        ultimate();
                    }
                }
                // smash attacks
            } else if (attackState == AttackState.SMASH) {
                Vector2 vec = controller == null ? moveVector : rStick;

                if (vec.y > 0) {
                    //up
                    animState = AnimationState.SMASH_U;
                    smashUp();
                } else if (vec.y < 0) {
                    //down
                    animState = AnimationState.SMASH_D;
                    smashDown();
                } else if (vec.x < 0) {
                    //side
                    facingLeft = false;
                    animState = AnimationState.SMASH_S;
                    smashSide();
                } else if (vec.x > 0) {
                    facingLeft = true;
                    animState = AnimationState.SMASH_S;
                    smashSide();
                }
            }
        }
    }

    public void resetControls() {
        moveVector = new Vector2(0, 0);
        rStick = new Vector2(0, 0);
        jumping = false;
        running = false;
    }

    public TextureRegion getFrame(float deltaTime) {
        elapsedTime += deltaTime;
        TextureRegion region;

        elapsedTime = animState == prevAnimState ? elapsedTime + deltaTime : 0;
        prevAnimState = animState;

        switch (animState) {
            case WALK:
                region = walk.getKeyFrame(elapsedTime, true);
                attackAnimation = null;
                break;
            case RUN:
                region = run.getKeyFrame(elapsedTime, true);
                attackAnimation = null;
                break;
            case JUMP:
                region = jump.getKeyFrame(elapsedTime, true);
                attackAnimation = null;
                break;
            case FALL:
                region = fall.getKeyFrame(elapsedTime, true);
                attackAnimation = null;
                break;
            case BASIC_N:
                region = neutralAttack.getKeyFrame(elapsedTime, false);
                attackAnimation = neutralAttack;
                break;
            case BASIC_U:
                region = upTilt.getKeyFrame(elapsedTime, false);
                attackAnimation = upTilt;
                break;
            case BASIC_D:
                region = downTilt.getKeyFrame(elapsedTime, false);
                attackAnimation = downTilt;
                break;
            case BASIC_S:
                region = sideTilt.getKeyFrame(elapsedTime, false);
                attackAnimation = sideTilt;
                break;
            case SPECIAL_N:
                region = neutralB.getKeyFrame(elapsedTime, false);
                attackAnimation = neutralB;
                break;
            case SPECIAL_U:
                region = upB.getKeyFrame(elapsedTime, false);
                attackAnimation = upB;
                break;
            case SPECIAL_D:
                region = downB.getKeyFrame(elapsedTime, false);
                attackAnimation = downB;
                break;
            case SPECIAL_S:
                region = sideB.getKeyFrame(elapsedTime, false);
                attackAnimation = sideB;
                break;
            case AIR_N:
                region = nair.getKeyFrame(elapsedTime, false);
                attackAnimation = nair;
                break;
            case AIR_U:
                region = uair.getKeyFrame(elapsedTime, false);
                attackAnimation = uair;
                break;
            case AIR_D:
                region = dair.getKeyFrame(elapsedTime, false);
                attackAnimation = dair;
                break;
            case AIR_F:
                region = fair.getKeyFrame(elapsedTime, false);
                attackAnimation = fair;
                break;
            case AIR_B:
                region = bair.getKeyFrame(elapsedTime, false);
                attackAnimation = bair;
                break;
            case SMASH_U:
                region = upSmash.getKeyFrame(elapsedTime, false);
                attackAnimation = upSmash;
                break;
            case SMASH_D:
                region = downSmash.getKeyFrame(elapsedTime, false);
                attackAnimation = downSmash;
                break;
            case SMASH_S:
                region = sideSmash.getKeyFrame(elapsedTime, false);
                attackAnimation = sideSmash;
                break;
            case DASH:
                region = dashAttack.getKeyFrame(elapsedTime, false);
                attackAnimation = dashAttack;
                break;
            case ULTIMATE:
            case HIT:
                region = hit.getKeyFrame(elapsedTime, true);
                break;
            case IDLE:
            default:
                region = idle.getKeyFrame(elapsedTime, true);
                attackAnimation = null;
                break;
        }

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
        } else {
            if (!facingLeft && !region.isFlipX()) {
                region.flip(true, false);
            } else if (facingLeft && region.isFlipX()) {
                region.flip(true, false);
            }
        }

        spriteOffset.x = ((TextureAtlas.AtlasRegion) region).offsetX;
        spriteOffset.y = ((TextureAtlas.AtlasRegion) region).offsetY;

        lockAnim = attackAnimation != null && (moveTimer > 0 || !attackAnimation.isAnimationFinished(elapsedTime));

        return region;
    }

    public void Hit(float damage, Vector2 force, float hitStun) {
        percent = Math.min(percent + damage, 999.9f);
        vel.set(force.scl(((percent / 100) + 1) / weight));

        stunTimer = hitStun * ((percent / 100) + 1);

        moveVector.x = 0;
    }

    public void setPos(int x, int y) {
        goToPos = new Vector2(x / Main.PPM, y / Main.PPM);
    }

    //    Basic Attacks
    abstract void basicNeutral();

    abstract void basicSide();

    abstract void basicUp();

    abstract void basicDown();

    abstract void dashAttack();

    //    Smash Attacks
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

    abstract void airForward();

    abstract void airBack();

    abstract void airUp();

    abstract void airDown();


}
