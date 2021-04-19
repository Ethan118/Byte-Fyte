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
    Texture charWall = new Texture("sprites/menu/charwall.png");

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

        new Button(new Vector2(1510, 430), game, new Texture[]{ new Texture("sprites/menu/characters/masterchief.png"), new Texture("sprites/menu/characters/masterchief_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "masterchief";
                if (checkChars()) {
                    createButton();
                }
            }
        };

        new Button(new Vector2(365, 840), game, new Texture[]{ new Texture("sprites/menu/characters/shyguy.png"), new Texture("sprites/menu/characters/shyguy_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "shyguy";
                if (checkChars()) {
                    createButton();
                }
            }
        };

        new Button(new Vector2(929, 840), game, new Texture[]{ new Texture("sprites/menu/characters/kirby.png"), new Texture("sprites/menu/characters/kirby_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "kirby";
                if (checkChars()) {
                    createButton();
                }
            }
        };

        new Button(new Vector2(365, 430), game, new Texture[]{ new Texture("sprites/menu/characters/madeline.png"), new Texture("sprites/menu/characters/madeline_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "madeline";
                if (checkChars()) {
                    createButton();
                }
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

    public void render(float delta) {
        super.render(delta);
    }
}

