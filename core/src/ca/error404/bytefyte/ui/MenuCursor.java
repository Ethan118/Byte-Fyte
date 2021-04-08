package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.scene.LoadTMap;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class MenuCursor {
    public Texture cursorImage;
    public Vector2 cursorPos;
    public Controller controller;
    private Main game;
    private Screen screen;
    private Vector2 max;

    public MenuCursor(Vector2 cursorPos, Controller controller, Texture cursorImage, Main game, Vector2 max) {
        this.cursorPos = cursorPos;
        this.controller = controller;
        this.cursorImage = cursorImage;
        this.game = game;
        this.max = max;
    }

    public void update(float deltaTime) {
        System.out.println(cursorPos);

        if (Main.controllers.size >= 1) {
            if (!(controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) >= -Main.deadZone && controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) <= Main.deadZone)) {
                if (cursorPos.x > 0 && controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) < -Main.deadZone) {
                    cursorPos.x += (controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) * 10);
                }
                if (cursorPos.x < max.x && controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) > Main.deadZone) {
                    cursorPos.x += (controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) * 10);
                }
            }

            if (!(controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) >= -Main.deadZone && controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) <= Main.deadZone)) {
                if (cursorPos.y > 0 && controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) > -Main.deadZone) {
                    cursorPos.y -= (controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) * 10);
                }
                if (cursorPos.y < max.y && controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) < Main.deadZone) {
                    cursorPos.y -= (controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) * 10);
                }
            }


            if (controller.getButton(ControllerButtons.A)) {
                if (cursorPos.x > 20 && cursorPos.x < 350) {
                    if (cursorPos.y > 20 && cursorPos.y < 350) {
                        game.setScreen(new LoadTMap("Halberd", game, new Vector2(-350, 0)));
                    }
                }
            }
        } else {
            if (Gdx.input.isKeyPressed(Keys.MENU_RIGHT) && cursorPos.x < max.x) {
                cursorPos.x += 10;
            }

            if (Gdx.input.isKeyPressed(Keys.MENU_LEFT) && cursorPos.x > 0) {
                cursorPos.x -= 10;
            }

            if (Gdx.input.isKeyPressed(Keys.MENU_UP) && cursorPos.y < max.y) {
                cursorPos.y += 10;
            }

            if (Gdx.input.isKeyPressed(Keys.MENU_DOWN) && cursorPos.y > 0) {
                cursorPos.y -= 10;
            }

            if (Gdx.input.isKeyJustPressed(Keys.MENU_SELECT)) {
                if (cursorPos.x > 20 && cursorPos.x < 350) {
                    if (cursorPos.y > 20 && cursorPos.y < 350) {
                        game.setScreen(new LoadTMap("Training Room", game, new Vector2(-350, 0)));
                    }
                }
            }
        }
    }
}
