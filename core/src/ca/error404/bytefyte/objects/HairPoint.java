package ca.error404.bytefyte.objects;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class HairPoint extends Sprite {
    private Character parent;

    public Vector2 pos;
    public Vector2 targetPos;

    public float size;

    private TextureRegion textureRegion;

    final float speed = 35.0f;

    public HairPoint(Character parent, float size, String texturePath, String atlasPath) {
        this.parent = parent;

        this.pos = parent.pos.cpy();
        this.targetPos = parent.pos.cpy();
        this.size = size / 6;

        TextureAtlas textureAtlas = Main.manager.get(String.format("sprites/%s.atlas", atlasPath), TextureAtlas.class);
        textureRegion = textureAtlas.findRegion(texturePath);

        setRegion(textureRegion);
        setBounds(pos.x - (getWidth() / 2), pos.y - (getHeight() / 2), (getRegionWidth() / this.parent.spriteScale / Main.PPM) * size, (getRegionHeight() / this.parent.spriteScale / Main.PPM) * size);
    }

    public void update(float delta) {
        pos.lerp(targetPos, speed * delta);

        setRegion(textureRegion);
        setBounds(pos.x - (getWidth() / 2), pos.y - (getHeight() / 2), (getRegionWidth() / this.parent.spriteScale / Main.PPM) * size, (getRegionHeight() / this.parent.spriteScale / Main.PPM) * size);
    }
}
