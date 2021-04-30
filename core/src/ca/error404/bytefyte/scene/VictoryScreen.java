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

// Class to create a victory, extends Menuscene to access those methods
public class VictoryScreen extends MenuScene {

//    Initializing variable
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

    /*
    * Constructor
    * Pre: A game instance
    * Post: A new victory screen
    * */
    public VictoryScreen(Main game) {

//        Calls the super() method
        super(game);

//        Sets variables
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

    /*
    * Pre: None
    * Post: A method that renders the victory screen
    * */
    public void render(float delta) {

//        Calls the render method of the super class
        super.render(delta);

//        Draws the icons
        drawIcon();

//        Checks if all players have readied up, and if they have, transition the screen if not
//        already in a transition
        if (checkReady()) {
            if (!hasTransitioned) {
                new ScreenWipe(new CharacterSelect(game), game);
                hasTransitioned = true;
            }
        }

//        For loop to draw the transition
        for (int i=0; i < Main.transitions.size(); i++) Main.transitions.get(i).draw();
    }


    /*
    * Pre: None
    * Post: Instantiates all necessary cameras and viewports
    * */
    @Override
    public void show() {

//        Clears cursors
        Main.cursors.clear();

//        Sets camera if null
        if (cam == null) {
            cam = new OrthographicCamera(ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0), ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1));
            cam.position.set((float)(ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0)) / 2, (float)(ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1)) / 2, cam.position.z);

        }
        cam.update();

//        Sets shaperenderer if null
        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
        }

//        Sets viewport if null
        if (viewport == null) {
            viewport = new FitViewport(cam.viewportWidth, cam.viewportHeight, cam);
        }

        shapeRenderer.setProjectionMatrix(cam.combined);

//        Sets menucursors
        for (int i = 0; i < 4; i ++) {
            if (Main.controllers[i] != null) {
                new MenuCursor(new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2), Main.controllers[i], game);
            } else if (!keyboardUsed){
                new MenuCursor(new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2), null, game);
                keyboardUsed = true;
            }
        }

//        Sets a button the size of the screen which readies up the player when clicked
        new Button(new Vector2(960, 540), game, new Texture[] { new Texture("sprites/menu/main_bg.png"), new Texture("sprites/menu/main_bg.png") }) {
            public void click(){
                ready[cursor.getID()] = true;
        }};
    }

    /*
    * Pre: None
    * Post: Draws all icons
    * */
    private void drawIcon() {

//        Initializes the shape renderer
        ShapeRenderer shapeRenderer = new ShapeRenderer();

//        For loop that cycles through all players' characters
        for (int i = 0; i < numOfPlayers; i++) {

//            Gets the player in the list of characters
            Character character = BattleMap.alive.get(i);

//            Initializes the shaperenderer as well as other variables
            character.facingLeft = false;
            shapeRenderer.setColor(colors[i]);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            float w = cam.viewportWidth/ numOfPlayers;
            float h = cam.viewportHeight;
            float y = 0;
            float x = w * i;

//            Creates a rectangle at desired dimensions
            shapeRenderer.rect(x, y, w, h);
            shapeRenderer.end();

//            Sets the font size based on number of players (to fit screen)
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
//            Draws position
            layout.setText(font, "#" + character.rank, Color.WHITE, 0, Align.center, false);
            font.draw(game.batch, layout, x + w/2, h - h / 10);

//            Draws the checkbox
            game.batch.draw(checkBox, x + w/2 - (checkBox.getWidth() / 2f), h / 10);

//            Draws the player icons based on number of players
            if (numOfPlayers > 1) {
                game.batch.draw(characterIcons[i], x + w / 2 - (((float) characterIcons[i].getWidth() / numOfPlayers) / 2f), h / 2 - (((float) characterIcons[i].getHeight() / numOfPlayers) / 2f), (float) characterIcons[i].getWidth() / numOfPlayers, (float) characterIcons[i].getHeight() / numOfPlayers);
            } else {
                game.batch.draw(characterIcons[i], x + w/2 - (((float) characterIcons[i].getWidth() / (numOfPlayers + 1)) / 2f), h / 2 - (((float) characterIcons[i].getHeight() / (numOfPlayers + 1)) / 2f), (float)characterIcons[i].getWidth() / (numOfPlayers + 1), (float)characterIcons[i].getHeight() / (numOfPlayers + 1));
            }

//            If the player is ready, a checkmark is drawn for them
            if (ready[i]) {
                game.batch.draw(checkMark, x + w/2 - (checkBox.getWidth() / 2f), h / 10);
            }
            game.batch.end();

        }

    }

    /*
    * Pre: Dimensions
    * Post: Allows resize
    * */
    @Override
    public void resize(int width, int height) {

    }

    /*
    * Pre: None
    * Post: Returns true if all players have readied up
    * */
    private boolean checkReady() {

//        Cycles through the player ready list.  If any player is not ready, it returns false
        for (boolean playerIsReady: ready) {
            if (!playerIsReady) {
                return false;
            }
        }

//        If all players are ready, it returns true
        return true;
    }

    /*
    * Pre: None
    * Post: None
    * */
    @Override
    public void pause() {

    }

    /*
     * Pre: None
     * Post: None
     * */
    @Override
    public void resume() {

    }

    /*
     * Pre: None
     * Post: None
     * */
    @Override
    public void hide() {

    }

    /*
     * Pre: None
     * Post: Disposes all assets
     * */
    @Override
    public void dispose() {
        game.batch.dispose();
        shapeRenderer.dispose();

    }
}
