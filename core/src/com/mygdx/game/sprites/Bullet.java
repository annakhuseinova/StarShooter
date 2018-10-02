package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.basic.Sprite;
import com.mygdx.game.math.Rect;

public class Bullet extends Sprite {

    private Rect worldBounds;
    private Vector2 bulletSpeed = new Vector2();
    private int damage;
    private Object bulletShooter;

    public Bullet() {
       regions = new TextureRegion[1];
    }


    public void set(Object bulletShooter, TextureRegion region, Vector2 bulletOrigin, Vector2 bulletSpeed,
                    float bulletSize, Rect worldBounds,int damage){
        this.bulletShooter = bulletShooter;
        this.regions[0] = region;
        this.pos.set(bulletOrigin);
        this.bulletSpeed.set(bulletSpeed);
        setHeightProportion(bulletSize);
        this.worldBounds = worldBounds;
        this.damage = damage;

    }

    @Override
    public void update(float delta) {
        this.pos.mulAdd(bulletSpeed,delta);
        if (isOutside(worldBounds)){
            markAsDestroyed();
        }
    }

    public int getDamage() {
        return damage;
    }

    public Object getBulletShooter() {
        return bulletShooter;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setBulletShooter(Object bulletShooter) {
        this.bulletShooter = bulletShooter;
    }
}
