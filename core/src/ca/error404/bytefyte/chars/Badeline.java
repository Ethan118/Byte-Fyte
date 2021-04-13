package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class Badeline extends GameObject {
    private Character parent;

    public Vector2 pos;

    private Vector2 spriteOffset = Vector2.Zero;
    public Vector2 manualSpriteOffset = Vector2.Zero;

    Vector2 leftOffset = new Vector2(40f, 30f);
    Vector2 rightOffset = new Vector2(60f, 30f);

    private boolean lockAnim;

    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> hit;

    private final Animation<TextureRegion> charge_beam;
    //private final Animation<TextureRegion> charge_projectile;

    private Animation<TextureRegion> currentAnim;

    private float elapsedTime = 0f;

    private enum STATE {
        IDLE,
        HIT,
        PROJECTILE,
        LASER
    }

    private STATE prevState;
    private STATE state;

    private final float spriteScale = 0.4f;

    public Badeline(Character parent) {
        super();

        this.parent = parent;
        pos = parent.pos;

        manualSpriteOffset = rightOffset;

        state = STATE.IDLE;
        prevState = STATE.IDLE;

        TextureAtlas textureAtlas = Main.manager.get(String.format("sprites/%s.atlas", "madeline"), TextureAtlas.class);

        idle = new Animation<TextureRegion>(1f/60, textureAtlas.findRegions("badeline_idle"), Animation.PlayMode.LOOP);
        hit = new Animation<TextureRegion>(1f/60, textureAtlas.findRegions("hit"), Animation.PlayMode.LOOP);

        charge_beam = new Animation<TextureRegion>(1f/60, textureAtlas.findRegions("badeline_beam_charge"), Animation.PlayMode.NORMAL);
        //charge_projectile = new Animation<TextureRegion>(1f/60, textureAtlas.findRegions("charge_projectile"), Animation.PlayMode.NORMAL);

        TextureRegion sprite = idle.getKeyFrame(elapsedTime, true);
        lockAnim = false;

        BodyDef bdef = new BodyDef();
        bdef.position.set(pos.x / Main.PPM, pos.y / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = parent.world.createBody(bdef);

        setBounds(parent.pos.x - (getWidth() / 2), parent.pos.y - (getHeight() / 2), getRegionWidth() / spriteScale / Main.PPM, getRegionHeight() / spriteScale / Main.PPM);
        setRegion(sprite);
    }

    @Override
    public void update(float delta) {
        pos = parent.pos;

        prevState = state;

        if (!lockAnim) {
            getState();
        }

        if (parent.facingLeft) {
            manualSpriteOffset = leftOffset;
        } else {
            manualSpriteOffset = rightOffset;
        }

        setRegion(getFrame(delta));
        setBounds(pos.x + (spriteOffset.x / spriteScale / Main.PPM) - (manualSpriteOffset.x / spriteScale / Main.PPM), pos.y - (manualSpriteOffset.y / spriteScale / Main.PPM) + (spriteOffset.y / spriteScale / Main.PPM), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
    }

    private void getState() {
        if (parent.attackState == Character.AttackState.BASIC) {
            state = STATE.PROJECTILE;
        } else if (parent.attackState == Character.AttackState.SMASH) {
            state = STATE.LASER;
        } else if (parent.animState == Character.AnimationState.HIT) {
            state = STATE.HIT;
        } else {
            state = STATE.IDLE;
        }
    }

    private TextureRegion getFrame(float delta) {
        TextureRegion region;

        elapsedTime = state == prevState ? elapsedTime + delta : 0;

        switch (state) {
            case LASER:
                region = charge_beam.getKeyFrame(elapsedTime, false);
                currentAnim = charge_beam;
                break;
//            case PROJECTILE:
//                region = charge_projectile.getKeyFrame(elapsedTime, false);
//                break;
            case HIT:
                region = hit.getKeyFrame(elapsedTime, true);
                currentAnim = null;
                break;
            case IDLE:
            default:
                region = idle.getKeyFrame(elapsedTime, true);
                currentAnim = null;
                break;
        }

        if (!parent.facingLeft && !region.isFlipX()) {
            region.flip(true, false);
        } else if (parent.facingLeft && region.isFlipX()) {
            region.flip(true, false);
        }

        spriteOffset.x = ((TextureAtlas.AtlasRegion) region).offsetX;
        spriteOffset.y = ((TextureAtlas.AtlasRegion) region).offsetY;

        lockAnim = currentAnim != null && !currentAnim.isAnimationFinished(elapsedTime);

        return region;
    }
}
