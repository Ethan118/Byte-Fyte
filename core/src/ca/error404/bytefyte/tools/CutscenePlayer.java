package ca.error404.bytefyte.tools;

import ca.error404.bytefyte.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
    public Viewport viewport;

    public CutscenePlayer(String filename) {
        file = Gdx.files.internal( "movies/" + filename + ".ogv");
        viewport = new FitViewport(1920, 1080, new OrthographicCamera());
    }

    public void play() {
        try {
            videoPlayer.play(file);
        } catch (Exception e) {
            // Do not
        }

        videoPlayer.update();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(videoPlayer.getTexture(), (-Main.WIDTH / 2) / Main.PPM, (-Main.HEIGHT / 2) / Main.PPM, Main.WIDTH / Main.PPM, Main.HEIGHT / Main.PPM);
        videoPlayer.update();
    }

    public boolean isPlaying() {
        return videoPlayer.isPlaying();
    }

    @Override
    public void dispose() {
        videoPlayer.dispose();
    }

    public void stop() {
        if (videoPlayer.isPlaying()) {
            videoPlayer.stop();
            dispose();
        }
    }
}
