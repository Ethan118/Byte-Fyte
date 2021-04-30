package ca.error404.bytefyte;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class HUD implements Disposable {
    public Stage stage;
    private final Viewport viewport;
    private final SpriteBatch batch;

    private float timeCount;

    Label timerLabel;
    Label placeHolder;

    private final FreeTypeFontGenerator fontGenerator;
    private final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private final BitmapFont font;

    /*
     * Constructor
     * Pre: None
     * Post: creates a hud
     * */
    public HUD() {
        batch = new SpriteBatch();
        timeCount = 300f;
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/songNames.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 80;
        fontParameter.borderWidth = 5;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.WHITE;

        font = fontGenerator.generateFont(fontParameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;

        viewport = new FitViewport(1920, 1080, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table header = new Table();
        Table footer = new Table();

        header.top();
        header.setFillParent(true);

        footer.bottom();
        footer.setFillParent(true);

        timerLabel = new Label(String.format("%.2f", timeCount), style);
        timerLabel.setAlignment(Align.left);

        // header.add(timerLabel).growX().pad(10, 10, 0, 0);

        stage.addActor(header);
        stage.addActor(footer);

    }

    /*
     * Pre: delta time, class is called
     * Post: updates the hud
     * */
    public void update(float delta) {
        for (GameObject ui : Main.ui) {
            ui.update(delta);
        }
        Main.ui.addAll(Main.uiToAdd);
        Main.ui.removeAll(Main.uiToRemove);
        Main.uiToAdd.clear();
        Main.uiToRemove.clear();

        timeCount -= delta;
        timerLabel.setText(String.format("%.2f", timeCount));
    }

    /*
     * Pre: delta time, class is called
     * Post: draws the hud
     * */
    public void draw() {
        stage.draw();
        batch.begin();
        for (GameObject ui : Main.ui) {
            ui.draw(batch);
        }
        batch.end();
    }

    @Override
    /*
     * Pre: delta time, class is called
     * Post: deletes the hud
     * */
    public void dispose() {
        stage.dispose();
        font.dispose();
    }
}
