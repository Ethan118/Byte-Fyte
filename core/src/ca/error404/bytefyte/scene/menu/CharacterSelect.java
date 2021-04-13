package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
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

        new MenuCursor(new Vector2(0, 0), null, game);

        new Button(new Vector2(200, 400), new Vector2(150, 50), game) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "masterchief";
            }
        };

        new Button(new Vector2(400, 400), new Vector2(150, 50), game) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "shyguy";
            }
        };

        new Button(new Vector2(600, 400), new Vector2(150, 50), game) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "kirby";
            }
        };

        new Button(new Vector2(600, 200), new Vector2(150, 50), game) {
            public void click() {
                game.setScreen(new MapSelect(this.game, CharacterSelect.characters));
            }
        };
    }
}
