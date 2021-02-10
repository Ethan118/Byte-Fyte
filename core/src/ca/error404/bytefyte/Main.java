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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Main extends Game {
	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int WIDTH = 384;
	public static final int HEIGHT = 216;
	public static final float PPM = 100;
	public static final float FRICTION = 100;

	public SpriteBatch batch;

	public static String songName = "MEGALOVANIA";
	public double songLoopStart = 0;
	public double songLoopEnd = 0;

	public static AssetManager manager;

	public static Controller controllerP1;
	public static Array<Integer> recentButtonsP1 = new Array<>();

	public static Controller controllerP2;
	public static Array<Integer> recentButtonsP2 = new Array<>();

	public static Controller controllerP3;
	public static Array<Integer> recentButtonsP3 = new Array<>();

	public static Controller controllerP4;
	public static Array<Integer> recentButtonsP4 = new Array<>();

	@Override
	public void create () {
		batch = new SpriteBatch();

		manager = new AssetManager();
		loadSongs();
		manager.finishLoading();

		if (Controllers.getControllers().size > 0) {
			controllerP1 = Controllers.getControllers().first();
			controllerP1.addListener(new ControllerAdapter() {
				@Override
				public boolean buttonDown(Controller controller, int buttonIndex) {
					recentButtonsP1.add(buttonIndex);
					return false;
				}
			});
		}

		setScreen(new TestScene(this));
	}

	public void loadSongs() {
		// Locate file
		String fileName = "audio/music/songdata.csv";

		ClassLoader classLoader = Main.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);
		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

		BufferedReader br = new BufferedReader(streamReader);

		String oneData = "";
		int i = 0;

		// Loops through CSV
		try {
			while ((oneData = br.readLine()) != null) {
				String[] data = oneData.split(",");

				if (i > 0) manager.load("audio/music/" + data[0] + ".wav", Music.class);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void newSong(String song) {
		// Locate file
		String fileName = "audio/music/songdata.csv";

		ClassLoader classLoader = Main.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);
		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

		BufferedReader br = new BufferedReader(streamReader);

		String oneData = "";
		int i = 0;
		boolean keepLooping = true;

		// Loops through CSV
		try {
			while ((oneData = br.readLine()) != null && keepLooping) {
				String[] data = oneData.split(",");

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
	}

	public void songFromSeries(String series) {
		// Locate file
		String fileName = "audio/music/songdata.csv";

		ClassLoader classLoader = Main.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);
		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

		BufferedReader br = new BufferedReader(streamReader);

		String oneData = "";
		int i = 0;
		Array<String> names = new Array<>();
		Array<Double> start = new Array<>();
		Array<Double> end = new Array<>();

		// Loops through CSV
		try {
			Random rand = new Random();
			while ((oneData = br.readLine()) != null) {
				String[] data = oneData.split(",");

				if (i > 0 && data[4].equalsIgnoreCase(series)) {
					names.add(data[0]);
					start.add(Double.parseDouble(data[2]));
					end.add(Double.parseDouble(data[3]));
				}

				i++;
			}

			boolean choose = false;
			while (!choose) {
				for (i=0; i < names.size; i++) {
					int next = rand.nextInt(100);
					System.out.println(next);
					if (next == 0) {
						songName = names.get(i);
						songLoopStart = start.get(i);
						songLoopEnd = end.get(i);
						choose = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (songLoopEnd == -1) {
			songLoopEnd = Double.POSITIVE_INFINITY;
		}
	}

	@Override
	public void render () {
		super.render();
	}

	public static Vector2 leftStick() {
		Vector2 moveVector = new Vector2();

		if (controllerP1 != null) {
			moveVector.x = Math.abs(controllerP1.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS)) >= 0.1f ? controllerP1.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) : 0f;
			moveVector.y = Math.abs(controllerP1.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS)) >= 0.1f ? controllerP1.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) : 0f;
		}

		if (Gdx.input.isKeyPressed(Keys.MOVE_RIGHT)) moveVector.x += 1;
		if (Gdx.input.isKeyPressed(Keys.MOVE_LEFT)) moveVector.x -= 1;
		if (Gdx.input.isKeyPressed(Keys.MOVE_UP)) moveVector.y += 1;
		if (Gdx.input.isKeyPressed(Keys.MOVE_DOWN)) moveVector.y -= 1;

		return moveVector;
	}

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
