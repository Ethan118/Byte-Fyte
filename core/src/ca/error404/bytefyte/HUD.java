package ca.error404.bytefyte;

import ca.error404.bytefyte.ui.PlayerHealth;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HUD implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;

    private float timeCount;

    Label timerLabel;
    Label placeHolder;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;

    public HUD() {
        batch = new SpriteBatch();
        timeCount = 300f;
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 80;
        fontParameter.color = Color.WHITE;

        font = fontGenerator.generateFont(fontParameter);

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

        header.add(timerLabel).growX().pad(10, 10, 0, 0);

        stage.addActor(header);
        stage.addActor(footer);
    }

    public void update(float delta) {
        for (PlayerHealth playerUI : Main.ui) {
            playerUI.update(delta);
        }
        Main.ui.addAll(Main.uiToAdd);
        Main.ui.removeAll(Main.uiToRemove);
        Main.uiToAdd.clear();
        Main.uiToRemove.clear();

        timeCount -= delta;
        timerLabel.setText(String.format("%.2f", timeCount));
    }

    public void draw() {
        batch.begin();
        for (PlayerHealth playerUI : Main.ui) {
            playerUI.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
