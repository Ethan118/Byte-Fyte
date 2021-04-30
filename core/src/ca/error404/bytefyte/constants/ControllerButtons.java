package ca.error404.bytefyte.constants;

import com.badlogic.gdx.controllers.Controller;

import java.util.Locale;

/*
 * Pre: controller detected
 * Post: pairs controller buttons with keys
 * */
public class ControllerButtons
{
    // Buttons
    public static final int A;
    public static final int B;
    public static final int X;
    public static final int Y;
    public static final int GUIDE;
    public static final int L_BUMPER;
    public static final int R_BUMPER;
    public static final int BACK;
    public static final int START;
    public static final int DPAD;
    public static final int L3;
    public static final int R3;


    // Axes
    /**
     * left trigger, -1 if not pressed, 1 if pressed, 0 is initial value
     **/
    public static final int L_TRIGGER;
    /**
     * right trigger, -1 if not pressed, 1 if pressed, 0 is initial value
     **/
    public static final int R_TRIGGER;
    /**
     * left stick vertical axis, -1 if up, 1 if down
     **/
    public static final int L_STICK_VERTICAL_AXIS;
    /**
     * left stick horizontal axis, -1 if left, 1 if right
     **/
    public static final int L_STICK_HORIZONTAL_AXIS;
    /**
     * right stick vertical axis, -1 if up, 1 if down
     **/
    public static final int R_STICK_VERTICAL_AXIS;
    /**
     * right stick horizontal axis, -1 if left, 1 if right
     **/
    public static final int R_STICK_HORIZONTAL_AXIS;

    static {
            A = 0;
            B = 1;
            X = 2;
            Y = 3;
            GUIDE = -1;
            L_BUMPER = 4;
            R_BUMPER = 5;
            BACK = 6;
            START = 7;
            DPAD = 0;
            L_TRIGGER = 2; // postive value
            R_TRIGGER = 2; // negative value
            L_STICK_VERTICAL_AXIS = 0; // Down = -1, up = 1
            L_STICK_HORIZONTAL_AXIS = 1; // Left = -1, right = 1
            R_STICK_VERTICAL_AXIS = 2; // Assume same story
            R_STICK_HORIZONTAL_AXIS = 3;
            L3 = 8;
            R3 = 9;
    }

    /**
     * Different names:
     * - Microsoft PC-joystick driver
     *
     * @return whether the {@link Controller} is an Xbox controller
     */
    public static boolean isXboxController(Controller controller) {
        String name = controller.getName().toLowerCase(Locale.ROOT);
        return (name.contains("xbox") || name.contains("bluetooth xinput compatible input device"));
    }
}
