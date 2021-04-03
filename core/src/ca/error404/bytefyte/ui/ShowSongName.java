package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.GameObject;
import ca.error404.bytefyte.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class ShowSongName extends GameObject {
    private BitmapFont font;
    private float timer = 4f;
    private float speed = 600f;

    private float xPos = 10;

    public ShowSongName() {
        super();
        Main.objectsToAdd.remove(this);
        Main.uiToAdd.add(this);
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 40;
        fontParameter.borderWidth = 5;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.WHITE;

        font = fontGenerator.generateFont(fontParameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
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
        font.draw(batch, Main.songName, xPos, 1070);
    }

    @Override
    public void destroy() {
        Main.uiToRemove.add(this);
    }
}
