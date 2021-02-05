package ca.error404.bytefyte;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import java.io.FileNotFoundException;

public class CutscenePlayer {

    VideoPlayer videoPlayer = VideoPlayerCreator.createVideoPlayer();
    FileHandle file;
    float currentFrame = 0;

    public CutscenePlayer(String filename) {
        file = Gdx.files.internal( "movies/" + filename + ".ogv");
    }

    public void play() {
        try {
            videoPlayer.play(file);
            videoPlayer.update();
        } catch (Exception e) {
            // Do not
        }
    }

    public void draw(SpriteBatch batch) {
        videoPlayer.update();
        System.out.println(videoPlayer.getCurrentTimestamp());
        batch.draw(videoPlayer.getTexture(), (-Main.WIDTH / 2) / Main.PPM, (-Main.HEIGHT / 2) / Main.PPM, Main.WIDTH / Main.PPM, Main.HEIGHT / Main.PPM);
    }

    public boolean isPlaying() {
        return videoPlayer.isPlaying();
    }
}
