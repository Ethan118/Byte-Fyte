package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public class PlayerHealth extends GameObject {

    private int playerNum;
    private String charname;

    private Vector2 headOffset = new Vector2();
    private Vector2 baseOffset = new Vector2();
    private Vector2 countryOffset = new Vector2();
    private Vector2 pos = new Vector2();

    private final Character chara;

    TextureRegion playerBase;
    TextureRegion playerHead;
    TextureRegion country;
    TextureRegion stock;
    TextureRegion fsCharge;
    TextureRegion fsFull;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;

    private GlyphLayout layout = new GlyphLayout();

    BitmapFont name;
    BitmapFont percent;

    private Color color;

    public PlayerHealth(int number, String charname, Character chara) {
        super();
        Main.objectsToAdd.remove(this);
        Main.uiToAdd.add(this);
        this.playerNum = number;
        this.charname = charname;
        this.chara = chara;

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 20;
        fontParameter.color = Color.WHITE;

        name = fontGenerator.generateFont(fontParameter);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Gilroy-ExtraboldItalic.ttf"));
        fontParameter.size = 60;
        fontParameter.borderWidth = 5;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.shadowColor = new Color(0, 0, 0, 1);
        fontParameter.shadowOffsetX = 5;
        fontParameter.shadowOffsetY = 5;

        percent = fontGenerator.generateFont(fontParameter);

        fontGenerator.dispose();

        if (number == 1) {
            pos = new Vector2(172, 25);
        } else if (number == 2) {
            pos = new Vector2(574, 25);
        } else if (number == 3) {
            pos = new Vector2(975, 25);
        } else if (number == 4) {
            pos = new Vector2(1366, 25);
        }

        TextureAtlas textureAtlas = new TextureAtlas("sprites/ui.atlas");

        playerBase = new TextureRegion(textureAtlas.findRegion(String.format("player_%d_ingame", number)));
        playerHead = new TextureRegion(textureAtlas.findRegion(String.format("%s_ingame", charname)));
        stock = new TextureRegion(textureAtlas.findRegion(String.format("%s_stock", charname)));
        country = new TextureRegion(textureAtlas.findRegion(String.format("%s_%d_country", charname, number)));
        fsCharge = new TextureRegion(textureAtlas.findRegion("fs_meter_charge"));
        fsFull = new TextureRegion(textureAtlas.findRegion("fs_meter_full"));

        baseOffset.x = (textureAtlas.findRegion(String.format("player_%d_ingame", number))).offsetX;
        baseOffset.y = (textureAtlas.findRegion(String.format("player_%d_ingame", number))).offsetY;
        headOffset.x = (textureAtlas.findRegion(String.format("%s_ingame", charname))).offsetX;
        headOffset.y = (textureAtlas.findRegion(String.format("%s_ingame", charname))).offsetY;
        countryOffset.x = (textureAtlas.findRegion(String.format("%s_country", charname))).offsetX;
        countryOffset.y = (textureAtlas.findRegion(String.format("%s_country", charname))).offsetY;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch) {
        setColor();

        batch.draw(playerBase, pos.x + (baseOffset.x * 0.13f), pos.y + (baseOffset.y * 0.13f), playerBase.getRegionWidth() * 0.13f, playerBase.getRegionHeight() * 0.13f);
        batch.draw(playerHead, pos.x + (headOffset.x * 0.13f), pos.y + (headOffset.y * 0.13f), playerHead.getRegionWidth() * 0.13f, playerHead.getRegionHeight() * 0.13f);
        batch.draw(country, pos.x + (countryOffset.x * 0.13f), pos.y + (countryOffset.y * 0.13f), country.getRegionWidth() * 0.13f, country.getRegionHeight() * 0.13f);

        for (int i=0; i < chara.stockCount; i++) {
            batch.draw(stock, pos.x + (stock.getRegionWidth() * 0.13f * i) + (5 * i) + 100, pos.y, stock.getRegionWidth() * 0.13f, stock.getRegionHeight() * 0.13f);
        }

        name.draw(batch, chara.playerName, pos.x + 200, pos.y + 70);

        layout.setText(percent, String.format("%.0f", chara.percent) + "%", color, 0, Align.right, false);
        percent.draw(batch, layout, pos.x + 350, pos.y + 135);
    }

    private void setColor() {
        if (chara.percent <= 19) {
            color = new Color(255, 255, 255, 1);
        } else if (chara.percent <= 29) {
            color = new Color(253 / 255f, 240 / 255f, 210 / 255f, 1);
        } else if (chara.percent <= 39) {
            color = new Color(252 / 255f, 220 / 255f, 120 / 255f, 1);
        } else if (chara.percent <= 49) {
            color = new Color(248 / 255f, 193 / 255f, 70 / 255f, 1);
        } else if (chara.percent <= 59) {
            color = new Color(243 / 255f, 153 / 255f, 62 / 255f, 1);
        } else if (chara.percent <= 69) {
            color = new Color(241 / 255f, 127 / 255f, 58 / 255f, 1);
        } else if (chara.percent <= 79) {
            color = new Color(239 / 255f, 98 / 255f, 54 / 255f, 1);
        } else if (chara.percent <= 149) {
            color = new Color(237 / 255f, 59 / 255f, 51 / 255f, 1);
        } else if (chara.percent <= 199) {
            color = new Color(206 / 255f, 47 / 255f, 43 / 255f, 1);
        } else if (chara.percent <= 249) {
            color = new Color(168 / 255f, 38 / 255f, 49 / 255f, 1);
        } else if (chara.percent <= 299) {
            color = new Color(145 / 255f, 31 / 255f, 38 / 255f, 1);
        } else {
            color = new Color(112 / 255f, 22 / 255f, 34 / 255f, 1);
        }
    }
}
