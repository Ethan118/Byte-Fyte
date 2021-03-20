 package ca.error404.bytefyte;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameObject extends Sprite {

    public boolean remove;

    public GameObject() {
        Main.objectsToAdd.add(this);
    }

    public abstract void update(float delta);

    public void destroy() {
        remove = true;
    }
}
