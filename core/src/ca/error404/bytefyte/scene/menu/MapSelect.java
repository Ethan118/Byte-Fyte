package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.LoadBattleMap;
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
    private String[] characters;

    // menuscene function
    public MapSelect(Main game, final String[] characters) {
        super(game);
        background = new Texture("sprites/menu/main_bg.png");
        this.characters = characters;

        new MenuCursor(new Vector2(0, 0), null, game);

        new Button(new Vector2(300, 400), new Vector2(150, 50), game) { public void click() {
            game.setScreen(new LoadBattleMap("Halberd", game, new Vector2(0, 0), "kirby"));
        }};

        new Button(new Vector2(730, 250), new Vector2(150, 50), game){ public void click() {
            game.setScreen(new LoadBattleMap("Training Room", game, new Vector2(0, 0), null));
        }};

        new Button(new Vector2(1030, 250), new Vector2(150, 50), game){ public void click() {
            game.setScreen(new LoadBattleMap("Russia", game, new Vector2(0, 0), "russia"));
        }};
    }
}
