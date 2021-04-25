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
import java.util.Random;

public class MainMenu extends MenuScene {
    // menuscene function
    public MainMenu(Main game) {
        super(game);
        background = new Texture("sprites/menu/main_bg.png");
        xSpeed = 0;
    }

    public void show() {
        super.show();
        new MenuCursor(new Vector2(420, 540), Main.controllers[0], game);

        new Button(new Vector2(420, 540), game, new Texture[] { new Texture("sprites/menu/fyte.png"), new Texture("sprites/menu/fyte_selected.png") }) {
            public void click() {
                new ScreenWipe(new CharacterSelect(this.game), game);
                for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
            }};

        new Button(new Vector2(1520, 304), game, new Texture[] { new Texture("sprites/menu/settings.png"), new Texture("sprites/menu/settings_selected.png") }) {
            public void click() {
                new ScreenWipe(new SettingsMenu(this.game), game);
                for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
            }};

        new Button(new Vector2(968, 540), game, new Texture[] { new Texture("sprites/menu/fawful.png"), new Texture("sprites/menu/fawful_selected.png") }) {
            public void click() {
                new ScreenWipe(new SaveSelect(this.game), game);
                for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
            }};

        new Button(new Vector2(1520, 707), game, new Texture[] { new Texture("sprites/menu/vault.png"), new Texture("sprites/menu/vault_selected.png") }) {
            public void click() {
//                new ScreenWipe(new SettingsMenu(this.game), game);
//                for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
            }};

        if (game.music == null) {
            Random rand = new Random();
            int i = rand.nextInt(100);
            System.out.println(i);

            if (i == 2) {
                game.music = game.newSong("never gonna give you up");
            } else {
                game.music = game.newSong("menu");
            }
            game.music.setVolume(Main.musicVolume / 10f);
            game.music.play();
        }
    }
}
