package ca.error404.bytefyte.constants;

import java.util.ArrayList;

// Screen sizes class
public class ScreenSizes {
    public static final ArrayList<ArrayList<Integer>> screenSizes = new ArrayList<>();
    public static int screenSize = 3;
    public static boolean fullScreen = false;

    static {
        ArrayList<Integer> array = new ArrayList<Integer>();
        array.add(0, 16);
        array.add(1, 9);
        screenSizes.add(array);
        array = new ArrayList<>();

        array.add(0, 640);
        array.add(1, 360);
        screenSizes.add(array);
        array = new ArrayList<>();

        array.add(0, 1024);
        array.add(1, 576);
        screenSizes.add(array);
        array = new ArrayList<>();

        array.add(0, 1280);
        array.add(1, 720);
        screenSizes.add(array);
        array = new ArrayList<>();

        array.add(0, 1536);
        array.add(1, 864);
        screenSizes.add(array);
        array = new ArrayList<>();

        array.add(0, 1920);
        array.add(1, 1080);
        screenSizes.add(array);
        array = new ArrayList<>();

        array.add(0, 2304);
        array.add(1, 1296);
        screenSizes.add(array);
        array = new ArrayList<>();

        array.add(0, 2560);
        array.add(1, 1440);
        screenSizes.add(array);
        array = new ArrayList<>();

        array.add(0, 3456);
        array.add(1, 1944);
        screenSizes.add(array);
        array = new ArrayList<>();

        array.add(0, 3840);
        array.add(1, 2160);
        screenSizes.add(array);
        array = new ArrayList<>();

        array.add(0, 4736);
        array.add(1, 2664);
        screenSizes.add(array);
        array = new ArrayList<>();

        array.add(0, 5120);
        array.add(1, 2880);
        screenSizes.add(array);
        array = new ArrayList<>();

        array.add(0, 7680);
        array.add(1, 4320);
        screenSizes.add(array);
    }
}
