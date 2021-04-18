package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class CharacterSelect extends MenuScene {

    public static String[] characters = {null, null, null, null};

    // menuscene function
    public CharacterSelect(Main game) {
        super(game);
        background = new Texture("sprites/menu/main_bg.png");
    }

    public void show() {
        super.show();
        new MenuCursor(new Vector2(0, 0), Main.controllers[0], game);
        new MenuCursor(new Vector2(0, 0), Main.controllers[1], game);
        new MenuCursor(new Vector2(0, 0), Main.controllers[2], game);
        new MenuCursor(new Vector2(0, 0), Main.controllers[3], game);

        new Button(new Vector2(200, 600), game, "Master Chief") {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "masterchief";
            }
        };

        new Button(new Vector2(400, 400), game, "Shy Guy") {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "shyguy";
            }
        };

        new Button(new Vector2(600, 800), game, "Kirby") {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "kirby";
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
}
