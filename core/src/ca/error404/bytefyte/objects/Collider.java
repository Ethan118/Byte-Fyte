package ca.error404.bytefyte.objects;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.constants.Tags;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Collider extends GameObject {
    public Vector2 pos;
    public Vector2 offset;

    public float width;
    public float height;

    public Character parent;
    private final World world;

    public Vector2 dir = null;
    public float power;
    public float damage;

    public float hitStun;

    public boolean lifeSteal;

    private float delay;
    private float timer;

    private Music sfx = null;

    private Character.AnimationState parentAnim;

    /**
     * pre: offset, width, height, parent Character, force applied on hit, damage on hit, duration of stun on hit, duration before instantiating
     * post: instantiates a new collided with the parameters
     */
    public Collider(Vector2 offset, float width, float height, Character parent, float power, float damage, float hitStun, float delay) {
        super();

        this.pos = parent.pos;
        this.offset = new Vector2(offset.x / Main.PPM, offset.y / Main.PPM);
        this.width = width;
        this.height = height;
        this.parent = parent;
        this.parentAnim = parent.animState;

        this.power = power;
        this.damage = damage;

        this.hitStun = hitStun;

        this.delay = delay;
        this.timer = Float.POSITIVE_INFINITY;

        this.world = parent.world;
    }


    /**
     * pre: offset, width, height, parent Character, force applied on hit, damage on hit, duration of stun on hit, duration before instantiating, amount of time
     * post: defines the physics body and colliders
     */
    public Collider(Vector2 offset, float width, float height, Character parent, float power, float damage, float hitStun, float delay, float timer) {
        super();

        this.pos = parent.pos;
        this.offset = new Vector2(offset.x / Main.PPM, offset.y / Main.PPM);
        this.width = width;
        this.height = height;
        this.parent = parent;
        this.parentAnim = parent.animState;

        this.power = power;
        this.damage = damage;

        this.hitStun = hitStun;

        this.delay = delay;
        this.timer = timer;

        this.world = parent.world;
    }

    /**
     * pre: offset, width, height, parent Character, force applied on hit, damage on hit, duration of stun on hit, should take health?, duration before instantiating
     * post: instantiates a new collided with the parameters
     */
    public Collider(Vector2 offset, float width, float height, Character parent, float power, float damage, float hitStun, boolean lifeSteal, float delay) {
        super();

        this.pos = parent.pos;
        this.offset = new Vector2(offset.x / Main.PPM, offset.y / Main.PPM);
        this.width = width;
        this.height = height;
        this.parent = parent;
        this.parentAnim = parent.animState;

        this.power = power;
        this.damage = damage;

        this.hitStun = hitStun;

        this.delay = delay;
        this.timer = Float.POSITIVE_INFINITY;

        this.lifeSteal = lifeSteal;

        this.world = parent.world;
    }

    /**
     * pre: offset, width, height, parent Character, force applied on hit, damage on hit, duration of stun on hit, duration before instantiating
     * post: instantiates a new collided with the parameters
     */
    public Collider(Vector2 offset, float width, float height, Character parent, float power, float damage, float hitStun, float delay, Vector2 dir) {
        this(offset, width, height, parent, power, damage, hitStun, delay);
        this.dir = dir;
    }

    /**
     * pre: offset, width, height, parent Character, force applied on hit, damage on hit, duration of stun on hit, duration before instantiating
     * post: instantiates a new collided with the parameters
     */
    public Collider(Vector2 offset, float width, float height, Character parent, float power, float damage, float hitStun, float delay, Music sfx) {
        this(offset, width, height, parent, power, damage, hitStun, delay);
        this.sfx = sfx;
    }


    /**
     * pre: offset, width, height, parent Character, force applied on hit, damage on hit, duration of stun on hit, duration before instantiating, amount of time
     * post: defines the physics body and colliders
     */
    public Collider(Vector2 offset, float width, float height, Character parent, float power, float damage, float hitStun, float delay, float timer, Vector2 dir) {
        this(offset, width, height, parent, power, damage, hitStun, delay, timer);
        this.dir = dir;
    }

    /**
     * pre: offset, width, height, parent Character, force applied on hit, damage on hit, duration of stun on hit, should take health?, duration before instantiating
     * post: instantiates a new collided with the parameters
     */
    public Collider(Vector2 offset, float width, float height, Character parent, float power, float damage, float hitStun, boolean lifeSteal, float delay, Vector2 dir) {
        this(offset, width, height, parent, power, damage, hitStun, lifeSteal, delay);
        this.dir = dir;
    }

    /*
    * Pre: None
    * Post: Defines a collider body
    * */
    private void define() {
        // creates a new body definition and sets the position, and type
        BodyDef bdef = new BodyDef();
        bdef.position.set((pos.x + offset.x) / Main.PPM, (pos.y + offset.y) / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        // defines the shape of the body
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(width / 2 / Main.PPM,height / 2 / Main.PPM);

        fdef.shape = shape;

        // sets the body as a sensor (no collision physics)
        fdef.isSensor = true;

        // sets tags to define the body
        fdef.filter.categoryBits = Tags.ATTACK_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    /*
     * pre: delta time, the amount of time between frames
     * post: updates the projectiles position, orientation and graphics
     */
    @Override
    public void update(float delta) {
        // checks if the body has been removed or the delay is still active
        if (!remove && (delay <= 0)) {

            if (sfx != null) {
                sfx.play();
            }
            // if the body is not defined, define it
            if (b2body == null) {
                define();
            }

            // sets the bodies position and offsets it based on which way the player is facing
            timer -= delta;

            if (parent.facingLeft) {
                b2body.setTransform(parent.pos.x + (offset.x * -1), parent.pos.y + (offset.y), 0);
            } else {
                b2body.setTransform(parent.pos.x + offset.x, parent.pos.y + (offset.y), 0);
            }

            // destroys the player once the animation is done
            if (parent.animState != parentAnim || timer <= 0) {
                b2body.setTransform(-5000f, -5000f, 0);
                destroy();
                if (sfx != null) {
                    sfx.dispose();
                }
            }
        } else {
            delay -= delta;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {

    }
}
