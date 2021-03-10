package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.EventListener;

public abstract class Character extends Sprite {
    private enum State {
        IDLE,
        RUN,
        WALK,
        JUMP,
        FALL,
        BASIC,
        SPECIAL,
        SMASH,
        FINAL,
    }

    private State state;
    private State prevState;

    private Vector2 vel;
    private Vector2 pos;
    private float deltaTime;

    private World world;
    private Body b2Body;

    private int speed;
    private int jumpPower;

    private int gravity;
    private int friction;

    private float spriteScale;
    private float collisionScale;

    private boolean grounded;

    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> walk;
    private Animation<TextureRegion> run;
    private Animation<TextureRegion> jump;
    private Animation<TextureRegion> fall;

    private Controller controller;
    private float deadZone = 0.1f;

    private void defBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(pos);

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox((float) getRegionWidth() / (collisionScale * 2 * Main.PPM), (float) getRegionHeight() / (collisionScale * 2 * Main.PPM));

        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Tags.PLAYER_BIT;
        fixtureDef.friction = 0;
        b2Body.createFixture(fixtureDef).setUserData(this);

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2((float) -getRegionWidth() / collisionScale / 2.2f / Main.PPM, -getRegionHeight() / (collisionScale * 1.9f) / Main.PPM), new Vector2((float) getRegionWidth() / collisionScale / 2.2f / Main.PPM, -getRegionHeight() / (collisionScale * 1.9f) / Main.PPM));
        fixtureDef.isSensor = true;
        fixtureDef.shape = feet;
        fixtureDef.filter.categoryBits = Tags.PLAYER_FEET_BIT;
        b2Body.createFixture(fixtureDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2((float) -getRegionWidth() / collisionScale / 2.2f / Main.PPM, getRegionHeight() / (collisionScale * 1.9f) / Main.PPM), new Vector2((float) getRegionWidth() / collisionScale / 2.2f / Main.PPM, getRegionHeight() / (collisionScale * 1.9f) / Main.PPM));
        fixtureDef.isSensor = true;
        fixtureDef.shape = head;
        fixtureDef.filter.categoryBits = Tags.PLAYER_HEAD_BIT;
        b2Body.createFixture(fixtureDef).setUserData(this);
    }

    public Character(TestScene scene, Vector2 spawn, Controller controller) {
        this.world = scene.getWorld();
        this.controller = controller;

        this.pos = spawn;

        state = State.IDLE;
        prevState = State.IDLE;

        defBody();

        setBounds(b2Body.getPosition().x - getWidth() / 2, (b2Body.getPosition().y - getHeight() / 2), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
    }

    private void handleInput() {
        Vector2 moveVector = new Vector2();

        if (Main.controllers.size > 0) {
            moveVector.x = Math.abs(controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS)) >= deadZone ? Math.abs(controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS)) : 0f;
        } else {
            if (Gdx.input.isKeyPressed(Keys.MOVE_RIGHT)) moveVector.x += 1;
            if (Gdx.input.isKeyPressed(Keys.MOVE_LEFT)) moveVector.x -= 1;
        }
    }

    private void update() {
        handleInput();


    }

    private void render() {

    }
}
