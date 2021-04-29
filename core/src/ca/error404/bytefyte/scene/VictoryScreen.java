package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.constants.ScreenSizes;
import ca.error404.bytefyte.scene.menu.CharacterSelect;
import ca.error404.bytefyte.scene.menu.MenuScene;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class VictoryScreen extends MenuScene {
    private final Main game;
    private BitmapFont font;
    private boolean keyboardUsed = false;
    private GlyphLayout layout = new GlyphLayout();

    private Texture[] characterIcons;
    private boolean hasTransitioned = false;

    private Color[] colors = {new Color(255/255f, 17/255f, 35/255f, 1), new Color(0/255f, 139/255f, 255/255f, 1), new Color(255/255f, 185/255f, 21/255f, 1), new Color(11/255f, 185/255f, 52/255f, 1)};

    private Texture checkBox = new Texture(Gdx.files.internal("sprites/ready1.png"));
    private Texture checkMark = new Texture(Gdx.files.internal("sprites/checkmark.png"));

    private Button readyButton;
    private boolean[] ready;
    public String[] characters;

    private int numOfPlayers = 0;
    public VictoryScreen(Main game) {
        super(game);
        this.game = game;
        background = new Texture("sprites/menu/main_bg.png");
        this.characters = CharacterSelect.characters;

        for (String character: characters) {
            if (character != null) {
                numOfPlayers += 1;
            }
        }
        ready = new boolean[numOfPlayers];
        characterIcons = new Texture[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            characterIcons[i] = new Texture(String.format("sprites/menu/icons/%s_icon.png", BattleMap.alive.get(i).charname));

        }
    }

    public void render(float delta) {
        super.render(delta);
        if (checkReady()) {
            if (!hasTransitioned) {
                BattleMap.alive.clear();
                new ScreenWipe(new CharacterSelect(game), game);
                hasTransitioned = true;
            }
        } else {
            drawIcon();
        }

    }

    @Override
    public void show() {
        Main.cursors.clear();
        if (cam == null) {
            cam = new OrthographicCamera(ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0), ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1));
            cam.position.set((float)(ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0)) / 2, (float)(ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1)) / 2, cam.position.z);

        }
        cam.update();

        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
        }
        if (viewport == null) {
            viewport = new FitViewport(cam.viewportWidth, cam.viewportHeight, cam);
        }

        shapeRenderer.setProjectionMatrix(cam.combined);

        for (int i = 0; i < 4; i ++) {
            if (Main.controllers[i] != null) {
                new MenuCursor(new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2), Main.controllers[i], game);
            } else if (!keyboardUsed){
                new MenuCursor(new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2), null, game);
                keyboardUsed = true;
            }
        }

        new Button(new Vector2(960, 540), game, new Texture[] { new Texture("sprites/menu/main_bg.png"), new Texture("sprites/menu/main_bg.png") }) {
            public void click(){
                ready[cursor.getID()] = true;
        }};
    }

    private void drawIcon() {
        ShapeRenderer shapeRenderer = new ShapeRenderer();

        for (int i = 0; i < numOfPlayers; i++) {
            Character character = BattleMap.alive.get(i);

            character.facingLeft = false;
            shapeRenderer.setColor(colors[i]);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            float w = cam.viewportWidth/ numOfPlayers;
            float h = cam.viewportHeight;
            float y = 0;
            float x = w * i;
            shapeRenderer.rect(x, y, w, h);
            shapeRenderer.end();

            if (numOfPlayers == 1 || numOfPlayers == 2) {
                font = Main.menuFont;
            } else if (numOfPlayers == 3) {
                FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/upheaval.ttf"));
                FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
                parameter.size = 70;
                parameter.borderWidth = 5;
                parameter.borderColor = Color.BLACK;
                parameter.color = Color.WHITE;
                parameter.shadowColor = new Color(28 / 255f, 28 / 255f, 28 / 255f, 1);
                parameter.shadowOffsetX = 5;
                parameter.shadowOffsetY = 5;
                font = fontGenerator.generateFont(parameter);
            } else {
                FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/upheaval.ttf"));
                FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
                parameter.size = 50;
                parameter.borderWidth = 5;
                parameter.borderColor = Color.BLACK;
                parameter.color = Color.WHITE;
                parameter.shadowColor = new Color(28 / 255f, 28 / 255f, 28 / 255f, 1);
                parameter.shadowOffsetX = 5;
                parameter.shadowOffsetY = 5;
                font = fontGenerator.generateFont(parameter);
            }

            game.batch.begin();
            layout.setText(font, "#" + character.rank, Color.WHITE, 0, Align.center, false);
            font.draw(game.batch, layout, x + w/2, h - h / 10);
            game.batch.draw(checkBox, x + w/2 - (checkBox.getWidth() / 2f), h / 10);
            game.batch.draw(characterIcons[i], x + w/2 - (((float) characterIcons[i].getWidth() / numOfPlayers) / 2f), h / 2 - (((float) characterIcons[i].getHeight() / numOfPlayers) / 2f), (float)characterIcons[i].getWidth() / numOfPlayers, (float)characterIcons[i].getHeight() / numOfPlayers);
            if (ready[i]) {
                game.batch.draw(checkMark, x + w/2 - (checkBox.getWidth() / 2f), h / 10);
            }
            game.batch.end();

        }

    }

    @Override
    public void resize(int width, int height) {

    }
    private boolean checkReady() {
        for (boolean playerIsReady: ready) {
            if (!playerIsReady) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.batch.dispose();
        shapeRenderer.dispose();

    }
}
