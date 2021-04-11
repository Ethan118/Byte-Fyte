package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MenuCursor {
    public static Texture selectedSprite = new Texture("sprites/cursor_selected.png");
    public static Texture sprite = new Texture("sprites/cursor.png");
    public Vector2 cursorPos;
    public Controller controller;
    private Main game;
    private Vector2 max;
    public Rectangle cursorRect;
    private Vector2 size;
    private int speed = 700;

    public MenuCursor(Vector2 cursorPos, Controller controller, Main game, Vector2 max, Vector2 size, Rectangle cursorRect) {
        Main.cursors.add(this);
        this.cursorPos = cursorPos;
        this.controller = controller;
        this.game = game;
        this.max = max;
        this.size = size;
        this.cursorRect = cursorRect;

        cursorRect.width = size.x;
        cursorRect.height = size.y;
    }

    public void update(float deltaTime) {
        cursorRect.set(cursorPos.x + 30, cursorPos.y + 80, 1, 1);

        if (controller != null) {
            if (!(controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) >= -Main.deadZone && controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) <= Main.deadZone)) {
                if (cursorPos.x > 0 && controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) < -Main.deadZone) {
                    cursorPos.x += (controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) * speed) * deltaTime;
                }
                if (cursorPos.x < max.x && controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) > Main.deadZone) {
                    cursorPos.x += (controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) * speed) * deltaTime;
                }
            }

            if (!(controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) >= -Main.deadZone && controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) <= Main.deadZone)) {
                if (cursorPos.y > 0 && controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) > -Main.deadZone) {
                    cursorPos.y -= (controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) * speed) * deltaTime;
                }
                if (cursorPos.y < max.y && controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) < Main.deadZone) {
                    cursorPos.y -= (controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) * speed) * deltaTime;
                }
            }
        } else {
            if (Gdx.input.isKeyPressed(Keys.MENU_RIGHT) && cursorPos.x < max.x) {
                cursorPos.x += speed * deltaTime;
            }

            if (Gdx.input.isKeyPressed(Keys.MENU_LEFT) && cursorPos.x > 0) {
                cursorPos.x -= speed * deltaTime;
            }

            if (Gdx.input.isKeyPressed(Keys.MENU_UP) && cursorPos.y < max.y) {
                cursorPos.y += speed * deltaTime;
            }

            if (Gdx.input.isKeyPressed(Keys.MENU_DOWN) && cursorPos.y > 0) {
                cursorPos.y -= speed * deltaTime;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        boolean over = false;
        for (Button button : game.buttons) {
            if (button.isCursorOver(this)) {
                over = true;
                break;
            }
        }

        if (over) {
            batch.draw(selectedSprite, cursorPos.x, cursorPos.y);
        } else {
            batch.draw(sprite, cursorPos.x, cursorPos.y);
        }
    }

    public Vector2 getCursorPos() {
        return cursorPos;
    }
}
