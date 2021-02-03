package ca.error404.bytefyte;

import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
}
