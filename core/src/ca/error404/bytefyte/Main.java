package ca.error404.bytefyte;

import ca.error404.bytefyte.chars.Badeline;
import ca.error404.bytefyte.chars.Character;
import ca.error404.bytefyte.chars.Luigi;
import ca.error404.bytefyte.chars.bosses.Boss;
import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Globals;
import ca.error404.bytefyte.scene.LoadBattleMap;
import ca.error404.bytefyte.scene.LoadBossRoom;
import ca.error404.bytefyte.scene.ScreenWipe;
import ca.error404.bytefyte.scene.menu.CharacterSelect;
import ca.error404.bytefyte.scene.menu.TitleScreen;
import ca.error404.bytefyte.ui.Button;
import ca.error404.bytefyte.ui.MenuCursor;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.apache.commons.io.FileUtils;
import org.ini4j.Wini;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class Main extends Game {
	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int WIDTH = 384;
	public static final int HEIGHT = 216;
	public static final float PPM = 100;
	public static boolean debug = false;
	public static boolean stamina = false;
	public Music music;

	public static int musicVolume = 5;
	public static int sfxVolume = 5;
	public static int cutsceneVolume = 5;

	public static float deadZone = 0.25f;

	public SpriteBatch batch;

	public static String internalSongName = "";
	public static String songName = "";
	public double songLoopStart = Double.POSITIVE_INFINITY;
	public double songLoopEnd = Double.POSITIVE_INFINITY;

	public static AssetManager audioManager;
	public static AssetManager manager;

	public static Controller[] controllers = {null, null, null, null};
	public static Array<Controller> allControllers = new Array<>();
	public static Hashtable<Controller, Array<Integer>> recentButtons = new Hashtable<>();

	public static ArrayList<GameObject> gameObjects = new ArrayList<>();
	public static ArrayList<Character> players = new ArrayList<>();
	public static ArrayList<Luigi> luigis = new ArrayList<>();
	public static ArrayList<Badeline> badeline = new ArrayList<>();
	public static ArrayList<Boss> bosses = new ArrayList<>();
	public static ArrayList<GameObject> objectsToAdd = new ArrayList<>();
	public static ArrayList<GameObject> objectsToRemove = new ArrayList<>();

	public static ArrayList<GameObject> ui = new ArrayList<>();
	public static ArrayList<GameObject> uiToAdd = new ArrayList<>();
	public static ArrayList<GameObject> uiToRemove = new ArrayList<>();

	public static ArrayList<MenuCursor> cursors = new ArrayList<>();
	public static ArrayList<Button> buttons = new ArrayList<>();
	public static ArrayList<ScreenWipe> transitions = new ArrayList<>();

	public static BitmapFont battleNameFont;
	public static BitmapFont percentNumFont;
	public static BitmapFont percentFont;
	public static BitmapFont menuFont;

	public static boolean bill = false;

	/**
	 * Pre: N/A
	 * Post: Loads save data and creates global variables
	 */
	@Override
	public void create () {
		File save = new File(Globals.workingDirectory + "settings.ini");

		// Checks for save data
		try {
			// Reads from save data
			Wini ini = new Wini(save);

			bill = Boolean.parseBoolean(ini.get("Menu", "bill"));
			stamina = Boolean.parseBoolean(ini.get("Menu", "stamina"));
		} catch (Exception e) {
			// Writes default save data
			try {
				Wini ini = new Wini(save);

				ini.add("Menu", "bill", bill);
				ini.add("Menu", "stamina", stamina);
				ini.store();
			} catch (Exception ignored) {
			}
		}

		// if the save data doesn't say otherwise, have a 1:1000 chance of loading the funky menu
		if (!bill) {
			Random rand = new Random();
			int funky = rand.nextInt(1000);
			if (funky == 2) {
				bill = true;
			}
		}

		// Loads shy guy songs
		for (int i=0; i < 24; i++) {
			try {
				String fileName = String.format("audio/sound effects/shysongs/shyguy_song_%d.wav", i + 1);

				File file = new File(String.valueOf(Gdx.files.internal(String.format("audio/sound effects/shysongs/shyguy_song_%d.wav", i + 1))));

				ClassLoader classLoader = Main.class.getClassLoader();
				InputStream inputStream = classLoader.getResourceAsStream(fileName);

				FileUtils.copyInputStreamToFile(inputStream, file);

				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
				AudioFormat format = audioInputStream.getFormat();

				Globals.healSongWAV1.add(format);
				Globals.healSongWAV2.add(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Creates visual and auditory capability
		batch = new SpriteBatch();
		audioManager = new AssetManager();
		manager = new AssetManager();

		// Loads all fonts to be used
		loadFonts();

		// Gets all active controllers
		reloadControllers();

		setScreen(new TitleScreen(this));
	}

	/**
	 * Pre: N/A
	 * Post: Gets list of all active controllers
	 */
	public void reloadControllers() {
		// makes sure that there are controllers plugged in
		if (Controllers.getControllers().size > 0) {
			int currentController = 0;

			// loops through all controllers
			for (int i=0; i < Controllers.getControllers().size; i++) {
				Controller cont = Controllers.getControllers().get(i);

				// Checks for Xbox controllers
				if (ControllerButtons.isXboxController(cont)) {
					if (currentController < 4) {
						controllers[currentController] = cont;
						currentController += 1;
						recentButtons.put(cont, new Array<Integer>());

						// Creates controller
						cont.addListener(new ControllerAdapter() {
							public boolean buttonDown(Controller controller, int buttonIndex) {
								recentButtons.get(controller).add(buttonIndex);
								return false;
							}
						});
					}

					// Add controller to controller array
					allControllers.add(cont);
					recentButtons.put(cont, new Array<Integer>());
					cont.addListener(new ControllerAdapter() {
						public boolean buttonDown(Controller controller, int buttonIndex) {
							recentButtons.get(controller).add(buttonIndex);
							return false;
						}
					});
				}
			}
		}
	}

	/**
	 * Pre: N/A
	 * Post: Loads all fonts to be used
	 */
	public void loadFonts() {
		// Var Init
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/fighterNames.otf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		// Set font settings
		fontParameter.size = 20;
		fontParameter.color = Color.WHITE;

		battleNameFont = fontGenerator.generateFont(fontParameter);

		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/percent.ttf"));
		fontParameter.size = 90;
		fontParameter.borderWidth = 5;
		fontParameter.borderColor = Color.BLACK;
		fontParameter.shadowColor = new Color(28 / 255f, 28 / 255f, 28 / 255f, 1);
		fontParameter.shadowOffsetX = 5;
		fontParameter.shadowOffsetY = 5;

		percentNumFont = fontGenerator.generateFont(fontParameter);

		fontParameter.size = 20;

		percentFont = fontGenerator.generateFont(fontParameter);

		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/upheaval.ttf"));
		fontParameter.size = 90;
		fontParameter.borderWidth = 5;
		fontParameter.borderColor = Color.BLACK;
		fontParameter.shadowColor = new Color(28 / 255f, 28 / 255f, 28 / 255f, 1);
		fontParameter.shadowOffsetX = 5;
		fontParameter.shadowOffsetY = 5;
		menuFont = fontGenerator.generateFont(fontParameter);

		battleNameFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		percentFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		percentNumFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		fontGenerator.dispose();
	}

	/**
	 * Pre: Song Title
	 * Post: Loads the song to be played
	 */
	public Music newSong(String song) {
		// Locate file
		String fileName = "songdata.tsv";

		ClassLoader classLoader = Main.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);
		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

		BufferedReader br = new BufferedReader(streamReader);

		String oneData = "";
		int i = 0;
		boolean keepLooping = true;

		// Loops through CSV and changes song info if criteria met
		try {
			while ((oneData = br.readLine()) != null && keepLooping) {
				String[] data = oneData.split("	");

				if (i > 0 && data[0].equalsIgnoreCase(song)) {
					internalSongName = data[0];
					songName = data[1];
					songLoopStart = Double.parseDouble(data[2]);
					songLoopEnd = Double.parseDouble(data[3]);
					keepLooping = false;
				}

				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (songLoopEnd == -1) {
			songLoopEnd = Double.POSITIVE_INFINITY;
		}

		audioManager.load("audio/music/" + internalSongName + ".wav", Music.class);
		audioManager.finishLoading();

		Music music = audioManager.get("audio/music/" + internalSongName + ".wav", Music.class);
		music.setLooping(true);
		return music;
	}

	/**
	 * Pre: Game Series
	 * Post: Loads random song in series
	 */
	public Music songFromSeries(String series) {
		// Locate file
		String fileName = "songdata.tsv";

		ClassLoader classLoader = Main.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);
		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

		BufferedReader br = new BufferedReader(streamReader);

		String oneData = "";
		int i = 0;
		Array<String> names = new Array<>();
		Array<String> songNames = new Array<>();
		Array<Double> start = new Array<>();
		Array<Double> end = new Array<>();

		// Loops through CSV and adds info to lists if requirements met
		try {
			Random rand = new Random();
			while ((oneData = br.readLine()) != null) {
				String[] data = oneData.split("	");

				if (i > 0 && data[4].equalsIgnoreCase(series)) {
					for (int j=0; j < Integer.parseInt(data[6]); j++) {
						names.add(data[0]);
						songNames.add(data[1]);
						start.add(Double.parseDouble(data[2]));
						end.add(Double.parseDouble(data[3]));
					}
				}

				i++;
			}

			// randomly selects song in list
			i = rand.nextInt(names.size);

			internalSongName = names.get(i);
			songName = songNames.get(i);
			songLoopStart = start.get(i);
			songLoopEnd = end.get(i);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (songLoopEnd == -1) {
			songLoopEnd = Double.POSITIVE_INFINITY;
		}

		audioManager.load("audio/music/" + internalSongName + ".wav", Music.class);
		audioManager.finishLoading();

		Music music = audioManager.get("audio/music/" + internalSongName + ".wav", Music.class);
		music.setLooping(true);
		return music;
	}

	/**
	 * Pre: N/A
	 * Post: Loads Random song from entire OST
	 */
	public Music newSong() {
		// Locate file
		String fileName = "songdata.tsv";

		ClassLoader classLoader = Main.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);
		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

		BufferedReader br = new BufferedReader(streamReader);

		String oneData = "";
		int i = 0;
		Array<String> names = new Array<>();
		Array<String> songNames = new Array<>();
		Array<Double> start = new Array<>();
		Array<Double> end = new Array<>();

		// Loops through CSV and adds all songs to data
		try {
			Random rand = new Random();
			while ((oneData = br.readLine()) != null) {
				String[] data = oneData.split("	");

				if (i > 0) {
					for (int j=0; j < Integer.parseInt(data[6]); j++) {
						if (!data[4].equalsIgnoreCase("n/a")) {
							names.add(data[0]);
							songNames.add(data[1]);
							start.add(Double.parseDouble(data[2]));
							end.add(Double.parseDouble(data[3]));
						}
					}
				}

				i++;
			}

			// randomly selects song in list
			i = rand.nextInt(names.size);

			internalSongName = names.get(i);
			songName = songNames.get(i);
			songLoopStart = start.get(i);
			songLoopEnd = end.get(i);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (songLoopEnd == -1) {
			songLoopEnd = Double.POSITIVE_INFINITY;
		}

		audioManager.load("audio/music/" + internalSongName + ".wav", Music.class);
		audioManager.finishLoading();

		Music music = audioManager.get("audio/music/" + internalSongName + ".wav", Music.class);
		music.setLooping(true);
		return music;
	}

	// calls default render method
	@Override
	public void render () {
		super.render();
	}

	// checks if element is in list
	public static boolean contains(Array<Integer> array, final int num) {

		boolean result = false;

		for(int i : array){
			if(i == num){
				result = true;
				break;
			}
		}

		return result;
	}
}
