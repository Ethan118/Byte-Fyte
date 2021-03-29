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
    private Vector2 pos = new Vector2();

    TextureRegion playerBase;
    TextureRegion playerHead;

    public PlayerHealth(int number, String charname) {
        super();
        Main.objectsToAdd.remove(this);
        Main.uiToAdd.add(this);
        this.playerNum = number;
        this.charname = charname;

        if (number == 1) {
            pos = new Vector2(328, 25);
        } else if (number == 2) {
            pos = new Vector2(735, 25);
        }

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
        batch.draw(playerBase, pos.x + (baseOffset.x * 0.13f), pos.y + (baseOffset.y * 0.13f), playerBase.getRegionWidth() * 0.13f, playerBase.getRegionHeight() * 0.13f);
        batch.draw(playerHead, pos.x + (headOffset.x * 0.13f), pos.y + (headOffset.y * 0.13f), playerHead.getRegionWidth() * 0.13f, playerHead.getRegionHeight() * 0.13f);
    }
}
