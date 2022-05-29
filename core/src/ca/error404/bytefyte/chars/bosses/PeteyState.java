package ca.error404.bytefyte.chars.bosses;

import ca.error404.bytefyte.Main;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;

/*
 * Pre: game launch, single player started, loading finished
 * Post: sets Petey's state for other use
 * */
public enum PeteyState implements State<Petey> {


    //Idle state code
    IDLE() {
        public void enter(Petey petey) {
            petey.elapsedTime = 0;
            petey.b2body.setLinearVelocity(new Vector2(0, -petey.flySpeed * 10));
        }

        // update state for Petey
        public void update(Petey petey) {
            int i = petey.rand.nextInt(500);

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

    // walking state
    WALK() {
        public void enter(Petey petey) {
            petey.elapsedTime = 0;
            petey.facingLeft = petey.rand.nextBoolean();

            //code for walking in the direction Petey is facing
            if (petey.facingLeft) {
                petey.b2body.setLinearVelocity(new Vector2(-petey.speed, petey.b2body.getLinearVelocity().y));
            } else {
                petey.b2body.setLinearVelocity(new Vector2(petey.speed, petey.b2body.getLinearVelocity().y));
            }

            petey.walk();
        }

        public void update(Petey petey) {
            int i = petey.rand.nextInt(350);

            // change Petey's state
            if (i == 2) {
                petey.state.changeState(IDLE);
            } else if (i == 3) {
                petey.state.changeState(FLY);
            } else if (i == 4) {
                petey.state.changeState(SPIN);
            }

            petey.walk();
        }

        public void exit(Petey petey) {
            petey.b2body.setLinearVelocity(new Vector2(0, petey.b2body.getLinearVelocity().y));
        }
    },

    //flight state
    FLY() {
        public void enter(Petey petey) {
            petey.elapsedTime = 0;
            petey.b2body.setLinearVelocity(new Vector2(petey.b2body.getLinearVelocity().x, petey.flySpeed));
        }

        public void update(Petey petey) {
            petey.fly();

            // makes Petey in the fall state if he flies off the screen
            if (petey.b2body.getPosition().y >= (petey.screen.mProp.get("height", Integer.class) * petey.screen.mProp.get("tileheight", Integer.class)) / Main.PPM + petey.getHeight()) {
                petey.state.changeState(FALL);
            }
        }
    },

    // fall state
    FALL() {
        public void enter(Petey petey) {
            petey.elapsedTime = 0;

            //sets petey's vertical speed
            petey.b2body.setLinearVelocity(new Vector2(petey.b2body.getLinearVelocity().x, -petey.flySpeed * 10));
            petey.b2body.setTransform(Main.players.get(0).b2body.getPosition().x, (petey.screen.mProp.get("height", Integer.class) * petey.screen.mProp.get("tileheight", Integer.class)) / Main.PPM + petey.getHeight(), petey.b2body.getTransform().getRotation());
        }

        public void update(Petey petey) {
            petey.fall();
        }

        public void exit(Petey petey) {
            petey.b2body.setLinearVelocity(new Vector2(petey.b2body.getLinearVelocity().x, 0));
        }
    },

    //puts Petey in Spin state
    SPIN() {
        public void enter(Petey petey) {
            petey.elapsedTime = 0;
            petey.spinTimer = petey.rand.nextInt(17) + 3;
            if (petey.rand.nextBoolean()) {
                petey.b2body.setLinearVelocity(new Vector2(petey.speed * 10, petey.speed * 10));
            } else {
                petey.b2body.setLinearVelocity(new Vector2(-petey.speed * 10, petey.speed * 10));
            }
        }

        //updates Pety's spin state if he runs out of time
        public void update(Petey petey) {
            petey.spin();
            petey.spinTimer -= petey.deltaTime;
            if (petey.spinTimer <= 0) {
               petey.state.changeState(FALL);
            }
        }
    },

    //hit state
    HIT() {
        public void enter(Petey petey) {
            petey.elapsedTime = 0;
            petey.hitTime = 0.5f;
            petey.b2body.setLinearVelocity(new Vector2(0, -petey.flySpeed * 10));
        }

        //ends the state if it runs out of time
        public void update(Petey petey) {
            petey.hitState();
            petey.hitTime -= petey.deltaTime;
            if (petey.hitTime <= 0) {
                petey.state.changeState(IDLE);
            }
        }
    };

    /*
     * Pre: Petey
     * Post:
     * */
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
