package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screen.MenuScreenRealGame;

public class StarShooterClass extends Game {

	@Override
	public void create() {
        setScreen(new MenuScreenRealGame(this));
	}
}
