package com.kacstudios.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FarmaniaGame extends BaseGame {
	public void create () {
		super.create();


		setActiveScreen( new MainMenu() );

	}
}
