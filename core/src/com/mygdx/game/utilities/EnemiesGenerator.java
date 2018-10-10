package com.mygdx.game.utilities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.math.RandomGenerator;
import com.mygdx.game.math.Rect;
import com.mygdx.game.poolOfObjects.EnemiesPool;
import com.mygdx.game.sprites.EnemyShip;

public class EnemiesGenerator {

    // Настройки для маленького EnemyShip
    private static float ENEMY_SMALL_HEIGHT = 0.1f;
    private static float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static float ENEMY_SMALL_BULLET_VY = - 0.3f;
    private static int   ENEMY_SMALL_HEIGHT_DAMAGE = 1;
    private static float ENEMY_SMALL_RELOAD_INTERVAL = 3f;
    private static int   ENEMY_SMALL_NUMBER_OF_LIVES = 1;
    // Для среднего EnemyShip
    private static float ENEMY__MEDIUM_HEIGHT = 0.1f;
    private static float ENEMY_MEDIUM_BULLET_HEIGHT = 0.02f;
    private static float ENEMY_MEDIUM_BULLET_VY = - 0.25f;
    private static int   ENEMY_MEDIUM_HEIGHT_DAMAGE = 5;
    private static float ENEMY_MEDIUM_RELOAD_INTERVAL = 4f;
    private static int   ENEMY_MEDIUM_NUMBER_OF_LIVES = 5;
    // Для большого EnemyShip
    private static float ENEMY_BIG_HEIGHT = 0.2f;
    private static float ENEMY_BIG_BULLET_HEIGHT = 0.04f;
    private static float ENEMY_BIG_BULLET_VY = - 0.3f;
    private static int   ENEMY_BIG_HEIGHT_DAMAGE = 10;
    private static float ENEMY_BIG_RELOAD_INTERVAL = 3f;
    private static int   ENEMY_BIG_NUMBER_OF_LIVES = 10;

    private TextureRegion bulletRegion;


    private final TextureRegion[] enemySmallRegion;

    private final TextureRegion[] enemyMediumRegion;

    private final TextureRegion[] enemyBigRegion;

    private final Vector2 enemySmallV = new Vector2(0f,-0.2f);
    private final Vector2 enemyMediumV = new Vector2(0f,-0.03f);
    private final Vector2 enemyBigV = new Vector2(0f,-0.005f);

    private final EnemiesPool enemiesPool;

    private float generateInterval = 4f;
    private float generateTimer;
    private Rect worldBounds;
    private int level = 1;


    public EnemiesGenerator(EnemiesPool enemiesPool, TextureAtlas atlas,Rect worldBounds) {
        this.enemiesPool = enemiesPool;
        this.worldBounds = worldBounds;
        TextureRegion textureRegion0 = atlas.findRegion("enemy0");
        this.enemySmallRegion = Regions.split(textureRegion0, 1, 2, 2);
        TextureRegion textureRegion1 = atlas.findRegion("enemy1");
        this.enemyMediumRegion = Regions.split(textureRegion1, 1, 2, 2);
        TextureRegion textureRegion2 = atlas.findRegion("enemy2");
        this.enemyBigRegion = Regions.split(textureRegion2, 1, 2, 2);
        this.bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generateEnemies(float delta, int frags ){
        level = frags / 25 + 1;
        generateTimer += delta;
        if (generateTimer >= generateInterval){
            generateTimer = 0f;
            EnemyShip enemyShip = enemiesPool.obtain();
            float type = (float)Math.random();
            if (type <0.5f) {
                enemyShip.set(
                        enemySmallRegion,
                        enemySmallV,
                        bulletRegion,
                        ENEMY_SMALL_BULLET_HEIGHT,
                        ENEMY_SMALL_BULLET_VY,
                        ENEMY_SMALL_HEIGHT_DAMAGE* level,
                        ENEMY_SMALL_RELOAD_INTERVAL,
                        ENEMY_SMALL_HEIGHT,
                        ENEMY_SMALL_NUMBER_OF_LIVES*level,
                        worldBounds
                );
            }else if(type < 0.8f) {
                enemyShip.set(
                        enemyMediumRegion,
                        enemyMediumV,
                        bulletRegion,
                        ENEMY_MEDIUM_BULLET_HEIGHT,
                        ENEMY_MEDIUM_BULLET_VY,
                        ENEMY_MEDIUM_HEIGHT_DAMAGE* level,
                        ENEMY_MEDIUM_RELOAD_INTERVAL,
                        ENEMY__MEDIUM_HEIGHT,
                        ENEMY_MEDIUM_NUMBER_OF_LIVES*level,
                        worldBounds);
            } else {
                enemyShip.set(
                        enemyBigRegion,
                        enemyBigV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_VY,
                        ENEMY_BIG_HEIGHT_DAMAGE* level,
                        ENEMY_BIG_RELOAD_INTERVAL,
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_NUMBER_OF_LIVES*level,
                        worldBounds);

            }
            enemyShip.pos.x = RandomGenerator.nextFloat(worldBounds.getLeft()+enemyShip.getHalfWidth(),
                    worldBounds.getRight()-enemyShip.getHalfWidth());
            enemyShip.setBottom(worldBounds.getTop());
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
