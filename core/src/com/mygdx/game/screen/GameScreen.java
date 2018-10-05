package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.basic.ActionListener;
import com.mygdx.game.basic.BasicStarShooterScreen;
import com.mygdx.game.math.Rect;
import com.mygdx.game.poolOfObjects.BulletPool;
import com.mygdx.game.poolOfObjects.EnemiesPool;
import com.mygdx.game.poolOfObjects.ExplosionsPool;
import com.mygdx.game.sprites.Background;
import com.mygdx.game.sprites.Bullet;
import com.mygdx.game.sprites.EnemyShip;
import com.mygdx.game.sprites.Explosion;
import com.mygdx.game.sprites.HeroShip;
import com.mygdx.game.sprites.MessageGameOver;
import com.mygdx.game.sprites.Star;
import com.mygdx.game.sprites.StartNewGameButton;
import com.mygdx.game.utilities.EnemiesGenerator;

import java.util.List;


public class GameScreen extends BasicStarShooterScreen implements ActionListener{

    private enum gameState { PLAYING, GAMEOVER}
    Sound newGameSound = Gdx.audio.newSound(Gdx.files.internal("data/newgamesound.mp3"));
    Sound soundOfExplosion = Gdx.audio.newSound(Gdx.files.internal("data/explosionsound.mp3"));;
    private static final int STAR_COUNT = 64;
    protected Sound shootingSound =  Gdx.audio.newSound(Gdx.files.internal("data/shootingsound.mp3"));
    Sound bulletSound = Gdx.audio.newSound(Gdx.files.internal("data/shootingsound.mp3"));
    Background background;
    Texture bg;
    TextureAtlas atlas;
    Star[] star;
    HeroShip heroShip;
    BulletPool bulletPool;
    EnemiesPool enemiesPool;
    EnemiesGenerator enemiesGenerator;
    ExplosionsPool explosionsPool;
    gameState gameState;
    MessageGameOver messageGameOver;
    StartNewGameButton startNewGameButton;

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
        explosionsPool = new ExplosionsPool(atlas);
        heroShip = new HeroShip(atlas, bulletPool,explosionsPool,shootingSound);
        enemiesPool = new EnemiesPool(bulletPool,explosionsPool,bulletSound,heroShip);
        enemiesGenerator = new EnemiesGenerator(enemiesPool,atlas,worldBounds);
        messageGameOver = new MessageGameOver(atlas);
        startNewGameButton = new StartNewGameButton(atlas,this);
        startNewGame();
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
        enemiesPool.drawActiveObjects(batch);
        explosionsPool.drawActiveObjects(batch);
        if (gameState == gameState.GAMEOVER){
            messageGameOver.draw(batch);
            startNewGameButton.draw(batch);
        }
        batch.end();
    }
    public void update(float delta){
        for (int i = 0; i < star.length ; i++) {
            star[i].update(delta);
        }

        explosionsPool.updateActiveObjects(delta);
        bulletPool.updateActiveObjects(delta);
        if (heroShip.isDestroyed()){
            gameState = gameState.GAMEOVER;
        }

        switch (gameState){
            case PLAYING:
                heroShip.update(delta);
                enemiesPool.updateActiveObjects(delta);
                enemiesGenerator.generateEnemies(delta);
                break;
            case GAMEOVER:
                break;
        }
    }
    public void checkCollisions(){
        // Ship collision check
        List<EnemyShip> enemiesList = enemiesPool.getActiveObjects();
        for(EnemyShip enemyShip: enemiesList){
            if (enemyShip.isDestroyed()){
                continue;
            }
            float minDistance = enemyShip.getHalfWidth()+ heroShip.getHalfWidth();
            if (enemyShip.pos.dst2(heroShip.pos)< minDistance*minDistance){
                enemyShip.markAsDestroyed();
                enemyShip.boom();
                heroShip.damage(10*enemyShip.getBulletDamage());
                return;
            }
        }
        // Enemy ship and heroShip bullet collision
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (EnemyShip enemyShip : enemiesList){
             if (enemyShip.isDestroyed()){
                 continue;
             }
             for (Bullet bullet : bulletList){
                 if (bullet.getBulletShooter() != heroShip || bullet.isDestroyed()){
                     continue;
                 }
                 if (enemyShip.isBulletCollision(bullet)){
                     bullet.markAsDestroyed();
                     enemyShip.damage(bullet.getDamage());
                 }
             }
        }
        // Checking if bullets strike the heroShip
        for (Bullet bullet: bulletList ){
            if (bullet.getBulletShooter() == heroShip || bullet.isDestroyed()){
                continue;
            }
            if (heroShip.isBulletCollision(bullet)){
                heroShip.damage(bullet.getDamage());
                bullet.markAsDestroyed();
            }
        }
    }
    @Override
    public boolean keyUp(int keycode) {
        if (gameState == gameState.PLAYING){
            heroShip.keyUp(keycode);
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (gameState == gameState.PLAYING){
            heroShip.keyDown(keycode);
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (gameState == gameState.PLAYING){
            heroShip.touchDown(touch,pointer);
        }
        startNewGameButton.touchDown(touch,pointer);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (gameState == gameState.PLAYING){
            heroShip.touchUp(touch,pointer);
        }
        startNewGameButton.touchUp(touch,pointer);
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
        explosionsPool.dispose();
        enemiesPool.dispose();
        super.dispose();
    }

    public void deleteDestroyedElements(){
        bulletPool.freeDestroyedActiveObjects();
        explosionsPool.freeDestroyedActiveObjects();
        enemiesPool.freeDestroyedActiveObjects();
    }

    private void startNewGame(){
        gameState = gameState.PLAYING;
        heroShip.startNewGame();
        bulletPool.freeAllActiveObjects();
        explosionsPool.freeAllActiveObjects();
        enemiesPool.freeAllActiveObjects();
    }
    @Override
    public void actionPerformed(Object source) {
        if (source == startNewGameButton){
            newGameSound.play();
            startNewGame();
        }
    }




}
