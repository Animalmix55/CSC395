package com.kacstudios.game;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kacstudios.game.games.FarmaniaGame;

import java.awt.*;

public class DesktopLauncher {

	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();


		config.width = dimension.width;
		config.height = dimension.height;

		config.fullscreen = true;

		config.resizable = false;
//		config.samples = 1000;

		config.addIcon("misc/icon-256.png", Files.FileType.Internal);
		config.addIcon("misc/icon-64.png", Files.FileType.Internal);
		config.addIcon("misc/icon-32.png", Files.FileType.Internal);
		config.title = "Farmania";
		//config.fullscreen = true;

		new LwjglApplication(new FarmaniaGame(), config);
	}
}
