package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

// Button class to create menus
public class Button {

//    Instantiating variables
    public Rectangle buttonRect;
    public Vector2 pos;
    public Texture texture;
    public Texture selectTexture;
    public String string;
    public Main game;
    public MenuCursor cursor;

    public Color unselectedColor = Color.WHITE;
    public Color selectedColor = Color.YELLOW;
    public GlyphLayout layout = new GlyphLayout();

    private boolean prevSelect = false;
    private Sound clickSound;

    /*
    * Constructor 1
    * Pre: Inputs for parameters
    * Post: A new word button
    * */

    public Button(Vector2 pos, Main game, String string) {

//        Setting variables
        this.buttonRect = new Rectangle();
        this.pos = pos;
        this.string = string;
        this.texture = null;
        this.game = game;

//        Adds this button to the buttons list
        Main.buttons.add(this);

//        Sets text and position
        layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);
        this.buttonRect.set(pos.x - (layout.width / 2), pos.y - layout.height, layout.width, layout.height);
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound effects/menuChoose.wav"));
    }

    /*
     * Constructor 1
     * Pre: Inputs for parameters
     * Post: A new image button
     * */
    public Button(Vector2 pos, Main game, Texture[] texture) {
//        Setting variables
        this.buttonRect = new Rectangle();
        this.pos = pos;
        this.string = null;
        this.texture = texture[0];
        if (texture[1] != null) {
            this.selectTexture = texture[1];
        }
        this.game = game;

//        Adding the button to the list of buttons
        Main.buttons.add(this);

//        Setting position
        this.buttonRect.set(pos.x - (texture[0].getWidth() / 2f), pos.y - (texture[0].getHeight() / 2f), texture[0].getWidth(), texture[0].getHeight());
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound effects/menuChoose.wav"));
    }

    /*
    * Pre: Sprite batch
    * Post: Draws the button
    * */
    public void draw(SpriteBatch batch) {
//        If there is no image (word button)
        if (texture == null) {

//            If any menu cursor...
            for (MenuCursor cursor : Main.cursors) {

//                 Is over the button, change the colour to the "selected" version
                if (isCursorOver(cursor)) {
                    layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);
                    break;

//                 Is not over the button, change the colour to the "unselected" version
                } else {
                    layout.setText(Main.menuFont, string, unselectedColor, 0, Align.center, false);
                }
            }

//            Draw the text
            Main.menuFont.draw(batch, layout, pos.x, pos.y);

//            If there is an image
        } else {

//            If there is no cursors
            if (Main.cursors.size() == 0) {

//                If previously selected draw regular texture
                if (prevSelect) {
                    batch.draw(texture, buttonRect.x, buttonRect.y);

//                Otherwise, draw the selected texture
                } else {
                    batch.draw(selectTexture, buttonRect.x, buttonRect.y);
                }

//            If there is a cursor
            } else {

//                Cycle through all cursors
                for (MenuCursor cursor : Main.cursors) {

//                    If the cursor is over the button, draw the selected texture
                    if (isCursorOver(cursor)) {
                        batch.draw(selectTexture, buttonRect.x, buttonRect.y);
                        prevSelect = false;
                        break;

//                    Otherwise, draw the regular texture, previously selected is now true
                    } else {
                        batch.draw(texture, buttonRect.x, buttonRect.y);
                        prevSelect = true;
                    }
                }
            }
        }
    }

    /*
    * Pre: A cursor
    * Post: A boolean determining if the cursor is over the button
    * */
    public boolean isCursorOver(MenuCursor cursor) {
        return cursor.rect.overlaps(buttonRect);
    }

    /*
    * Pre: A list of menu cursors
    * Post: Determines if the button is clicked
    * */
    public boolean isClicked() {

//        Cycles through all menu cursors
        for (MenuCursor cursor : Main.cursors) {

//            If the cursor has a controller and the select button is clicked, return true
            if (cursor.controller != null) {
                if (Main.contains(Main.recentButtons.get(cursor.controller), ControllerButtons.A)) {
                    if (isCursorOver(cursor)) {
                        this.cursor = cursor;
                        return true;
                    }
                }

//            Otherwise, if the cursor is using a keyboard and the select button is pressed, return true
            } else {
                if (Gdx.input.isKeyJustPressed(Keys.MENU_SELECT)) {
                    if (isCursorOver(cursor)) {
                        this.cursor = cursor;
                        return true;
                    }
                }
            }
        }
//        If none of these events occur, return false
        return false;
    }

    /*
    * Pre: None
    * Post: Checks if the button was clicked
    * */
    public void update() {

//        If the button is clicked, call the click method and play sound effect
        if (isClicked()) {
            clickSound.play(Main.sfxVolume / 10f);
            click();
        }
    }

    /*
    * Pre: None
    * Post: Perform the desired function (will be overwritten)
    * */
    public void click() {
        System.out.println("Click");
    }
}
