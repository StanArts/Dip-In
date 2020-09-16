package com.stangvel.dipin.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.stangvel.dipin.GameMain;

import helpers.GameInfo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setWindowedMode(GameInfo.WIDTH, GameInfo.HEIGHT);

		new Lwjgl3Application(new GameMain(), config);
	}
}
