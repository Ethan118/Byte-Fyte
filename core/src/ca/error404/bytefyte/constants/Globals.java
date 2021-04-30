package ca.error404.bytefyte.constants;

import javax.sound.sampled.AudioFormat;
import java.io.File;
import java.util.ArrayList;


// Globals class
public class Globals {
    public static String workingDirectory;
    //here, we assign the name of the OS, according to Java, to a variable...
    public static String OS = (System.getProperty("os.name")).toUpperCase();
    //to determine what the workingDirectory is.
    //if it is some version of Windows

    public static ArrayList<AudioFormat> healSongWAV1 = new ArrayList<>();
    public static ArrayList<File> healSongWAV2 = new ArrayList<>();

    /*
    * Constructor
    * Pre: None
    * Post: Finds appdata folder
    * */
    public Globals() {

        if (OS.contains("WIN"))
        {
            //it is simply the location of the "AppData" folder
            workingDirectory = System.getenv("AppData");
        }
        //Otherwise, we assume Linux or Mac
        else
        {
            //in either case, we would start in the user's home directory
            workingDirectory = System.getProperty("user.home");
            //if we are on a Mac, we are not done, we look for "Application Support"
            workingDirectory += "/Library/Application Support";
        }
        //we are now free to set the workingDirectory to the subdirectory that is our
        //folder.

        workingDirectory += "/ByteFyte/";
    }
}
