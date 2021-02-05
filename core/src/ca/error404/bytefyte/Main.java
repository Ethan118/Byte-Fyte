package ca.error404.bytefyte;

import ca.error404.bytefyte.constants.Keys;
import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Main extends Game {
	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int WIDTH = 384;
	public static final int HEIGHT = 216;
	public static final float PPM = 100;
	public static final float FRICTION = 100;

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new TestScene(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public static Vector2 leftStick() {
		Vector2 moveVector = new Vector2();

		if (Gdx.input.isKeyPressed(Keys.MOVE_RIGHT)) moveVector.x += 1;
		if (Gdx.input.isKeyPressed(Keys.MOVE_LEFT)) moveVector.x -= 1;
		if (Gdx.input.isKeyPressed(Keys.MOVE_UP)) moveVector.y += 1;
		if (Gdx.input.isKeyPressed(Keys.MOVE_DOWN)) moveVector.y -= 1;

		return moveVector;
	}
}
