package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.tools.CutscenePlayer;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Vault extends MenuScene {

    CutscenePlayer tut = new CutscenePlayer("how to play");
    float timer = 1f;
    boolean playTut = false;

    public Vault(Main game) {
        super(game);
        ySpeed = 50;
        xSpeed = 100;

        if (game.getScreen().getClass() == Vault.class) {
            playTut = true;
        } else {
            timer = 0;
        }

        if (Main.bill) {
            background = new Texture("sprites/menu/bill_bg.png");
        } else {
            background = new Texture("sprites/menu/vault_bg.png");
        }
    }

    @Override
    public void show() {
        if (playTut) {
            if (game.music != null) {
                game.music.stop();
                game.music = null;
                Main.bill = false;
            }
            tut.play();
        }
        super.show();
        new MenuCursor(new Vector2(420, 540), Main.controllers[0], game);

        new Button(new Vector2(200, 100), game, "Back") {
            public void click() {
                new ScreenWipe(new MainMenu(game), game);
                for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
            }
        };

        new Button(new Vector2(968, 540), game, new Texture[]{new Texture("sprites/menu/tutorial.png"), new Texture("sprites/menu/tutorial_selected.png")}) {
            public void click() {
                new ScreenWipe(new Vault(this.game), game);
                for (MenuCursor cursor : Main.cursors) {
                    cursor.canMove = false;
                }
            }
        };
    }

    public void render(float delta) {
        if (tut.isPlaying() || timer > 0) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            game.batch.begin();
            tut.draw(game.batch);
            game.batch.end();
            if (!tut.isPlaying()) {
                timer -= delta;
            }
        } else {
            if (game.music == null) {
                game.music = game.newSong("menu");
                game.music.play();
            }
            super.render(delta);
        }
    }
}
