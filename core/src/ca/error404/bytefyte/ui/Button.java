package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Button {
    public Rectangle buttonRect;
    private Vector2 pos;
    private Vector2 size;
    public Main game;
    public MenuCursor cursor;

    public Button(Vector2 pos, Vector2 size, Main game) {
        this.buttonRect = new Rectangle();
        this.pos = pos;
        this.size = size;
        this.game = game;
        game.buttons.add(this);

        this.buttonRect.set(pos.x, pos.y, size.x, size.y);
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
                if (cursor.controller.getButton(ControllerButtons.A)) {
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