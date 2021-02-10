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
    public Vector2 prevPos = new Vector2();
    public Vector2 prevVel = new Vector2();

    public World world;
    public Body b2body;

    public int walkSpeed = 1;
    public int jumpSpeed = 2;

    public int friction = 3;
    public int upGravity = 5;
    public int fallGravity = 6;

    public float turnCooldown = 0;
    public float maxTurnCooldown = 0.1f;

    public boolean grounded = false;

    public TestChar(TestScene screen) {
        this.world = screen.getWorld();
        currentState = State.IDLE;
        prevState = State.IDLE;
        
        defineChar();
    }
    
    public void defineChar() {
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

    public void update(float deltaTime) {
        prevVel = vel;
        Vector2 moveVector = Main.leftStick();

        if (moveVector.x != 0 && Math.abs(vel.x) <= walkSpeed) {
            if (((moveVector.x < 0 && vel.x > 0) || (moveVector.x > 0 && vel.x < 0)) && Math.abs(turnCooldown) >= maxTurnCooldown) {
                vel.x += walkSpeed * deltaTime * 2 * moveVector.x;
            } else {
                vel.x = walkSpeed * moveVector.x;
            }
            turnCooldown += moveVector.x * deltaTime;
        }

        if (Gdx.input.isKeyJustPressed(Keys.JUMP) || Main.contains(Main.recentButtonsP1, ControllerButtons.X) || Main.contains(Main.recentButtonsP1, ControllerButtons.Y)) {
            if (vel.y <= 0) {
                vel.y = jumpSpeed;
            } else {
                vel.y += jumpSpeed;
            }

            grounded = false;
        }

        applyFriction(deltaTime);
        if (grounded) {
            vel.y = 0;
        }

        b2body.setLinearVelocity(vel);
    }

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
