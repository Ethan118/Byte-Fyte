package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.chars.Character;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

public class PlayerUI extends WidgetGroup {
    private final Character player;

    private final float percent;
    private final String name;

    Image base;
    Image head;
    Label nameLabel;
    Label percentLabel;

    public PlayerUI(Character player) {
        this.player = player;

        percent = player.percent;
        name = player.charname;

        TextureAtlas textureAtlas = new TextureAtlas("sprites/ui.atlas");

        base = new Image(new TextureRegion());
        head = new Image(new TextureRegion());
    }
}
