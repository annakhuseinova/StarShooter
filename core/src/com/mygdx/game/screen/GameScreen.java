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
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.basic.ActionListener;
import com.mygdx.game.basic.BasicStarShooterScreen;
import com.mygdx.game.basic.Font;
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
    Sound bulletCollision = Gdx.audio.newSound(Gdx.files.internal("data/bulletcollision.mp3"));
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
    Font font;
    int frags;
    StringBuilder sbFrags = new StringBuilder();
    StringBuilder sbNumberOfLives = new StringBuilder();
    StringBuilder sbLevel = new StringBuilder();
    StringBuilder sbLevelToShow = new StringBuilder();
    private static final String FRAGS = "Frags: ";
    private static final String NUMBER_OF_LIVES = "Lives: ";
    private static final String LEVEL = "Level: ";
    private static final String LEVEL_TO_SHOW = "LEVEL ";
    private float timer;
    private float interval = 120f;


    public GameScreen(Game game) {
        super(game);
    }


    @Override
    public void show() {
        super.show();
        bg = new Texture("bg.png");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        font = new Font("fonts/font.fnt","fonts/font.png");
        font.setFontSize(0.03f);
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
        System.out.println(heroShip.getNumberOfLives());
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
        Gdx.gl.glClearColor(1, 1, 1f, 1);
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
        printInfo();
        timer++;
        if (timer < interval && frags % 25 == 0){
            showLevel();
        }else if (timer > interval && frags % 25 == 0){
            timer = 121;
        }else if (frags % 25 != 0 && timer > interval){
            timer = 0;
        }
        batch.end();
    }
    public void printInfo(){
        sbFrags.setLength(0);
        font.draw(batch,sbFrags.append(FRAGS).append(frags),worldBounds.getLeft() + 0.02f,worldBounds.getTop()- 0.02f,
                Align.left);
        sbNumberOfLives.setLength(0);
        font.draw(batch,sbNumberOfLives.append(NUMBER_OF_LIVES).append(heroShip.getNumberOfLives()),worldBounds.pos.x ,worldBounds.getTop()- 0.02f,
                Align.center);
        sbLevel.setLength(0);
        font.draw(batch,sbLevel.append(LEVEL).append(enemiesGenerator.getLevel()),worldBounds.getRight() - 0.02f,worldBounds.getTop()- 0.02f,
                Align.right);
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
                enemiesGenerator.generateEnemies(delta,frags);
                break;
            case GAMEOVER:
                break;
        }

    }
    public void showLevel() {
            sbLevelToShow.setLength(0);
            font.draw(batch, sbLevelToShow.append(LEVEL_TO_SHOW).append(enemiesGenerator.getLevel()), worldBounds.pos.x, worldBounds.pos.y, Align.center);
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
                     bulletCollision.play(0.3f);
                     bullet.markAsDestroyed();
                     enemyShip.damage(bullet.getDamage());
                     if (enemyShip.isDestroyed()){
                         frags ++;
                     }
                 }
             }
        }
        // Checking if bullets strike the heroShip
        for (Bullet bullet: bulletList ){
            if (bullet.getBulletShooter() == heroShip || bullet.isDestroyed()){
                continue;
            }
            if (heroShip.isBulletCollision(bullet)){
                bulletCollision.play(0.3f);
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
        heroShip.touchDown(touch,pointer);
        if (gameState == gameState.GAMEOVER){
            startNewGameButton.touchDown(touch,pointer);
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        heroShip.touchUp(touch,pointer);
        if (gameState == gameState.GAMEOVER){
            startNewGameButton.touchUp(touch,pointer);
        }
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
        font.dispose();
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
        batch.setColor(1, 1, 1, 1);
        frags = 0;
        timer = 0;
        enemiesGenerator.setLevel(1);
    }
    @Override
    public void actionPerformed(Object source) {
        if (source == startNewGameButton){
            newGameSound.play();
            startNewGame();
        }
    }




}
