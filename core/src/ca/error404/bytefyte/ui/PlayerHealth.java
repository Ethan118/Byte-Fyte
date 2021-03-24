package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PlayerHealth extends GameObject {

    private int playerNum;
    private String charname;

    private Vector2 headOffset = new Vector2();
    private Vector2 baseOffset = new Vector2();

    TextureRegion playerBase;
    TextureRegion playerHead;

    public PlayerHealth(int number, String charname) {
        super();
        Main.objectsToAdd.remove(this);
        this.playerNum = number;
        this.charname = charname;

        TextureAtlas textureAtlas = new TextureAtlas("sprites/ui.atlas");

        playerBase = new TextureRegion(textureAtlas.findRegion(String.format("player_%d_ingame", number)));
        playerHead = new TextureRegion(textureAtlas.findRegion(String.format("%s_ingame", charname)));

        baseOffset.x = (textureAtlas.findRegion(String.format("player_%d_ingame", number))).offsetX;
        baseOffset.y = (textureAtlas.findRegion(String.format("player_%d_ingame", number))).offsetY;
        headOffset.x = (textureAtlas.findRegion(String.format("%s_ingame", charname))).offsetX;
        headOffset.y = (textureAtlas.findRegion(String.format("%s_ingame", charname))).offsetY;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(playerBase, 0 + (baseOffset.x / 15 / Main.PPM), 0 + (baseOffset.y / 15 / Main.PPM), playerBase.getRegionWidth() / Main.PPM / 15, playerBase.getRegionHeight() / Main.PPM / 15);
        batch.draw(playerHead, 0 + (headOffset.x / 15 / Main.PPM), 0 + (headOffset.y / 15 / Main.PPM), playerHead.getRegionWidth() / Main.PPM / 15, playerHead.getRegionHeight() / Main.PPM / 15);
    }
}
