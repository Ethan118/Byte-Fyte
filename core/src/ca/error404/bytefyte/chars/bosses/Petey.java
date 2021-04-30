package ca.error404.bytefyte.chars.bosses;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.scene.BattleMap;
import ca.error404.bytefyte.scene.PlayRoom;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.scene.menu.MainMenu;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/*constructor
 * Pre: single player mode entered, stage created
 * Post: creates Petey
 * */
public class Petey extends Boss {
    private TextureRegion sprite;
    public Main game;
    public float spinTimer;
    public float hitTime;
    public float flySpeed;

    private boolean hasTransitioned = false;
    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> fall;
    private Animation<TextureRegion> fly;
    private Animation<TextureRegion> spin;
    private Animation<TextureRegion> walk;
    private Animation<TextureRegion> hit;


    /*constructor
     * Pre: stage loaded, in single player mode
     * Post: creates the character
     * */
    public Petey(PlayRoom screen, Vector2 spawnPoint, Main game) {
        super(screen, spawnPoint);
        hitboxScale = 1.5f;
        state = new DefaultStateMachine<>(this, PeteyState.IDLE);
        speed = 1;
        flySpeed = 2;
        this.game = game;

        manualSpriteOffset = new Vector2(250, 212);

        TextureAtlas textureAtlas = Main.manager.get("sprites/enemies/petey piranha.atlas", TextureAtlas.class);

        idle = new Animation<TextureRegion>(1f/15f, textureAtlas.findRegions("idle"), Animation.PlayMode.LOOP);
        fall = new Animation<TextureRegion>(1f/15f, textureAtlas.findRegions("fall"), Animation.PlayMode.LOOP);
        fly = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("fly"), Animation.PlayMode.LOOP);
        spin = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("spin"), Animation.PlayMode.LOOP);
        walk = new Animation<TextureRegion>(1f/15f, textureAtlas.findRegions("walk"), Animation.PlayMode.LOOP);
        hit = new Animation<TextureRegion>(1f/15f, textureAtlas.findRegions("hit"), Animation.PlayMode.LOOP);

        TextureRegion sprite = idle.getKeyFrame(0, true);
        setRegion(sprite);

        defineChar();

        setBounds(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y - getHeight() / 2), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
        setRegion(sprite);
    }

    /*
    * Pre: Delta time
    * Post: Updates the boss
    * */
    @Override
    public void update(float delta) {
        deltaTime = delta;
        elapsedTime += deltaTime;
        state.update();

        setRegion(sprite);
        setBounds(b2body.getPosition().x + (spriteOffset.x / spriteScale / Main.PPM) - (manualSpriteOffset.x / spriteScale / Main.PPM), b2body.getPosition().y - (manualSpriteOffset.y / spriteScale / Main.PPM) + (spriteOffset.y / spriteScale / Main.PPM), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);

        if ((hp <= 0 || Main.players.size() == 0) && !hasTransitioned) {
            new ScreenWipe(new MainMenu(game), game);
            Main.uiToAdd.clear();
            Main.ui.clear();
            Main.uiToRemove.clear();
            Main.players.clear();
            Main.bosses.clear();
            if (BattleMap.alive != null) {
                BattleMap.alive.clear();
            }
            hasTransitioned = true;
        }
    }
    /*
     * Pre: none
     * Post: sets idle animations
     * */
    public void idle() {
        sprite = checkFacing(idle.getKeyFrame(elapsedTime, true));

        spriteOffset.x = ((TextureAtlas.AtlasRegion) sprite).offsetX;
        spriteOffset.y = ((TextureAtlas.AtlasRegion) sprite).offsetY;
    }

    /*
     * Pre: None
     * Post:sets walking animation
     * */
    public void walk() {
        sprite = checkFacing(walk.getKeyFrame(elapsedTime, true));

        spriteOffset.x = ((TextureAtlas.AtlasRegion) sprite).offsetX;
        spriteOffset.y = ((TextureAtlas.AtlasRegion) sprite).offsetY;
    }

    /*
     * Pre: None
     * Post: sets flight animations
     * */
    public void fly() {
        sprite = checkFacing(fly.getKeyFrame(elapsedTime, true));

        spriteOffset.x = ((TextureAtlas.AtlasRegion) sprite).offsetX;
        spriteOffset.y = ((TextureAtlas.AtlasRegion) sprite).offsetY;
    }

    /*
     * Pre:
     * Post: sets falling animations
     * */
    public void fall() {
        sprite = checkFacing(fall.getKeyFrame(elapsedTime, true));

        spriteOffset.x = ((TextureAtlas.AtlasRegion) sprite).offsetX;
        spriteOffset.y = ((TextureAtlas.AtlasRegion) sprite).offsetY;
    }

    /*
     * Pre: None
     * Post: sets spinning animations
     * */
    public void spin() {
        sprite = checkFacing(spin.getKeyFrame(elapsedTime, true));

        spriteOffset.x = ((TextureAtlas.AtlasRegion) sprite).offsetX;
        spriteOffset.y = ((TextureAtlas.AtlasRegion) sprite).offsetY;
    }

    /*
     * Pre: None
     * Post: creates hitstate
     * */
    public void hitState() {
        sprite = checkFacing(hit.getKeyFrame(elapsedTime, true));

        spriteOffset.x = ((TextureAtlas.AtlasRegion) sprite).offsetX;
        spriteOffset.y = ((TextureAtlas.AtlasRegion) sprite).offsetY;
    }


    /*
     * Pre: Texture region
     * Post: Gets a direction to face
     * */
    public TextureRegion checkFacing(TextureRegion region) {
        // Decide which direction to face
        if (!facingLeft && !region.isFlipX()) {
            region.flip(true, false);
        } else if (facingLeft && region.isFlipX()) {
            region.flip(true, false);
        }

        return region;
    }

    /*
     * Pre: A bit
     * Post: Performs Petey's actions after hitting a wall
     * */
    @Override
    public void hitWall(int bit) {

        //if petey hits the wall this sets new actions
        if (state.getCurrentState() == PeteyState.FALL && bit == Tags.BOSS_FEET_BIT) {
            state.changeState(PeteyState.IDLE);
        } if (state.getCurrentState() == PeteyState.SPIN) {
            switch (bit) {
                case Tags.BOSS_FEET_BIT:
                case Tags.BOSS_HEAD_BIT:
                    b2body.setLinearVelocity(b2body.getLinearVelocity().x, b2body.getLinearVelocity().y * -1);
                    break;
                case Tags.BOSS_SIDE_BIT:
                    b2body.setLinearVelocity(b2body.getLinearVelocity().x * -1, b2body.getLinearVelocity().y);
            }
        }
    }


    /*
     * Pre: None
     * Post: damage code
     * */
    @Override
    public void hit(float damage) {
        if (state.getCurrentState() != PeteyState.HIT) {
            hitSFX.play(Main.sfxVolume / 10f);
            state.changeState(PeteyState.HIT);
            hp -= damage;
        }
    }
}
