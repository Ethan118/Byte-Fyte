package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.scene.PlayRoom;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;

/*
 * Pre: stage created
 * Post: creates the deathwall class
 * */
public class DeathWall extends Wall {

    /*constructor
     * Pre: stage created
     * Post: adds collision for the deathwall
     * */
    public DeathWall(int x, int y, float w, float h, PlayRoom screen) {
        super(x, y, w, h, screen);
        Filter filter = new Filter();
        filter.categoryBits = Tags.DEATH_BARRIER_BIT;
        fix.setFilterData(filter);
    }

    /*
     * Pre: character collides with deathwall
     * Post: kills the character
     * */
    public void contact(Character chara) {
        chara.die();
        chara.vel = new Vector2(0, 0);
        chara.prevVel = new Vector2(0, 0);
    }
}
