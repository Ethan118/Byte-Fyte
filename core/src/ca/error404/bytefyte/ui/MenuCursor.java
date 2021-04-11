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
    public Vector2 pos;
    public Controller controller;
    private Main game;
    private Vector2 max = new Vector2(1920, 1080);
    public Rectangle rect = new Rectangle();
    private int speed = 500;

    public MenuCursor(Vector2 cursorPos, Controller controller, Main game) {
        Main.cursors.add(this);
        this.pos = cursorPos;
        this.controller = controller;
        this.game = game;
    }

    public void update(float deltaTime) {
        if (controller != null) {
            if (!(controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) >= -Main.deadZone && controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) <= Main.deadZone)) {
                if (pos.x > 0 && controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) < -Main.deadZone) {
                    pos.x += (controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) * speed) * deltaTime;
                }
                if (pos.x < max.x && controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) > Main.deadZone) {
                    pos.x += (controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) * speed) * deltaTime;
                }
            }

            if (!(controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) >= -Main.deadZone && controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) <= Main.deadZone)) {
                if (pos.y > 0 && controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) > -Main.deadZone) {
                    pos.y -= (controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) * speed) * deltaTime;
                }
                if (pos.y < max.y && controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) < Main.deadZone) {
                    pos.y -= (controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) * speed) * deltaTime;
                }
            }
        } else {
            if (Gdx.input.isKeyPressed(Keys.MENU_RIGHT) && pos.x < max.x) {
                pos.x += speed * deltaTime;
            }

            if (Gdx.input.isKeyPressed(Keys.MENU_LEFT) && pos.x > 0) {
                pos.x -= speed * deltaTime;
            }

            if (Gdx.input.isKeyPressed(Keys.MENU_UP) && pos.y < max.y) {
                pos.y += speed * deltaTime;
            }

            if (Gdx.input.isKeyPressed(Keys.MENU_DOWN) && pos.y > 0) {
                pos.y -= speed * deltaTime;
            }
        }

        rect.set(pos.x + 30, pos.y + 80, 1, 1);
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
            batch.draw(selectedSprite, pos.x, pos.y);
        } else {
            batch.draw(sprite, pos.x, pos.y);
        }
    }

    public int getID() {
        for (int i=0; i < game.cursors.size(); i++) {
            if (game.cursors.get(i) == this) {
                return i;
            }
        }

        return 0;
    }

    public Vector2 getCursorPos() {
        return pos;
    }
}
