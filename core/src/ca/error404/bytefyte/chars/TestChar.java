package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.Keys;
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
    public int jumpSpeed = 1;

    public int friction = 3;
    public int gravity = 2;

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
        fdef.friction = 0;
        b2body.createFixture(fdef);

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-10 / Main.PPM, -12 / Main.PPM), new Vector2(10 / Main.PPM, -12 / Main.PPM));
        fdef.isSensor = true;

        fdef.shape = feet;
        b2body.createFixture(fdef).setUserData("feet");
    }

    public void update(float deltaTime) {
        prevVel = vel;

        if (Gdx.input.isKeyPressed(Keys.MOVE_RIGHT) && vel.x <= walkSpeed) {
            if (vel.x < 0) {
                vel.x += walkSpeed * deltaTime * 2;
            } else {
                vel.x = walkSpeed;
            }
        }

        if (Gdx.input.isKeyPressed(Keys.MOVE_LEFT) && vel.x >= -walkSpeed) {
            if (vel.x > 0) {
                vel.x -= walkSpeed * deltaTime * 2;
            } else {
                vel.x = -walkSpeed;
            }
        }

        if ((Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
            if (vel.y <= 0) {
                vel.y = jumpSpeed;
            } else {
                vel.y += jumpSpeed;
            }
        }

        applyFriction(deltaTime);

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

        vel.y -= gravity * deltaTime;
    }
}
