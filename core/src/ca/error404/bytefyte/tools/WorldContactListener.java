package ca.error404.bytefyte.tools;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.DeathWall;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.objects.Collider;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        Character chara;

        switch (cDef) {
            // if a player is contacting the ground, call the grounded function
            case Tags.GROUND_BIT | Tags.PLAYER_FEET_BIT:
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_FEET_BIT) {
                    chara = ((Character) fixA.getUserData());
                } else {
                    chara = ((Character) fixB.getUserData());
                }

                if (chara.vel.y <= 0) chara.ground();
                break;
            case Tags.PLAYER_HEAD_BIT | Tags.GROUND_BIT:
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_HEAD_BIT) {
                    chara = ((Character) fixA.getUserData());
                } else {
                    chara = ((Character) fixB.getUserData());
                }

                chara.vel.y = 0;
                break;
            case Tags.PLAYER_BIT | Tags.DEATH_BARRIER_BIT:
                DeathWall wall;
                if (fixA.getFilterData().categoryBits == Tags.DEATH_BARRIER_BIT) {
                    wall = ((DeathWall) fixA.getUserData());
                } else {
                    wall = ((DeathWall) fixB.getUserData());
                }

                wall.contact();
                break;
            case Tags.ATTACK_BIT | Tags.PLAYER_BIT:
                Collider collider;
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_BIT) {
                    chara = (Character) fixA.getUserData();
                    collider = (Collider) fixB.getUserData();
                } else {
                    chara = (Character) fixB.getUserData();
                    collider = (Collider) fixA.getUserData();
                }

                if (!(collider.parent == chara)) {
                    Vector2 force = new Vector2(((chara.pos.x) - (collider.parent.pos.x)) * collider.power, ((chara.b2body.getPosition().y) - (collider.parent.b2body.getPosition().y)) * collider.power);
                    force.x = Math.round(force.x * 100.0f) / 100.0f;
                    force.y = Math.round(force.y * 100.0f) / 100.0f;
                    chara.Hit(collider.damage, force, collider.minPower, collider.hitStun);
                }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            // if player left the ground, tell them that they have left the ground
            case Tags.GROUND_BIT | Tags.PLAYER_FEET_BIT:
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_FEET_BIT) {
                    ((Character) fixA.getUserData()).grounded = false;
                } else {
                    ((Character) fixB.getUserData()).grounded = false;
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
