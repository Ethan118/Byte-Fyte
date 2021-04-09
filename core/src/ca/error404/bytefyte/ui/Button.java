package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Button {
    public Rectangle buttonRect;
    private Vector2 pos;
    private Vector2 size;

    public MenuCursor[] cursors;

    public Button(MenuCursor[] cursors, Rectangle buttonRect, Vector2 pos, Vector2 size) {
        this.cursors = cursors;

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

    public int isClicked() {
        for (int i = 0; i < cursors.length; i++) {
            if (cursors[i] != null) {
                if (cursors[i].controller != null) {
                    if (cursors[i].controller.getButton(ControllerButtons.A)) {
                        if (isCursorOver(cursors[i])) {
                            return (i + 1);
                        }
                    }
                } else {
                    if (Gdx.input.isKeyJustPressed(Keys.MENU_SELECT)) {
                        if (isCursorOver(cursors[i])) {
                            return (i + 1);
                        }
                    }
                }
            }
        }
        return 0;
    }
}
