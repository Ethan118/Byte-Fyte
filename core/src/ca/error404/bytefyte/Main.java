package ca.error404.bytefyte;

import ca.error404.bytefyte.constants.ControllerButtons;
import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.ini4j.Wini;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Random;

public class Main extends Game {
	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int WIDTH = 384;
	public static final int HEIGHT = 216;
	public static final float PPM = 100;

	public static int musicVolume = 5;
	public static int sfxVolume = 5;
	public static int cutsceneVolume = 5;

	public static float deadZone = 0.1f;

	public SpriteBatch batch;

	public static String songName = "";
	public double songLoopStart = Double.POSITIVE_INFINITY;
	public double songLoopEnd = Double.POSITIVE_INFINITY;

	public AssetManager manager;

	public static Array<Controller> controllers = new Array<>();
	public static Hashtable<Controller, Array<Integer>> recentButtons = new Hashtable<>();

	@Override
	public void create () {
		batch = new SpriteBatch();

		manager = new AssetManager();
		loadSongs();
		manager.finishLoading();

		if (Controllers.getControllers().size > 0) {
			for (int i=0; i < Controllers.getControllers().size; i++) {
				Controller cont = Controllers.getControllers().get(i);
				if (ControllerButtons.isXboxController(cont)) {
					controllers.add(cont);
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

		setScreen(new TestScene(this));
	}

	public void loadSongs() {
		// Locate file
		String fileName = "songdata.tsv";

		ClassLoader classLoader = Main.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);
		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

		BufferedReader br = new BufferedReader(streamReader);

		String oneData = "";
		int i = 0;

		// Loops through CSV and loads songs
		try {
			while ((oneData = br.readLine()) != null) {
				String[] data = oneData.split("	");

				if (i > 0) manager.load("audio/music/" + data[0] + ".wav", Music.class);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
					songName = data[0];
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

		Music music = manager.get("audio/music/" + songName + ".wav", Music.class);
		music.setLooping(true);
		return music;
	}

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
						start.add(Double.parseDouble(data[2]));
						end.add(Double.parseDouble(data[3]));
					}
				}

				i++;
			}

			// randomly selects song in list
			i = rand.nextInt(names.size);

			songName = names.get(i);
			songLoopStart = start.get(i);
			songLoopEnd = end.get(i);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (songLoopEnd == -1) {
			songLoopEnd = Double.POSITIVE_INFINITY;
		}

		Music music = manager.get("audio/music/" + songName + ".wav", Music.class);
		music.setLooping(true);
		return music;
	}

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
		Array<Double> start = new Array<>();
		Array<Double> end = new Array<>();

		// Loops through CSV and adds all songs to data
		try {
			Random rand = new Random();
			while ((oneData = br.readLine()) != null) {
				String[] data = oneData.split("	");

				if (i > 0) {
					for (int j=0; j < Integer.parseInt(data[6]); j++) {
						names.add(data[0]);
						start.add(Double.parseDouble(data[2]));
						end.add(Double.parseDouble(data[3]));
					}
				}

				i++;
			}

			// randomly selects song in list
			i = rand.nextInt(names.size);

			songName = names.get(i);
			songLoopStart = start.get(i);
			songLoopEnd = end.get(i);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (songLoopEnd == -1) {
			songLoopEnd = Double.POSITIVE_INFINITY;
		}

		Music music = manager.get("audio/music/" + songName + ".wav", Music.class);
		music.setLooping(true);
		return music;
	}

	// calls default render method
	@Override
	public void render () {
		super.render();
	}

	// this is bad code that should be replaced later
	public static Vector2 leftStick() {
		Vector2 moveVector = new Vector2();

		if (controllers.size > 0) {
			moveVector.x = Math.abs(controllers.get(0).getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS)) >= deadZone ? controllers.get(0).getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) : 0f;
			moveVector.y = Math.abs(controllers.get(0).getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS)) >= deadZone ? -controllers.get(0).getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) : 0f;
		}

		if (Gdx.input.isKeyPressed(Keys.MOVE_RIGHT)) moveVector.x += 1;
		if (Gdx.input.isKeyPressed(Keys.MOVE_LEFT)) moveVector.x -= 1;
		if (Gdx.input.isKeyPressed(Keys.MOVE_UP)) moveVector.y += 1;
		if (Gdx.input.isKeyPressed(Keys.MOVE_DOWN)) moveVector.y -= 1;

		return moveVector;
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
