package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.scene.BattleMap;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Mario extends Character {


    public Mario(BattleMap screen, Vector2 spawnPoint, Controller controller, int playerNumber) {
        super(screen, spawnPoint, controller, playerNumber, "marioluigi", "MARIO & LUIGI", 15, 16);

        manualSpriteOffset = new Vector2(0, 0);
        projectilesOnScreen = new ArrayList<>(1);
    }

    @Override
    void basicNeutral() {

    }

    @Override
    void basicSide() {

    }

    @Override
    void basicUp() {

    }

    @Override
    void basicDown() {

    }

    @Override
    void dashAttack() {

    }

    @Override
    void smashSide() {

    }

    @Override
    void smashUp() {

    }

    @Override
    void smashDown() {

    }

    @Override
    void specialNeutral() {

    }

    @Override
    void specialSide() {

    }

    @Override
    void specialUp() {

    }

    @Override
    void specialDown() {

    }

    @Override
    void ultimate() {

    }

    @Override
    void airNeutral() {

    }

    @Override
    void airForward() {

    }

    @Override
    void airBack() {

    }

    @Override
    void airUp() {

    }

    @Override
    void airDown() {

    }
}
