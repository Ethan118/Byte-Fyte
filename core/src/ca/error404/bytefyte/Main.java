package ca.error404.bytefyte;

import ca.error404.bytefyte.scene.TestScene;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
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
