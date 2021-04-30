package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.PlayRoom;
import ca.error404.bytefyte.ui.PlayerHealth;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

// Abstract parent class to create characters
public abstract class Character extends GameObject {

//    Initializing variables
    public World world;

    public Controller controller;
    public float deadzone = Main.deadZone;
    public boolean hasBeenHit = false;
    public boolean invinsible = false;

    private Sound hitSFX;

    public float percent = 0f;
    public float stunTimer;

    private float respawnTimer;
    private boolean respawned = true;
    private final float respawnTime = 5.0f;

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

    public float hitboxScale;
    public float spriteScale;
    protected Vector2 spriteOffset = Vector2.Zero;
    public Vector2 manualSpriteOffset = Vector2.Zero;

    public int rank;

    protected float elapsedTime = 0f;

    public Animation<TextureRegion> attackAnimation;
    public boolean lockAnim = false;

    public Animation<TextureRegion> idle;
    protected Animation<TextureRegion> walk;
    protected Animation<TextureRegion> run;

    protected Animation<TextureRegion> jump;
    protected Animation<TextureRegion> fall;
    protected Animation<TextureRegion> hit;

    protected Animation<TextureRegion> neutralAttack;
    protected Animation<TextureRegion> upTilt;
    protected Animation<TextureRegion> downTilt;
    protected Animation<TextureRegion> sideTilt;

    protected Animation<TextureRegion> neutralB;
    protected Animation<TextureRegion> upB;
    protected Animation<TextureRegion> downB;
    protected Animation<TextureRegion> sideB;

    protected Animation<TextureRegion> nair;
    protected Animation<TextureRegion> dair;
    protected Animation<TextureRegion> fair;
    protected Animation<TextureRegion> bair;
    protected Animation<TextureRegion> uair;

    protected Animation<TextureRegion> upSmash;
    protected Animation<TextureRegion> downSmash;
    protected Animation<TextureRegion> sideSmash;

    protected Animation<TextureRegion> dashAttack;


    public ArrayList<Projectile> projectilesOnScreen;


    protected double moveTimer = 0;

    protected boolean afterUpB = false;
    protected Boolean canDownB = true;


    public int stockCount = 3;
    public Vector2 respawnPos = new Vector2();

    public boolean dead = false;
    protected boolean knockedOff = false;

//    Creating an enum to handle the state of movement the player is in
    protected enum MovementState {
        IDLE,
        RUN,
        WALK,
        JUMP,
        FALL
    }

//    Creating an enum to handle the state of attack the player is in
    protected enum AttackState {
        HIT,
        BASIC,
        SPECIAL,
        SMASH,
        ULTIMATE,
        NONE
    }

//    Creating an enum to handle the animation being displayed for the player
    public enum AnimationState {
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

//    Initializing more variables
    protected float animDuration;

    private final float ultMeter = 0;

    protected AttackState attackState;
    protected final AttackState prevAttackState;

    protected MovementState moveState;
    protected final MovementState prevMoveState;

    public AnimationState animState;
    public AnimationState prevAnimState;

    protected int playerNumber;
    public String charname;
    public String playerName;
    public float width;
    public float height;
    public boolean stamina = false;
    public int defaultStamina = 0;
    public PlayerHealth health;

    /*
     * pre: reference to the scene, position to spawn, controller, player number
     * post: instantiates a character with the parameters
     */
    public Character(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playerNumber, String charname, String playerName, float spriteScale, float hitboxScale, int hp) {
        this(screen, spawnPoint, controller, playerNumber, charname, playerName, spriteScale, hitboxScale);

//        Setting all variables
        this.percent = hp;
        defaultStamina = hp;
        if (hp != 0) {
            stamina = true;
            health.stamina = true;
        }

    }

    /*
     * pre: reference to the scene, position to spawn, controller, player number
     * post: instantiates a character with the parameters
     */
    public Character(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playerNumber, String charname, String playerName, int hp) {
        this(screen, spawnPoint, controller, playerNumber, charname, playerName, 15, 18);
//        Setting all variables
        this.percent = hp;
        defaultStamina = hp;
        if (hp != 0) {
            stamina = true;
            health.stamina = true;
        }

    }

