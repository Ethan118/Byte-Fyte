package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;


public class Button {
    public Rectangle buttonRect;
    public Vector2 pos;
    public Texture texture;
    public String string;
    public Main game;
    public MenuCursor cursor;

    public Color unselectedColor = Color.WHITE;
    public Color selectedColor = Color.YELLOW;
    public GlyphLayout layout = new GlyphLayout();

    public Button(Vector2 pos, Main game, String string) {
        this.buttonRect = new Rectangle();
        this.pos = pos;
        this.string = string;
        this.texture = null;
        this.game = game;
        game.buttons.add(this);
        layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);

        this.buttonRect.set(pos.x - (layout.width / 2), pos.y - layout.height, layout.width, layout.height);
    }

    public Button(Vector2 pos, Main game, Texture texture) {
        this.buttonRect = new Rectangle();
        this.pos = pos;
        this.string = null;
        this.texture = texture;
        this.game = game;
        game.buttons.add(this);

        this.buttonRect.set(pos.x, pos.y, texture.getWidth(), texture.getHeight());
    }

    public void draw(SpriteBatch batch) {
        if (texture == null) {
            for (MenuCursor cursor : Main.cursors) {
                if (isCursorOver(cursor)) {
                    layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);
                    break;
                } else {
                    layout.setText(Main.menuFont, string, unselectedColor, 0, Align.center, false);
                }
            }
            Main.menuFont.draw(batch, layout, pos.x, pos.y);
        }
    }

    public boolean isCursorOver(MenuCursor cursor) {
        if (cursor.rect.overlaps(buttonRect)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isClicked() {
        for (MenuCursor cursor : Main.cursors) {
            if (cursor.controller != null) {
                if (Main.contains(Main.recentButtons.get(cursor.controller), ControllerButtons.A)) {
                    if (isCursorOver(cursor)) {
                        this.cursor = cursor;
                        return true;
                    }
                }
            } else {
                if (Gdx.input.isKeyJustPressed(Keys.MENU_SELECT)) {
                    if (isCursorOver(cursor)) {
                        this.cursor = cursor;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void update() {
        if (isClicked()) {
            click();
        }
    }

    public void click() {
        System.out.println("ow");
    }
}
