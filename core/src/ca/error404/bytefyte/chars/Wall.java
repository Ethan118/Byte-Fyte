package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.scene.PlayRoom;
import com.badlogic.gdx.physics.box2d.*;


/*
 * Pre: stage created
 * Post: creates the wall class
 * */
public class Wall {

    //declaring and initializing variables
    public World world;
    public PlayRoom scene;
    public int x;
    public int y;
    public float w;
    public float h;
    public Fixture fix;
    public Body b2body;

    /*constructor
     * Pre: wall class called
     * Post: creates the wall
     * finds out if user collides with wall
     * */
    public Wall(int x, int y, float w, float h, PlayRoom screen) {
        this.world = screen.getWorld();
        this.scene = screen;

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        BodyDef bdef = new BodyDef();
        bdef.position.set(this.x / Main.PPM, this.y / Main.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

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
