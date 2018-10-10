package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.basic.Sprite;

public class Explosion extends Sprite {

    private Sound soundOfExplosion;
    private float animateInterval = 0.017f;
    private float animateTimer;


    public Explosion(TextureRegion region, int rows,int cols,int frames, Sound soundOfExplosion){
          super(region,rows,cols,frames);
          this.soundOfExplosion = soundOfExplosion;
    }

    public void set(float height, Vector2 pos) {
        this.pos.set(pos);
        setHeightProportion(height);

    }
    @Override
    public void update(float delta) {
        animateTimer += delta;
        if (animateTimer >= animateInterval) {
            animateTimer = 0f;
            if (++frame == regions.length) {
                markAsDestroyed();
            }
        }
    }

    @Override
    public void markAsDestroyed() {
        super.markAsDestroyed();
        frame = 0;
    }
}
