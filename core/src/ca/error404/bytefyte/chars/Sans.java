package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.GasterBlaster;
import ca.error404.bytefyte.objects.Laser;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.PlayRoom;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Sans extends Character{
    private Music healSFX;
    private boolean upBStart = true;
    private boolean upBEnd = false;
    private boolean usingUpB = false;
    private boolean sit = false;
    private boolean usingDownB = false;
    private float moveCooldown;
    private boolean usingDTilt = false;
    private GasterBlaster gasterBlaster;
    private Laser laser;

    private TextureAtlas textureAtlas = Main.manager.get(String.format("sprites/%s.atlas", charname), TextureAtlas.class);
    private boolean hasTeleported = false;

    public Sans(PlayRoom screen, Vector2 spawnPoint, Controller controller, int playernumber) {
        super(screen, spawnPoint, controller, playernumber, "sans", "SANS", 0.8f, 0.9f);
        upB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("up_b_start"), Animation.PlayMode.NORMAL);

        weight = 0.9f;
        walkSpeed = 1.1f;
        runSpeed = 2.2f;
        manualSpriteOffset = new Vector2(50, 15f);

        projectilesOnScreen = new ArrayList<>(1);


        walk.setFrameDuration(0.02f);

        healSFX = Gdx.audio.newMusic(Gdx.files.internal("audio/sound effects/fullRestore.wav"));
        healSFX.setLooping(false);
        healSFX.setVolume(5);
    }

    /*
     * Pre: Delta time
     * Post: Updates character
     * */
    public void update(float deltaTime) {
        if (moveCooldown > 0) {
            moveCooldown -= deltaTime;
        } else {
            moveCooldown = 0;
        }
        if (sit) {
            lockAnim = true;
            if (vel.y > 0) {
                vel.y = 0;
            }

            handleInput();
            if (attackState == AttackState.SPECIAL && moveVector.y == 0 || hasBeenHit) {
                sit = false;
            } else {
                resetControls();
            }
        }
        super.update(deltaTime);

        if (dead) {
            healSFX.dispose();
        }

        if (upBStart) {
            upB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("up_b_start"), Animation.PlayMode.NORMAL);
        } else {
            upB = new Animation<TextureRegion>(1f/60f, textureAtlas.findRegions("up_b_end"), Animation.PlayMode.NORMAL);
        }
        if (animState == AnimationState.SPECIAL_U) {
            if (!usingUpB) {
                animDuration = 0.6f;
                usingUpB = true;
            }
        }
        if (usingUpB) {
            animState = AnimationState.SPECIAL_U;
            if (animDuration > 0.41) {
                upBStart = true;
                upBEnd = false;
                if (upB.getKeyFrameIndex(elapsedTime) == 7) {
                    b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y + 0.67f, 0);
                    grounded = false;
                }
            } else if (animDuration > 0.01) {
                upBStart = false;
                upBEnd = true;
            } else if (animDuration == 0){
                usingUpB = false;
            }
        }

        if (animState == AnimationState.SPECIAL_D) {
            if (!usingDownB) {
                animDuration = 1.75f;
                usingDownB = true;
                healSFX.play();
            }

            if (animDuration > 0) {
                lockAnim = true;
                if (!stamina) {
                    if (percent <= deltaTime) {
                        percent = 0;
                    } else {
                        percent -= deltaTime;
                    }
                } else {
                    if (percent + deltaTime >= 999.9) {
                        percent = 999.9f;
                    } else {
                        percent += deltaTime;
                    }
                }

            } else {
                usingDownB = false;
                lockAnim = false;
            }
        }

        if (animState == AnimationState.AIR_N) {
            if (animDuration == 0) {
                animDuration = 0.4f;
                airNeutral();
            }
        }

        if (animState == AnimationState.BASIC_D) {
            if (!usingDTilt) {
                animDuration = 0.5f;
                usingDTilt = true;
            }
            if (animDuration > 0) {
                lockAnim = true;
            } else {
                lockAnim = false;
                usingDTilt = false;
            }
        }



    }

    //    All abilities.  Will add colliders or move Sans as applicable
    @Override
    void basicNeutral() {
        if (facingLeft) {
            new Projectile(this, new Vector2(0, 0), new Vector2(5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_top", "sprites/sans.atlas", 0, 0.9f);
            new Projectile(this, new Vector2(0.0375f, 0), new Vector2(5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_mid", "sprites/sans.atlas", 0.001f, 0.9f);
            new Projectile(this, new Vector2(-0.01f, 0), new Vector2(5, 0), 0, 0f, 20, 3, 2, 1, "bone_v_bottom", "sprites/sans.atlas", 0.002f, 0.9f);

        } else {
            new Projectile(this, new Vector2(0, 0), new Vector2(-5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_top", "sprites/sans.atlas", 0, 0.9f);
            new Projectile(this, new Vector2(-0.0375f, 0), new Vector2(-5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_mid", "sprites/sans.atlas", 0.001f, 0.9f);
            new Projectile(this, new Vector2(0.01f, 0), new Vector2(-5, 0), 0, 0f, 20, 3, 2, 1, "bone_v_bottom", "sprites/sans.atlas", 0.002f, 0.9f);
        }
        resetControls();
    }

    @Override
    void basicSide() {
        if (!facingLeft) {
            new Projectile(this, new Vector2(0, 0), new Vector2(5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_top", "sprites/sans.atlas", 0, 0.9f);
            new Projectile(this, new Vector2(0.0375f, 0), new Vector2(5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_mid", "sprites/sans.atlas", 0.001f, 0.9f);
            new Projectile(this, new Vector2(-0.01f, 0), new Vector2(5, 0), 0, 0f, 20, 3, 2, 1, "bone_v_bottom", "sprites/sans.atlas", 0.002f, 0.9f);

        } else {
            new Projectile(this, new Vector2(0, 0), new Vector2(-5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_top", "sprites/sans.atlas", 0, 0.9f);
            new Projectile(this, new Vector2(-0.0375f, 0), new Vector2(-5, 0), 0, 0f, 20, 0, 2, 0, "bone_v_mid", "sprites/sans.atlas", 0.001f, 0.9f);
            new Projectile(this, new Vector2(0.01f, 0), new Vector2(-5, 0), 0, 0f, 20, 3, 2, 1, "bone_v_bottom", "sprites/sans.atlas", 0.002f, 0.9f);
        }
        resetControls();
    }

    @Override
    void basicUp() {
        new Projectile(this, new Vector2(0, height + 0.05f), new Vector2(0, 1), 0, 0f, 0.1f, 2, 4, 2, "bone_v_top", "sprites/sans.atlas", 0, 0.5f);
        new Projectile(this, new Vector2(0, height - 0.0375f + 0.05f), new Vector2(0, 1), 0, 0f, 0.1f, 0, 2, 0, "bone_v_mid", "sprites/sans.atlas", 0.01f, 0.5f);
        new Projectile(this, new Vector2(0, height - 0.0375f + 0.05f), new Vector2(0, 1), 0, 0f, 0.075f, 0, 0, 0, "bone_v_mid", "sprites/sans.atlas", 0.03f, 0.5f);
        new Projectile(this, new Vector2(0, height - 0.0375f + 0.05f), new Vector2(0, 1), 0, 0f, 0.05f, 0, 0, 0, "bone_v_mid", "sprites/sans.atlas", 0.05f, 0.5f);
        new Projectile(this, new Vector2(0, height - 0.0375f + 0.05f), new Vector2(0, 1), 0, 0f, 0.025f, 0, 0, 0, "bone_v_mid", "sprites/sans.atlas", 0.07f, 0.5f);

        resetControls();
    }

    @Override
    void basicDown() {
        new Collider(new Vector2(0, 0f), 75, 35, this, 2f, 5f, 0.25f, 0);

        resetControls();
    }

    @Override
    void dashAttack() {
        new Collider(new Vector2(7.5f, 0), 15, 30, this, 3f, 9f, 0.25f, 0);
        resetControls();
    }

    @Override
    void smashSide() {
        if (moveCooldown == 0) {
            moveCooldown = 1f;

            gasterBlaster = new GasterBlaster(this, 10f, laser);

        }
        resetControls();
    }

    @Override
    void smashUp() {
        smashSide();
    }

    @Override
    void smashDown() {
        smashSide();

    }

    @Override
    void specialNeutral() {
        sit = true;
        resetControls();
    }

    @Override
    void specialSide() {
        new Collider(new Vector2(0, 5), 40, 60, this, 2f, 4f, 0.25f, 0);
        animDuration = 1f;

        vel.set(new Vector2(7 * moveVector.x, 0));
        resetControls();
    }

    @Override
    void specialUp() {
        if (animDuration > 0.2) {
            upBStart = false;
            upBEnd = true;
            if (animDuration < 0.4) {
                if (!hasTeleported) {
                    pos.y += 10;
                    hasTeleported = true;
                }
            }
        } else {
            upBStart = true;
            upBEnd = false;
        }
    }

    @Override
    void specialDown() {

        resetControls();
    }

    @Override
    void ultimate() {
    }

    @Override
    void airNeutral() {
        new Collider(new Vector2(0, 7.5f), 50, 35, this, 2f, 5f, 0.25f, 0);

        resetControls();
    }

    @Override
    void airForward() {
        if (facingLeft) {
            new Projectile(this, new Vector2(0, 0), new Vector2(-5, 0), 0, 0f, 20, 3, 4, 1, "bullet", "sprites/sans.atlas", 0, 0.9f);
        } else {
            new Projectile(this, new Vector2(0, 0), new Vector2(5, 0), 0, 0f, 20, 3, 4, 1, "bullet", "sprites/sans.atlas", 0, 0.9f);
        }
        resetControls();
    }

    @Override
    void airBack() {
        if (facingLeft) {
            new Projectile(this, new Vector2(0, 0), new Vector2(5, 0), 0, 0f, 20, 3, 4, 1, "bullet", "sprites/sans.atlas", 0, 0.9f);
        } else {
            new Projectile(this, new Vector2(0, 0), new Vector2(-5, 0), 0, 0f, 20, 3, 4, 1, "bullet", "sprites/sans.atlas", 0, 0.9f);
        }
        resetControls();
    }

    @Override
    void airUp() {
        new Projectile(this, new Vector2(0, 0), new Vector2(0, 5), 0, 0f, 20, 3, 4, 1, "bullet", "sprites/sans.atlas", 0, 0.9f);

        resetControls();
    }

    @Override
    void airDown() {
        new Projectile(this, new Vector2(0, 0), new Vector2(0, -5), 0, 0f, 20, 3, 4, 1, "bullet", "sprites/sans.atlas", 0, 0.9f);

        resetControls();
    }

}
