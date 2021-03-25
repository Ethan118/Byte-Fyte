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

	public static void main (String[] arg) throws IOException {
		new Globals();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// Window settings
		config.forceExit = false;

		File settings = new File(Globals.workingDirectory + "settings.ini");

		if (!settings.exists()) {
			File file = new File(Globals.workingDirectory);
			file.mkdirs();

			settings.createNewFile();

			Wini ini = new Wini(settings);
			ini.add("Settings", "screen size", ScreenSizes.screenSize);
			ini.add("Settings", "music volume", Main.musicVolume);
			ini.store();
		} else {
			Wini ini = new Wini(settings);
			try {
				ScreenSizes.screenSize = Integer.parseInt(ini.get("Settings", "screen size"));
				Main.musicVolume = Integer.parseInt(ini.get("Settings", "music volume"));
			} catch (Exception ignored) {

			}
		}

		if (Globals.OS.contains("WIN")) {
			config.width = ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(0);
			config.height = ScreenSizes.screenSizes.get(ScreenSizes.screenSize).get(1);
		} else {
			config.width = 1280;
			config.height = 720;
		}

		config.title = "Byte Fyte";
		config.resizable = false;
		config.addIcon("icons/windows + linux.png", Files.FileType.Internal);
		config.addIcon("icons/windows old.png", Files.FileType.Internal);
		config.addIcon("icons/mac.png", Files.FileType.Internal);
		new LwjglApplication(new Main(), config);
	}
}
