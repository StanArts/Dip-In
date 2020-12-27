package com.stangvel.dipin;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import helpers.GameManager;
import scenes.SplashScreen;

public class GameMain extends Game {

	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		GameManager.getInstance().initializeGameData();
		setScreen(new SplashScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public SpriteBatch getBatch() {
		return this.batch;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
