package ca.error404.bytefyte.objects;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.constants.Tags;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Projectile extends GameObject {
    public Vector2 pos;
    public Vector2 vel;

    private float maxDistance;

    public Character parent;
    private final World world;

    public float power;
    public float damage;

    public float gravity;
    public float spin;

    public float hitStun;

    private Animation<TextureRegion> anim;
    private TextureAtlas atlas;
    private float elapsedTime = 0f;

    private float delay;

    private final float spriteScale = 60;

    /**
     * pre: parent Character, position, velocity, gravity, spin, max distance to travel, force applied on hit, damage dealt, duration of stun on hit, path to the animation, path to the atlas containing animation, duration before spawned
     * post: instantiates a new projectile with given parameters
     */
    public Projectile(Character parent, Vector2 pos, Vector2 vel, float gravity, float spin, float maxDistance, float power, float damage, float hitStun, String animPath, String atlasPath, float delay) {
        super();

        this.parent = parent;
        this.world = parent.world;

        this.pos = pos;
        this.vel = vel;
        this.maxDistance = maxDistance;

        this.power = power;
        this.damage = damage;
        this.hitStun = hitStun;

        this.gravity = gravity;
        this.spin = spin;

        this.delay = delay;

        // creates the atlas and loads the animation from the atlas
        atlas = Main.manager.get(atlasPath, TextureAtlas.class);
        anim = new Animation<TextureRegion>(1f/30f, atlas.findRegions(animPath), Animation.PlayMode.LOOP);

        // sets the texture region to the first keyframe
        TextureRegion sprite = anim.getKeyFrame(elapsedTime, true);
        setRegion(sprite);
    }

    /**
     * pre:
     * post: defines the physics body and colliders
     */
    private void define() {
        // creates a new body definition and sets the position, and type
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos.x / Main.PPM, pos.y / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        // defines the shape of the body
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(getRegionWidth() / spriteScale / 2f / Main.PPM,getRegionHeight() / spriteScale / 2f / Main.PPM);
        fdef.shape = shape;

        // sets the body as a sensor (no collision physics)
        fdef.isSensor = true;

        // sets tags to define the body
        fdef.filter.categoryBits = Tags.PROJECTILE_BIT;
        b2body.createFixture(fdef).setUserData(this);

        // sets position and angle
        b2body.setTransform(pos, (float) Math.toRadians(vel.angleDeg() - 90));
    }

    /**
     * pre: delta time, the amount of time between frames
     * post: updates the projectiles position, orientation and graphics
     */
    @Override
    public void update(float delta) {
        // checks if the body has been removed or the delay is still active
        if (!remove && (delay <= 0)) {

            // if the body is not defined, define it
            if (b2body == null) {
                pos = parent.pos;
                define();
            }

            // set the projectiles position to the physics body position
            pos = b2body.getPosition();

            // apply gravity
            vel.y -= gravity;

            // checks if the projectile has reached a max distance
            if (pos.len() >= maxDistance) {
                destroy();
            } else {

                // sets physics body's velocity
                b2body.setLinearVelocity(vel);
                b2body.setAngularVelocity(spin);
            }

            // points the object in the direction it is travelling if there is no spin applied
            if (spin == 0) {
                b2body.setTransform(b2body.getPosition(), (float) Math.toRadians(vel.angleDeg() - 90));
            }

            // sets the bounds of the graphics
            setBounds(b2body.getPosition().x - (getRegionWidth() / spriteScale / Main.PPM / 2), b2body.getPosition().y - (getRegionHeight() / spriteScale / Main.PPM / 2), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);

            // resets the rotational origin and rotates the sprite
            setOriginCenter();
            setRotation((float) Math.toDegrees(b2body.getAngle()));
        } else {
            delay -= delta;
        }
    }

    @Override
    public void destroy() {
        remove = true;

//        Removes this projectile from the parent's arraylist
        parent.projectilesOnScreen.remove(this);
    }
}
