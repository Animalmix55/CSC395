package com.kacstudios.game;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kacstudios.game.games.FarmaniaGame;

public class DesktopLauncher {

	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();


		config.width = 1280;
		config.height = 720;

		config.resizable = true;
//		config.samples = 1000;

		config.addIcon("core/assets/misc/icon-256.png", Files.FileType.Internal);
		config.addIcon("core/assets/misc/icon-64.png", Files.FileType.Internal);
		config.addIcon("core/assets/misc/icon-32.png", Files.FileType.Internal);
		config.title = "Farmania";
		//config.fullscreen = true;

		new LwjglApplication(new FarmaniaGame(), config);
	}
}
