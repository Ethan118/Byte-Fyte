package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/*
 * Pre: game instance
 * Post: handles the saveselect screen
 * */
public class SaveSelect extends MenuScene {

    /*constructor
     * Pre: game instance
     * Post: handles the variables and imports needed for the screen
     * */
    public SaveSelect(Main game) {
        super(game);
        ySpeed = 50;
        xSpeed = 100;

        if (Main.bill) {
            background = new Texture("sprites/menu/bill_bg.png");
        } else {
            background = new Texture("sprites/menu/save select_bg.png");
        }
    }

    @Override
    /*
     * Pre: game instance
     * Post: handles what is shown to the user
     * */
    public void show() {
        super.show();

        //creates the cursor
        new MenuCursor(new Vector2(420, 540), Main.controllers[0], game);

        //creates the back button
        new Button(new Vector2(200, 100), game, "Back") {
            public void click() {
                new ScreenWipe(new MainMenu(game), game);
                for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
            }
        };

        //creates the boss mode button
        new Button(new Vector2(968, 540), game, new Texture[]{new Texture("sprites/menu/petey.png"), new Texture("sprites/menu/petey_selected.png")}) {
            public void click() {
                new ScreenWipe(new CharacterSelectSingle(this.game), game);
                for (MenuCursor cursor : Main.cursors) {
                    cursor.canMove = false;
                }
            }
        };
    }
}
