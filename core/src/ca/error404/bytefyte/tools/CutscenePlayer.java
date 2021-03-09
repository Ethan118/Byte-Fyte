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

public class CutscenePlayer implements Disposable {

    VideoPlayer videoPlayer = VideoPlayerCreator.createVideoPlayer();
    FileHandle file;
    Viewport viewport;
    Camera cam;

    // loads video file
    public CutscenePlayer(String filename) {
        file = Gdx.files.internal( "movies/" + filename + ".ogv");
        cam = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, cam);
    }

    // starts playing video file
    public void play() {
        try {
            videoPlayer.play(file);
            videoPlayer.setVolume(Main.cutsceneVolume / 10f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        videoPlayer.update();
    }

    // draws current frame to screen
    public void draw(SpriteBatch batch) {
        batch.setProjectionMatrix(cam.combined);
        viewport.apply();
        videoPlayer.update();
        batch.draw(videoPlayer.getTexture(), -(1920f / 2f), -(1080f / 2f));
    }

    public boolean isPlaying() {
        return videoPlayer.isPlaying();
    }

    @Override
    public void dispose() {
        videoPlayer.dispose();
    }

    // stops playing and clears from memory
    public void stop() {
        if (videoPlayer.isPlaying()) {
            videoPlayer.stop();
            dispose();
        }
    }
}
