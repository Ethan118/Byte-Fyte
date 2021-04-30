package ca.error404.bytefyte.tools;

import ca.error404.bytefyte.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;


/*
 * Pre: delta time, class is called
 * Post: creates a cutscene
 * */
public class CutscenePlayer implements Disposable {

//    Initialize variables
    VideoPlayer videoPlayer = VideoPlayerCreator.createVideoPlayer();
    FileHandle file;
    Viewport viewport;
    Camera cam;

    /*
    * Constructor
    * Pre: A video file
    * Post: A new cutscene plays
    * */
    public CutscenePlayer(String filename) {

//        Setting variables
        file = Gdx.files.internal( "movies/" + filename + ".ogv");
        cam = new OrthographicCamera(1920, 1080);
        viewport = new FitViewport(1920, 1080, cam);
    }

    /*
    * Pre: A video file to play
    * Post: starts playing video file
    * */
    public void play() {

//        Play the file, catch any errors and notify developer
        try {
            videoPlayer.play(file);
            videoPlayer.update();
            videoPlayer.setVolume(Main.cutsceneVolume / 10f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Pre: A sprite batch
    * Post: draws current frame to screen
    * */
    public void draw(SpriteBatch batch) {

//        Update and draw the video player
        batch.setProjectionMatrix(cam.combined);
        videoPlayer.update();
        batch.draw(videoPlayer.getTexture(), -(1920f / 2f), -(1080f / 2f));
    }

    /*
    * Pre: A video
    * Post: Whether the video is playing
    * */
    public boolean isPlaying() {
        return videoPlayer.isPlaying();
    }

    /*
    * Pre: None
    * Post: Disposes the video player
    * */
    @Override
    public void dispose() {
        videoPlayer.dispose();
    }

    /*
     * Pre: None
     * Post: stops playing and clears from memory
     * */
    public void stop() {
        if (videoPlayer.isPlaying()) {
            videoPlayer.stop();
            dispose();
        }
    }
}
