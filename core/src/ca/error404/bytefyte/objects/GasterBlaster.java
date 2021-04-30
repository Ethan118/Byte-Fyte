package ca.error404.bytefyte.objects;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GasterBlaster extends GameObject {

    private Character parent;
    private float speed;
    private Laser laser;
    public Vector2 pos = new Vector2(0, 0);
    public Vector2 targetPos;
    private TextureAtlas textureAtlas;
    private Animation<TextureRegion> animation;
    private float elapsedTime = 0;
    private float spriteScale;
    private TextureRegion sprite;
    private Vector2 angle;
    private boolean laserHasSpawned = false;
    private Vector2 offset = new Vector2(0, 0);
    private Music arriveSFX;
    private Music fireSFX;
    private boolean arriveHasPlayed = false;



    public GasterBlaster(Character parent, float speed, Laser laser) {
        this.parent = parent;
        this.laser = laser;
        this.speed = speed;
        this.targetPos = parent.pos.cpy();
        this.spriteScale = parent.spriteScale;

        this.textureAtlas = Main.manager.get("sprites/sans.atlas", TextureAtlas.class);
        animation = new Animation<TextureRegion>(100f, textureAtlas.findRegions("gb_normal"), Animation.PlayMode.NORMAL);
        sprite = animation.getKeyFrame(elapsedTime, true);

        setBounds(parent.pos.x - (getWidth() / 2), parent.pos.y - (getHeight() / 2), getRegionWidth() / spriteScale / Main.PPM, getRegionHeight() / spriteScale / Main.PPM);
        setRegion(sprite);
        setOriginCenter();

        if (parent.controller != null) {
            this.angle = parent.rStick.cpy();
        } else {
            this.angle = parent.moveVector.cpy();
        }

        setRotation(angle.angleDeg() + 90);
        arriveSFX = Gdx.audio.newMusic(Gdx.files.internal("audio/sound effects/gasterBlasterArrive.wav"));
        arriveSFX.setLooping(false);
        arriveSFX.setVolume(Main.sfxVolume / 10f);

        fireSFX = Gdx.audio.newMusic(Gdx.files.internal("audio/sound effects/gasterBlasterFire.wav"));
        fireSFX.setLooping(false);
        fireSFX.setVolume(Main.sfxVolume / 10f);

    }

    @Override
    public void update(float delta) {
        pos.lerp(targetPos, speed * delta);
        offset.y = pos.y - parent.pos.cpy().y;
        offset.x = (float) ((pos.x - parent.pos.cpy().x) + (sprite.getRegionWidth()/0.8 / Main.PPM)/4 );

        if (pos.dst(targetPos) < 0.1) {
            animation = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("gb_fire"), Animation.PlayMode.NORMAL);
            elapsedTime += delta;

            if (!laserHasSpawned) {
                fireSFX.play();

                if (parent.controller != null) {
                    if (parent.facingLeft) {
                        laser = new Laser(parent, offset, angle, 100, 2.5f, 4, 0.5f, 0.1f, 1f, "gb_laser", "sprites/sans.atlas", 0.8f);
                    } else {
                        laser = new Laser(parent, offset, angle, 100, 2.5f, 4, 0.5f, 0.1f, 1f, "gb_laser", "sprites/sans.atlas", 0.8f);
                    }
                } else {
                    if (parent.facingLeft) {
                        laser = new Laser(parent, offset, angle, 100, 2.5f, 4, 0.5f, 0.1f, 1f, "gb_laser", "sprites/sans.atlas", 0.8f);
                    } else {
                        laser = new Laser(parent, offset, angle, 100, 2.5f, 4, 0.5f, 0.1f, 1f, "gb_laser", "sprites/sans.atlas", 0.8f);
                    }
                }

                laserHasSpawned = true;


            }
            if (elapsedTime > 1) {
                destroy();
            }
        } else if (!arriveHasPlayed) {
            arriveSFX.play();
            arriveHasPlayed = true;
        }


        if (laserHasSpawned) {
            Main.gameObjects.remove(this);
            Main.gameObjects.add(this);
        }
        sprite = animation.getKeyFrame(elapsedTime, true);

        setBounds(pos.x - (getWidth() / 2), pos.y - (getHeight() / 2), getRegionWidth() / spriteScale / Main.PPM, getRegionHeight() / spriteScale / Main.PPM);
        setRegion(sprite);
        setOriginCenter();





    }

    @Override
    public void destroy() {
        super.destroy();
        arriveSFX.dispose();
    }
}
