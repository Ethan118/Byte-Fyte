package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class ScreenWipe {
    ShapeRenderer renderer = new ShapeRenderer();
    Rectangle rect;
    Viewport screen;
    Screen newScreen;
    boolean hasSwitched = false;
    Main game;

    public ScreenWipe(Screen newScreen, Main game) {
        Main.transitions.add(this);
        this.newScreen = newScreen;
        this.game = game;
        screen  = new FitViewport(1920, 1080, new OrthographicCamera());
        rect = new Rectangle(-(int) (screen.getWorldWidth() * 1.2), 0, (int) (screen.getWorldWidth() * 1.2), screen.getWorldHeight());
    }

    public void update(float delta) {
        rect.x += delta * 4000;

        if (rect.x > -screen.getWorldWidth() * 0.1 && !hasSwitched) {
            hasSwitched = true;
            game.setScreen(newScreen);
        } else if (rect.x > screen.getWorldWidth()) {
            renderer.dispose();
            Main.transitions.remove(this);
        }
    }

    public void draw() {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rect(rect.x, rect.y, rect.getWidth(), rect.getHeight());
        renderer.end();
    }
}
