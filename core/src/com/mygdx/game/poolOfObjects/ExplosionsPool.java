package com.mygdx.game.poolOfObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.basic.SpritesPool;
import com.mygdx.game.sprites.Explosion;

public class ExplosionsPool extends SpritesPool<Explosion> {
    Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("data/explosionsound.mp3"));
    private final TextureRegion textureRegion;

    public ExplosionsPool(TextureAtlas atlas){
        this.textureRegion = atlas.findRegion("explosion");
    }
    @Override
    protected Explosion newObject() {
        return new Explosion(textureRegion,9,9,74,explosionSound);
    }


}
