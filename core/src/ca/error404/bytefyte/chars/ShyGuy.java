package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;
import org.apache.commons.io.FileUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class ShyGuy extends Character {
    private  ArrayList<Sound> healSongs;
    private ArrayList<Float> healSongLengths;
    private int hovertimer = 2;
    private int timer = 2;
    Random rand = new Random();

    private boolean hasHovered = false;
    private float flyAcceleration = 0f;

    public ShyGuy(TestScene screen, Vector2 spawnPoint, Controller controller) {
        super(screen, spawnPoint, controller);
        manualSpriteOffset = new Vector2(2200, 300);
        healSongs = new ArrayList<>();
        healSongLengths = new ArrayList<>();


        int i = 0;
        while (true) {
            i++;

            try {
                healSongs.add(Gdx.audio.newSound(Gdx.files.internal(String.format("audio/sound effects/shyguy_song_%d.wav", i))));

                String fileName = String.format("audio/sound effects/shyguy_song_%d.wav", i);

                File file = new File("bye-bye.world");

                ClassLoader classLoader = Main.class.getClassLoader();
                InputStream inputStream = classLoader.getResourceAsStream(fileName);

                FileUtils.copyInputStreamToFile(inputStream, file);

                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                AudioFormat format = audioInputStream.getFormat();

                long audioFileLength = file.length();
                int frameSize = format.getFrameSize();
                float frameRate = format.getFrameRate();
                float durationInSeconds = (audioFileLength / (frameSize * frameRate));


                healSongLengths.add(durationInSeconds);
            } catch(Exception e) {
                break;
            }
        }
    }


    public void update(float deltaTime) {
        super.update(deltaTime);
        System.out.println(hasHovered);


        if (animState == AnimationState.SPECIAL_U && vel.y < 0) {
            lockAnim = false;
        }

        if (animState == AnimationState.SPECIAL_U) {
            specialUp();
            hasHovered = true;
        }
    }

    @Override
    void basicNeutral() {
        Collider hitBox = new Collider(new Vector2(20, 0), 5, 30, this, 2f, 5f, 0.25f);
        colliders.add(hitBox);
        moveVector = new Vector2(0, 0);
    }

    @Override
    void basicSide() {
        Collider hitBox = new Collider(new Vector2(20, 0), 25, 30, this, 3f, 7f, 0.25f);
        colliders.add(hitBox);
        moveVector = new Vector2(0, 0);
    }

    @Override
    void basicUp() {
        Collider hitBox = new Collider(new Vector2(0, 20), 30, 5, this, 3f, 7f, 0.25f);
        colliders.add(hitBox);
        moveVector = new Vector2(0, 0);
    }

    @Override
    void basicDown() {
        Collider hitBox = new Collider(new Vector2(0, -10), 40, 20, this, 3f, 6f, 0.25f);
        colliders.add(hitBox);
        moveVector = new Vector2(0, 0);
    }

    @Override
    void dashAttack() {
        moveVector = new Vector2(0, 0);
    }

    @Override
    void smashSide() {
        moveVector = new Vector2(0, 0);
    }

    @Override
    void smashUp() {
        moveVector = new Vector2(0, 0);
    }

    @Override
    void smashDown() {
        moveVector = new Vector2(0, 0);
    }

    @Override
    void specialNeutral() {
        moveVector = new Vector2(0, 0);
    }

    @Override
    void specialSide() {
        moveVector = new Vector2(0, 0);
    }

    @Override
    void specialUp() {
        if (animDuration == 0) {
            if (!hasHovered && !grounded) {
                vel.y = -3;
            }

            flyAcceleration = 0;
            hasHovered = true;
            animDuration = 1.4f;
        }

        vel.y = (flyAcceleration * flyAcceleration);
        flyAcceleration += 0.02;



//        vel.y = jumpPower + 3;
//        jumpsLeft = 0;
    }

    @Override
    void specialDown() {
        int i = rand.nextInt(healSongs.size() - 1);
        healSongs.get(i).play(Main.sfxVolume / 10f);

        animDuration = healSongLengths.get(i);
        moveTimer = healSongLengths.get(i);
        lockAnim = true;
    }

    @Override
    void ultimate() {
    }

    @Override
    void airNeutral() {
        Collider hitBox = new Collider(new Vector2(0, 0), 40, 40, this, 4f, 5f, 0.25f);
        colliders.add(hitBox);
    }

    @Override
    void airForward() {
    }

    @Override
    void airBack() {
        Collider hitBox = new Collider(new Vector2(-20, 0), 20, 20, this, 3f, 4f, 0.25f);
        colliders.add(hitBox);
    }

    @Override
    void airUp() {
        Collider hitBox = new Collider(new Vector2(0, 20), 40, 20, this, 4f, 4f, 0.5f);
        colliders.add(hitBox);
    }

    @Override
    void airDown() {
    }
}
