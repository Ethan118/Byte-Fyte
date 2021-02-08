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

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main extends Game {
	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int WIDTH = 384;
	public static final int HEIGHT = 216;
	public static final float PPM = 100;
	public static final float FRICTION = 100;

	public SpriteBatch batch;

	public static String songName = "badeline fight";
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
		manager.load("audio/music/" + Main.songName + ".wav", Music.class);
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

		// Loops through CSV and adds data to food class
		try {
			while ((oneData = br.readLine()) != null && keepLooping) {
				String[] data = oneData.split(",");

				if (i > 0 && data[0].equalsIgnoreCase(song)) {
					songName = data[0];
					songLoopStart = Double.parseDouble(data[1]);
					songLoopEnd = Double.parseDouble(data[2]);
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

	@Override
	public void render () {
		super.render();
	}

	public static Vector2 leftStick() {
		Vector2 moveVector = new Vector2();

		if (Gdx.input.isKeyPressed(Keys.MOVE_RIGHT)) moveVector.x += 1;
		if (Gdx.input.isKeyPressed(Keys.MOVE_LEFT)) moveVector.x -= 1;
		if (Gdx.input.isKeyPressed(Keys.MOVE_UP)) moveVector.y += 1;
		if (Gdx.input.isKeyPressed(Keys.MOVE_DOWN)) moveVector.y -= 1;

		if (controllerP1 != null) {
			moveVector.x = Math.abs(controllerP1.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS)) >= 0.3f ? controllerP1.getAxis(ControllerButtons.L_STICK_HORIZONTAL_AXIS) : 0f;
			moveVector.y = Math.abs(controllerP1.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS)) >= 0.3f ? controllerP1.getAxis(ControllerButtons.L_STICK_VERTICAL_AXIS) : 0f;
		}

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
