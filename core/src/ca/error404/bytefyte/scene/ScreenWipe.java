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

// Class to wipe the screen and replace it
public class ScreenWipe {

//    Initializing variables
    ShapeRenderer renderer = new ShapeRenderer();
    Rectangle rect;
    Viewport screen;
    Screen newScreen;
    boolean hasSwitched = false;
    Main game;
    float timer = 2f;

    /*
    * Constructor
    * Pre: Screen to switch to, game instance
    * Post: A new screen wipe
    * */
    public ScreenWipe(Screen newScreen, Main game) {

//        Setting all variables
        Main.transitions.add(this);
        this.newScreen = newScreen;
        this.game = game;
        Main.cursors.clear();
        Camera cam = new OrthographicCamera(1920, 1080);
        cam.position.set(960, 540, cam.position.z);
        cam.update();
        screen  = new FitViewport(1920, 1080, cam);
        rect = new Rectangle(-(int) (screen.getWorldWidth() * 1.2), 0, (int) (screen.getWorldWidth() * 1.2), screen.getWorldHeight());
    }

    /*
    * Pre: Delta time
    * Post: Updates the screen wipe
    * */
    public void update(float delta) {

//        If the timer is equal to 2 or less than or equal to 0, add to the rectangle x
        if (timer == 2f || timer <= 0) {
            rect.x += delta * 4000;
        }

//        If it is greater than the screen size times 0.1 and it hasnt switch screens
        if (rect.x > -screen.getWorldWidth() * 0.1 && !hasSwitched) {

//            Adjust timer, switch screen if less than or equal to 0
            timer -= delta;
            if (timer <= 0) {
                hasSwitched = true;
                Main.gameObjects.clear();
                game.setScreen(newScreen);
            }

//        Otherwise, if the x is greater than the screen width, dispose of the renderer, remove this transition
        } else if (rect.x > screen.getWorldWidth()) {
            renderer.dispose();
            Main.transitions.remove(this);
        }
    }

    /*
    * Pre: None
    * Post: Draws the transition
    * */
    public void draw() {

//        Sets renderer up
        renderer.setProjectionMatrix(screen.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

//        Draws the rectangle for the transition
        renderer.setColor(Color.BLACK);
        renderer.rect(-5, -5, 10, 10);
        renderer.rect(rect.x, rect.y, rect.getWidth(), rect.getHeight());
        renderer.end();
    }
}
