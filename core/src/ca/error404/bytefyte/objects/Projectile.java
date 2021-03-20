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

    public Body b2body;

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

        atlas = Main.manager.get(atlasPath, TextureAtlas.class);
        anim = new Animation<TextureRegion>(1f/30f, atlas.findRegions(animPath), Animation.PlayMode.LOOP);

        TextureRegion sprite = anim.getKeyFrame(elapsedTime, true);
        setRegion(sprite);
    }

    private void define() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos.x / Main.PPM, pos.y / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(getRegionWidth() / spriteScale / 2f / Main.PPM,getRegionHeight() / spriteScale / 2f / Main.PPM);

        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Tags.PROJECTILE_BIT;
        b2body.createFixture(fdef).setUserData(this);

        b2body.setTransform(pos, (float) Math.toRadians(vel.angleDeg() - 90));
    }

    @Override
    public void update(float delta) {
        if (!remove && (delay <= 0)) {
            if (b2body == null) {
                define();
            }
            pos = b2body.getPosition();

            vel.y -= gravity;

            if (pos.len() >= maxDistance) {
                destroy();
            } else {
                b2body.setLinearVelocity(vel);
                b2body.setAngularVelocity(spin);
            }

            if (spin == 0) {
                b2body.setTransform(b2body.getPosition(), (float) Math.toRadians(vel.angleDeg() - 90));
            }

            setBounds(b2body.getPosition().x - (getRegionWidth() / spriteScale / Main.PPM / 2), b2body.getPosition().y - (getRegionHeight() / spriteScale / Main.PPM / 2), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);

            setOriginCenter();
            setRotation((float) Math.toDegrees(b2body.getAngle()));
        } else {
            delay -= delta;
        }
    }
}
