package ca.error404.bytefyte.scene.menu;


import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.LoadBattleMap;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;

public class MainMenu extends MenuScene {
    private Vector2 cursorPos = new Vector2(0, 0);

    // menuscene function
    public MainMenu(Main game) {
        super(game);
        background = new Texture("sprites/menu/main_bg.png");
    }

    public void show() {
        super.show();
        new MenuCursor(new Vector2(0, 0), Main.controllers[0], game);

        new Button(new Vector2(960, 400), game, "Fyte!") {
            public void click() {
                new ScreenWipe(new CharacterSelect(this.game), game);
            }};

        new Button(new Vector2(960, 500), game, "Settings") {
            public void click() {
                new ScreenWipe(new SettingsMenu(this.game), game);
            }};

        if (game.music == null) {
            game.music = game.newSong("menu");
            game.music.setVolume(Main.musicVolume / 10f);
            game.music.play();
        }
    }
}
