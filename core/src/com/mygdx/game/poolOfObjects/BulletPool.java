package com.mygdx.game.poolOfObjects;

import com.mygdx.game.basic.SpritesPool;
import com.mygdx.game.sprites.Bullet;

public class BulletPool extends SpritesPool<Bullet> {

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    @Override
    protected void log() {
        System.out.println("Bullet active/free"+activeObjects.size()+ "/"+freeObjects.size());
    }
}
