package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.basic.Sprite;
import com.mygdx.game.math.Rect;
import com.mygdx.game.poolOfObjects.BulletPool;

public class HeroShip extends Sprite {


    Sound sound;
    private static final int INVALID_POINTER = -1;
    private Vector2 defaultSpeed = new Vector2(0.5f,0f);
    private Vector2 actualSpeed = new Vector2();

    private boolean isLeftPressed;
    private boolean isRightPressed;

    private Rect worldBounds;


    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    protected BulletPool bulletPool;
    private TextureRegion bulletRegion;
    private Vector2 heroShipBulletSpeed = new Vector2(0, 0.5f);

    public  HeroShip(TextureAtlas atlas, BulletPool bulletPool){
        super(atlas.findRegion("main_ship"),1,2,2);
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        setHeightProportion(0.15f);
        this.bulletPool = bulletPool;
        sound = Gdx.audio.newSound(Gdx.files.internal("data/shootingsound.mp3"));


    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setBottom(worldBounds.getBottom() + 0.1f);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(actualSpeed,delta);
        if (getRight()> worldBounds.getRight()){
            setRight(worldBounds.getRight());
            stop();
        }
        if (getLeft() < worldBounds.getLeft()){
            setLeft(worldBounds.getLeft());
            stop();
        }
    }

    public void keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                isLeftPressed = false;
                if (isRightPressed){
                    moveToTheRight();
                }else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                isRightPressed = false;
                if (isLeftPressed){
                    moveToTheLeft();
                }else {
                    stop();
                }
                break;
        }
    }

    public void keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                isLeftPressed = true;
                moveToTheLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                isRightPressed = true;
                moveToTheRight();
                break;
            case Input.Keys.UP:
                shoot();
                break;
        }

    }
    private void moveToTheRight(){
        actualSpeed.set(defaultSpeed);
    }
    private void moveToTheLeft(){
        actualSpeed.set(defaultSpeed).rotate(180);
    }
    private void stop(){
        actualSpeed.setZero();

    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (touch.x < worldBounds.pos.x){
            if (leftPointer != INVALID_POINTER) return false;
            leftPointer = pointer;
            moveToTheLeft();
        }else {
            if (rightPointer != INVALID_POINTER) return false;
            rightPointer = pointer;
            moveToTheRight();
        }
        return false;

    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (pointer == leftPointer){
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER){
                moveToTheRight();
            }else {
                stop();
            }
        }else if (pointer == rightPointer){
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER){
                moveToTheLeft();
            }else {
                stop();
            }
        }
        return false;

    }
    public void shoot(){
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, heroShipBulletSpeed, 0.02f, worldBounds,1 );
        sound.play(0.1f);

    }
}