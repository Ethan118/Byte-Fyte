package ca.error404.bytefyte.objects;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class HairPoint extends Sprite {
    private Character parent;
    public Vector2 pos;

    private TextureRegion texture;

    private Vector2 restPos;
    private float size;

    private Color color;

    public HairPoint(Character parent, String texturePath, String atlasPath, Vector2 restPos, float size, Color color) {
        TextureAtlas textureAtlas = Main.manager.get(String.format("sprites/%s.atlas", parent.charname), TextureAtlas.class);
        texture = textureAtlas.findRegion("hair");

        this.parent = parent;

        this.restPos = restPos;
        this.size = size;

        this.color = color;

        setRegion(texture);
        setBounds(pos.x - getWidth() / 2, pos.y - getHeight() / 2, getRegionWidth() / parent.spriteScale / Main.PPM, getRegionHeight() / parent.spriteScale / Main.PPM);
    }

    public void update(float delta) {
        setBounds(pos.x - getWidth() / 2, pos.y - getHeight() / 2, getRegionWidth() / parent.spriteScale / Main.PPM, getRegionHeight() / parent.spriteScale / Main.PPM);
    }
}
