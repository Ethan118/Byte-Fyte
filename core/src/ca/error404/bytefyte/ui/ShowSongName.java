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

public class ShowSongName extends GameObject {
    private final BitmapFont font;
    private float timer = 4f;
    private float speed = 600f;

    private float xPos = 10;

    private final GlyphLayout layout = new GlyphLayout();

    public ShowSongName() {
        super();
        Main.objectsToAdd.remove(this);
        Main.uiToAdd.add(this);
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/songNames.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 40;
        fontParameter.borderWidth = 5;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.WHITE;

        font = fontGenerator.generateFont(fontParameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Random rand = new Random();

        if (rand.nextInt(100) == 2) {
            layout.setText(font, "Ohw'v Pdnh D Ghdo! (Mxvw Vkdnh Pb Kdqg)", Color.WHITE, 1000f, Align.left, true);
        } else {
            layout.setText(font, Main.songName, Color.WHITE, 1000f, Align.left, true);
        }
    }

    @Override
    public void update(float delta) {
        timer -= delta;

        if (timer <= 0) {
            xPos -= speed * delta;
        } if (xPos <= -2000) {
            destroy();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        font.draw(batch, layout, xPos, 1070);
    }

    @Override
    public void destroy() {
        Main.uiToRemove.add(this);
    }
}
