package ca.error404.bytefyte.desktop;

import ca.error404.bytefyte.constants.Globals;
import ca.error404.bytefyte.constants.ScreenSizes;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ca.error404.bytefyte.Main;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;



public class DesktopLauncher {

	/*
	 * Pre: None
	 * Post: launches the game with its settings
	 * */
	public static void main (String[] arg) throws IOException {
		new Globals();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// Window settings
		config.forceExit = false;

		File settings = new File(Globals.workingDirectory + "settings.ini");

		// Checks for active save data
		if (!settings.exists()) {
			// Creates save file and writes default save data
			File file = new File(Globals.workingDirectory);
			file.mkdirs();

			settings.createNewFile();

			Wini ini = new Wini(settings);
			ini.add("Settings", "screen size", ScreenSizes.screenSize);
			ini.add("Settings", "music volume", Main.musicVolume);
			ini.add("Settings", "sfx volume", Main.sfxVolume);
			ini.add("Settings", "cutscene volume", Main.cutsceneVolume);
			ini.add("Settings", "fullscreen", ScreenSizes.fullScreen);
			ini.add("Settings", "debug", Main.debug);
			ini.add("Menu", "bill", Main.bill);
			ini.add("Menu", "stamina", Main.stamina);
			ini.store();
		} else {
			Wini ini = new Wini(settings);
			// loads save data and assigns variables
			try {
				ScreenSizes.screenSize = Integer.parseInt(ini.get("Settings", "screen size"));
				Main.musicVolume = Integer.parseInt(ini.get("Settings", "music volume"));
				Main.cutsceneVolume = Integer.parseInt(ini.get("Settings", "cutscene volume"));
				Main.sfxVolume = Integer.parseInt(ini.get("Settings", "sfx volume"));
				ScreenSizes.fullScreen = Boolean.parseBoolean(ini.get("Settings", "fullscreen"));
				Main.debug = Boolean.parseBoolean(ini.get("Settings", "debug"));
				Main.bill = Boolean.parseBoolean(ini.get("Menu", "bill"));
				Main.stamina = Boolean.parseBoolean(ini.get("Menu", "stamina"));
			} catch (Exception ignored) {

			}
		}

		// Sets screen size
		config.width = ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0);
		config.height = ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1);

		// toggles fullscreen
		config.fullscreen = ScreenSizes.fullScreen;

		// window title and window icon
		config.title = "Byte Fyte";
		config.resizable = false;
		config.addIcon("icons/windows + linux.png", Files.FileType.Internal);
		config.addIcon("icons/windows old.png", Files.FileType.Internal);
		config.addIcon("icons/mac.png", Files.FileType.Internal);

		// Starts app
		new LwjglApplication(new Main(), config);
	}
}
