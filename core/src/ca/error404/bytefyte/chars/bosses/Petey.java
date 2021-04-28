package ca.error404.bytefyte.chars.bosses;

import ca.error404.bytefyte.GameObject;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;

import java.util.Random;

public class Petey extends GameObject {
    float deltaTime;
    public StateMachine<Petey, PeteyState> state;
    public float hp = 1000f;
    public Random rand = new Random();

    public Petey() {
        state = new DefaultStateMachine<>(this, PeteyState.IDLE);
    }

    @Override
    public void update(float delta) {
        state.update();
    }

    public void idle() {

    }
}
