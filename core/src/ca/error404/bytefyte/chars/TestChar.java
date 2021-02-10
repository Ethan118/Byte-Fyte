package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

public class TestChar extends Sprite {
    public enum State {IDLE, WALK, RUN, JUMP, FALL}

    public State currentState;
    public State prevState;

    public Vector2 pos = new Vector2();
    public Vector2 vel = new Vector2();
    public Vector2 prevPos = Vector2.Zero;
    public Vector2 prevVel = Vector2.Zero;

    public World world;
    public Body b2body;

    public int walkSpeed = 1;
    public int jumpSpeed = 2;

    public int friction = 3;
    public int upGravity = 5;
    public int fallGravity = 6;
    public int fastFall = 60;

    public float turnCooldown = 0;
    public float maxTurnCooldown = 0.1f;
    public int maxJumps = 2;
    public int jumpsLeft = 0;

    public boolean grounded = false;

    public TestChar(TestScene screen) {
        this.world = screen.getWorld();
        currentState = State.IDLE;
        prevState = State.IDLE;
        
        defineChar();
    }
    
    public void defineChar() {
        // loads collision box
        BodyDef bdef = new BodyDef();
        bdef.position.set(0, 0);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(10 / Main.PPM,10 / Main.PPM);

        fdef.shape = shape;
        fdef.filter.categoryBits = Tags.GROUND_BIT;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-9 / Main.PPM, -12 / Main.PPM), new Vector2(9 / Main.PPM, -12 / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = feet;
        fdef.filter.categoryBits = Tags.PLAYER_FEET_BIT;
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
        pos = b2body.getPosition();

        Vector2 moveVector = Main.leftStick();

        // horizontal movement
        if (moveVector.x != 0 && Math.abs(vel.x) <= walkSpeed) {
            if (((moveVector.x < 0 && vel.x > 0) || (moveVector.x > 0 && vel.x < 0)) && Math.abs(turnCooldown) >= maxTurnCooldown) {
                vel.x += walkSpeed * deltaTime * 2 * moveVector.x;
            } else {
                vel.x = walkSpeed * moveVector.x;
            }
            turnCooldown += moveVector.x * deltaTime;
        }

        // the biggest if statement you have ever seen. checks if jump button is pressed
        if ((Gdx.input.isKeyJustPressed(Keys.JUMP) || Main.contains(Main.recentButtons.get(Main.controllers.get(0)), ControllerButtons.X) || Main.contains(Main.recentButtons.get(Main.controllers.get(0)), ControllerButtons.Y)) && jumpsLeft > 0) {
            if (vel.y <= 0) {
                vel.y = jumpSpeed;
            } else {
                vel.y += jumpSpeed;
            }

            jumpsLeft -= 1;
            grounded = false;
        }

        // fast fall if down is held
        if (moveVector.y < -0.9f && !grounded) {
            if (vel.y > 0) {
                vel.y = 0;
            }
            vel.y -= fastFall * deltaTime;
        }

        applyFriction(deltaTime);
        if (grounded) vel.y = 0;

        // grounds player if y position hasn't changed in a while because sometimes
        // the game doesn't register the player landing on the ground
        if (pos.y == prevPos.y && vel.y <= -10) {
            ground();
        }

        b2body.setLinearVelocity(vel);
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
}
