package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.basic.BasicStarShooterScreen;

public class MenuScreen extends BasicStarShooterScreen{

    SpriteBatch batch;
    Texture img;
    Vector2 position;
    Vector2 direction;


    public MenuScreen(Game game){
        super(game);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        direction = new Vector2(screenX,(Gdx.graphics.getHeight()-screenY));
        System.out.println("touchDown screenX = " + screenX + " screenY = "+ (Gdx.graphics.getHeight() - screenY));
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        position = new Vector2(0,0);
        direction = new Vector2(0,0);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(2,0.5f,2.0f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img,position.x,position.y);
        if (position.x <= direction.x && position.y <= direction.y){
            position.add(direction.x/15,direction.y/15);
        }else {
            position.add((direction.x - position.x)/15,(direction.y-position.y)/15);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        super.dispose();
    }
}