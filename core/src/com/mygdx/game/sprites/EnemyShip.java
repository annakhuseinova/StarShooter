package com.mygdx.game.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.basic.Ship;
import com.mygdx.game.math.Rect;
import com.mygdx.game.poolOfObjects.BulletPool;
import com.mygdx.game.poolOfObjects.ExplosionsPool;

public class EnemyShip extends Ship {


    private enum State {DESCENT,FIGHT}
    private HeroShip heroShip;
    private Vector2 startingSpeed = new Vector2();
    private Vector2 v0 = new Vector2();
    private Vector2 descentSpeed = new Vector2(0,-0.15f);
    private State state;

    public EnemyShip(BulletPool bulletPool, ExplosionsPool explosionsPool, Sound shootingSound, HeroShip heroShip) {
        super(bulletPool, explosionsPool, shootingSound);
        this.heroShip = heroShip;
        this.actualSpeed.set(v0);

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(actualSpeed,delta);
        switch (state){
            case DESCENT:
                if (getTop() <= worldBounds.getTop()) {
                    descentSpeed.set(v0);
                    state = State.FIGHT;
                }
                break;
            case FIGHT:
                reloadTimer += delta;
                if (reloadTimer >= reloadInterval) {
                    reloadTimer = 0f;
                    shoot();
                }
                if (getBottom() < worldBounds.getBottom()){
                    markAsDestroyed();
                    boom();
                }
                break;
        }
    }

    public void set(TextureRegion[] regions, Vector2 v0, TextureRegion bulletRegion,
                    float bulletHeight, float bulletVY, int bulletDamage, float reloadInterval, float height, int numberOfLives,
                    Rect worldBounds){
        this.regions = regions;
        this.descentSpeed.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.shipBulletSpeed.set(0,bulletVY);
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        this.numberOfLives = numberOfLives;
        setHeightProportion(height);
        reloadTimer = reloadInterval;
        actualSpeed.set(descentSpeed);
        state = State.DESCENT;
        this.worldBounds = worldBounds;


    }

    public boolean isBulletCollision(Rect bullet){
        return !(
                bullet.getRight() < getLeft() || bullet.getLeft() > getRight() || bullet.getBottom()>
                        getTop() || bullet.getTop() < pos.y
                );
    }
}
