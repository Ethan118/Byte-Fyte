package ca.error404.bytefyte.objects;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.chars.Character;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

// Creating a class to handle all attacks which hit a player multiple times
public class MultiHit extends GameObject {

//    Creating an arraylist to quickly make the user's colliders
    ArrayList<Collider> collisions;

//    Creating all necessary variables
    private int numOfCollisions;
    private float leadUpPower;
    private float lastHitPower;
    private float leadUpDamage;
    private float lastHitDamage;

    public Vector2 offset;

    public Vector2 pos;

    public float width;
    public float height;

    public Character parent;
    private final World world;


    public float hitStun;

    private float delay;


    /*
    * Constructor
    * Pre: Parameters to create desired collisions
    * Post: An attack which hits the opponent multiple times
    * */
    public MultiHit(Vector2 offset, float width, float height, Character parent, float hitStun, float delay, int numOfCollisions, float leadUpPower, float lastHitPower, float leadUpDamage, float lastHitDamage) {
//        Calls all methods of the super class
        super();

//        Setting the position to equal the position of the parent character
        this.pos = parent.pos;

//        Instantiating the arraylist
        collisions = new ArrayList<>();

//        Setting all declared variables to the respective parameters
        this.numOfCollisions = numOfCollisions;
        this.leadUpPower = leadUpPower;
        this.lastHitPower = lastHitPower;
        this.leadUpDamage = leadUpDamage;
        this.lastHitDamage = lastHitDamage;

        this.offset = offset;
        this.width = width;
        this.height = height;
        this.parent = parent;
        this.hitStun = hitStun;
        this.delay = delay;

//        Setting the world to the world of the parent character
        this.world = parent.world;



    }

    /*
    * Pre: A method in the GameObject class to update the multihit ability
    * Post: The multihit ability hits the opponent multiple times
    * */
    @Override
    public void update(float delta) {

//        Clears the arraylist
        collisions.clear();

//        If the user isn't on the last hit of the ability
        if (numOfCollisions > 1) {
//            Hit the opponent with the weaker hits, reduce the collision count by 1
            collisions.add(new Collider(offset, width, height, parent, leadUpPower, leadUpDamage, hitStun, delay));
            numOfCollisions -= 1;

//            If they are on the last hit
        } else if (numOfCollisions == 1) {

//            Hit the opponent with the strong hit, reduce the collision count by 1
            collisions.add(new Collider(offset, width, height, parent, lastHitPower, lastHitDamage, hitStun, delay));
            numOfCollisions -= 1;
        }
    }

    /*
    * Pre: A method in the game object class which draws sprites
    * Post: Sprites are drawn onto the screen
    * */
    @Override
    public void draw(SpriteBatch batch) {

    }
}


