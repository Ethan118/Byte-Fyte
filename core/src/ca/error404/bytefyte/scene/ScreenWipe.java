package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.Main;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
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
    float timer = 2f;

    public ScreenWipe(Screen newScreen, Main game) {
        Main.transitions.add(this);
        this.newScreen = newScreen;
        this.game = game;
        game.cursors.clear();
        Camera cam = new OrthographicCamera(1920, 1080);
        cam.position.set(960, 540, cam.position.z);
        cam.update();
        screen  = new FitViewport(1920, 1080, cam);
        rect = new Rectangle(-(int) (screen.getWorldWidth() * 1.2), 0, (int) (screen.getWorldWidth() * 1.2), screen.getWorldHeight());
    }

    public void update(float delta) {
        if (timer == 2f || timer <= 0) {
            rect.x += delta * 4000;
        }

        if (rect.x > -screen.getWorldWidth() * 0.1 && !hasSwitched) {
            timer -= delta;
            if (timer <= 0) {
                hasSwitched = true;
                Main.gameObjects.clear();
                game.setScreen(newScreen);
            }
        } else if (rect.x > screen.getWorldWidth()) {
            renderer.dispose();
            Main.transitions.remove(this);
        }
    }

    public void draw() {
        renderer.setProjectionMatrix(screen.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rect(-5, -5, 10, 10);
        renderer.rect(rect.x, rect.y, rect.getWidth(), rect.getHeight());
        renderer.end();
    }
}
