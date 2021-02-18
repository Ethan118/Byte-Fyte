package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;

public class DeathWall extends Wall {

    public DeathWall(int x, int y, int w, int h, TestScene screen) {
        super(x, y, w, h, screen);
        Filter filter = new Filter();
        filter.categoryBits = Tags.DEATH_BARRIER_BIT;
        fix.setFilterData(filter);
    }

    @Override
    public void contact() {
        scene.player.setPos(0, 30);
        scene.player.vel = new Vector2(0, 0);
        scene.player.prevVel = new Vector2(0, 0);
    }
}
