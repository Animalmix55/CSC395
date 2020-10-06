package com.kacstudios.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FarmaniaGame extends BaseGame {

	private Viewport viewport;
	private PerspectiveCamera camera;

	public void create () {
		super.create();

		setActiveScreen( new MainMenu() );
		camera = new PerspectiveCamera();
		viewport = new FitViewport(1280,720, camera);


	}

	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
