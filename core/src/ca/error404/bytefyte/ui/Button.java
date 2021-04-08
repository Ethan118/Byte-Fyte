package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.scene.LoadTMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;

public class Button {
    public Rectangle buttonRect;
    private Vector2 pos;
    private Vector2 size;

    public MenuCursor cursor;

    public Button(MenuCursor cursor, Rectangle buttonRect, Vector2 pos, Vector2 size) {
        this.cursor = cursor;
        this.buttonRect = buttonRect;
        this.pos = pos;
        this.size = size;

        this.buttonRect.set(pos.x, pos.y, size.x, size.y);
    }

    public boolean isCursorOver() {
        if (cursor.cursorRect.overlaps(buttonRect)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isClicked(Controller controller) {
        if (Main.controllers.size >= 1) {
            if (cursor.controller.getButton(ControllerButtons.A)) {
                if (isCursorOver()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            if (Gdx.input.isKeyJustPressed(Keys.MENU_SELECT)) {
                if (isCursorOver()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}
