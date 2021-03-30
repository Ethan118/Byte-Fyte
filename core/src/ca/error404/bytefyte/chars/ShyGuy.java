package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.MultiHit;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.TMap;
import ca.error404.bytefyte.scene.TestScene;
import ca.error404.bytefyte.ui.PlayerHealth;
import ca.error404.bytefyte.scene.TMap;
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
    private ArrayList<Sound> healSongs;
    private ArrayList<Float> healSongLengths;

    private float currentSongLength;
    private int hovertimer = 2;
    private int timer = 2;
    Random rand = new Random();

    private boolean hasHovered = false;
    private float flyAcceleration = 0f;

    public ShyGuy(TMap screen, Vector2 spawnPoint, Controller controller, int playernumber) {
        super(screen, spawnPoint, controller, playernumber, "shyguy");
        manualSpriteOffset = new Vector2(2200, 300);
        healSongs = new ArrayList<>();
        healSongLengths = new ArrayList<>();
        projectilesOnScreen = new ArrayList<>(1);

        int i = 0;
        while (true) {
            i++;

//            Load all songs
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
            } catch (Exception e) {
                break;
            }
        }
    }

    /*
    * Pre: Delta time
    * Post: Updates character
    * */
    public void update(float deltaTime) {
        super.update(deltaTime);
//        Heals shy guy for song duration
        if (animState == AnimationState.SPECIAL_D) {
            if (animDuration <= 0) {
                percent = Math.max(percent - (currentSongLength), 0);
            }
        }

//        Ensuring shy guy's song stop if dead
        if (dead || knockedOff || animState == AnimationState.HIT) {

            animDuration = 0;
            for (Sound song : healSongs) {
                song.stop();
            }
        }

//        Ensuring he respawns
        if (knockedOff && stockCount != 0) {
            knockedOff = false;
        }

//        Allowing the animation of up b to play
        if (animState == AnimationState.SPECIAL_U && vel.y < 0) {
            lockAnim = false;
        }

//        Hovering for up b
        if (animState == AnimationState.SPECIAL_U) {
            specialUp();
            hasHovered = true;
        }

//        Sets cooldown for side b
        if (attackState == AttackState.SPECIAL && moveState == MovementState.WALK) {
            animDuration = 0.075f;
        }

    }

