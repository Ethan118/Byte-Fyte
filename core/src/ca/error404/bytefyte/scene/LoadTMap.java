package ca.error404.bytefyte.scene;

import ca.error404.bytefyte.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.apache.commons.io.FileUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadTMap implements Screen {
    OrthographicCamera cam;
    Main game;
    String tmap;
    Viewport viewport;
    float rotation = 0;
    int rotateSpeed = 180;
    Texture loadTex;
    Texture loadTexSpin;
    TiledMap map;
    TmxMapLoader mapLoader = new TmxMapLoader();

    public LoadTMap(String tmap, Main game) {
        this.tmap = tmap;
        this.game = game;
        game.music = this.game.music;
        cam = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, cam);

        for (int i=1; i < 25; i++) {
            Main.audioManager.load(String.format("audio/sound effects/shysongs/shyguy_song_%d.wav", i), Sound.class);
        }

        mapLoader.getDependencies(tmap, Gdx.files.internal(tmap), null);
        mapLoader.loadAsync(Main.manager, tmap, Gdx.files.internal(tmap), null);
        map = mapLoader.loadSync(Main.manager, tmap, Gdx.files.internal(tmap), null);

        // plays a song so I can hear things
        game.music = game.newSong();
        game.music.setVolume(Main.musicVolume / 10f);
        game.music.play();

        loadTex = new Texture("sprites/load.png");
        loadTexSpin = new Texture("sprites/loadSpin.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Main.manager.update()) {
            loadTex.dispose();
            loadTexSpin.dispose();
            game.setScreen(new TMap(tmap, game, map));
        }

        // game.music looping
        if (game.music.getPosition() >= game.songLoopEnd) {
            game.music.setPosition((float) (game.music.getPosition() - (game.songLoopEnd - game.songLoopStart)));
        }

        if (Math.abs(rotation) >= 360) {
            rotation -= 360;
        }

        rotation += delta * rotateSpeed;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);
        //viewport.apply();
        game.batch.begin();
        game.batch.draw(new TextureRegion(loadTex), (1920 / 2f) - (loadTex.getWidth()) - 52.5f, -(1080 /2f) + 50);
        game.batch.draw(new TextureRegion(loadTexSpin), (1920 / 2f) - (loadTexSpin.getWidth()) - 20f, -(1080 /2f) + 20, (float) loadTexSpin.getWidth() / 2f, (float) loadTexSpin.getHeight() / 2f, (float) loadTexSpin.getWidth(), (float) loadTexSpin.getHeight(), 1f, 1f, rotation);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
