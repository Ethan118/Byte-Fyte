package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.Laser;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.BattleMap;
import ca.error404.bytefyte.scene.PlayRoom;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Luigi extends Character {
//    initializing variables
    Mario parent;

    public Vector2 targetPos = new Vector2();
    private Random rand = new Random();

    public boolean hide = false;

    public int damageMultiplier = 1;

    public float maxRange = 0.2f;

    public float savedWalk;
    public float savedRun;
    public float savedJump;

    public Luigi(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playernumber, Mario parent) {
        this(screen, spawnPoint, controller, playernumber, parent, 0);
    }

    /**
     * Constructor
     * @param screen
     * @param spawnPoint
     * @param controller
     * @param playerNumber
     * @param parent
     * pre: the screen luigi is on, the point to spawn, the controller that controls, player number, the parent attached
     * post: instantiates luigi instance
     */
    public Luigi(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playerNumber, Mario parent, int hp) {
//        call parent constructor
        super(screen, spawnPoint, controller, playerNumber, parent.charname, parent.playerName, parent.spriteScale, parent.hitboxScale, hp);
//        defining variables
        manualSpriteOffset = parent.manualSpriteOffset;
        Main.players.remove(this);
        Main.luigis.add(this);
        Main.uiToRemove.add(health);

        savedWalk = walkSpeed;
        savedRun = runSpeed;
        savedJump = jumpPower;

        this.parent = parent;

        // loads animations
        TextureAtlas textureAtlas = Main.manager.get(String.format("sprites/%s.atlas", charname), TextureAtlas.class);

        idle = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_idle"), Animation.PlayMode.LOOP);
        walk = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_walk"), Animation.PlayMode.LOOP);
        run = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_run"), Animation.PlayMode.LOOP);

        jump = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_jump"), Animation.PlayMode.LOOP);
        fall = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_fall"), Animation.PlayMode.LOOP);
        hit = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_hit"), Animation.PlayMode.NORMAL);

        neutralAttack = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_neutral"), Animation.PlayMode.NORMAL);
        sideTilt = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_stilt"), Animation.PlayMode.NORMAL);
        upTilt = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_idle"), Animation.PlayMode.NORMAL);
        downTilt = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_dtilt"), Animation.PlayMode.NORMAL);

        neutralB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_neutral_b"), Animation.PlayMode.NORMAL);
        upB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_idle"), Animation.PlayMode.LOOP);
        downB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_idle"), Animation.PlayMode.LOOP);
        sideB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("side_b"), Animation.PlayMode.NORMAL);

        nair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_nair"), Animation.PlayMode.NORMAL);
        dair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_fall"), Animation.PlayMode.NORMAL);
        fair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_fall"), Animation.PlayMode.NORMAL);
        bair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_fall"), Animation.PlayMode.NORMAL);
        uair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_fall"), Animation.PlayMode.NORMAL);

        upSmash = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_idle"), Animation.PlayMode.NORMAL);
        downSmash = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_down_smash"), Animation.PlayMode.NORMAL);
        sideSmash = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_side_smash"), Animation.PlayMode.NORMAL);

        dashAttack = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_run"), Animation.PlayMode.NORMAL);

        TextureRegion sprite = idle.getKeyFrame(elapsedTime, true);
        attackAnimation = null;
        setRegion(sprite);

        defineChar();

        setBounds(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y - getHeight() / 2), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
        setRegion(sprite);
    }

    @Override
    public void handleInput(){
//        reset move vector
        moveVector.set(0, 0);

//        if there is a controller
        if (controller != null) {

//            moves luigi to the target position
            if (Math.abs(targetPos.x - b2body.getPosition().x) < 0.01f) {
                moveVector.x = 0;
            } else {
                moveVector.x = Math.signum(targetPos.x - b2body.getPosition().x);
            }

//            if it is not after upB set jumping to whether the user clicks the jump button
            if (!afterUpB) {
                jumping = Main.contains(Main.recentButtons.get(controller), ControllerButtons.Y) || Main.contains(Main.recentButtons.get(controller), ControllerButtons.X);
            }

//            set running to if the user clicks the bumpers
            running = controller.getButton(ControllerButtons.R_BUMPER) || controller.getButton(ControllerButtons.L_BUMPER);

        } else {
//            if it is not after upB set jumping to whether the user clicks the jump button
            if (!afterUpB) {
                jumping = Gdx.input.isKeyJustPressed(Keys.JUMP_ALT);
            }

//            moves luigi to the target position
            if (Math.abs(targetPos.x - b2body.getPosition().x) < maxRange) {
                moveVector.x = 0;
            } else {
                moveVector.x = Math.signum(targetPos.x - b2body.getPosition().x);
            }

//            set running to if the user clicks the bumpers
            running = Gdx.input.isKeyPressed(Keys.RUN);
        }

//        set y move vector and the attack state to the parent's
        moveVector.y = parent.moveVector.y;
        attackState = parent.attackState;
    }

    @Override
    public void update(float delta) {
//        hide luigi for certain animations
        if (parent.animState == AnimationState.BASIC_U) {
            hide = true;
        } else if (parent.animState == AnimationState.SPECIAL_S) {
            hide = true;
        } else if (parent.animState == AnimationState.SPECIAL_D) {
            hide = true;
        } else if (parent.animState == AnimationState.SPECIAL_U) {
            hide = true;
            grounded = false;
        } else if (parent.animState == AnimationState.AIR_D) {
            hide = true;
        } else {
            hide = false;
        }

//        launch luigi up during up tilt
        if (parent.animState == AnimationState.BASIC_U && parent.upB.getKeyFrameIndex(parent.elapsedTime) == 9) {
            vel.y = 9;
        }

//        update target position to the parent's follow point
        targetPos.set(parent.followPoint.cpy());

        super.update(delta);
        if (dead) {
            Main.luigis.remove(this);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
//        only draw if hide is false
        if (!hide) {
            super.draw(batch);
        }
    }

    @Override
    public void Hit(float damage, Vector2 force, float hitStun) {
//        only get hit if visible
        if (!hide) {
            super.Hit(damage, force, hitStun);
        }
    }

//    spawn lightning from the sky at a random distance away
    @Override
    void basicNeutral() {
        if (parent.moveCooldown == 0 && !hide) {
            facingLeft = !parent.facingLeft;

            parent.moveCooldown = 1f;

            float xOffset = rand.nextFloat();
            xOffset += 0.5f;

            if (facingLeft) {
                new Laser(this, new Vector2(-xOffset, 2), new Vector2(0, -1), 3, 5, 10, 0.5f, 0f, 20f / 30f, "lightning", "sprites/marioluigi.atlas", 1f);
            } else {
                new Laser(this, new Vector2(xOffset, 2), new Vector2(0, -1), 3, 5, 10, 0.5f, 0f, 20f / 30f, "lightning", "sprites/marioluigi.atlas", 1f);
            }
        }
    }

//    spawn a fireball
    @Override
    void basicSide() {
        if (!hide) {
            facingLeft = !parent.facingLeft;

            if (facingLeft) {
                new Projectile(this, new Vector2(-0.1f, 0f), new Vector2(-5, 0), 0, 0, 10, 2f, 2f, 0.15f, "fireball", "sprites/marioluigi.atlas", 20f / 30f, 0.4f);
            } else {
                new Projectile(this, new Vector2(0.1f, 0f), new Vector2(5, 0), 0, 0, 10, 2f, 2f, 0.15f, "fireball", "sprites/marioluigi.atlas", 20f / 30f, 0.4f);
            }
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

//    spawn a shell projectile
    @Override
    void smashSide() {
        if (!hide) {
            facingLeft = !parent.facingLeft;

            if (facingLeft) {
                new Projectile(this, new Vector2(-0.1f, -0.07f), new Vector2(-5, 0), 0, 0, 10, 4f, 5f, 0.25f, "green-shell", "sprites/marioluigi.atlas", 10 / 30f, spriteScale);
            } else {
                new Projectile(this, new Vector2(0.1f, -0.07f), new Vector2(5, 0), 0, 0, 10, 4f, 5f, 0.25f, "green-shell", "sprites/marioluigi.atlas", 10 / 30f, spriteScale);
            }
        }
    }

    @Override
    void smashUp() {

    }

//    create a collider
    @Override
    void smashDown() {
        if (!hide) {
            facingLeft = !parent.facingLeft;

            new Collider(new Vector2(30, 0), 55, 40, this, 2f, 2f, 0.25f, 0);
        }
    }

//    create a collider
    @Override
    void specialNeutral() {
        if (!hide) {
            facingLeft = !parent.facingLeft;
            new Collider(new Vector2(45, -10), 25, 30, this, 2f, 2f, 0.25f, 79f / 30f);
        }
    }

//    hold animation for 1 second
    @Override
    void specialSide() {
        if (!hide) {
            animDuration = 1f;
        }
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
