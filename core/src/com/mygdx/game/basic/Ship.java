package com.mygdx.game.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.math.Rect;
import com.mygdx.game.poolOfObjects.BulletPool;
import com.mygdx.game.poolOfObjects.ExplosionsPool;
import com.mygdx.game.sprites.Bullet;
import com.mygdx.game.sprites.Explosion;
import com.mygdx.game.sprites.HeroShip;

public class Ship extends Sprite{

    Sound soundOfExplosion =Gdx.audio.newSound(Gdx.files.internal("data/explosionsound.mp3"));
    Sound soundOfExplosionAsCollision =Gdx.audio.newSound(Gdx.files.internal("data/collisionsound.mp3"));
    protected Vector2 actualSpeed = new Vector2();
    protected Rect worldBounds;
    protected Vector2 shipBulletSpeed = new Vector2();
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected int bulletDamage;
    protected Sound shootingSound;
    protected ExplosionsPool explosionsPool;
    protected float bulletHeight;
    protected float reloadInterval;
    protected float reloadTimer;
    protected float damageAnimateInterval = 0.1f;
    protected float damageAnimateTimer;
    Explosion explosion;
    protected int numberOfLives;

    public Ship(TextureRegion region, int rows, int cols, int frames, BulletPool bulletPool, ExplosionsPool explosionsPool,
                Sound shootingSound){
        super(region,rows,cols,frames);
        this.bulletPool = bulletPool;
        this.shootingSound = shootingSound;
        this.bulletHeight = 0.01f;
        this.bulletDamage = 1;
        this.explosionsPool = explosionsPool;

    }
    public Ship(BulletPool bulletPool, ExplosionsPool explosionsPool, Sound shootingSound){
        this.bulletPool = bulletPool;
        this.shootingSound = shootingSound;
        this.explosionsPool = explosionsPool;

    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;

    }
    public void shoot(){
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, shipBulletSpeed, bulletHeight, worldBounds,bulletDamage);
        shootingSound.play(0.1f);

    }
    public void boom(){
        Explosion explosion = explosionsPool.obtain();
        explosion.set(getHeight(), pos);
        soundOfExplosion.play(0.6f);
        numberOfLives = 0;

    }

    public int getNumberOfLives() {
        return numberOfLives;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= damageAnimateInterval){
            frame = 0;
        }
    }

    public void damage(int damage){
         frame = 1;
         damageAnimateTimer = 0f;
         numberOfLives -= damage;
         if (numberOfLives <= 0 ){
             boom();
             markAsDestroyed();
         }
    }

    public int getBulletDamage() {
        return bulletDamage;
    }
}
