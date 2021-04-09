package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Button {
    public Rectangle buttonRect;
    private Vector2 pos;
    private Vector2 size;

    public MenuCursor cursor;
    public MenuCursor cursor2;
    public MenuCursor cursor3;
    public MenuCursor cursor4;

    public Button(MenuCursor cursor, MenuCursor cursor2, MenuCursor cursor3, MenuCursor cursor4, Rectangle buttonRect, Vector2 pos, Vector2 size) {
        this.cursor = cursor;
        this.cursor2 = cursor2;
        this.cursor3 = cursor3;
        this.cursor4 = cursor4;

        this.buttonRect = buttonRect;
        this.pos = pos;
        this.size = size;

        this.buttonRect.set(pos.x, pos.y, size.x, size.y);
    }

    public boolean isCursorOver(MenuCursor cursor) {
        if (cursor.cursorRect.overlaps(buttonRect)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isClicked() {
        if (cursor != null) {
            if (cursor.controller != null) {
                if (cursor.controller.getButton(ControllerButtons.A)) {
                    if (isCursorOver(cursor)) {
                        return true;
                    }
                }
            } else {
                if (Gdx.input.isKeyJustPressed(Keys.MENU_SELECT)) {
                    if (isCursorOver(cursor)) {
                        return true;
                    }
                }
            }
        }

        if (cursor2 != null) {
            if (cursor2.controller != null) {
                if (cursor2.controller.getButton(ControllerButtons.A)) {
                    if (isCursorOver(cursor2)) {
                        return true;
                    }
                }
            } else {
                if (Gdx.input.isKeyJustPressed(Keys.MENU_SELECT)) {
                    if (isCursorOver(cursor2)) {
                        return true;
                    }
                }
            }
        }
        if (cursor3 != null) {
            if (cursor3.controller != null) {
                if (cursor3.controller.getButton(ControllerButtons.A)) {
                    if (isCursorOver(cursor3)) {
                        return true;
                    }
                }

            } else {
                if (Gdx.input.isKeyJustPressed(Keys.MENU_SELECT)) {
                    if (isCursorOver(cursor3)) {
                        return true;
                    }
                }

            }
        }

        if (cursor4 != null) {
            if (cursor4.controller != null) {
                if (cursor4.controller.getButton(ControllerButtons.A)) {
                    if (isCursorOver(cursor4)) {
                        return true;
                    }
                }

            } else {
                if (Gdx.input.isKeyJustPressed(Keys.MENU_SELECT)) {
                    if (isCursorOver(cursor4)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
