package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.Set;

public class CharacterSelect extends MenuScene {

    private boolean[] charsSelected = new boolean[4];

    private boolean transition = false;

    private Button startButton;

    public static String[] characters = {null, null, null, null};

    boolean keyboardUsed = false;
    // menuscene function
    public CharacterSelect(Main game) {
        super(game);
        background = new Texture("sprites/menu/main_bg.png");
    }

    public void show() {
        super.show();
        for (int i = 0; i < 4; i ++) {
            if (Main.controllers[i] != null) {
                new MenuCursor(new Vector2(0, 0), Main.controllers[i], game);
            } else if (!keyboardUsed){
                new MenuCursor(new Vector2(0, 0), null, game);
                keyboardUsed = true;
            }
        }

        new Button(new Vector2(200, 600), game, "Master Chief") {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "masterchief";
                if (checkChars()) {
                    createButton();
                }
            }
        };

        new Button(new Vector2(400, 400), game, "Shy Guy") {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "shyguy";
                if (checkChars()) {
                    createButton();
                }
            }
        };

        new Button(new Vector2(600, 800), game, "Kirby") {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "kirby";
                if (checkChars()) {
                    createButton();
                }
            }
        };

        new Button(new Vector2(600, 1000), game, "Madeline") {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "madeline";
            }
        };

        new Button(new Vector2(600, 200), game, "Maps") {
            public void click() {
                new ScreenWipe(new MapSelect(game), game);
            }
        };

    }

    private boolean checkChars() {
        check();
        for (int i = 0; i < Main.cursors.size(); i++) {
            System.out.println(charsSelected[i]);
            if (!charsSelected[i]) {
                return false;
            }
        }
        return true;
    }

    public void check() {
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] != null) {
                charsSelected[i] = true;
            } else {
                charsSelected[i] = false;
            }
        }
    }

    private void createButton() {
        if (checkChars()) {
            startButton = new Button(new Vector2(600, 200), game, "Maps") {
                public void click() {
                    if (!transition) {
                        new ScreenWipe(new MapSelect(game), game);
                        transition = true;
                    }
                }
            };
        }
    }
}

