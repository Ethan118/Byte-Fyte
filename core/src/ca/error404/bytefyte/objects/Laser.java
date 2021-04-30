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

// Class to create a laser
public class Laser extends GameObject {

//    Initializing variables
    private Vector2 p1, p2, collision, dir;
    private float length;

    public Character parent;
    private final World world;

    public float power;
    public float damage;
    public float hitStun;

    private float delay;
    private float dur;

    private final Animation<TextureRegion> anim;
    private final TextureAtlas atlas;
    private float elapsedTime = 0f;

    private float spriteScale;

    /*
    * Constructor
    * Pre: Inputs for parameters
    * Post: A new laser
    * */
    public Laser(Character parent, Vector2 offset, Vector2 dir, float range, float power, float damage, float hitStun, float delay, float duration, String animPath, String atlasPath, float spriteScale) {

//        Setting variables
        this.parent = parent;
        world = parent.world;

        this.dir = dir;

        this.power = power;
        this.damage = damage;
        this.hitStun = hitStun;

        this.delay = delay;
        this.dur = duration;

        this.spriteScale = spriteScale;

        atlas = Main.manager.get(atlasPath, TextureAtlas.class);
        anim = new Animation<TextureRegion>(1f/30f, atlas.findRegions(animPath), Animation.PlayMode.NORMAL);

        TextureRegion sprite = anim.getKeyFrame(elapsedTime, false);
        setRegion(sprite);

        p1 = this.parent.pos.cpy().add(offset);
        p2 = this.dir.cpy().scl(range).add(p1);

        collision = p2;

        castRay();

        length = collision.cpy().dst(p1);

        setBounds(p1.x, p1.y - (getRegionHeight() / spriteScale / Main.PPM / 2), length, (float) getRegionHeight() / spriteScale / Main.PPM);
        setOrigin(getOriginX(), getHeight() / 2);
        setRotation(dir.angleDeg());
    }

    /*
    * Pre: None
    * Post: A mathematical ray that returns intersects
    * */
    private void castRay() {

//        Callback for collision
        RayCastCallback callback = new RayCastCallback() {

            /*
            * Pre: A ray
            * Post: Instructs ray on what to do if colliding
            * */
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
//                If colliding with the death barrier, copy point of collision
                if (fixture.getFilterData().categoryBits == Tags.DEATH_BARRIER_BIT) {
                    collision = point.cpy();

//                    Cut ray there
                    return fraction;

//                Otherwise extend the ray
                } else {
                    return -1;
                }
            }
        };

//        Casts the ray
        world.rayCast(callback, p1, p2);
    }

    /*
    * Pre: None
    * Post: A defined ray body
    * */
    private void define() {
//        Creates a new body definition and sets the position, and type
        BodyDef bdef = new BodyDef();

//        Defines the ray position with two points on it (for direction)
        bdef.position.set(p1.x, p1.y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

//         defines the shape of the body
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(length / 2,getRegionHeight() / spriteScale / 2 / Main.PPM, new Vector2(length / 2, 0), 0);

        fdef.shape = shape;

//         sets the body as a sensor (no collision physics)
        fdef.isSensor = true;

//         sets tags to define the body
        fdef.filter.categoryBits = Tags.LASER_BIT;
        b2body.createFixture(fdef).setUserData(this);

        b2body.setTransform(p1, (float) Math.toRadians(dir.angleDeg()));
    }

    /*
    * Pre: Delta time
    * Post: Updates the laser
    * */
    @Override
    public void update(float delta) {

//        If there is a delay still, reduce it by the elapsed time
        if (delay > 0) {
            delay -= delta;

//        Otherwise, define the laser if not already defined
        } else {
            if (b2body == null) {
                define();
            }
        }

//        If the laser duration is still above 0
        if (dur > 0) {

//            Set the texture, region, position, and origin
            setRegion(getFrame(delta));
            setBounds(p1.x, p1.y - (getRegionHeight() / spriteScale / Main.PPM / 2), length, (float) getRegionHeight() / spriteScale / Main.PPM);
            setOrigin(getOriginX(), getHeight() / 2);
            setRotation(dir.angleDeg());

//            Counts down the duration
            dur -= delta;

//        Otherwise, destroy the laser
        } else {
            destroy();
        }
    }

    /*
    * Pre: Delta time
    * Post: The frame of the animation
    * */
    private TextureRegion getFrame(float delta) {
        TextureRegion region;

//        Updates the elapsed time
        elapsedTime += delta;

//        Gets and returns the frame of the animation
        region = anim.getKeyFrame(elapsedTime, false);
        return region;
    }
}