package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;

import java.util.Random;

// Class to show song names
public class ShowSongName extends GameObject {

//    Initializing variables
    private final BitmapFont font;
    private float timer = 4f;
    private float speed = 600f;

    private float xPos = 10;

    private final GlyphLayout layout = new GlyphLayout();

    /*
    * Constructor
    * Pre: None
    * Post: Shows the song name being played
    * */
    public ShowSongName() {

//        Calls the super() method
        super();

//        Adds this instance to the appropriate lists to render
        Main.objectsToAdd.remove(this);
        Main.uiToAdd.add(this);

//        Setting variables
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/songNames.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 40;
        fontParameter.borderWidth = 5;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.WHITE;

        font = fontGenerator.generateFont(fontParameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Random rand = new Random();

//        Adds chances for easter egg
        if (rand.nextInt(100) == 2) {
            layout.setText(font, "Ohw'v Pdnh D Ghdo! (Mxvw Vkdnh Pb Kdqg)", Color.WHITE, 1000f, Align.left, true);
//        If the easter egg doesn't happen, set the correct text
        } else {
            layout.setText(font, Main.songName, Color.WHITE, 1000f, Align.left, true);
        }
    }

    /*
    * Pre: Delta time
    * Post: Updates the song display
    * */
    @Override
    public void update(float delta) {
//        Updates the timer variable
        timer -= delta;

//        If the timer is below 0, move the song name left
        if (timer <= 0) {
            xPos -= speed * delta;
//            If the x position is off the screen, destroy it
        } if (xPos <= -2000) {
            destroy();
        }
    }

    /*
    * Pre: A sprite batch
    * Post: Draws the font
    * */
    @Override
    public void draw(SpriteBatch batch) {

//        Draw the font
        font.draw(batch, layout, xPos, 1070);
    }


    /*
    * Pre: None
    * Post: Adds this instance to the uiToRemove list
    * */
    @Override
    public void destroy() {
//        Post: Add this instance to the uiToRemove list
        Main.uiToRemove.add(this);
    }
}
