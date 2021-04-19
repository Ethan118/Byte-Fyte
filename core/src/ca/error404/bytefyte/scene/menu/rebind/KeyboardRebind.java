package ca.error404.bytefyte.scene.menu.rebind;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.menu.MenuScene;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class KeyboardRebind extends MenuScene {
    public KeyboardRebind(Main game) {
        super(game);
    }

    public void show() {
        super.show();

        background = new Texture("sprites/menu/main_bg.png");

        new MenuCursor(new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2), Main.controllers[0], game);
    }
}
