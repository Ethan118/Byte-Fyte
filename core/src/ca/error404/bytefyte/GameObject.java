package ca.error404.bytefyte;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

/*
 * Pre: delta time, sprite called
 * Post: creates and destroys objects
 * */
public abstract class GameObject extends Sprite {

    public boolean remove;
    public Body b2body;

    public GameObject() {
        Main.objectsToAdd.add(this);
    }

    public abstract void update(float delta);

    public void destroy() {
        remove = true;
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }
}
