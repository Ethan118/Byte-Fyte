package ca.error404.bytefyte;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

// Game object class
public abstract class GameObject extends Sprite {

    public boolean remove;
    public Body b2body;

    /*
     * Constructor
     * Pre: delta time, sprite called
     * Post: creates and destroys objects
     * */
    public GameObject() {
        Main.objectsToAdd.add(this);
    }

    /*
     * Pre: None
     * Post: Updates this game object
     * */
    public abstract void update(float delta);

    /*
    * Pre: None
    * Post: Removes this game object
    * */
    public void destroy() {
        remove = true;
    }

    /*
     * Pre: None
     * Post: Removes this game object
     * */
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }
}
