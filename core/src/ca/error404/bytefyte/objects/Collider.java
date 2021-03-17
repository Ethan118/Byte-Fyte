package ca.error404.bytefyte.objects;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Collider {
    public Vector2 pos;
    public Vector2 offset;

    public float width;
    public float height;

    public Character parent;
    private final World world;

    public Body b2body;

    public float power;
    public float minPower;
    public float damage;
    public Vector2 direction;

    public float hitStun;

    public Collider(Vector2 offset, float width, float height, Character parent, float power, float minPower, float damage, Vector2 direction, float hitStun) {
        this.pos = parent.pos;
        this.offset = offset;
        this.width = width;
        this.height = height;
        this.parent = parent;

        this.power = power;
        this.minPower = minPower;
        this.damage = damage;
        this.direction = direction;

        this.hitStun = hitStun;

        this.world = parent.world;

        define();
    }

    private void define() {
        BodyDef bdef = new BodyDef();
        bdef.position.set((pos.x + offset.x) / Main.PPM, (pos.y + offset.y) / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(width / 2 / Main.PPM,height / 2 / Main.PPM);

        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Tags.ATTACK_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void setPosition(Vector2 pos, float direction) {
        b2body.setTransform(pos.x + (offset.x * direction), pos.y + (offset.y), 0);
    }

    public void destroy() {
        world.destroyBody(b2body);
    }
}
