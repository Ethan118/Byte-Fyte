package ca.error404.bytefyte.chars.bosses;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.scene.PlayRoom;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

public class Petey extends GameObject {
    float deltaTime;
    public StateMachine<Petey, PeteyState> state;
    public float hp = 1000f;
    public Random rand = new Random();
    public float spriteScale = 1;
    public float hitboxScale = 1;
    public PlayRoom screen;
    public Vector2 spawnPoint;
    public Vector2 goToPos;
    public Vector2 prevGoToPos = Vector2.Zero;

    public float width;
    public float height;

    public Vector2 pos = new Vector2();
    public Vector2 prevPos = Vector2.Zero;

    public float elapsedTime;
    
    public World world;

    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> fall;
    private Animation<TextureRegion> fly;
    private Animation<TextureRegion> spin;
    private Animation<TextureRegion> walk;

    protected Vector2 spriteOffset = Vector2.Zero;
    public Vector2 manualSpriteOffset;

    public Petey(PlayRoom screen, Vector2 spawnPoint) {
        state = new DefaultStateMachine<>(this, PeteyState.IDLE);
        this.screen = screen;
        this.spawnPoint = spawnPoint;
        this.world = screen.getWorld();

        manualSpriteOffset = new Vector2(250, 252);

        goToPos = new Vector2(spawnPoint.x / Main.PPM, spawnPoint.y / Main.PPM);

        TextureAtlas textureAtlas = Main.manager.get("sprites/enemies/petey piranha.atlas", TextureAtlas.class);

        idle = new Animation<TextureRegion>(1f/15f, textureAtlas.findRegions("idle"), Animation.PlayMode.LOOP);
        fall = new Animation<TextureRegion>(1f/15f, textureAtlas.findRegions("fall"), Animation.PlayMode.LOOP);
        fly = new Animation<TextureRegion>(1f/15f, textureAtlas.findRegions("fly"), Animation.PlayMode.LOOP);
        spin = new Animation<TextureRegion>(1f/15f, textureAtlas.findRegions("spin"), Animation.PlayMode.LOOP);
        walk = new Animation<TextureRegion>(1f/15f, textureAtlas.findRegions("walk"), Animation.PlayMode.LOOP);

        TextureRegion sprite = idle.getKeyFrame(0, true);
        setRegion(sprite);

        defineChar();

        setBounds(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y - getHeight() / 2), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
        setRegion(sprite);
    }

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
        fdef.filter.maskBits = Tags.GROUND_BIT | Tags.DEATH_BARRIER_BIT | Tags.ATTACK_BIT | Tags.PROJECTILE_BIT | Tags.LASER_BIT;
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

        EdgeShape left = new EdgeShape();
        left.set(new Vector2((float) -getRegionWidth() / (hitboxScale * 1.9f) / Main.PPM, (float) -getRegionHeight() / hitboxScale / 2.2f / Main.PPM), new Vector2((float) -getRegionWidth() / (hitboxScale * 1.9f) / Main.PPM, (float) getRegionHeight() / hitboxScale / 2.2f / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = left;
        fdef.filter.categoryBits = Tags.BOSS_SIDE_BIT;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape right = new EdgeShape();
        right.set(new Vector2((float) getRegionWidth() / (hitboxScale * 1.9f) / Main.PPM, (float) -getRegionHeight() / hitboxScale / 2.2f / Main.PPM), new Vector2((float) getRegionWidth() / (hitboxScale * 1.9f) / Main.PPM, (float) getRegionHeight() / hitboxScale / 2.2f / Main.PPM));
        fdef.isSensor = true;
        fdef.shape = right;
        fdef.filter.categoryBits = Tags.BOSS_SIDE_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float delta) {
        state.update();
        deltaTime = delta;
        elapsedTime += deltaTime;

        TextureRegion sprite = idle.getKeyFrame(elapsedTime, true);

        spriteOffset.x = ((TextureAtlas.AtlasRegion) sprite).offsetX;
        spriteOffset.y = ((TextureAtlas.AtlasRegion) sprite).offsetY;

        setRegion(sprite);
        setBounds(b2body.getPosition().x + (spriteOffset.x / spriteScale / Main.PPM) - (manualSpriteOffset.x / spriteScale / Main.PPM), b2body.getPosition().y - (manualSpriteOffset.y / spriteScale / Main.PPM) + (spriteOffset.y / spriteScale / Main.PPM), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
    }

    public void idle() {

    }
}
