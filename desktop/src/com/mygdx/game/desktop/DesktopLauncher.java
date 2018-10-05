package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.StarShooterClass;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		float aspect = 3f/4f;
		config.resizable = false;
		config.width = 500;
		config.height = (int)(config.width/aspect);
		new LwjglApplication(new StarShooterClass(), config);

	}
}
