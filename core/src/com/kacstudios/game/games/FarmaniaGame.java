package com.kacstudios.game.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kacstudios.game.screens.MainMenu;
import com.kacstudios.game.utilities.Setting;

public class FarmaniaGame extends BaseGame {

	private Viewport viewport;
	private PerspectiveCamera camera;

	public static Sound music;
	public static int musicid;

	public static Music Gamenoise;
	public static int Gameid;

	public static Music walkingSound;
	public static float volume = 0;

	public void create () {
		super.create();


		music = Gdx.audio.newSound(Gdx.files.internal("MusicAndGame/Music.ogg"));
		long musicid = music.loop(Setting.MusicVolume*0.01f);

		Gamenoise = Gdx.audio.newMusic(Gdx.files.internal("MusicAndGame/gamenoise.ogg"));
		//long Gameid = Gamenoise.loop(Setting.GameVolume*0.01f);
		Gamenoise.setLooping(true);
		Gamenoise.setVolume(Setting.GameVolume*0.05f);
		Gamenoise.play();

		walkingSound = Gdx.audio.newMusic(Gdx.files.internal("SoundEffects/walking.ogg"));
		//walkingSound.setLooping(true);
		walkingSound.setVolume(0);
		//walkingSound.play();


		setActiveScreen( new MainMenu() );
		camera = new PerspectiveCamera();
		viewport = new FitViewport(1280,720, camera);


	}

	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
