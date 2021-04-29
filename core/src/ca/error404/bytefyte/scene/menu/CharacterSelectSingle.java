package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.LoadBossRoom;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class CharacterSelectSingle extends MenuScene {

    private boolean[] charsSelected;

    private Button startButton;

    public static String[] characters = {null, null, null, null};

    boolean keyboardUsed = false;

    // menuscene function
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

    public void show() {
        Main.cursors.clear();
        Main.players.clear();
        super.show();

        new MenuCursor(new Vector2(420, 540), Main.controllers[0], game);

        new Button(new Vector2(1510, 430), game, new Texture[] { new Texture("sprites/menu/characters/masterchief.png"), new Texture("sprites/menu/characters/masterchief_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "";
                CharacterSelect.characters[cursor.getID()] = "masterchief";

                new ScreenWipe(new LoadBossRoom("dimble wood/Dimble Wood Boss", "dimble wood/Dimble Wood Tileset", "dimble wood/Dimble Wood Boss_background", this.game, new Vector2(0, 0), "boss theme"), game);
            }
        };

        new Button(new Vector2(365, 840), game, new Texture[] { new Texture("sprites/menu/characters/shyguy.png"), new Texture("sprites/menu/characters/shyguy_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "";
                CharacterSelect.characters[cursor.getID()] = "shyguy";

                new ScreenWipe(new LoadBossRoom("dimble wood/Dimble Wood Boss", "dimble wood/Dimble Wood Tileset", "dimble wood/Dimble Wood Boss_background", this.game, new Vector2(0, 0), "boss theme"), game);
            }
        };

        new Button(new Vector2(929, 840), game, new Texture[] { new Texture("sprites/menu/characters/kirby.png"), new Texture("sprites/menu/characters/kirby_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "";
                CharacterSelect.characters[cursor.getID()] = "kirby";

                new ScreenWipe(new LoadBossRoom("dimble wood/Dimble Wood Boss", "dimble wood/Dimble Wood Tileset", "dimble wood/Dimble Wood Boss_background", this.game, new Vector2(0, 0), "boss theme"), game);
            }
        };

        new Button(new Vector2(365, 430), game, new Texture[] { new Texture("sprites/menu/characters/madeline.png"), new Texture("sprites/menu/characters/madeline_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "";
                CharacterSelect.characters[cursor.getID()] = "madeline";

                new ScreenWipe(new LoadBossRoom("dimble wood/Dimble Wood Boss", "dimble wood/Dimble Wood Tileset", "dimble wood/Dimble Wood Boss_background", this.game, new Vector2(0, 0), "boss theme"), game);
            }
        };

        new Button(new Vector2(1510, 840), game, new Texture[] { new Texture("sprites/menu/characters/mal.png"), new Texture("sprites/menu/characters/mal_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "";
                CharacterSelect.characters[cursor.getID()] = "marioluigi";

                new ScreenWipe(new LoadBossRoom("dimble wood/Dimble Wood Boss", "dimble wood/Dimble Wood Tileset", "dimble wood/Dimble Wood Boss_background", this.game, new Vector2(0, 0), "boss theme"), game);
            }
        };

        new Button(new Vector2(929, 430), game, new Texture[] { new Texture("sprites/menu/characters/sans.png"), new Texture("sprites/menu/characters/sans_selected.png") }) {
            public void click() {
                CharacterSelect.characters[cursor.getID()] = "";
                CharacterSelect.characters[cursor.getID()] = "sans";

                new ScreenWipe(new LoadBossRoom("dimble wood/Dimble Wood Boss", "dimble wood/Dimble Wood Tileset", "dimble wood/Dimble Wood Boss_background", this.game, new Vector2(0, 0), "boss theme"), game);
            }
        };

        new Button(new Vector2(200, 100), game, "Back") {
            public void click() {
                new ScreenWipe(new SaveSelect(game), game);
                for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
            }
        };
    }

    private boolean checkChars() {
        check();
        for (int i = 0; i < Main.cursors.size(); i++) {
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
            startButton = new Button(new Vector2(960, 100), game, "Begin") {
                public void click() {
                    for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
                }
            };
        }
    }

    public void render(float delta) {
        super.render(delta);
    }
}

