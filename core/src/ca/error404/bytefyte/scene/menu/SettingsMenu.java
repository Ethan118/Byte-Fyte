package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.Globals;
import ca.error404.bytefyte.constants.ScreenSizes;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

/*
 * Pre: game instance
 * Post: creates the settingsMenu screen
 * */
public class SettingsMenu extends MenuScene {

    //declaring variables
    public MenuCursor pointer;
    public static boolean toRefresh = false;
    public static float timer;

    /*
    * Constructor
    * Pre: A game instance
    * Post: A new Settings menu
    * */
    public SettingsMenu(Main game) {
        super(game);
    }


    /*
     * Pre: game instance
     * Post: handles what the screen shows to the user
     * */
    public void show() {
        super.show();
        if (Main.internalSongName.equalsIgnoreCase("menu weird") && Main.bill) {
            background = new Texture("sprites/menu/bill_bg.png");
        } else {
            background = new Texture("sprites/menu/settings_bg.png");
        }

        xSpeed = 0;

        //creates cursor
        pointer = new MenuCursor(new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2), Main.controllers[0], game);

        // Screen Settings
        new Button(new Vector2(500, 1000), game, "Screen Size") {
            public boolean isCursorOver(MenuCursor cursor) {
                return false;
            }
        };

        //create button for screen size
        new Button(new Vector2(500, 925), game, "< " +  ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0) + "x" + ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1) + " >") {
            public void update() {
                if (isClicked()) {
                    click();
                }

                string = "< " +  ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0) + "x" + ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1) + " >";

                layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);

                this.buttonRect.set(pos.x - (layout.width / 2), pos.y - layout.height, layout.width, layout.height);
            }

            public void click() {

                //changes the screen size up or down an interval
                if (cursor.pos.x < pos.x) {
                    ScreenSizes.screenSize = ScreenSizes.screenSize <= 0 ? ScreenSizes.screenSizes.size() - 1 : ScreenSizes.screenSize - 1;
                } else {
                    ScreenSizes.screenSize = ScreenSizes.screenSize >= ScreenSizes.screenSizes.size() - 1 ? 0 : ScreenSizes.screenSize + 1;
                }
            }
        };

        //creates the full screen button
        new Button(new Vector2(1300, 1000), game, "Full Screen") {
            public boolean isCursorOver(MenuCursor cursor) {
                return false;
            }
        };

        //creates the "off" button
        new Button(new Vector2(1300, 925), game, "Off") {
            public void update() {
                if (isClicked()) {
                    click();
                }

                if (ScreenSizes.fullScreen) {
                    string = "On";
                } else {
                    string = "Off";
                }

                layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);

                this.buttonRect.set(pos.x - (layout.width / 2), pos.y - layout.height, layout.width, layout.height);
            }

            public void click() {
                ScreenSizes.fullScreen = !ScreenSizes.fullScreen;
            }
        };

        // Volume Settings
        new Button(new Vector2(1920 / 2f, 800), game, "Music Volume") {
            public boolean isCursorOver(MenuCursor cursor) {
                return false;
            }
        };

        //creates button to change volume
        new Button(new Vector2(1920 / 2f, 725), game, "< " +  Main.musicVolume + " >") {
            public void update() {
                if (isClicked()) {
                    click();
                }

                string = "< " +  Main.musicVolume + " >";

                layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);

                this.buttonRect.set(pos.x - (layout.width / 2), pos.y - layout.height, layout.width, layout.height);
            }

            public void click() {

                //makes the music volume go up or down
                if (cursor.pos.x < pos.x) {
                    Main.musicVolume = Math.max(Main.musicVolume - 1, 0);
                } else {
                    Main.musicVolume = Math.min(Main.musicVolume + 1, 10);
                }
            }
        };

        //creates the SFX volume button
        new Button(new Vector2(400, 725), game, "SFX Volume") {
            public boolean isCursorOver(MenuCursor cursor) {
                return false;
            }
        };

        //creates another sfx volume button
        new Button(new Vector2(400, 650), game, "< " +  Main.sfxVolume + " >") {
            public void update() {
                if (isClicked()) {
                    click();
                }

                string = "< " +  Main.sfxVolume + " >";

                layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);

                this.buttonRect.set(pos.x - (layout.width / 2), pos.y - layout.height, layout.width, layout.height);
            }

            public void click() {

                //makes the SFX volume go up or down
                if (cursor.pos.x < pos.x) {
                    Main.sfxVolume = Math.max(Main.sfxVolume - 1, 0);
                } else {
                    Main.sfxVolume = Math.min(Main.sfxVolume + 1, 10);
                }
            }
        };


        //creates a video volume button
        new Button(new Vector2(1550, 725), game, "Video Volume") {
            public boolean isCursorOver(MenuCursor cursor) {
                return false;
            }
        };

        //creates another volume button
        new Button(new Vector2(1550, 650), game, "< " +  Main.cutsceneVolume + " >") {
            public void update() {
                if (isClicked()) {
                    click();
                }

                string = "< " +  Main.cutsceneVolume + " >";

                layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);

                this.buttonRect.set(pos.x - (layout.width / 2), pos.y - layout.height, layout.width, layout.height);
            }

            public void click() {

                //makes the video volume go up or down
                if (cursor.pos.x < pos.x) {
                    Main.cutsceneVolume = Math.max(Main.cutsceneVolume - 1, 0);
                } else {
                    Main.cutsceneVolume = Math.min(Main.cutsceneVolume + 1, 10);
                }
            }
        };

        // Debug Settings
        new Button(new Vector2(1920 / 2f, 600), game, "Hitboxes") {
            public boolean isCursorOver(MenuCursor cursor) {
                return false;
            }
        };

        //creates the button for hitboxes and if the user can see them
        new Button(new Vector2(1920 / 2f, 525), game, "Invisible") {
            public void update() {
                if (isClicked()) {
                    click();
                }

                if (Main.debug) {
                    string = "Visible";
                } else {
                    string = "Invisible";
                }

                layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);

                this.buttonRect.set(pos.x - (layout.width / 2), pos.y - layout.height, layout.width, layout.height);
            }

            public void click() {
                Main.debug = !Main.debug;
            }
        };

        // Controlller Rebind

        //stamina button, changes the
        new Button(new Vector2(1920 / 2f, 350), game, "Stamina") {
            public boolean isCursorOver(MenuCursor cursor) {
                return false;
            }
        };

        //on or off button for stamina
        new Button(new Vector2(1920 / 2f, 275), game, "Off") {
            public void update() {
                if (isClicked()) {
                    click();
                }

                if (Main.stamina) {
                    string = "On";
                } else {
                    string = "Off";
                }

                layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);

                this.buttonRect.set(pos.x - (layout.width / 2), pos.y - layout.height, layout.width, layout.height);
            }

            public void click() {
                Main.stamina = !Main.stamina;
            }
        };
        // Navigation Buttons

        //creates back button
        new Button(new Vector2(200, 100), game, "Back") {
            public void click() {
                new ScreenWipe(new MainMenu(game), game);
                for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
            }
        };

        new Button(new Vector2(1920 / 2f, 100), game, "Apply") {
            public void click() {

                File settings = new File(Globals.workingDirectory + "settings.ini");

                //try and catch statement for swapping setting, detects errors
                try {
                    Wini ini = new Wini(settings);
                    ini.add("Settings", "screen size", ScreenSizes.screenSize);
                    ini.add("Settings", "music volume", Main.musicVolume);
                    ini.add("Settings", "sfx volume", Main.sfxVolume);
                    ini.add("Settings", "cutscene volume", Main.cutsceneVolume);
                    ini.add("Settings", "fullscreen", ScreenSizes.fullScreen);
                    ini.add("Settings", "debug", Main.debug);
                    ini.add("Menu", "stamina", Main.stamina);
                    ini.add("Menu", "bill", Main.bill);
                    ini.store();

                    if (ScreenSizes.fullScreen) {
                        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    } else {
                        Gdx.graphics.setWindowedMode(ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0), ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                game.music.setVolume(Main.musicVolume / 10f);

                Main.cursors.clear();
                Main.recentButtons.clear();
                game.reloadControllers();
                game.loadFonts();

                SettingsMenu.toRefresh = true;
                SettingsMenu.timer = 1f;
            }
        };
    }


    /*
     * Pre: game instance
     * Post: handles the screen updating
     * */
    public void update(float delta) {
        if (toRefresh && timer <= 0) {
            Main.cursors.clear();
            Main.recentButtons.clear();
            game.reloadControllers();
            game.loadFonts();
            pointer = new MenuCursor(new Vector2(pointer.pos.x, pointer.pos.y), Main.controllers[0], game);

            for (Button button : Main.buttons) { button.cursor = pointer; }
            toRefresh = false;
        }

        timer -= delta;

        super.update(delta);
    }
}
