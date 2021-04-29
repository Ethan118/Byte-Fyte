package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.scene.BattleMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Luigi extends Character {
    Mario parent;

    public Vector2 targetPos = new Vector2();

    public Luigi(BattleMap screen, Vector2 spawnPoint, Controller controller, int playerNumber, Mario parent) {
        super(screen, spawnPoint, controller, playerNumber, parent.charname, parent.playerName, parent.spriteScale, parent.hitboxScale);
        manualSpriteOffset = parent.manualSpriteOffset;
        Main.players.remove(this);
        Main.uiToRemove.add(health);

        this.parent = parent;

        // loads animations
        TextureAtlas textureAtlas = Main.manager.get(String.format("sprites/%s.atlas", charname), TextureAtlas.class);

        idle = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_idle"), Animation.PlayMode.LOOP);
        walk = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_walk"), Animation.PlayMode.LOOP);
        run = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_run"), Animation.PlayMode.LOOP);

        jump = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_jump"), Animation.PlayMode.LOOP);
        fall = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_fall"), Animation.PlayMode.LOOP);
        hit = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_hit"), Animation.PlayMode.NORMAL);

        neutralAttack = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_neutral"), Animation.PlayMode.NORMAL);
        sideTilt = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_stilt"), Animation.PlayMode.NORMAL);
        upTilt = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_utilt"), Animation.PlayMode.NORMAL);
        downTilt = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_dtilt"), Animation.PlayMode.NORMAL);

        neutralB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_neutral_b"), Animation.PlayMode.NORMAL);
        upB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_up_b"), Animation.PlayMode.LOOP);
        downB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_down_b"), Animation.PlayMode.LOOP);
        sideB = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_side_b"), Animation.PlayMode.NORMAL);

        nair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_nair"), Animation.PlayMode.NORMAL);
        dair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_dair"), Animation.PlayMode.NORMAL);
        fair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_fair"), Animation.PlayMode.NORMAL);
        bair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_bair"), Animation.PlayMode.NORMAL);
        uair = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_uair"), Animation.PlayMode.NORMAL);

        upSmash = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_up_smash"), Animation.PlayMode.NORMAL);
        downSmash = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_down_smash"), Animation.PlayMode.NORMAL);
        sideSmash = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_side_smash"), Animation.PlayMode.NORMAL);

        dashAttack = new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions("luigi_dash_attack"), Animation.PlayMode.NORMAL);

        TextureRegion sprite = idle.getKeyFrame(elapsedTime, true);
        attackAnimation = null;
        setRegion(sprite);

        defineChar();

        setBounds(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y - getHeight() / 2), (float) getRegionWidth() / spriteScale / Main.PPM, (float) getRegionHeight() / spriteScale / Main.PPM);
        setRegion(sprite);
    }

    @Override
    public void handleInput(){
        if (controller != null) {

            if (Math.abs(targetPos.x - b2body.getPosition().x) < 0.01f) {
                moveVector.x = 0;
            } else {
                moveVector.x = Math.signum(targetPos.x - b2body.getPosition().x);
            }

            if (!afterUpB) {
                jumping = Main.contains(Main.recentButtons.get(controller), ControllerButtons.Y) || Main.contains(Main.recentButtons.get(controller), ControllerButtons.X);
            }

            running = controller.getButton(ControllerButtons.R_BUMPER) || controller.getButton(ControllerButtons.L_BUMPER);

        } else {
            if (!afterUpB) {
                jumping = Gdx.input.isKeyJustPressed(Keys.JUMP_ALT);
            }

            if (Math.abs(targetPos.x - b2body.getPosition().x) < 0.1f) {
                moveVector.x = 0;
            } else {
                moveVector.x = Math.signum(targetPos.x - b2body.getPosition().x);
            }

            running = Gdx.input.isKeyPressed(Keys.RUN);
        }
    }

    @Override
    public void update(float delta) {
        targetPos.set(parent.followPoint.cpy());
        super.update(delta);
    }

    @Override
    void basicNeutral() {

    }

    @Override
    void basicSide() {

    }

    @Override
    void basicUp() {

    }

    @Override
    void basicDown() {

    }

    @Override
    void dashAttack() {

    }

    @Override
    void smashSide() {

    }

    @Override
    void smashUp() {

    }

    @Override
    void smashDown() {

    }

    @Override
    void specialNeutral() {

    }

    @Override
    void specialSide() {

    }

    @Override
    void specialUp() {

    }

    @Override
    void specialDown() {

    }

    @Override
    void ultimate() {

    }

    @Override
    void airNeutral() {

    }

    @Override
    void airForward() {

    }

    @Override
    void airBack() {

    }

    @Override
    void airUp() {

    }

    @Override
    void airDown() {

    }
}
