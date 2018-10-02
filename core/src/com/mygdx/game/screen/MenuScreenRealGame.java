package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.basic.ActionListener;
import com.mygdx.game.basic.BasicStarShooterScreen;
import com.mygdx.game.math.Rect;
import com.mygdx.game.sprites.Background;
import com.mygdx.game.sprites.ExitButton;
import com.mygdx.game.sprites.PlayButton;
import com.mygdx.game.sprites.Star;

public class MenuScreenRealGame extends BasicStarShooterScreen implements ActionListener {

    private static final int STAR_COUNT = 256;
    Music gameMusic;
    Music mainScreenMusic;
    Background background;
    Texture bg;
    TextureAtlas atlas;
    ExitButton exitButton;
    PlayButton playButton;
    Star[] star;


    public MenuScreenRealGame(Game game) {
        super(game);
        mainScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("data/mainscreenmusic.mp3"));
        mainScreenMusic.setLooping(true);
        mainScreenMusic.setVolume(0.5f);
        mainScreenMusic.play();
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("bg.png");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas("textures/menuAtlas.tpack");
        exitButton = new ExitButton(atlas,this);
        playButton = new PlayButton(atlas,this);
        star = new Star[STAR_COUNT ];
        for (int i = 0; i < star.length ; i++) {
            star[i] = new Star(atlas);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
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
        exitButton.draw(batch);
        playButton.draw(batch);
        batch.end();
    }
    public void update(float delta){
        for (int i = 0; i < star.length ; i++) {
            star[i].update(delta);
        }

    }
    @Override
    protected void resize(Rect worldBounds) {

        background.resize(worldBounds);
        for (int i = 0; i < star.length ; i++) {
            star[i].resize(worldBounds);
        }
        exitButton.resize(worldBounds);
        playButton.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        exitButton.touchDown(touch,pointer);
        playButton.touchDown(touch,pointer);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        exitButton.touchUp(touch,pointer);
        playButton.touchUp(touch,pointer);
        return super.touchUp(touch,pointer);
    }

    @Override
    public void actionPerformed(Object source) {

        if (source == exitButton){
            Gdx.app.exit();
        }else if (source == playButton){

            mainScreenMusic.stop();
            game.setScreen(new GameScreen(game));
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("data/gamemusic.mp3"));
            gameMusic.setVolume(0.5f);
            gameMusic.setLooping(true);
            gameMusic.play();
        }

    }
}
