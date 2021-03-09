package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

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
}
