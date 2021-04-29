package ca.error404.bytefyte.chars.bosses;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum PeteyState implements State<Petey> {


    IDLE() {
        public void update(Petey petey) {
            int i = petey.rand.nextInt(40);

            if (i == 2) {
                petey.state.changeState(WALK);
            } else if (i == 3) {
                petey.state.changeState(FLY);
            } else if (i == 4) {
                petey.state.changeState(SPIN);
            } else {
                petey.idle();
            }
        }
    },

    WALK() {},

    FLY() {},

    FALL() {},

    SPIN() {},

    HIT() {}
    ;

    @Override
    public void enter(Petey entity) {

    }

    @Override
    public void update(Petey entity) {

    }

    @Override
    public void exit(Petey entity) {

    }

    @Override
    public boolean onMessage(Petey entity, Telegram telegram) {
        return false;
    }
}
