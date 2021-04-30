package ca.error404.bytefyte.chars.bosses;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.scene.PlayRoom;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

/*
 * Pre: game launch, single player called
 * Post: creates the boss class
 * */
public class Boss extends GameObject {

    //declaring variables
    float deltaTime;
    public StateMachine state;
    public float hp = 300f;
    public Random rand = new Random();
    public float spriteScale = 1;
    public float hitboxScale = 1;
    public PlayRoom screen;
    public Vector2 spawnPoint;
    public Vector2 goToPos;
    public Vector2 prevGoToPos = Vector2.Zero;
    public float speed = 1;
    public float width;
    public float height;
    public Vector2 pos = new Vector2();
    public Vector2 prevPos = Vector2.Zero;

    public float elapsedTime;

    public World world;

    protected Vector2 spriteOffset = Vector2.Zero;
    public Vector2 manualSpriteOffset;

    public boolean facingLeft = true;

    public Sound hitSFX;
    public float damage = 10f;
    public float hitStun = 0.25f;

    /*constructor
     * Pre: single player mode entered, stage created
     * Post: creates the Boss
     * */
    public Boss(PlayRoom screen, Vector2 spawnPoint) {
        this.screen = screen;
        this.spawnPoint = spawnPoint;
        this.world = screen.getWorld();

        goToPos = new Vector2(spawnPoint.x / Main.PPM, spawnPoint.y / Main.PPM);
        Main.bosses.add(this);
        hitSFX = Gdx.audio.newSound(Gdx.files.internal("audio/sound effects/playerHit.wav"));
    }

    /*
     * Pre: None
     * Post: Defines a character
     * */
    public void defineChar() {
        // loads collision box
        BodyDef bdef = new BodyDef();
        bdef.position.set(goToPos.x, goToPos.y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox((float) getRegionWidth() / hitboxScale / 2 / Main.PPM,(float) getRegionHeight() / hitboxScale / 2 / Main.PPM);
        width = getRegionWidth() / hitboxScale / 2 / Main.PPM;
        height = getRegionHeight() / hitboxScale / 2 / Main.PPM;

        fdef.shape = shape;
        fdef.filter.categoryBits = Tags.BOSS_BIT;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);

        // loads feet trigger
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2((float) -getRegionWidth() / hitboxScale / 2.2f / Main.PPM, -getRegionHeight() / (hitboxScale * 1.9f) / Main.PPM), new Vector2((float) getRegionWidth() / hitboxScale / 2.2f / Main.PPM, -getRegionHeight() / (hitboxScale * 1.9f) / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = feet;
        fdef.filter.categoryBits = Tags.BOSS_FEET_BIT;
        b2body.createFixture(fdef).setUserData(this);

        // loads head trigger
        EdgeShape head = new EdgeShape();
        head.set(new Vector2((float) -getRegionWidth() / hitboxScale / 2.2f / Main.PPM, getRegionHeight() / (hitboxScale * 1.9f) / Main.PPM), new Vector2((float) getRegionWidth() / hitboxScale / 2.2f / Main.PPM, getRegionHeight() / (hitboxScale * 1.9f) / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = head;
        fdef.filter.categoryBits = Tags.BOSS_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

        //loads left triggers
        EdgeShape left = new EdgeShape();
        left.set(new Vector2((float) -getRegionWidth() / (hitboxScale * 1.9f) / Main.PPM, (float) -getRegionHeight() / hitboxScale / 2.2f / Main.PPM), new Vector2((float) -getRegionWidth() / (hitboxScale * 1.9f) / Main.PPM, (float) getRegionHeight() / hitboxScale / 2.2f / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = left;
        fdef.filter.categoryBits = Tags.BOSS_SIDE_BIT;
        b2body.createFixture(fdef).setUserData(this);

        //loads right trigger
        EdgeShape right = new EdgeShape();
        right.set(new Vector2((float) getRegionWidth() / (hitboxScale * 1.9f) / Main.PPM, (float) -getRegionHeight() / hitboxScale / 2.2f / Main.PPM), new Vector2((float) getRegionWidth() / (hitboxScale * 1.9f) / Main.PPM, (float) getRegionHeight() / hitboxScale / 2.2f / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = right;
        fdef.filter.categoryBits = Tags.BOSS_SIDE_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

//    The following methods perform what is specified in the children classes
    @Override
    public void update(float delta) {}

    public void hitWall(int bit) {}
    public void hit(float damage) {}
    public void leaveGround() {}
}
