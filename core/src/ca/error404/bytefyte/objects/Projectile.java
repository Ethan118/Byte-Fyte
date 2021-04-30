package ca.error404.bytefyte.objects;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.constants.Tags;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

// Class to create projectiles
public class Projectile extends GameObject {

//    Initializing variables
    public Vector2 startPos;
    public Vector2 pos;
    public Vector2 vel;
    public Vector2 offset;

    private final float maxDistance;

    public Character parent;
    private final World world;

    public float power;
    public float damage;

    public float gravity;
    public float spin;

    public float hitStun;

    private final Animation<TextureRegion> anim;
    private final TextureAtlas atlas;
    private float elapsedTime = 0f;

    private float delay;

    private float spriteScale = 60;

    private final String animPath;

    private boolean disableEditing = false;

    private Music sfx = null;

    /**
     * pre: parent Character, position, velocity, gravity, spin, max distance to travel, force applied on hit, damage dealt, duration of stun on hit, path to the animation, path to the atlas containing animation, duration before spawned
     * post: instantiates a new projectile with given parameters
     */

    public Projectile(Character parent, Vector2 offset, Vector2 vel, float gravity, float spin, float maxDistance, float power, float damage, float hitStun, String animPath, String atlasPath, float delay) {
        super();

        this.parent = parent;
        this.world = parent.world;
        this.offset = offset;

        this.pos = new Vector2(parent.pos.x + offset.x, parent.pos.y + offset.y);
        startPos = this.pos;
        this.vel = vel;
        this.maxDistance = maxDistance;

        this.power = power;
        this.damage = damage;
        this.hitStun = hitStun;

        this.gravity = gravity;
        this.spin = spin;

        this.delay = delay;

        this.animPath = animPath;

        // creates the atlas and loads the animation from the atlas
        atlas = Main.manager.get(atlasPath, TextureAtlas.class);
        anim = new Animation<TextureRegion>(1f/30f, atlas.findRegions(animPath), Animation.PlayMode.LOOP);

        // sets the texture region to the first keyframe
        TextureRegion sprite = anim.getKeyFrame(elapsedTime, true);


        setRegion(sprite);
    }

    public Projectile (Character parent, Vector2 offset, Vector2 vel, float gravity, float spin, float maxDistance, float power, float damage, float hitStun, String animPath, String atlasPath, float delay, float spriteScale) {
        this(parent, offset, vel, gravity, spin, maxDistance, power, damage, hitStun, animPath, atlasPath, delay);

        this.spriteScale = spriteScale;
    }

    public Projectile (Character parent, Vector2 offset, Vector2 vel, float gravity, float spin, float maxDistance, float power, float damage, float hitStun, String animPath, String atlasPath, float delay, Music sfx) {
        this(parent, offset, vel, gravity, spin, maxDistance, power, damage, hitStun, animPath, atlasPath, delay);

        this.sfx = sfx;
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

//            Plays sound effect
            if (sfx != null) {
                sfx.play();
            }
            // if the body is not defined, define it
            if (b2body == null) {
                pos = new Vector2(parent.pos.x + offset.x, parent.pos.y + offset.y);
                startPos = pos;
                define();
            }

            // set the projectiles position to the physics body position
            pos = b2body.getPosition();

            // apply gravity
            vel.y -= gravity;

            // checks if the projectile has reached a max distance
            if (Math.abs(pos.dst(startPos)) >= maxDistance) {
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

            setRegion(getFrame(delta));
            // sets the bounds of the graphics
            setBounds(b2body.getPosition().x - (getRegionWidth() / spriteScale / Main.PPM / 2), b2body.getPosition().y - (getRegionHeight() / spriteScale / Main.PPM / 2), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);

            // resets the rotational origin and rotates the sprite
            setOriginCenter();

//            Adjusts sprite for master chief orb
            if (animPath.equals("orb")) {
                if (!disableEditing) {
                    spriteScale = 20f;
                    disableEditing = true;
                }
            }

//            Adjusts sprite for master chief rocket or laser
            if (animPath.equals("laser") || animPath.equals("bazooka")) {

//                Adjusts once based on left direction
                if (parent.facingLeft && !disableEditing) {
                    setRotation(0);
                    spriteScale = 20f;
                    disableEditing = true;

//                Adjusts once based on right direction
                } else if (!parent.facingLeft && !disableEditing){
                    setRotation(180);
                    spriteScale = 20f;
                    disableEditing = true;

//                Adjusts for only vertical motion
                } else if (vel.x == 0) {
                    setRotation(90);
                }

//            Adjusts for red or green shell
            } else if (animPath.equals("red-shell") || animPath.equals("green-shell")) {
                setRotation(0);
                disableEditing = true;

//            Otherwise set the rotation to the body direction
            } else {
                setRotation((float) Math.toDegrees(b2body.getAngle()));
            }

//        Reduce delay
        } else {
            delay -= delta;
        }
    }

    /*
    * Pre: Delta time
    * Post: Returns the animation frame
    * */
    private TextureRegion getFrame(float delta) {

//        Sets a new texture region
        TextureRegion region;

//        Updates elapsed time
        elapsedTime += delta;

//        Set the animation region
        region = anim.getKeyFrame(elapsedTime, true);
        return region;
    }


    /*
    * Pre: None
    * Post: Destroys this instance of projectile
    * */
    @Override
    public void destroy() {
        remove = true;

//        Removes this projectile from the parent's arraylist
        try {
            parent.projectilesOnScreen.remove(this);
        } catch (Exception ignored) {

        }

//        Disposes of the sound effect if it exists
        if (sfx != null) {
            sfx.dispose();
        }
    }
}
