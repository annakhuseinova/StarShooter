package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.basic.BasicStarShooterScreen;
import com.mygdx.game.math.Rect;
import com.mygdx.game.poolOfObjects.BulletPool;
import com.mygdx.game.sprites.Background;
import com.mygdx.game.sprites.HeroShip;
import com.mygdx.game.sprites.Star;


public class GameScreen extends BasicStarShooterScreen {


    private static final int STAR_COUNT = 64;
    Background background;
    Texture bg;
    TextureAtlas atlas;
    Star[] star;
    HeroShip heroShip;
    BulletPool bulletPool;

    public GameScreen(Game game) {
        super(game);

    }


    @Override
    public void show() {
        super.show();
        bg = new Texture("bg.png");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        star = new Star[STAR_COUNT];
        for (int i = 0; i < star.length ; i++) {
            star[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        heroShip = new HeroShip(atlas, bulletPool);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        deleteDestroyedElements();
        draw();

    }

    public void draw(){
        Gdx.gl.glClearColor(1, 0.4f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < star.length ; i++) {
            star[i].draw(batch);
        }
        heroShip.draw(batch);
        bulletPool.drawActiveObjects(batch);
        batch.end();
    }
    public void update(float delta){
        for (int i = 0; i < star.length ; i++) {
            star[i].update(delta);
        }
        heroShip.update(delta);
        bulletPool.updateActiveObjects(delta);
    }
    public void checkCollisions(){}

    @Override
    public boolean keyUp(int keycode) {
        heroShip.keyUp(keycode);
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        heroShip.keyDown(keycode);
        return super.keyDown(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        heroShip.touchDown(touch,pointer);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        heroShip.touchUp(touch,pointer);
        return super.touchUp(touch, pointer);
    }

    @Override
    protected void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (int i = 0; i < star.length ; i++) {
            star[i].resize(worldBounds);
        }
        heroShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        super.dispose();
    }

    public void deleteDestroyedElements(){
        bulletPool.freeDestroyedActiveObjects();
    }
}
