package ca.error404.bytefyte.tools;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.DeathWall;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.chars.Wall;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.Projectile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        Character chara;
        Projectile projectile;
        Wall wall;
        DeathWall deathWall;
        Collider collider;

        switch (cDef) {
            // if a player is contacting the ground, call the grounded function
            case Tags.GROUND_BIT | Tags.PLAYER_FEET_BIT:
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_FEET_BIT) {
                    chara = ((Character) fixA.getUserData());
                } else {
                    chara = ((Character) fixB.getUserData());
                }

                if ((Math.abs(chara.vel.x) >= 3 || Math.abs(chara.vel.y) >= 3) && chara.stunTimer > 0) {
                    chara.vel.y *= -0.5;
                } else if (chara.vel.y <= 0) {
                    chara.ground();
                } else if (chara.animState == Character.AnimationState.SPECIAL_U) {
                    chara.ground();
                }
                break;
            case Tags.PLAYER_HEAD_BIT | Tags.GROUND_BIT:
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_HEAD_BIT) {
                    chara = ((Character) fixA.getUserData());
                } else {
                    chara = ((Character) fixB.getUserData());
                }

                if ((Math.abs(chara.vel.x) >= 3 || Math.abs(chara.vel.y) >= 3) && chara.stunTimer > 0) {
                    chara.vel.x *= -0.5;
                } else {
                    chara.vel.y = 0;
                }
                break;
            case Tags.GROUND_BIT | Tags.PLAYER_SIDE_BIT:
                if (fixA.getFilterData().categoryBits == Tags.WALL_TRIGGER_BIT) {
                    chara = (Character) fixB.getUserData();
                } else {
                    chara = (Character) fixA.getUserData();
                }

                if ((Math.abs(chara.vel.x) >= 3 || Math.abs(chara.vel.y) >= 3) && chara.stunTimer > 0) {
                    chara.vel.x *= -0.5;
                }
                break;
            case Tags.PLAYER_BIT | Tags.DEATH_BARRIER_BIT:
                if (fixA.getFilterData().categoryBits == Tags.DEATH_BARRIER_BIT) {
                    deathWall = ((DeathWall) fixA.getUserData());
                    chara = ((Character) fixB.getUserData());
                } else {
                    deathWall = ((DeathWall) fixB.getUserData());
                    chara = ((Character) fixA.getUserData());
                }

                deathWall.contact(chara);
                break;
            case Tags.ATTACK_BIT | Tags.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_BIT) {
                    chara = (Character) fixA.getUserData();
                    collider = (Collider) fixB.getUserData();

                } else {
                    chara = (Character) fixB.getUserData();
                    collider = (Collider) fixA.getUserData();
                }

                if (!(collider.parent == chara)) {
                    Vector2 direction = new Vector2(Math.round(((chara.pos.x) - (collider.parent.pos.x)) * 100.0f) / 100.0f, Math.round(((chara.pos.y) - (collider.parent.pos.y)) * 100.0f) / 100.0f);
                    direction.x = Math.signum(direction.x);
                    direction.y = Math.signum(direction.y);

                    Vector2 force = new Vector2(direction.x * collider.power, direction.y * collider.power);
                    chara.Hit(collider.damage, force, collider.hitStun);

                    if (collider.lifeSteal) {
                        if (collider.parent.percent >= collider.damage) {
                            collider.parent.percent -= collider.damage;
                        } else {
                            collider.parent.percent = 0;
                        }
                    }
                }
                break;

            case Tags.PROJECTILE_BIT | Tags.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_BIT) {
                    chara = (Character) fixA.getUserData();
                    projectile = (Projectile) fixB.getUserData();
                } else {
                    chara = (Character) fixB.getUserData();
                    projectile = (Projectile) fixA.getUserData();
                }

                if (!(projectile.parent == chara)) {
                    Vector2 direction = new Vector2(Math.round(((chara.pos.x) - (projectile.parent.pos.x)) * 100.0f) / 100.0f, Math.round(((chara.pos.y) - (projectile.parent.pos.y)) * 100.0f) / 100.0f);
                    direction.x = Math.signum(direction.x);
                    direction.y = Math.signum(direction.y);

                    Vector2 force = new Vector2(direction.x * projectile.power, direction.y * projectile.power);
                    chara.Hit(projectile.damage, force, projectile.hitStun);

                    projectile.destroy();
                }
                break;

            case Tags.PROJECTILE_BIT | Tags.GROUND_BIT:
            case Tags.PROJECTILE_BIT | Tags.DEATH_BARRIER_BIT:
                if (fixA.getFilterData().categoryBits == Tags.PROJECTILE_BIT) {
                    projectile = (Projectile) fixA.getUserData();
                } else {
                    projectile = (Projectile) fixB.getUserData();
                }

                projectile.destroy();
                break;
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

    // These are required for the ContactListener class to work, they do not do anything
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
