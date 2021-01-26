package ca.error404.bytefyte.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ca.error404.bytefyte.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		config.width = 1280;
		config.height = 720;
		config.title = "Byte Fyte";
		config.resizable = false;
		System.out.println("H");
		config.addIcon("icons/mac.png", Files.FileType.Internal);
		config.addIcon("icons/windows + linux.png", Files.FileType.Internal);
		config.addIcon("icons/windows old.png", Files.FileType.Internal);
		new LwjglApplication(new Main(), config);
	}
}
