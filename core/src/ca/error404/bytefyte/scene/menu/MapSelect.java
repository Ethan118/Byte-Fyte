package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.LoadBattleMap;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MapSelect extends MenuScene {

    // menuscene function
    public MapSelect(Main game) {
        super(game);
    }

    public void show() {
        super.show();
        background = new Texture("sprites/menu/main_bg.png");

        new MenuCursor(new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2), Main.controllers[0], game);

        new Button(new Vector2(585, 870), game, new Texture[]{ new Texture("sprites/menu/maps/halberd.png"), new Texture("sprites/menu/maps/halberd_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Halberd", game, new Vector2(-350, 0), "kirby"), game);
        }};

        new Button(new Vector2(585, 540), game, new Texture[]{ new Texture("sprites/menu/maps/training.png"), new Texture("sprites/menu/maps/training_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Training Room", game, new Vector2(0, 0), "other"), game);
        }};

        new Button(new Vector2(1335, 540), game, new Texture[]{ new Texture("sprites/menu/maps/russia.png"), new Texture("sprites/menu/maps/russia_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Russia", game, new Vector2(0, 0), "russia"), game);
        }};

        new Button(new Vector2(960, 870), game, new Texture[]{ new Texture("sprites/menu/maps/celeste.png"), new Texture("sprites/menu/maps/celeste_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Forsaken City", game, new Vector2(0.5f, 0), "celeste"), game);
        }};

        new Button(new Vector2(960, 540), game, new Texture[]{ new Texture("sprites/menu/maps/bleck.png"), new Texture("sprites/menu/maps/bleck_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Castle Bleck", game, new Vector2(20, 0), "paper mario"), game);
        }};

        new Button(new Vector2(1335, 870), game, new Texture[]{ new Texture("sprites/menu/maps/fawful's castle.png"), new Texture("sprites/menu/maps/fawful's castle_selected.png") }) { public void click() {
            new ScreenWipe(new LoadBattleMap("Fawful's Castle", game, new Vector2(0, 0), "mario"), game);
        }};
    }
}
