package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.scene.menu.CharacterSelect;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.text.DecimalFormat;
import java.util.Random;

public class PlayerHealth extends GameObject {

    private final Vector2 headOffset = new Vector2();
    private final Vector2 baseOffset = new Vector2();
    private final Vector2 countryOffset = new Vector2();
    private Vector2 pos = new Vector2();

    public static int nerds;

    private final Character chara;

    TextureRegion playerBase;
    TextureRegion playerHead;
    TextureRegion country;
    TextureRegion stock;
    TextureRegion fsCharge;
    TextureRegion fsFull;

    private final GlyphLayout layout = new GlyphLayout();
    private float numSpeed = 20f;
    private float num;
    private float prevNum = 0f;
    private String charname;

    private Color color;
    private Color playerColor;

    public PlayerHealth(int number, String charname, Character chara) {
        super();
        Main.objectsToAdd.remove(this);
        Main.uiToAdd.add(this);
        this.chara = chara;
        Random rand = new Random();

        if (number == 1) {
            pos = new Vector2(25, 25);
            this.charname = "I'M";
        } else if (number == 2) {
            pos = new Vector2(525, 25);
            this.charname = "WATCHING";
        } else if (number == 3) {
            pos = new Vector2(1025, 25);
            this.charname = "YOU,";
        } else if (number == 4) {
            pos = new Vector2(1525, 25);
            this.charname = "NERDS";
        }

        int bill = rand.nextInt(200);

        if (CharacterSelect.characters[3] == null || nerds != 2) {
            this.charname = chara.playerName;
        }

        TextureAtlas textureAtlas = Main.manager.get("sprites/battleUI.atlas", TextureAtlas.class);

        if (bill == 2) {
            playerHead = new TextureRegion(textureAtlas.findRegion("bill_ingame"));
            stock = new TextureRegion(textureAtlas.findRegion("bill_stock"));
            country = new TextureRegion(textureAtlas.findRegion("bill_country"));
            if (nerds != 2) {
                this.charname = "BILL";
            }
        } else {
            playerHead = new TextureRegion(textureAtlas.findRegion(String.format("%s_ingame", charname)));
            stock = new TextureRegion(textureAtlas.findRegion(String.format("%s_stock", charname)));
            country = new TextureRegion(textureAtlas.findRegion(String.format("%s_country", charname)));
        }
        playerBase = new TextureRegion(textureAtlas.findRegion(String.format("player_%d_ingame", number)));
        fsCharge = new TextureRegion(textureAtlas.findRegion("fs_meter_charge"));
        fsFull = new TextureRegion(textureAtlas.findRegion("fs_meter_full"));

        playerColor = setPlayerColor(number);

        baseOffset.x = (textureAtlas.findRegion(String.format("player_%d_ingame", number))).offsetX;
        baseOffset.y = (textureAtlas.findRegion(String.format("player_%d_ingame", number))).offsetY;

        if (bill == 2) {
            headOffset.x = (textureAtlas.findRegion("bill_ingame")).offsetX;
            headOffset.y = (textureAtlas.findRegion("bill_ingame")).offsetY;
            countryOffset.x = (textureAtlas.findRegion("bill_country")).offsetX;
            countryOffset.y = (textureAtlas.findRegion("bill_country")).offsetY;
        } else {
            headOffset.x = (textureAtlas.findRegion(String.format("%s_ingame", charname))).offsetX;
            headOffset.y = (textureAtlas.findRegion(String.format("%s_ingame", charname))).offsetY;
            countryOffset.x = (textureAtlas.findRegion(String.format("%s_country", charname))).offsetX;
            countryOffset.y = (textureAtlas.findRegion(String.format("%s_country", charname))).offsetY;
        }
    }

    @Override
    public void update(float delta) {
        float margain = delta * numSpeed;
        if (num > chara.percent + margain) {
            numSpeed = Math.abs(chara.percent - prevNum) * 4;
            num -= margain;
        } else if (num < chara.percent - margain) {
            numSpeed = Math.abs(chara.percent - prevNum) * 4;
            num += margain;
        } else {
            num = chara.percent;
            prevNum = num;
            numSpeed = 0;
        }

        num = (float) round(num);
    }

    @Override
    public void draw(SpriteBatch batch) {
        setColor();

        batch.setColor(playerColor);
        batch.draw(country, pos.x + (countryOffset.x * 0.13f), pos.y + (countryOffset.y * 0.13f), country.getRegionWidth() * 0.13f, country.getRegionHeight() * 0.13f);
        batch.setColor(Color.WHITE);

        batch.draw(playerBase, pos.x + (baseOffset.x * 0.13f), pos.y + (baseOffset.y * 0.13f), playerBase.getRegionWidth() * 0.13f, playerBase.getRegionHeight() * 0.13f);
        batch.draw(playerHead, pos.x + (headOffset.x * 0.13f), pos.y + (headOffset.y * 0.13f), playerHead.getRegionWidth() * 0.13f, playerHead.getRegionHeight() * 0.13f);

        if (chara.stockCount > 0 || num != 0) {
            DecimalFormat form = new DecimalFormat(".#");

            layout.setText(Main.percentNumFont, String.format("%d", (int) num), color, 0, Align.right, false);
            Main.percentNumFont.draw(batch, layout, pos.x + 325, pos.y + 159);
            layout.setText(Main.percentFont, form.format(num - Math.floor(num)) + "%", color, 0, Align.right, false);
            Main.percentFont.draw(batch, layout, pos.x + 360, pos.y + 107);
        }

        for (int i=0; i < chara.stockCount; i++) {
            batch.draw(stock, pos.x + (stock.getRegionWidth() * 0.13f * i) + (5 * i) + 100, pos.y - ((stock.getRegionHeight() / 2f) * 0.13f) + 20, stock.getRegionWidth() * 0.13f, stock.getRegionHeight() * 0.13f);
        }

        layout.setText(Main.battleNameFont, charname, Color.WHITE, 0, Align.center, false);
        Main.battleNameFont.draw(batch, layout, pos.x + 250, pos.y + 70);
    }

    private static double round(double value) {
        int scale = (int) Math.pow(10, 1);
        return (double) Math.round(value * scale) / scale;
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

    private Color setPlayerColor(int num) {
        if (num == 1) {
            return new Color(255/255f, 17/255f, 35/255f, 1);
        } else if (num == 2) {
            return new Color(0/255f, 139/255f, 255/255f, 1);
        } else if (num == 3) {
            return new Color(255/255f, 185/255f, 21/255f, 1);
        } else if (num == 4) {
            return new Color(11/255f, 185/255f, 52/255f, 1);
        }

        return Color.WHITE;
    }

    @Override
    public void destroy() {
        Main.uiToRemove.add(this);
    }
}