    /*
     * pre: reference to the scene, position to spawn, controller, player number
     * post: instantiates a character with the parameters
     */
    public Character(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playerNumber, String charname, String playerName) {
        this(screen, spawnPoint, controller, playerNumber, charname, playerName, 15, 18);

    }

    /*
     * pre: reference to the scene, position to spawn, controller, player number
     * post: instantiates a character with the parameters
     */
    public Character(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playerNumber, String charname, String playerName, float spriteScale, float hitboxScale) {
        super();

//        Setting all variables
        this.spriteScale = spriteScale;
        this.hitboxScale = hitboxScale;
        Main.players.add(this);
        this.playerNumber = playerNumber;
        this.charname = charname;
        this.playerName = playerName;
        this.world = screen.getWorld();
        this.controller = controller;

        hitSFX = Gdx.audio.newSound(Gdx.files.internal("audio/sound effects/playerHit.wav"));

        health = new PlayerHealth(playerNumber, charname, this);

        attackState = AttackState.NONE;
        prevAttackState = AttackState.NONE;

        moveState = MovementState.IDLE;
        prevMoveState = MovementState.IDLE;

        animState = AnimationState.IDLE;
        prevAnimState = AnimationState.IDLE;

        goToPos = new Vector2(spawnPoint.x / Main.PPM, spawnPoint.y / Main.PPM);

        // loads animations
        TextureAtlas textureAtlas = Main.manager.get(String.format("sprites/%s.atlas", charname), TextureAtlas.class);

        idle = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("idle"), Animation.PlayMode.LOOP);
        walk = new Animation<TextureRegion>(1f/120f, textureAtlas.findRegions("walk"), Animation.PlayMode.LOOP);
        run = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("run"), Animation.PlayMode.LOOP);

        jump = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("jump"), Animation.PlayMode.LOOP);
        fall = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("fall"), Animation.PlayMode.LOOP);
        hit = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("hit"), Animation.PlayMode.NORMAL);

        neutralAttack = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral"), Animation.PlayMode.NORMAL);
        sideTilt = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("stilt"), Animation.PlayMode.NORMAL);
        upTilt = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("utilt"), Animation.PlayMode.NORMAL);
        downTilt = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("dtilt"), Animation.PlayMode.NORMAL);

        neutralB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("neutral_b"), Animation.PlayMode.NORMAL);
        upB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("up_b"), Animation.PlayMode.LOOP);
        downB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("down_b"), Animation.PlayMode.LOOP);
        sideB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("side_b"), Animation.PlayMode.NORMAL);

        nair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("nair"), Animation.PlayMode.NORMAL);
        dair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("dair"), Animation.PlayMode.NORMAL);
        fair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("fair"), Animation.PlayMode.NORMAL);
        bair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("bair"), Animation.PlayMode.NORMAL);
        uair = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("uair"), Animation.PlayMode.NORMAL);

        upSmash = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("up_smash"), Animation.PlayMode.NORMAL);
        downSmash = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("down_smash"), Animation.PlayMode.NORMAL);
        sideSmash = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("side_smash"), Animation.PlayMode.NORMAL);

        dashAttack = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("dash_attack"), Animation.PlayMode.NORMAL);

        TextureRegion sprite = idle.getKeyFrame(elapsedTime, true);
        attackAnimation = null;
        setRegion(sprite);

        defineChar();

