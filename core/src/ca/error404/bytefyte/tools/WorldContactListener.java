package ca.error404.bytefyte.tools;

import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.chars.DeathWall;
import ca.error404.bytefyte.chars.Mario;
import ca.error404.bytefyte.chars.Wall;
import ca.error404.bytefyte.chars.bosses.Boss;
import ca.error404.bytefyte.constants.Tags;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.Laser;
import ca.error404.bytefyte.objects.Projectile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


/*
 * Pre: delta time, Contact listener, scene, chars
 * Post: handles contact between objects
 * */
public class WorldContactListener implements ContactListener {
    /**
     * @param contact
     * pre: the object representing the contact
     * post: controls logic on contact
     */
    @Override
    public void beginContact(Contact contact) {
//        define variabels
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        Character chara;
        Boss boss;
        Projectile projectile;
        Laser laser;
        Wall wall;
        DeathWall deathWall;
        Collider collider;
        int tag;

        switch (cDef) {
            case Tags.GROUND_BIT | Tags.PLAYER_FEET_BIT:
//                find character fixture
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
//                if a player contacts a roof, bounce character back
            case Tags.PLAYER_HEAD_BIT | Tags.GROUND_BIT:
//                find character fixture
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
//                if a player contact a wall from the side, bounce character back
            case Tags.GROUND_BIT | Tags.PLAYER_SIDE_BIT:
//                find character fixture
                if (fixA.getFilterData().categoryBits == Tags.WALL_TRIGGER_BIT) {
                    chara = (Character) fixB.getUserData();
                } else {
                    chara = (Character) fixA.getUserData();
                }

                if ((Math.abs(chara.vel.x) >= 3 || Math.abs(chara.vel.y) >= 3) && chara.stunTimer > 0) {
                    chara.vel.x *= -0.5;
                }
                break;
//                if a player contacts a death barrier call the death wall's contact function passing character
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
//                if an attack bit contacts a player deal damage and calculate force applied
            case Tags.ATTACK_BIT | Tags.PLAYER_BIT:
//                find character and collider
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_BIT) {
                    chara = (Character) fixA.getUserData();
                    collider = (Collider) fixB.getUserData();

                } else {
                    chara = (Character) fixB.getUserData();
                    collider = (Collider) fixA.getUserData();
                }

//                only hit if the character is not the colliders parent
                if (!(collider.parent == chara)) {
                    Vector2 force;

//                    find direction of force if the direction is null
                    if (collider.dir == null) {
                        Vector2 direction = new Vector2(Math.round(((chara.pos.x) - (collider.parent.pos.x)) * 100.0f) / 100.0f, Math.round(((chara.pos.y) - (collider.parent.pos.y)) * 100.0f) / 100.0f);
                        direction.x = Math.signum(direction.x);
                        direction.y = Math.signum(direction.y);

                        force = new Vector2(direction.x * collider.power, direction.y * collider.power);
                    } else {
                        force = new Vector2(collider.dir.x * collider.power, collider.dir.y * collider.power);
                    }
                    chara.Hit(collider.damage, force, collider.hitStun);

//                    heal if the collider has lifesteal
                    if (collider.lifeSteal) {
                        if (!chara.stamina) {
                            if (collider.parent.percent >= collider.damage) {
                                collider.parent.percent -= collider.damage;
                            } else {
                                collider.parent.percent = 0;
                            }
                        } else {
                            collider.parent.percent = Math.min(collider.parent.percent + collider.damage, 999.9f);
                        }
                    }
                }
                break;

//                if a player contacts a projectile
            case Tags.PROJECTILE_BIT | Tags.PLAYER_BIT:
//                find character and projectile
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_BIT) {
                    chara = (Character) fixA.getUserData();
                    projectile = (Projectile) fixB.getUserData();
                } else {
                    chara = (Character) fixB.getUserData();
                    projectile = (Projectile) fixA.getUserData();
                }

//                calculate force applied and call hit function on character
                if (!(projectile.parent == chara)) {
                    Vector2 direction = new Vector2(Math.round(((chara.pos.x) - (projectile.parent.pos.x)) * 100.0f) / 100.0f, Math.round(((chara.pos.y) - (projectile.parent.pos.y)) * 100.0f) / 100.0f);
                    direction.x = Math.signum(direction.x);
                    direction.y = Math.signum(direction.y);

                    Vector2 force = new Vector2(direction.x * projectile.power, direction.y * projectile.power);
                    chara.Hit(projectile.damage, force, projectile.hitStun);

                    projectile.destroy();
                }
                break;

//                if projectile contacts a boss
            case Tags.PROJECTILE_BIT | Tags.BOSS_BIT:
//                find boss and projectile
                if (fixA.getFilterData().categoryBits == Tags.BOSS_BIT) {
                    boss = (Boss) fixA.getUserData();
                    projectile = (Projectile) fixB.getUserData();
                } else {
                    boss = (Boss) fixB.getUserData();
                    projectile = (Projectile) fixA.getUserData();
                }

//                call hit function
                boss.hit(projectile.damage);

