package ca.error404.bytefyte;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameObject extends Sprite {
    public GameObject() {
        Main.gameObjects.add(this);
    }

    public abstract void update(float delta);
}
