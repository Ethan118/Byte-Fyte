package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.ScreenWipe;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.Color;

/*
 * Pre: game launch
 * Post: creates the title screen
 * */
public class TitleScreen extends MenuScene {
    public Texture logo = new Texture("sprites/menu/logo_title.png");
    private final GlyphLayout layout = new GlyphLayout();
    private float alpha = 4.5f;


    /*constructor
     * Pre: game instance
     * Post: handles the imports and variables needed
     * */
    public TitleScreen(Main game) {
        super(game);
        if (Main.internalSongName.equalsIgnoreCase("menu weird") && Main.bill) {
            background = new Texture("sprites/menu/bill_bg.png");
        } else {
            background = new Texture("sprites/menu/title_screen_bg.png");
        }
        xSpeed = 0;
    }

    /*
     * Pre: game instance
     * Post: handles the screen updating
     * */
    public void update(float deltaTime) {
        boolean controllerPressed = false;

        //checks if a controller has been pressed
        for (Controller controller : Main.allControllers) {
            if (Main.recentButtons.get(controller).size > 0) {
                controllerPressed = true;
            }
        }

        super.update(deltaTime);

        //handles if the user presses escape button
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || controllerPressed) {
            new ScreenWipe(new MainMenu(game), game);
        }


        alpha += deltaTime * 4;
        if (alpha > 180) {
            alpha = 4;
        }
    }

    /*
     * Pre: game instance
     * Post: handles the rendering for the screen
     * */
    public void render(float delta) {
        super.render(delta);

        game.batch.begin();
        game.batch.draw(logo, 0, 0);
        layout.setText(Main.menuFont, "PRESS ANY BUTTON TO START", new Color(1, 1, 1, (float) Math.toDegrees(Math.sin(alpha)) / 57), 0, Align.center, false);
        Main.menuFont.draw(game.batch, layout, 960, 400);
        layout.setText(Main.menuFont, "PRESS ESC TO EXIT", new Color(1, 1, 1, (float) Math.toDegrees(Math.sin(alpha)) / 57), 0, Align.center, false);
        Main.menuFont.draw(game.batch, layout, 960, 300);
        game.batch.setColor(Color.WHITE);
        game.batch.end();

        for (int i=0; i < Main.transitions.size(); i++) Main.transitions.get(i).draw();
    }
}