                projectile.destroy();
                break;

//                if laser contacts player
            case Tags.LASER_BIT | Tags.PLAYER_BIT:
//                find character and laser
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_BIT) {
                    chara = (Character) fixA.getUserData();
                    laser = (Laser) fixB.getUserData();
                } else {
                    chara = (Character) fixB.getUserData();
                    laser = (Laser) fixA.getUserData();
                }

//                if the character is not the laser's parent calculate force applied and call hit
                if (!(laser.parent == chara)) {
                    Vector2 direction = new Vector2(Math.round(((chara.pos.x) - (laser.parent.pos.x)) * 100.0f) / 100.0f, Math.round(((chara.pos.y) - (laser.parent.pos.y)) * 100.0f) / 100.0f);
                    direction.x = Math.signum(direction.x);
                    direction.y = Math.signum(direction.y);

                    Vector2 force = new Vector2(direction.x * laser.power, direction.y * laser.power);
                    chara.Hit(laser.damage, force, laser.hitStun);
                }
                break;

//                if projectile contacts death barrier or ground bit destroy it
            case Tags.PROJECTILE_BIT | Tags.GROUND_BIT:
            case Tags.PROJECTILE_BIT | Tags.DEATH_BARRIER_BIT:
                if (fixA.getFilterData().categoryBits == Tags.PROJECTILE_BIT) {
                    projectile = (Projectile) fixA.getUserData();
                } else {
                    projectile = (Projectile) fixB.getUserData();
                }

                projectile.destroy();
                break;

//                if the boss contacts the ground, death barrier or player call hitWall
            case Tags.GROUND_BIT | Tags.BOSS_SIDE_BIT:
            case Tags.GROUND_BIT | Tags.BOSS_HEAD_BIT:
            case Tags.GROUND_BIT | Tags.BOSS_FEET_BIT:
            case Tags.DEATH_BARRIER_BIT | Tags.BOSS_SIDE_BIT:
            case Tags.DEATH_BARRIER_BIT | Tags.BOSS_HEAD_BIT:
            case Tags.DEATH_BARRIER_BIT | Tags.BOSS_FEET_BIT:
            case Tags.PLAYER_BIT | Tags.BOSS_FEET_BIT:
                if (fixA.getFilterData().categoryBits == Tags.GROUND_BIT || fixA.getFilterData().categoryBits == Tags.DEATH_BARRIER_BIT || fixA.getFilterData().categoryBits == Tags.PLAYER_BIT) {
                    boss = (Boss) fixB.getUserData();
                    tag = fixB.getFilterData().categoryBits;
                } else {
                    boss = (Boss) fixA.getUserData();
                    tag = fixA.getFilterData().categoryBits;
                }

                boss.hitWall(tag);
                break;

//                if an attack contacts a boss call the boss' hit function
            case Tags.ATTACK_BIT | Tags.BOSS_BIT:
                if (fixA.getFilterData().categoryBits == Tags.BOSS_BIT) {
                    boss = (Boss) fixA.getUserData();
                    collider = (Collider) fixB.getUserData();
                } else {
                    boss = (Boss) fixB.getUserData();
                    collider = (Collider) fixA.getUserData();
                }

                boss.hit(collider.damage);
                break;

//                if a laser contacts a boss call the boss' hit function
            case  Tags.LASER_BIT | Tags.BOSS_BIT:
                if (fixA.getFilterData().categoryBits == Tags.BOSS_BIT) {
                    boss = (Boss) fixA.getUserData();
                    laser = (Laser) fixB.getUserData();
                } else {
                    boss = (Boss) fixB.getUserData();
                    laser = (Laser) fixA.getUserData();
                }

                boss.hit(laser.damage);
                break;

//                if a boss contacts a player apply the force and damage character
            case  Tags.BOSS_BIT | Tags.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_BIT) {
                    chara = (Character) fixA.getUserData();
                    boss = (Boss) fixB.getUserData();
                } else {
                    chara = (Character) fixB.getUserData();
                    boss = (Boss) fixA.getUserData();
                }

                Vector2 direction = new Vector2(Math.round(((chara.pos.x) - (boss.b2body.getTransform().getPosition().x)) * 100.0f) / 100.0f, Math.round(((chara.pos.y) - (boss.b2body.getTransform().getPosition().y)) * 100.0f) / 100.0f);
                direction.x = Math.signum(direction.x);
                direction.y = Math.signum(direction.y);

                Vector2 force = new Vector2(direction.x * boss.damage, direction.y * boss.damage);
                chara.Hit(boss.damage, force, boss.hitStun);
                break;
        }
    }

    /**
     * @param contact
     * pre: class containing information of contact
     * post: controls logic after contact
     */
    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int tag;
        Boss boss;

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            // if player left the ground, tell them that they have left the ground
            case Tags.GROUND_BIT | Tags.PLAYER_FEET_BIT:
                if (fixA.getFilterData().categoryBits == Tags.PLAYER_FEET_BIT) {
                    ((Character) fixA.getUserData()).grounded = false;
                } else {
                    ((Character) fixB.getUserData()).grounded = false;
                }
                break;
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
