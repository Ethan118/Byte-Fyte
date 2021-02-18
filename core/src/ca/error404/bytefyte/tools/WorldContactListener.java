package ca.error404.bytefyte.tools;

import ca.error404.bytefyte.chars.DeathWall;
import ca.error404.bytefyte.chars.TestChar;
import ca.error404.bytefyte.constants.Tags;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            // if a player is contacting the ground, call the grounded function
            case Tags.GROUND_BIT | Tags.PLAYER_FEET_BIT:
                TestChar chara;
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_FEET_BIT) {
                    chara = ((TestChar) fixA.getUserData());
                } else {
                    chara = ((TestChar) fixB.getUserData());
                }

                if (chara.vel.y <= 0) chara.ground();
                break;
            case Tags.PLAYER_BIT | Tags.DEATH_BARRIER_BIT:
                DeathWall wall;
                if (fixA.getFilterData().categoryBits == Tags.DEATH_BARRIER_BIT) {
                    wall = ((DeathWall) fixA.getUserData());
                } else {
                    wall = ((DeathWall) fixB.getUserData());
                }

                wall.contact();
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            // if player left the ground, disable grounded variable
            case Tags.GROUND_BIT | Tags.PLAYER_FEET_BIT:
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_FEET_BIT) {
                    ((TestChar) fixA.getUserData()).grounded = false;
                } else {
                    ((TestChar) fixB.getUserData()).grounded = false;
                }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
