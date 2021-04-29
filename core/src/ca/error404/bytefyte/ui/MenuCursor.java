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

// Class for virtual menu cursors
public class MenuCursor {

//    Instantiating variables
    public static Texture selectedSprite = new Texture("sprites/cursor_selected.png");
    public static Texture sprite = new Texture("sprites/cursor.png");
    public Vector2 pos;
    public Controller controller;
    private Main game;
    private Vector2 max = new Vector2(1920, 1080);
    public Rectangle rect = new Rectangle();
    private int speed = 500;
    public boolean canMove = true;

    /*
    * Constructor
    * Pre: Inputs for parameters
    * Post: A new virtual cursor
    * */
    public MenuCursor(Vector2 cursorPos, Controller controller, Main game) {

//        Setting variables and adding this instance of the menu cursor to the cursors list
        Main.cursors.add(this);
        this.pos = cursorPos;
        this.controller = controller;
        this.game = game;

        max.x -= sprite.getWidth();
        max.y -= sprite.getHeight();
    }

    /*
    * Pre: Delta Time
    * Post: Updates cursor
    * */
    public void update(float deltaTime) {

//        If the cursor can move
        if (canMove) {

//            If the user has a controller
            if (controller != null) {
//                Detects horizontal direction of stick movements and moves cursor accordingly
                if (!(controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) >= -Main.deadZone && controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) <= Main.deadZone)) {
                    if (pos.x > 0 && controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) < -Main.deadZone) {
                        pos.x += (controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) * speed) * deltaTime;
                    }
                    if (pos.x < max.x && controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) > Main.deadZone) {
                        pos.x += (controller.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) * speed) * deltaTime;
                    }
                }
//                Detects vertical direction of stick movements and moves cursor accordingly

                if (!(controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) >= -Main.deadZone && controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) <= Main.deadZone)) {
                    if (pos.y > 0 && controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) > -Main.deadZone) {
                        pos.y -= (controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) * speed) * deltaTime;
                    }
                    if (pos.y < max.y && controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) < Main.deadZone) {
                        pos.y -= (controller.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) * speed) * deltaTime;
                    }
                }

//            If the user only has a keyboard
            } else {

//                Cursor moves based on directional input
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

//            Sets the collider
            rect.set(pos.x + 30, pos.y + 80, 1, 1);
        }
    }

    /*
    * Pre: A sprite batch
    * Post: Draws the cursor
    * */
    public void draw(SpriteBatch batch) {

//        Sets the variable to detect button collision to false
        boolean over = false;

//        For loop which detects if a button is under the cursor, and makes the variable true if it is
        for (Button button : game.buttons) {
            if (button.isCursorOver(this)) {
                over = true;
                break;
            }
        }

//        If it is over, draw the selected sprite
        if (over) {
            batch.draw(selectedSprite, pos.x, pos.y);

//        Otherwise, draw the regular sprite
        } else {
            batch.draw(sprite, pos.x, pos.y);
        }
    }

    /*
    * Pre: None
    * Post: Returns the ID number of the cursor
    * */
    public int getID() {

//        Cycles through the cursors list and returns this instance's position
        for (int i=0; i < game.cursors.size(); i++) {
            if (game.cursors.get(i) == this) {
                return i;
            }
        }

        return 0;
    }

    /*
    * Pre: None
    * Post: Returns the cursor's position
    * */
    public Vector2 getCursorPos() {
        return pos;
    }
}
