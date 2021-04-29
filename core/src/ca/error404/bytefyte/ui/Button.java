package ca.error404.bytefyte.ui;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;


public class Button {
    public Rectangle buttonRect;
    public Vector2 pos;
    public Texture texture;
    public Texture selectTexture;
    public String string;
    public Main game;
    public MenuCursor cursor;

    public Color unselectedColor = Color.WHITE;
    public Color selectedColor = Color.YELLOW;
    public GlyphLayout layout = new GlyphLayout();

    private boolean prevSelect = false;

    private Sound clickSound;

    public Button(Vector2 pos, Main game, String string) {
        this.buttonRect = new Rectangle();
        this.pos = pos;
        this.string = string;
        this.texture = null;
        this.game = game;
        Main.buttons.add(this);
        layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);

        this.buttonRect.set(pos.x - (layout.width / 2), pos.y - layout.height, layout.width, layout.height);
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound effects/menuChoose.wav"));
    }

    public Button(Vector2 pos, Main game, Texture[] texture) {
        this.buttonRect = new Rectangle();
        this.pos = pos;
        this.string = null;
        this.texture = texture[0];
        if (texture[1] != null) {
            this.selectTexture = texture[1];
        }
        this.game = game;
        Main.buttons.add(this);

        this.buttonRect.set(pos.x - (texture[0].getWidth() / 2f), pos.y - (texture[0].getHeight() / 2f), texture[0].getWidth(), texture[0].getHeight());
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound effects/menuChoose.wav"));
    }

    public void draw(SpriteBatch batch) {
        if (texture == null) {
            for (MenuCursor cursor : Main.cursors) {
                if (isCursorOver(cursor)) {
                    layout.setText(Main.menuFont, string, selectedColor, 0, Align.center, false);
                    break;
                } else {
                    layout.setText(Main.menuFont, string, unselectedColor, 0, Align.center, false);
                }
            }
            Main.menuFont.draw(batch, layout, pos.x, pos.y);
        } else {
            if (Main.cursors.size() == 0) {
                if (prevSelect) {
                    batch.draw(texture, buttonRect.x, buttonRect.y);
                } else {
                    batch.draw(selectTexture, buttonRect.x, buttonRect.y);
                }
            } else {
                for (MenuCursor cursor : Main.cursors) {
                    if (isCursorOver(cursor)) {
                        batch.draw(selectTexture, buttonRect.x, buttonRect.y);
                        prevSelect = false;
                        break;
                    } else {
                        batch.draw(texture, buttonRect.x, buttonRect.y);
                        prevSelect = true;
                    }
                }
            }
        }
    }

    public boolean isCursorOver(MenuCursor cursor) {
        return cursor.rect.overlaps(buttonRect);
    }

    public boolean isClicked() {
        for (MenuCursor cursor : Main.cursors) {
            if (cursor.controller != null) {
                if (Main.contains(Main.recentButtons.get(cursor.controller), ControllerButtons.A)) {
                    if (isCursorOver(cursor)) {
                        this.cursor = cursor;
                        return true;
                    }
                }
            } else {
                if (Gdx.input.isKeyJustPressed(Keys.MENU_SELECT)) {
                    if (isCursorOver(cursor)) {
                        this.cursor = cursor;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void update() {
        if (isClicked()) {
            click();
            clickSound.play();
        }
    }

    public void click() {
        System.out.println("ow");
    }
}
