package ca.error404.bytefyte.scene.menu;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class TitleScreen extends MenuScene {
    public TitleScreen(Main game) {
        super(game);
        background = new Texture("sprites/menu/title_screen_bg.png");
        xSpeed = 0;
    }

    public void update(float deltaTime) {
        super.update(deltaTime);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new MainMenu(game));
        }
    }
}
