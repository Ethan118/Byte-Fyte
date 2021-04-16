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

        new MenuCursor(new Vector2(0, 0), Main.controllers[0], game);

        new Button(new Vector2(300, 400), game, "Halberd") { public void click() {
            new ScreenWipe(new LoadBattleMap("Halberd", game, new Vector2(-350, 0), "kirby"), game);
        }};

        new Button(new Vector2(730, 400), game, "Training Room"){ public void click() {
            new ScreenWipe(new LoadBattleMap("Training Room", game, new Vector2(0, 0), null), game);
        }};

        new Button(new Vector2(1030, 400), game, "Russia"){ public void click() {
            new ScreenWipe(new LoadBattleMap("Russia", game, new Vector2(0, 0), "russia"), game);
        }};

        new Button(new Vector2(1330, 400), game, "Forsaken City"){ public void click() {
            new ScreenWipe(new LoadBattleMap("Forsaken City", game, new Vector2(0.5f, 0), "celeste"), game);
        }};
    }
}
