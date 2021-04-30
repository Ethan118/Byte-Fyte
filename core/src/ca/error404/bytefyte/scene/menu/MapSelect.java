package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.BattleMap;
import ca.error404.bytefyte.scene.LoadBattleMap;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;


public class MapSelect extends MenuScene {

    /*
     * Pre: game instance
     * Post: handles the map select screen
     * */
    public MapSelect(Main game) {
        super(game);

        if (BattleMap.alive != null) {
            BattleMap.alive.clear();
        }
    }

    /*
    * Pre: None
    * Post: Initializes variables
    * */
    public void show() {
        super.show();

        //handles the background
        if (Main.bill) {
            background = new Texture("sprites/menu/bill_bg.png");
        } else {
            background = new Texture("sprites/menu/char_bg.png");
        }
        xSpeed = 0;

        new MenuCursor(new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2), Main.controllers[0], game);

        //creates the button for the halberd stage
        new Button(new Vector2(585, 870), game, new Texture[]{ new Texture("sprites/menu/maps/halberd.png"), new Texture("sprites/menu/maps/halberd_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Halberd", game, new Vector2(-350, 0), "kirby"), game);
            for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
        }};
        //creates the button for the training stage
        new Button(new Vector2(585, 540), game, new Texture[]{ new Texture("sprites/menu/maps/training.png"), new Texture("sprites/menu/maps/training_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Training Room", game, new Vector2(0, 0), "other"), game);
            for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
        }};

        //creates the button for the Russia stage
        new Button(new Vector2(1335, 540), game, new Texture[]{ new Texture("sprites/menu/maps/russia.png"), new Texture("sprites/menu/maps/russia_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Russia", game, new Vector2(0, 0), "russia"), game);
            for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
        }};

        //creates the button for the forsaken city stage
        new Button(new Vector2(960, 870), game, new Texture[]{ new Texture("sprites/menu/maps/celeste.png"), new Texture("sprites/menu/maps/celeste_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Forsaken City", game, new Vector2(0.5f, 0), "celeste"), game);
            for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
        }};

        //creates the button for the castle black stage
        new Button(new Vector2(960, 540), game, new Texture[]{ new Texture("sprites/menu/maps/bleck.png"), new Texture("sprites/menu/maps/bleck_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Castle Bleck", game, new Vector2(20, 0), "paper mario"), game);
            for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
        }};

        //creates the button for the Fawful's castle stage
        new Button(new Vector2(1335, 870), game, new Texture[]{ new Texture("sprites/menu/maps/fawful's castle.png"), new Texture("sprites/menu/maps/fawful's castle_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Fawful's Castle", game, new Vector2(0, 0), "mal"), game);
            for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
        }};

        //creates the button for the flowchart stage
        new Button(new Vector2(960, 195), game, new Texture[]{ new Texture("sprites/menu/maps/flowchart.png"), new Texture("sprites/menu/maps/flowchart_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Flowchart", game, new Vector2(50, -3000), "other"), game);
            for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
        }};

        //creates the back button and sends you to the character select screen
        new Button(new Vector2(200, 100), game, "Back") {
            public void click() {
                CharacterSelect.characters = new String[] {null, null, null, null};
                new ScreenWipe(new CharacterSelect(game), game);
                for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
            }
        };
    }
}
