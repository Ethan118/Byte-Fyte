package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.objects.Collider;
import ca.error404.bytefyte.objects.MultiHit;
import ca.error404.bytefyte.objects.Projectile;
import ca.error404.bytefyte.scene.TMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;
import org.apache.commons.io.FileUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class MasterChief extends Character {
    private ArrayList<Sound> healSongs;
    private ArrayList<Float> healSongLengths;

    private float currentSongLength;
    private int hovertimer = 2;
    private int timer = 2;
    Random rand = new Random();

    private boolean hasHovered = false;
    private float flyAcceleration = 0f;

    public MasterChief(TMap screen, Vector2 spawnPoint, Controller controller, int playernumber) {
        super(screen, spawnPoint, controller, playernumber, "masterchief");
        manualSpriteOffset = new Vector2(1100, 350);
        healSongs = new ArrayList<>();
        healSongLengths = new ArrayList<>();
        projectilesOnScreen = new ArrayList<>(1);
    }

    /*
    * Pre: Delta time
    * Post: Updates character
    * */
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

//    All abilities.  Will add colliders or move shy guy as applicable
    @Override
    void basicNeutral() {
        resetControls();
    }

    @Override
    void basicSide() {
        resetControls();
    }

    @Override
    void basicUp() {
        resetControls();
    }

    @Override
    void basicDown() {
        resetControls();
    }

    @Override
    void dashAttack() {
        resetControls();
    }

    @Override
    void smashSide() {
        resetControls();
    }

    @Override
    void smashUp() {
        resetControls();
    }

    @Override
    void smashDown() {
        resetControls();
    }

    @Override
    void specialNeutral() {
        resetControls();
    }

    @Override
    void specialSide() {
        resetControls();
    }

    @Override
    void specialUp() {
    }

    @Override
    void specialDown() {
        resetControls();
    }

    @Override
    void ultimate() {
    }

    @Override
    void airNeutral() {
        resetControls();
    }

    @Override
    void airForward() {
    }

    @Override
    void airBack() {
    }

    @Override
    void airUp() {
        resetControls();
    }

    @Override
    void airDown() {
        resetControls();
    }

}