//        Setting character position
        setBounds(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y - getHeight() / 2), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
        setRegion(sprite);
    }


    /*
    * Pre: None
    * Post: A new character and body is defined
    * */
    public void defineChar() {
//         loads collision box
        BodyDef bdef = new BodyDef();
        bdef.position.set(goToPos.x, goToPos.y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

//        Loads a fixture for collision
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox((float) getRegionWidth() / hitboxScale / 2 / Main.PPM,(float) getRegionHeight() / hitboxScale / 2 / Main.PPM);
        width = getRegionWidth() / hitboxScale / 2 / Main.PPM;
        height = getRegionHeight() / hitboxScale / 2 / Main.PPM;

//        Sets the shape of the hitbox
        fdef.shape = shape;
        fdef.filter.categoryBits = Tags.PLAYER_BIT;
        fdef.filter.maskBits = Tags.GROUND_BIT | Tags.DEATH_BARRIER_BIT | Tags.ATTACK_BIT | Tags.PROJECTILE_BIT | Tags.LASER_BIT | Tags.BOSS_BIT | Tags.BOSS_FEET_BIT;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);

//         loads feet trigger
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2((float) -getRegionWidth() / hitboxScale / 2.2f / Main.PPM, -getRegionHeight() / (hitboxScale * 1.9f) / Main.PPM), new Vector2((float) getRegionWidth() / hitboxScale / 2.2f / Main.PPM, -getRegionHeight() / (hitboxScale * 1.9f) / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = feet;
        fdef.filter.categoryBits = Tags.PLAYER_FEET_BIT;
        b2body.createFixture(fdef).setUserData(this);

//         loads head trigger
        EdgeShape head = new EdgeShape();
        head.set(new Vector2((float) -getRegionWidth() / hitboxScale / 2.2f / Main.PPM, getRegionHeight() / (hitboxScale * 1.9f) / Main.PPM), new Vector2((float) getRegionWidth() / hitboxScale / 2.2f / Main.PPM, getRegionHeight() / (hitboxScale * 1.9f) / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = head;
        fdef.filter.categoryBits = Tags.PLAYER_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

//        Loads the left side trigger
        EdgeShape left = new EdgeShape();
        left.set(new Vector2((float) -getRegionWidth() / (hitboxScale * 1.9f) / Main.PPM, (float) -getRegionHeight() / hitboxScale / 2.2f / Main.PPM), new Vector2((float) -getRegionWidth() / (hitboxScale * 1.9f) / Main.PPM, (float) getRegionHeight() / hitboxScale / 2.2f / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = left;
        fdef.filter.categoryBits = Tags.PLAYER_SIDE_BIT;
        b2body.createFixture(fdef).setUserData(this);

//        Loads the right side trigger
        EdgeShape right = new EdgeShape();
        right.set(new Vector2((float) getRegionWidth() / (hitboxScale * 1.9f) / Main.PPM, (float) -getRegionHeight() / hitboxScale / 2.2f / Main.PPM), new Vector2((float) getRegionWidth() / (hitboxScale * 1.9f) / Main.PPM, (float) getRegionHeight() / hitboxScale / 2.2f / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = right;
        fdef.filter.categoryBits = Tags.PLAYER_SIDE_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    /*
    * Pre: None
    * Post: Player is grounded
    * */
    public void ground() {
//        Sets the player to be grounded with no velocity
        vel.y = 0;
        jumpsLeft = maxJumps;
        grounded = true;
    }


    /*
    * pre: None
    * post: Handles any and all controller or keyboard inputs for a specific player
    */
    protected void handleInput() {

//        Sets the movement vector of the player to 0 (directional input)
        moveVector.set(0f, 0f);

//        If there is a controller detected by the game
        if (controller != null) {

//            Sets the movement vector of the player to the inputted stick values (to handle directional input)
            moveVector.x = Math.abs(controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS)) >= deadzone ? controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) : 0;
            moveVector.y = Math.abs(controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS)) >= deadzone ? -controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) : 0;

//            Gets the directional input of the right controller stick to handle attacks
            rStick.x = Math.abs(controller.getAxis(ControllerButtons.R_STICK_HORIZONTAL_AXIS)) >= deadzone ? controller.getAxis(ControllerButtons.R_STICK_HORIZONTAL_AXIS) : 0;
            rStick.y = Math.abs(controller.getAxis(ControllerButtons.R_STICK_VERTICAL_AXIS)) >= deadzone ? -controller.getAxis(ControllerButtons.R_STICK_VERTICAL_AXIS) : 0;

//            If the user hasn't performed an up special ability
            if (!afterUpB) {

//                Jumping is true if the user is pressing the jump button
                jumping = Main.contains(Main.recentButtons.get(controller), ControllerButtons.Y) || Main.contains(Main.recentButtons.get(controller), ControllerButtons.X);
            }

//            Running is true if the right bumper is clicked
            running = controller.getButton(ControllerButtons.R_BUMPER) || controller.getButton(ControllerButtons.L_BUMPER);

//            Checks if the special ability button was clicked
            if (Main.contains(Main.recentButtons.get(controller), ControllerButtons.A)) {

//                Checks if the user can use their ultimate ability, and if they have no directional input
                if (ultMeter >= 100 && moveVector == Vector2.Zero) {

//                    Use the ultimate ability
                    attackState = AttackState.ULTIMATE;
                } else {

//                    Otherwise, the user can perform a special ability
                    attackState = AttackState.SPECIAL;
                }

//                Checks if the user presses the buttons for basic or smash attacks, and responds if either
//                is pressed
            } else if (Main.contains(Main.recentButtons.get(controller), ControllerButtons.B)) {
                attackState = AttackState.BASIC;
            } else if (Math.abs(rStick.x) >= deadzone || Math.abs(rStick.y) >= deadzone) {
                if (attackState != AttackState.SMASH) {
                    attackState = AttackState.SMASH;
                } else {
                    attackState = AttackState.NONE;
                }

//                Otherwise the user is not attacking
            } else {
                attackState = AttackState.NONE;
            }

//            If there is no connected controller
        } else {

//            The x and y vectors are based on key inputs (if a key is pressed)
            moveVector.x += Gdx.input.isKeyPressed(Keys.MOVE_RIGHT) ? 1 : 0;
            moveVector.x -= Gdx.input.isKeyPressed(Keys.MOVE_LEFT) ? 1 : 0;
            moveVector.y += Gdx.input.isKeyPressed(Keys.MOVE_UP) ? 1 : 0;
            moveVector.y -= Gdx.input.isKeyPressed(Keys.MOVE_DOWN) ? 1 : 0;

//            User is running if the running key is pressed
            running = Gdx.input.isKeyPressed(Keys.RUN);

//            If the user did not just up special, and they pressed the jump key, they jump
            if (!afterUpB) {
                jumping = Gdx.input.isKeyJustPressed(Keys.JUMP);
            }

//            Checks if the user pressed the key corresponding to a special move
            if (Gdx.input.isKeyJustPressed(Keys.SPECIAL)) {

//                Checks if the user can use their ultimate ability, and if they have no directional input
                if (ultMeter >= 100 && moveVector == Vector2.Zero) {

//                    Uses the ultimate ability
                    attackState = AttackState.ULTIMATE;

                } else {
//                    Otherwise, a normal special attack is performed
                    attackState = AttackState.SPECIAL;
                }

//                Checks if the user clicks the key for a basic or smash attack, and responds accordingly
            } else if (Gdx.input.isKeyJustPressed(Keys.BASIC)) {
                attackState = AttackState.BASIC;
            } else if (Gdx.input.isKeyJustPressed(Keys.SMASH)) {
                attackState = AttackState.SMASH;

//                If no attack keys are pressed, the user is not attacking
            } else {
                attackState = AttackState.NONE;
            }
        }
    }

    /**
     * pre: deltaTime, the time between frames
     * post: updates the players state, including physics and rendering
     */
    public void update(float deltaTime) {

//        If the user has no health left in stamina mode, run the die() method
        if (stamina && percent <= 0) {
            die();
        }

        hasBeenHit = false;

//        If the user is dead, remove them and their song, destroy them
        if (dead) {
            Main.players.remove(this);
            destroy();
            hitSFX.dispose();
        }

//         Counts the animation duration down to zero
        if (animDuration > 0) {
            animDuration -= deltaTime;
        } else {
            animDuration = 0;
        }

//         Checks if the player is on the ground
        if (grounded) {
            afterUpB = false;
        }

//         Checks if the player is currently using up special
        if (animState == AnimationState.SPECIAL_U) {
            afterUpB = true;
        }

//         Counts down move timer
        if (moveTimer >= 0) {
            moveTimer -= deltaTime;
        }

//         Teleport Player
        if (prevGoToPos != goToPos) {
            b2body.setTransform(goToPos, 0f);
            prevGoToPos = goToPos;
        }

//         Set variables
        prevVel = vel;
        prevPos.set(pos);
        pos.set(b2body.getPosition());

//         Counts down stun timer
        if (stunTimer > 0) {
            stunTimer -= deltaTime;
        }

//         Locks the player control if the character is stunned or playing an attack animation
        if (!lockAnim && stunTimer <= 0) {
            handleInput();
        }

//        Count down respawn timer
        respawnTimer -= deltaTime;

//        If the respawn timer is above 0
        if (respawnTimer > 0 && !respawned) {

//            Set the states to idle
            moveState = MovementState.IDLE;
            animState = AnimationState.IDLE;

//            If the user is moving, they have respawned
            if (!moveVector.isZero() || jumping) {
                respawned = true;

//            If the respawn timer is up, respawn the user
            } else if (respawnTimer <= 0) {
                respawned = true;
            }

//        If not respawned and the timer is below 0, respawn the player
        } else if (!respawned) {
            respawned = true;
        }

//         checks if the player is on the ground
        if (grounded) {

//             sets the player's max speed to the run speed if running and the walk speed otherwise
            maxSpeed = running ? runSpeed : walkSpeed;
        } else {

//             sets the player's max speed to the air speed
            maxSpeed = airSpeed;
        }

//         updates the players velocity up to the maximum speed
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

//             checks if the player is pressing down
        if (moveVector.y < -deadzone) {

//             player falls at the fast fall speed
            maxFallSpeed = fastFallSpeed * weight;
            if (vel.y > 0) {
                vel.y = 0;
            }
        } else {

//             player falls at normal speed
            maxFallSpeed = fallSpeed * weight;
        }

        if (respawned) {
//             checks if the player is moving up or down
            if (vel.y > 0 && !grounded) {

//                 up gravity accelerates player down
                vel.y -= upGravity * weight * deltaTime;
            } else if (vel.y > maxFallSpeed && !grounded) {

//                 down gravity accelerates player down
                vel.y -= downGravity * weight * deltaTime;
            }
        }

//         jumping
        if (jumping && jumpsLeft > 0) {
            if (vel.y <= 0) {
                vel.y = jumpPower;
            } else {
                vel.y += jumpPower;
            }

//            If they are not able to jump, and they are not grounded
            if (!grounded) jumpsLeft -= 1;
            grounded = false;
            jumping = false;
        }

//         grounds player if y position hasn't changed in a while because sometimes
//         the game doesn't register the player landing on the ground
        if (pos.y == prevPos.y && vel.y < fastFallSpeed && !grounded) {
            ground();
        }

        prevAnimState = animState;

//         locks animation state if the player is stunned or an attack animation is playing
        if (!lockAnim && stunTimer <= 0 && respawned) {
            if (animDuration <= 0) {
                getState();
            } else {
                lockAnim = true;
            }
        }

//         sets the physics body's velocity
        b2body.setLinearVelocity(vel);

//         updates character graphics
        setRegion(getFrame(deltaTime));
        setBounds(b2body.getPosition().x + (spriteOffset.x / spriteScale / Main.PPM) - (manualSpriteOffset.x / spriteScale / Main.PPM), b2body.getPosition().y - (manualSpriteOffset.y / spriteScale / Main.PPM) + (spriteOffset.y / spriteScale / Main.PPM), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
    }


    /*
    * Pre: Delta time
    * Post: Applies friction
    * */
    public void applyFriction(float deltaTime) {

//        If the x velocity is above 0.1
        if (Math.abs(vel.x) > 0.1f) {

//            Move the user
            vel.x += Math.signum(vel.x) * friction * deltaTime;

//        Otherwise, set velocity to 0 (in the x direction)
        } else {
            vel.x = 0;
        }
    }

    /**
     * pre: None
     * post: gets the current state of the player
     */
    public void getState() {
//        If the user is not on the ground with a y velocity above 0, user jumped
        if (vel.y > 0 && !grounded) {
            moveState = MovementState.JUMP;
            animState = AnimationState.JUMP;

//        If they are not on the ground with a y velocity below 0, user is falling
        } else if (vel.y <= 0 && !grounded && animState != AnimationState.SPECIAL_U) {
            moveState = MovementState.FALL;
            animState = AnimationState.FALL;

//        If they have an x velocity above 0 but aren't running, user is walking
        } else if (Math.abs(vel.x) > 0 && !running) {
            moveState = MovementState.WALK;
            animState = AnimationState.WALK;

//        If they have an x velocity above 0 and are running, user is running
        } else if (Math.abs(vel.x) > 0 && running) {
            moveState = MovementState.RUN;
            animState = AnimationState.RUN;

//        Otherwise, they are idle
        } else {
            moveState = MovementState.IDLE;
            animState = AnimationState.IDLE;
        }

//        Ensures attacks are disabled directly after an up special ability
        if (!afterUpB) {
            // basic attacks
            if (attackState == AttackState.BASIC) {

                //tilt attacks
                if (grounded) {
                    if (moveVector.isZero()) {
                        // neutral
                        animState = AnimationState.BASIC_N;
                        basicNeutral();
                    } else if (Math.abs(moveVector.x) > deadzone) {
                        if (moveState == MovementState.RUN) {
                            //dash
                            animState = AnimationState.DASH;
                            dashAttack();
                        } else {
                            // side
                            animState = AnimationState.BASIC_S;
                            basicSide();
                        }
                    } else if (moveVector.y > 0) {
                        // up
                        animState = AnimationState.BASIC_U;
                        basicUp();
                    } else if (moveVector.y < 0) {
                        // down
                        animState = AnimationState.BASIC_D;
                        basicDown();
                    }

                // air attacks
                } else {
                    if (moveVector.isZero()) {
                        // neutral
                        animState = AnimationState.AIR_N;
                        airNeutral();
                    } else if ((!facingLeft && moveVector.x >= deadzone) || (facingLeft && moveVector.x <= -deadzone)) {
                        // forward
                        animState = AnimationState.AIR_F;
                        airForward();
                    } else if ((!facingLeft && moveVector.x <= -deadzone) || (facingLeft && moveVector.x >= deadzone)) {
                        // backward
                        animState = AnimationState.AIR_B;
                        airBack();
                    } else if (moveVector.y > 0) {
                        // up
                        animState = AnimationState.AIR_U;
                        airUp();
                    } else if (moveVector.y < 0) {
                        // down
                        animState = AnimationState.AIR_D;
                        airDown();
                    }
                }

            // special attacks
            } else if (attackState == AttackState.SPECIAL) {
                if (moveVector.isZero()) {
                    //neutral
                    animState = AnimationState.SPECIAL_N;
                    specialNeutral();
                } else if (Math.abs(moveVector.x) > deadzone) {
                    //side
                    animState = AnimationState.SPECIAL_S;
                    specialSide();
                } else if (moveVector.y > deadzone) {
                    //up
                    animState = AnimationState.SPECIAL_U;
                    specialUp();
                } else if (moveVector.y < 0 && canDownB) {
                    //down
                    animState = AnimationState.SPECIAL_D;
                    specialDown();
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

                 if (vec.x < -deadzone) {
                    //side
                    facingLeft = true;
                    animState = AnimationState.SMASH_S;
                    smashSide();
                } else if (vec.x > deadzone) {
                    facingLeft = false;
                    animState = AnimationState.SMASH_S;
                    smashSide();
                } else if (vec.y > 0) {
                    //up
                    animState = AnimationState.SMASH_U;
                    smashUp();
                } else if (vec.y < 0) {
                    //down
                    animState = AnimationState.SMASH_D;
                    smashDown();
                }
            }
        }
    }

    /**
     * pre: None
     * post: resets player state and control
     */
    public void resetControls() {

//        Resets all necessary player variables
        moveVector = new Vector2(0, 0);
        rStick = new Vector2(0, 0);
        jumping = false;
        running = false;
    }

    /**
     * pre: deltaTime, the time between frames
     * post: sets the animation based on the player's animation state
     */
    public TextureRegion getFrame(float deltaTime) {
        TextureRegion region;

        // sets elapsedTime
        elapsedTime = animState == prevAnimState ? elapsedTime + deltaTime : 0;

//        Switch case to set the animation.  Cycles through all scenarios and assigns the animation
//        accordingly.
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
                afterUpB = false;
                region = hit.getKeyFrame(elapsedTime, true);
                attackAnimation = null;
                afterUpB = false;
                break;
            case IDLE:
            default:
                region = idle.getKeyFrame(elapsedTime, true);
                attackAnimation = null;
                break;
        }

        region = checkFacing(region);

        // offsets sprite
        spriteOffset.x = ((TextureAtlas.AtlasRegion) region).offsetX;
        spriteOffset.y = ((TextureAtlas.AtlasRegion) region).offsetY;

        // sets the lock animation if the attack animation is set and the move timer is still counting or the animation is not finished
        lockAnim = attackAnimation != null && (moveTimer > 0 || !attackAnimation.isAnimationFinished(elapsedTime));

        return region;
    }

    /*
    * Pre: A texture region
    * Post: Checks which direction is being faced and gives a texture region
    * */
    public TextureRegion checkFacing(TextureRegion region) {
//         Decide which direction to face
        if (grounded && attackAnimation == null) {

//            Facing right
            if ((vel.x > 0) && !region.isFlipX()) {
                region.flip(true, false);
                facingLeft = false;

//            Facing left
            } else if ((vel.x < 0) && region.isFlipX()) {
                region.flip(true, false);
                facingLeft = true;

//            Checks if they are facing the correct direction, flips them if they arent
            } else {
                if (!facingLeft && !region.isFlipX()) {
                    region.flip(true, false);
                } else if (facingLeft && region.isFlipX()) {
                    region.flip(true, false);
                }
            }

//        Checks if they are facing the correct direction, flips them if they arent
        } else {
            if (!facingLeft && !region.isFlipX()) {
                region.flip(true, false);
            } else if (facingLeft && region.isFlipX()) {
                region.flip(true, false);
            }
        }

        return region;
    }

    /*
     * pre: damage to deal, force to apply, amount to stun
     * post: sets animation to hit, deals damage, applies knock-back, sets stun timer
     */
    public void Hit(float damage, Vector2 force, float hitStun) {
//        If the respawn timer is 0 or lower and not invincible
        if (respawnTimer <= 0 && !invinsible) {
//        Plays the hit sound effect
            hitSFX.play(Main.sfxVolume / 10f);

//            The user has been hit, and animation state is set accordingly
            hasBeenHit = true;
            animState = AnimationState.HIT;

//            Loses hp or gains percent based on game mode
            if (stamina) {
                percent = Math.max(percent - damage, 0f);
                vel.set(force.scl(1.2f / weight));
            } else {
                percent = Math.min(percent + damage, 999.9f);
                vel.set(force.scl(((percent / 100) + 1) / weight));
            }

//            Sets stun timer
            stunTimer = hitStun;

//            Removes their move vector
            moveVector.x = 0;
        }
    }

    /**
     * pre: x position, y position
     * post: resets player, sets position
     */
    public void die() {
//        Resets player for a potential respawn by resetting all variables
        if (!stamina) {
            percent = 0;
        } else {
            percent = defaultStamina;
        }
        animDuration = -1;
        lockAnim = false;
        stunTimer = 0;
        moveVector.x = 0;
        moveVector.y = 0;
        knockedOff = true;
        ground();
        afterUpB = false;
        grounded = false;

//        Checks if the user has stocks (lives) left
        prevGoToPos = new Vector2(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        if (stockCount > 1) {
//          If they do, their position is reset and they lose a stock
            goToPos = respawnPos;
            stockCount -= 1;
            respawned = false;

            respawnTimer = respawnTime;

//        Otherwise, the user's character dies
        } else {
            dead = true;
            stockCount = 0;
            percent = 0;
        }
    }

//    The following are abstract methods meant to be overwritten for each character, and replaced with
//    their specific abilities and attacks.

    /*
 FOR ALL BELOW METHODS
     * Pre: A call to the method
     * Post: Performs an action as specified
     * */

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
