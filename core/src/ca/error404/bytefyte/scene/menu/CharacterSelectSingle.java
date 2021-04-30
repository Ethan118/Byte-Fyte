package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.LoadBossRoom;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/*
 * Pre: game instance
 * Post: handles the character select screen for one player
 * */
public class CharacterSelectSingle extends MenuScene {

    // declaring variables
    private boolean[] charsSelected;
    private Button startButton;
    public static String[] characters = {null, null, null, null};
    boolean keyboardUsed = false;

    /*constructor
     * Pre: game instance
     * Post: handles the select screen and characters
     * */
    public CharacterSelectSingle(Main game) {
        super(game);
        xSpeed = 0;
        if (Main.bill) {
            background = new Texture("sprites/menu/bill_bg.png");
        } else {
            background = new Texture("sprites/menu/save select_bg.png");
        }
        CharacterSelect.characters = new String[] {null, null, null, null};
        charsSelected = new boolean[] {false, false, false, false};
    }

    /*
     * Pre: game instance
     * Post: handles the images that appear on screen
     * */
    public void show() {
        Main.cursors.clear();
        Main.players.clear();
        super.show();

        new MenuCursor(new Vector2(420, 540), Main.controllers[0], game);

        // master chief button
        new Button(new Vector2(1510, 430), game, new Texture[] { new Texture("sprites/menu/characters/masterchief.png"), new Texture("sprites/menu/characters/masterchief_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "";
                CharacterSelect.characters[cursor.getID()] = "masterchief";

                // loads the boss stage
                new ScreenWipe(new LoadBossRoom("dimble wood/Dimble Wood Boss", "dimble wood/Dimble Wood Tileset", "dimble wood/Dimble Wood Boss_background", this.game, new Vector2(0, 0), "boss theme"), game);
            }
        };

        // shyguy button
        new Button(new Vector2(365, 840), game, new Texture[] { new Texture("sprites/menu/characters/shyguy.png"), new Texture("sprites/menu/characters/shyguy_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "";
                CharacterSelect.characters[cursor.getID()] = "shyguy";

                // loads the boss stage
                new ScreenWipe(new LoadBossRoom("dimble wood/Dimble Wood Boss", "dimble wood/Dimble Wood Tileset", "dimble wood/Dimble Wood Boss_background", this.game, new Vector2(0, 0), "boss theme"), game);
            }
        };

        // kirby button
        new Button(new Vector2(929, 840), game, new Texture[] { new Texture("sprites/menu/characters/kirby.png"), new Texture("sprites/menu/characters/kirby_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "";
                CharacterSelect.characters[cursor.getID()] = "kirby";

                // loads the boss stage
                new ScreenWipe(new LoadBossRoom("dimble wood/Dimble Wood Boss", "dimble wood/Dimble Wood Tileset", "dimble wood/Dimble Wood Boss_background", this.game, new Vector2(0, 0), "boss theme"), game);
            }
        };

        // madeline button
        new Button(new Vector2(365, 430), game, new Texture[] { new Texture("sprites/menu/characters/madeline.png"), new Texture("sprites/menu/characters/madeline_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "";
                CharacterSelect.characters[cursor.getID()] = "madeline";

                // loads the boss stage
                new ScreenWipe(new LoadBossRoom("dimble wood/Dimble Wood Boss", "dimble wood/Dimble Wood Tileset", "dimble wood/Dimble Wood Boss_background", this.game, new Vector2(0, 0), "boss theme"), game);
            }
        };

        // mario and luigi button
        new Button(new Vector2(1510, 840), game, new Texture[] { new Texture("sprites/menu/characters/mal.png"), new Texture("sprites/menu/characters/mal_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "";
                CharacterSelect.characters[cursor.getID()] = "marioluigi";

                // loads the boss screen
                new ScreenWipe(new LoadBossRoom("dimble wood/Dimble Wood Boss", "dimble wood/Dimble Wood Tileset", "dimble wood/Dimble Wood Boss_background", this.game, new Vector2(0, 0), "boss theme"), game);
            }
        };

        // sans button
        new Button(new Vector2(929, 430), game, new Texture[] { new Texture("sprites/menu/characters/sans.png"), new Texture("sprites/menu/characters/sans_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "";
                CharacterSelect.characters[cursor.getID()] = "sans";

                //loads the boss stage
                new ScreenWipe(new LoadBossRoom("dimble wood/Dimble Wood Boss", "dimble wood/Dimble Wood Tileset", "dimble wood/Dimble Wood Boss_background", this.game, new Vector2(0, 0), "boss theme"), game);
            }
        };

        // back button
        new Button(new Vector2(200, 100), game, "Back") {
            public void click() {
                new ScreenWipe(new SaveSelect(game), game);
                for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
            }
        };
    }

    /*
    * Pre: None
    * Post: Checks if all characters have been selected
    * */
    private boolean checkChars() {
        check();
        for (int i = 0; i < Main.cursors.size(); i++) {
            if (!charsSelected[i]) {
                return false;
            }
        }
        return true;
    }

    /*
     * Pre: None
     * Post: Checks if a character has been selected
     * */
    public void check() {
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] != null) {
                charsSelected[i] = true;
            } else {
                charsSelected[i] = false;
            }
        }
    }


    /*
    * Pre: None
    * Post: Creates menu button
    * */
    private void createButton() {
        if (checkChars()) {

            // start button creation if there are characters picked
            startButton = new Button(new Vector2(960, 100), game, "Begin") {
                public void click() {
                    for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
                }
            };
        }
    }

    /*
    * Pre: None
    * Post: Calls render method
    * */
    public void render(float delta) {
        super.render(delta);
    }
}

