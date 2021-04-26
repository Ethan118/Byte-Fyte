package ca.error404.bytefyte.scene.menu;


import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.tools.CutscenePlayer;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class MainMenu extends MenuScene {
    CutscenePlayer videoPlayer = new CutscenePlayer("menu watching");
    float timer;

    // menuscene function
    public MainMenu(Main game) {
        super(game);
        if (Main.bill) {
            background = new Texture("sprites/menu/bill_bg.png");
            xSpeed = -30;
            ySpeed *= -1;
        } else {
            background = new Texture("sprites/menu/main_bg.png");
            xSpeed = 0;
        }
    }

    public void show() {
        super.show();
        new MenuCursor(new Vector2(420, 540), Main.controllers[0], game);

        if (Main.bill) {
            new Button(new Vector2(420, 540), game, new Texture[]{new Texture("sprites/menu/fyte_bill.png"), new Texture("sprites/menu/fyte_bill_selected.png")}) {
                public void click() {
//                    new ScreenWipe(new CharacterSelect(this.game), game);
//                    for (MenuCursor cursor : Main.cursors) {
//                        cursor.canMove = false;
//                    }
                }
            };

            new Button(new Vector2(1520, 304), game, new Texture[]{new Texture("sprites/menu/settings.png"), new Texture("sprites/menu/settings_selected.png")}) {
                public void click() {
                    new ScreenWipe(new SettingsMenu(this.game), game);
                    for (MenuCursor cursor : Main.cursors) {
                        cursor.canMove = false;
                    }
                }
            };

            new Button(new Vector2(968, 540), game, new Texture[]{new Texture("sprites/menu/bill.png"), new Texture("sprites/menu/bill_selected.png")}) {
                public void click() {
                    new ScreenWipe(new SaveSelect(this.game), game);
                    for (MenuCursor cursor : Main.cursors) {
                        cursor.canMove = false;
                    }
                }
            };

            new Button(new Vector2(1520, 707), game, new Texture[]{new Texture("sprites/menu/vault.png"), new Texture("sprites/menu/vault_selected.png")}) {
                public void click() {
//                    new ScreenWipe(new SettingsMenu(this.game), game);
//                    for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
                }
            };
        } else {
            new Button(new Vector2(420, 540), game, new Texture[]{new Texture("sprites/menu/fyte.png"), new Texture("sprites/menu/fyte_selected.png")}) {
                public void click() {
                    new ScreenWipe(new CharacterSelect(this.game), game);
                    for (MenuCursor cursor : Main.cursors) {
                        cursor.canMove = false;
                    }
                }
            };

            new Button(new Vector2(1520, 304), game, new Texture[]{new Texture("sprites/menu/settings.png"), new Texture("sprites/menu/settings_selected.png")}) {
                public void click() {
                    new ScreenWipe(new SettingsMenu(this.game), game);
                    for (MenuCursor cursor : Main.cursors) {
                        cursor.canMove = false;
                    }
                }
            };

            new Button(new Vector2(968, 540), game, new Texture[]{new Texture("sprites/menu/fawful.png"), new Texture("sprites/menu/fawful_selected.png")}) {
                public void click() {
                    new ScreenWipe(new SaveSelect(this.game), game);
                    for (MenuCursor cursor : Main.cursors) {
                        cursor.canMove = false;
                    }
                }
            };

            new Button(new Vector2(1520, 707), game, new Texture[]{new Texture("sprites/menu/vault.png"), new Texture("sprites/menu/vault_selected.png")}) {
                public void click() {
                new ScreenWipe(new SettingsMenu(this.game), game);
                for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
                }
            };
        }

        if (game.music == null) {
            Random rand = new Random();
            int i = rand.nextInt(100);

            if (Main.bill) {
                game.music = game.newSong("menu weird");
                videoPlayer.play();
                timer = 2f;
            } else {
                if (i == 2) {
                    game.music = game.newSong("never gonna give you up");
                } else {
                    game.music = game.newSong("menu");
                }
                game.music.setVolume(Main.musicVolume / 10f);
            }
            game.music.play();
        }
    }

    public void render(float delta) {
        if (videoPlayer.isPlaying() || timer > 0) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            game.batch.begin();
            videoPlayer.draw(game.batch);
            game.batch.end();
            if (!videoPlayer.isPlaying()) {
                timer -= delta;
            }
        } else {
            super.render(delta);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            new ScreenWipe(new TitleScreen(this.game), game);
            for (MenuCursor cursor : Main.cursors) {
                cursor.canMove = false;
            }
        }
    }
}
