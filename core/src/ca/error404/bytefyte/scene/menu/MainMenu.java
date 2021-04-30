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

/*
 * Pre: game instance
 * Post: handles the main menu screen
 * */
public class MainMenu extends MenuScene {
    CutscenePlayer videoPlayer = new CutscenePlayer("menu watching");
    float timer;

    /*constructor
     * Pre: game instance
     * Post: handles the main menu screen's variables and background
     * */
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

    /*
     * Pre: game instance
     * Post: handles what the menu shows to the user
     * */
    public void show() {
        super.show();

        if (Main.bill) {

            // creates cursor
            new MenuCursor(new Vector2(968, 540), Main.controllers[0], game);

            //creates a button to go to character select for the fyte button
            new Button(new Vector2(420, 540), game, new Texture[]{new Texture("sprites/menu/fyte_bill.png"), new Texture("sprites/menu/fyte_bill_selected.png")}) {
                public void click() {
                    new ScreenWipe(new CharacterSelect(this.game), game);
                    for (MenuCursor cursor : Main.cursors) {
                        cursor.canMove = false;
                    }
                }
            };

            // creates a button for the settings
            new Button(new Vector2(1520, 304), game, new Texture[]{new Texture("sprites/menu/settings.png"), new Texture("sprites/menu/settings_selected.png")}) {
                public void click() {

                    //goes to setting screen
                    new ScreenWipe(new SettingsMenu(this.game), game);
                    for (MenuCursor cursor : Main.cursors) {
                        cursor.canMove = false;
                    }
                }
            };

            //creates a button for the save select screen
            new Button(new Vector2(968, 540), game, new Texture[]{new Texture("sprites/menu/bill.png"), new Texture("sprites/menu/bill_selected.png")}) {
                public void click() {

                    //goes to the save select screen
                    new ScreenWipe(new SaveSelect(this.game), game);
                    for (MenuCursor cursor : Main.cursors) {
                        cursor.canMove = false;
                    }
                }
            };

            new Button(new Vector2(1520, 707), game, new Texture[]{new Texture("sprites/menu/vault.png"), new Texture("sprites/menu/vault_selected.png")}) {
                public void click() {

                    //goes to the settings menu
                    new ScreenWipe(new Vault(this.game), game);
                    for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
                }
            };
        } else {
            new MenuCursor(new Vector2(420, 540), Main.controllers[0], game);

            //creates a button to go to character select
            new Button(new Vector2(420, 540), game, new Texture[]{new Texture("sprites/menu/fyte.png"), new Texture("sprites/menu/fyte_selected.png")}) {
                public void click() {
                    new ScreenWipe(new CharacterSelect(this.game), game);
                    for (MenuCursor cursor : Main.cursors) {
                        cursor.canMove = false;
                    }
                }
            };

            //creates a button that goes to the settings menu
            new Button(new Vector2(1520, 304), game, new Texture[]{new Texture("sprites/menu/settings.png"), new Texture("sprites/menu/settings_selected.png")}) {
                public void click() {
                    new ScreenWipe(new SettingsMenu(this.game), game);
                    for (MenuCursor cursor : Main.cursors) {
                        cursor.canMove = false;
                    }
                }
            };

            //creates a single player button
            new Button(new Vector2(968, 540), game, new Texture[]{new Texture("sprites/menu/fawful.png"), new Texture("sprites/menu/fawful_selected.png")}) {
                public void click() {
                    new ScreenWipe(new SaveSelect(this.game), game);
                    for (MenuCursor cursor : Main.cursors) {
                        cursor.canMove = false;
                    }
                }
            };

            //creates a button that goes to the vault
            new Button(new Vector2(1520, 707), game, new Texture[]{new Texture("sprites/menu/vault.png"), new Texture("sprites/menu/vault_selected.png")}) {
                public void click() {
                    new ScreenWipe(new Vault(this.game), game);
                    for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
                }
            };
        }

        // starts the music
        if (game.music == null) {
            Random rand = new Random();
            int i = rand.nextInt(100);


            //handles menu music
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
            }
            game.music.setVolume(Main.musicVolume / 10f);
            game.music.play();
        }
    }

    /*
     * Pre: game instance
     * Post: handles the rendering for the screen
     * */
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

        // handles the escape key for leaving the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            new ScreenWipe(new TitleScreen(this.game), game);
            for (MenuCursor cursor : Main.cursors) {
                cursor.canMove = false;
            }
        }
    }
}
