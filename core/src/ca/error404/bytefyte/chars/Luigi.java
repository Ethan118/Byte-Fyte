package ca.error404.bytefyte.chars;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.scene.BattleMap;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;

public class Luigi extends Character {
    Mario parent;

    public Luigi(BattleMap screen, Vector2 spawnPoint, Controller controller, int playerNumber, Mario parent) {
        super(screen, spawnPoint, controller, playerNumber, parent.charname, parent.playerName, parent.spriteScale, parent.hitboxScale);
        Main.players.remove(this);

        this.parent = parent;
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
