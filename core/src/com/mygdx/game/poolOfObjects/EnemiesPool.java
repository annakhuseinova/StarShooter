package com.mygdx.game.poolOfObjects;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.basic.Sprite;
import com.mygdx.game.basic.SpritesPool;
import com.mygdx.game.math.Rect;
import com.mygdx.game.sprites.EnemyShip;
import com.mygdx.game.sprites.HeroShip;

public class EnemiesPool extends SpritesPool<EnemyShip> {

   private BulletPool bulletPool;
   private Sound shootingSound;
   private HeroShip heroShip;
   private Rect worldBounds;
   private ExplosionsPool explosionsPool;


    public EnemiesPool(BulletPool bulletPool,ExplosionsPool explosionsPool, Sound shootingSound, HeroShip heroShip) {
        this.bulletPool = bulletPool;
        this.shootingSound = shootingSound;
        this.heroShip = heroShip;
        this.explosionsPool = explosionsPool;

    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(bulletPool,explosionsPool,shootingSound,heroShip);
    }
}
