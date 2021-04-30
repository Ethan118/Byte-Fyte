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

// Class for vault menu
public class Vault extends MenuScene {

//    Initializing variables
    CutscenePlayer tut = new CutscenePlayer("how to play");
    float timer = 1f;
    boolean playTut = false;

    /*
    * Constructor
    * Pre: Game instance
    * Post: A new vault screen
    * */
    public Vault(Main game) {

//        Calling the super() method
        super(game);
        ySpeed = 50;
        xSpeed = 100;

//        Gets whether to play the tutorial
        if (game.getScreen().getClass() == Vault.class) {
            playTut = true;
        } else {
            timer = 0;
        }

//        Determines if the easter egg occurs
        if (Main.bill) {
            background = new Texture("sprites/menu/bill_bg.png");
        } else {
            background = new Texture("sprites/menu/vault_bg.png");
        }
    }

    /*
    * Pre: None
    * Post: Initializes variables, buttons, and other things
    * */
    @Override
    public void show() {

//        Determines whether the tutorial is playing, stops the music of the main menu if it is
        if (playTut) {
            if (game.music != null) {
                game.music.stop();
                game.music = null;
                Main.bill = false;
            }
            tut.play();
        }

//        Creates a menu cursor
        super.show();
        new MenuCursor(new Vector2(420, 540), Main.controllers[0], game);

//        Back button
        new Button(new Vector2(200, 100), game, "Back") {
            public void click() {
                new ScreenWipe(new MainMenu(game), game);
                for (MenuCursor cursor : Main.cursors) { cursor.canMove = false; }
            }
        };

//        Button to play the tutorial video
        new Button(new Vector2(968, 540), game, new Texture[]{new Texture("sprites/menu/tutorial.png"), new Texture("sprites/menu/tutorial_selected.png")}) {
            public void click() {
                new ScreenWipe(new Vault(this.game), game);
                for (MenuCursor cursor : Main.cursors) {
                    cursor.canMove = false;
                }
            }
        };
    }

    /*
    * Pre: Delta time
    * Post: Renders all images
    * */
    public void render(float delta) {
//        If the tutorial is playing and the timer is above 0
        if (tut.isPlaying() || timer > 0) {
//            Play the tutorial, update the screen
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            game.batch.begin();
            tut.draw(game.batch);
            game.batch.end();

//            If the tutorial isn't playing, update the timer
            if (!tut.isPlaying()) {
                timer -= delta;
            }


//        Otherwise, set the menu music to play
        } else {
            if (game.music == null) {
                game.music = game.newSong("menu");
                game.music.play();
            }
//            Call the render method from the super class
            super.render(delta);
        }
    }
}
