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

public class TitleScreen extends MenuScene {
    public Texture logo = new Texture("sprites/menu/logo_title.png");
    private final GlyphLayout layout = new GlyphLayout();
    private float alpha = 4.5f;

    public TitleScreen(Main game) {
        super(game);
        background = new Texture("sprites/menu/title_screen_bg.png");
        xSpeed = 0;
    }

    public void update(float deltaTime) {
        boolean controllerPressed = false;

        for (Controller controller : Main.allControllers) {
            if (Main.recentButtons.get(controller).size > 0) {
                controllerPressed = true;
            }
        }

        super.update(deltaTime);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || controllerPressed) {
            new ScreenWipe(new MainMenu(game), game);
        }

        alpha += deltaTime * 4;
        if (alpha > 180) {
            alpha = 4;
        }
    }

    public void render(float delta) {
        super.render(delta);

        game.batch.begin();
        game.batch.draw(logo, 0, 0);
        layout.setText(Main.menuFont, "PRESS ANY BUTTON", new Color(1, 1, 1, (float) Math.toDegrees(Math.sin(alpha)) / 57), 0, Align.center, false);
        Main.menuFont.draw(game.batch, layout, 960, 400);
        game.batch.setColor(Color.WHITE);
        game.batch.end();

        for (int i=0; i < Main.transitions.size(); i++) Main.transitions.get(i).draw();
    }
}
