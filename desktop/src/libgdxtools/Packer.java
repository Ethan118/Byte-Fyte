package libgdxtools;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.util.Scanner;

public class Packer {
    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("in");
        String inputDir = scanner.nextLine();
        System.out.println("out");
        String outputDir = scanner.nextLine();
        System.out.println("filename");
        String packFileName = scanner.nextLine();

        TexturePacker.Settings settings = new TexturePacker.Settings();

        settings.maxWidth = 16384;
        settings.maxHeight = 16384;
        settings.stripWhitespaceX = true;
        settings.stripWhitespaceY = true;

        TexturePacker.process(settings, inputDir, outputDir, packFileName);
    }
}