//    All abilities.  Will add colliders or move shy guy as applicable
    @Override
    void basicNeutral() {
        new Collider(new Vector2(20, 0), 5, 30, this, 2f, 5f, 0.25f, 0);
        resetControls();
    }

    @Override
    void basicSide() {
        new Collider(new Vector2(20, 0), 25, 30, this, 3f, 7f, 0.25f, 0);
        resetControls();
    }

    @Override
    void basicUp() {
        new Collider(new Vector2(0, 20), 30, 5, this, 3f, 7f, 0.25f, 0);
        resetControls();
    }

    @Override
    void basicDown() {
        new Collider(new Vector2(0, -10), 40, 20, this, 3f, 6f, 0.25f, 0);
        resetControls();
    }

    @Override
    void dashAttack() {
        new Collider(new Vector2(40, 0), 25, 30, this, 3f, 6f, 0.25f, 9f/60f);
        resetControls();
    }

    @Override
    void smashSide() {
        new Collider(new Vector2(40, 0), 50, 30, this, 5f, 7f, 0.25f, 13f / 60f);
        resetControls();
    }

    @Override
    void smashUp() {
        new Collider(new Vector2(0, 25), 30, 15, this, 5f, 7f, 0.25f, 12f / 60f, 7f / 60f);
        resetControls();
    }

    @Override
    void smashDown() {
        new Collider(new Vector2(20, 0), 25, 25, this, 5f, 7f, 0.25f, 6f / 60f, 19f / 60f);
        new Collider(new Vector2(-20, 0), 25, 25, this, 5f, 7f, 0.25f, 35f / 60f);
        resetControls();
    }

    @Override
    void specialNeutral() {
        new MultiHit(new Vector2(20, 0), 25, 30, this, 1f, 0f, 9, 0, 3, 0.25f, 2, true);
//        new Collider(new Vector2(20, 0), 25, 30, this, 0f, 4f, 70f / 60f, true, 0);
        resetControls();
    }

    @Override
    void specialSide() {
//        if (projectilesOnScreen.isEmpty()) {

//        If the delay timer (animation timer) is done
        if (animDuration == 0) {

//            Create a projectile based on the inputted user direction
            if (facingLeft) {
                projectilesOnScreen.add(new Projectile(this, new Vector2(pos.x, pos.y + 0.1f), new Vector2(-5, 1), 0.025f, 0, Float.POSITIVE_INFINITY, 2f, 7f, 0.25f, "spear", "sprites/shyguy.atlas", 12f / 60f));
            } else {
                projectilesOnScreen.add(new Projectile(this, new Vector2(pos.x, pos.y + 0.1f), new Vector2(5, 1), 0.025f, 0, Float.POSITIVE_INFINITY, 2f, 7f, 0.25f, "spear", "sprites/shyguy.atlas", 12f / 60f));
            }
        }

//        Reset the user controls
        resetControls();
    }

    @Override
    void specialUp() {

//        Hovers him for a frame
        if (animDuration == 0) {
            if (!hasHovered && !grounded) {
                vel.y = -3;
            }

            flyAcceleration = 0;
            hasHovered = true;
            animDuration = 1.4f;
        }

//        Exponentially flies him up after the initial frame
        vel.y = (flyAcceleration * flyAcceleration);
        flyAcceleration += 0.02;

//        Gets direction being faced
        if (moveVector.x > deadzone) {
            facingLeft = false;
        } else if (moveVector.x < -deadzone) {
            facingLeft = true;
        }
    }

    @Override
    void specialDown() {
        int i = rand.nextInt(healSongs.size() - 1);
        healSongs.get(i).play(Main.sfxVolume / 10f);

        animDuration = healSongLengths.get(i);
        moveTimer = healSongLengths.get(i);
        currentSongLength = healSongLengths.get(i);

        resetControls();
    }

    @Override
    void ultimate() {
    }

    @Override
    void airNeutral() {
//        A new multihit is created which will hit the opponent multiple times, as per the ability should
        new MultiHit(new Vector2(0, 0), 40, 40, this, 0.3f, 0, 7, 0.1f, 2f, 5, 15, false);
        resetControls();
    }

    @Override
    void airForward() {
//        If there are less than two of this projectile on the screen
        if (projectilesOnScreen.size() <= 2) {

//            Create a projectile based on the user direction
            if (facingLeft) {
                projectilesOnScreen.add(new Projectile(this, new Vector2(pos.x - 0.2f, pos.y + 0.1f), new Vector2(-4, -4), 0, 10, (float) Double.POSITIVE_INFINITY, 2f, 7f, 0.25f, "shoe", "sprites/shyguy.atlas", 9f / 60f));
            } else {
                projectilesOnScreen.add(new Projectile(this, new Vector2(pos.x + 0.2f, pos.y + 0.1f), new Vector2(4, -4), 0, 10, (float) Double.POSITIVE_INFINITY, 2f, 7f, 0.25f, "shoe", "sprites/shyguy.atlas", 9f / 60f));
            }
        }
    }

    @Override
    void airBack() {
        new Collider(new Vector2(-20, 0), 20, 20, this, 3f, 4f, 0.25f, 0);
    }

    @Override
    void airUp() {
        new Collider(new Vector2(0, 20), 40, 20, this, 4f, 4f, 0.5f, 0);
        resetControls();
    }

    @Override
    void airDown() {
        new Collider(new Vector2(10, -20), 25, 25, this, 5f, 7f, 0.25f, 6f / 60f);
        resetControls();
    }

}
