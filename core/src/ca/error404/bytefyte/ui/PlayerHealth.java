package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.chars.Madeline;
import ca.error404.bytefyte.chars.Mario;
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

    // Var init
    private final Vector2 headOffset = new Vector2();
    private final Vector2 baseOffset = new Vector2();
    private final Vector2 countryOffset = new Vector2();
    private Vector2 pos = new Vector2();

    public static int nerds;

    private final Character chara;

    TextureAtlas textureAtlas;

    TextureRegion playerBase;
    TextureRegion playerHead;
    TextureRegion country;
    TextureRegion stock;

    private final GlyphLayout layout = new GlyphLayout();
    private float numSpeed = 20f;
    private float num;
    private float prevNum = 0f;
    private String charname;
    public boolean stamina;

    private Color color;
    private Color playerColor;

    /* Pre: int, string, character object
       Post: loads player ui element
     */
    public PlayerHealth(int number, String charname, Character chara) {
        super();
        Main.objectsToAdd.remove(this);
        Main.uiToAdd.add(this);
        this.chara = chara;
        Random rand = new Random();

        // Sets position as well as spooky treat
        if (number == 1) {
            pos = new Vector2(25, 25);
            this.charname = "BOWSER";
        } else if (number == 2) {
            pos = new Vector2(525, 25);
            this.charname = "WAS";
        } else if (number == 3) {
            pos = new Vector2(1025, 25);
            this.charname = "HERE,";
        } else if (number == 4) {
            pos = new Vector2(1525, 25);
            this.charname = "HAHAHA";
        }

        // Sets the random chance for Bill Cipher and Bowser to make a cameo in the player's place
        int chanceForBowser = rand.nextInt(500000);

        // Overrides the spooky message if there are less than 4 players, or the random number was incorrect
        if (CharacterSelect.characters[3] == null || nerds != 2) {
            this.charname = chara.playerName;
        }

        textureAtlas = Main.manager.get("sprites/battleUI.atlas", TextureAtlas.class);

        // Sets images to Bowser
        if (chanceForBowser == 2) {
            playerHead = new TextureRegion(textureAtlas.findRegion("bowser_ingame"));
            stock = new TextureRegion(textureAtlas.findRegion("bowser_stock"));
            country = new TextureRegion(textureAtlas.findRegion("bowser_country"));
            if (CharacterSelect.characters[3] == null || nerds != 2) {
                this.charname = "BOWSER";
            }
        // Sets images to Bill Cipher
        } else if (chanceForBowser == 3) {
            playerHead = new TextureRegion(textureAtlas.findRegion("bill_ingame"));
            stock = new TextureRegion(textureAtlas.findRegion("bill_stock"));
            country = new TextureRegion(textureAtlas.findRegion("bill_country"));
            if (CharacterSelect.characters[3] == null || nerds != 2) {
                this.charname = "BILL";
            }
        // Sets images to the default character
        } else {
            playerHead = new TextureRegion(textureAtlas.findRegion(String.format("%s_ingame", charname)));
            stock = new TextureRegion(textureAtlas.findRegion(String.format("%s_stock", charname)));
            country = new TextureRegion(textureAtlas.findRegion(String.format("%s_country", charname)));
        }
        playerBase = new TextureRegion(textureAtlas.findRegion(String.format("player_%d_ingame", number)));

        playerColor = setPlayerColor(number);

        baseOffset.x = (textureAtlas.findRegion(String.format("player_%d_ingame", number))).offsetX;
        baseOffset.y = (textureAtlas.findRegion(String.format("player_%d_ingame", number))).offsetY;

        // Sets offset to bill's
        if (chanceForBowser == 3) {
            headOffset.x = (textureAtlas.findRegion("bill_ingame")).offsetX;
            headOffset.y = (textureAtlas.findRegion("bill_ingame")).offsetY;
            countryOffset.x = (textureAtlas.findRegion("bill_country")).offsetX;
            countryOffset.y = (textureAtlas.findRegion("bill_country")).offsetY;
        // Sets offset to bowser's
        } else if (chanceForBowser == 2) {
            headOffset.x = (textureAtlas.findRegion("bowser_ingame")).offsetX;
            headOffset.y = (textureAtlas.findRegion("bowser_ingame")).offsetY;
            countryOffset.x = (textureAtlas.findRegion("bowser_country")).offsetX;
            countryOffset.y = (textureAtlas.findRegion("bowser_country")).offsetY;
        // Sets offset to the player's
        } else {
            headOffset.x = (textureAtlas.findRegion(String.format("%s_ingame", charname))).offsetX;
            headOffset.y = (textureAtlas.findRegion(String.format("%s_ingame", charname))).offsetY;
            countryOffset.x = (textureAtlas.findRegion(String.format("%s_country", charname))).offsetX;
            countryOffset.y = (textureAtlas.findRegion(String.format("%s_country", charname))).offsetY;
        }

    }

    /* Pre: the time in between the last frame and this one
       Post: set the number to display in the percent spot
     */
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

    /* Pre: a sprite batch
       Post: the image is drawn to the screen
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (stamina) {
            setColorStamina();
        } else {
            setColor();
        }

        // Draw base and name
        batch.setColor(playerColor);
        batch.draw(country, pos.x + (countryOffset.x * 0.13f), pos.y + (countryOffset.y * 0.13f), country.getRegionWidth() * 0.13f, country.getRegionHeight() * 0.13f);
        batch.setColor(Color.WHITE);

        batch.draw(playerBase, pos.x + (baseOffset.x * 0.13f), pos.y + (baseOffset.y * 0.13f), playerBase.getRegionWidth() * 0.13f, playerBase.getRegionHeight() * 0.13f);

        layout.setText(Main.battleNameFont, charname, Color.WHITE, 0, Align.center, false);
        Main.battleNameFont.draw(batch, layout, pos.x + 250, pos.y + 70);

        batch.draw(playerHead, pos.x + (headOffset.x * 0.13f), pos.y + (headOffset.y * 0.13f), playerHead.getRegionWidth() * 0.13f, playerHead.getRegionHeight() * 0.13f);

        // Draw percent
        if (chara.stockCount > 0 || num != 0) {
            DecimalFormat form = new DecimalFormat(".#");

            layout.setText(Main.percentNumFont, String.format("%d", (int) num), color, 0, Align.right, false);
            Main.percentNumFont.draw(batch, layout, pos.x + 325, pos.y + 159);
            if (stamina) {
                layout.setText(Main.percentFont, form.format(num - Math.floor(num)) + "HP", color, 0, Align.right, false);
            } else {
                layout.setText(Main.percentFont, form.format(num - Math.floor(num)) + "%", color, 0, Align.right, false);
            }
            Main.percentFont.draw(batch, layout, pos.x + 360, pos.y + 107);
        }

        // Draw stock icons and smash meter
        for (int i=0; i < chara.stockCount; i++) {
            batch.draw(stock, pos.x + (stock.getRegionWidth() * 0.13f * i) + (5 * i) + 100, pos.y - ((stock.getRegionHeight() / 2f) * 0.13f) + 20, stock.getRegionWidth() * 0.13f, stock.getRegionHeight() * 0.13f);
        }

        if (chara.charname.equals("madeline")) {
            Madeline madeline = (Madeline) chara;

            TextureRegion barClip;
            if (madeline.badelineActive) {
                barClip = new TextureRegion(textureAtlas.findRegion("fs_meter_full"), 0, 0, (int) (1530 + (1140 * (madeline.badelineMeter / madeline.badelineMaxMeter))), textureAtlas.findRegion("fs_meter_full").getRegionHeight());
            } else {
                barClip = new TextureRegion(textureAtlas.findRegion("fs_meter_charge"), 0, 0, (int) (1530 + (1140 * (madeline.badelineMeter / madeline.badelineMaxMeter))), textureAtlas.findRegion("fs_meter_charge").getRegionHeight());
            }
            batch.draw(barClip, pos.x, pos.y, barClip.getRegionWidth() * 0.13f, barClip.getRegionHeight() * 0.13f);
        }

        if (chara.charname.equals("marioluigi")) {
            Mario mario = (Mario) chara;

            TextureRegion barClip;
            if (mario.badgeActive) {
                barClip = new TextureRegion(textureAtlas.findRegion("fs_meter_full"), 0, 0, (int) (1530 + (1140 * (mario.badgeMeter / mario.badgeMaxMeter))), textureAtlas.findRegion("fs_meter_full").getRegionHeight());
            } else {
                barClip = new TextureRegion(textureAtlas.findRegion("fs_meter_charge"), 0, 0, (int) (1530 + (1140 * (mario.badgeMeter / mario.badgeMaxMeter))), textureAtlas.findRegion("fs_meter_charge").getRegionHeight());
            }
            batch.draw(barClip, pos.x, pos.y, barClip.getRegionWidth() * 0.13f, barClip.getRegionHeight() * 0.13f);
        }
    }

    private static double round(double value) {
        int scale = (int) Math.pow(10, 1);
        return (double) Math.round(value * scale) / scale;
    }

    // Pre: N/A
    // Post: set the color of the percent text in normal gameplay
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

    // Pre: N/A
    // Post: set the color of the percent text in stamina mode
    private void setColorStamina() {
        if (chara.percent >= 300 - 19) {
            color = new Color(255, 255, 255, 1);
        } else if (chara.percent >= 300 - 29) {
            color = new Color(253 / 255f, 240 / 255f, 210 / 255f, 1);
        } else if (chara.percent >= 300 - 39) {
            color = new Color(252 / 255f, 220 / 255f, 120 / 255f, 1);
        } else if (chara.percent >= 300 - 49) {
            color = new Color(248 / 255f, 193 / 255f, 70 / 255f, 1);
        } else if (chara.percent >= 300 - 59) {
            color = new Color(243 / 255f, 153 / 255f, 62 / 255f, 1);
        } else if (chara.percent >= 300 - 69) {
            color = new Color(241 / 255f, 127 / 255f, 58 / 255f, 1);
        } else if (chara.percent >= 300 - 79) {
            color = new Color(239 / 255f, 98 / 255f, 54 / 255f, 1);
        } else if (chara.percent >= 300 - 149) {
            color = new Color(237 / 255f, 59 / 255f, 51 / 255f, 1);
        } else if (chara.percent >= 300 - 199) {
            color = new Color(206 / 255f, 47 / 255f, 43 / 255f, 1);
        } else if (chara.percent >= 300 - 249) {
            color = new Color(168 / 255f, 38 / 255f, 49 / 255f, 1);
        } else if (chara.percent >= 300 - 299) {
            color = new Color(145 / 255f, 31 / 255f, 38 / 255f, 1);
        } else {
            color = new Color(112 / 255f, 22 / 255f, 34 / 255f, 1);
        }
    }

    // Pre: The player number
    // Post: sets the color of the series icon in the corner
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
