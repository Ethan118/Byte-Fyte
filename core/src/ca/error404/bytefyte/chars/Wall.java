package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.physics.box2d.*;

public class Wall {
    public World world;
    public TestScene scene;

    public int x;
    public int y;
    public int w;
    public int h;

    public Fixture fix;

    public Wall(int x, int y, int w, int h, TestScene screen) {
        this.world = screen.getWorld();
        this.scene = screen;

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        BodyDef bdef = new BodyDef();
        bdef.position.set(this.x / Main.PPM, this.y / Main.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(this.w / Main.PPM,this.h / Main.PPM);
        fdef.friction = 0;
        fdef.filter.categoryBits = Tags.GROUND_BIT;
        fdef.shape = shape;
        fix = b2body.createFixture(fdef);
        fix.setUserData(this);
    }

    public void contact() {
        //
    }
}