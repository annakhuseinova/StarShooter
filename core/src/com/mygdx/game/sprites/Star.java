package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.basic.Sprite;
import com.mygdx.game.math.RandomGenerator;
import com.mygdx.game.math.Rect;

public class Star extends Sprite {

    private Vector2 speed = new Vector2();
    private Rect worldBounds;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        speed.set(RandomGenerator.nextFloat(-0.005f,0.005f),RandomGenerator.nextFloat(-0.5f,-0.1f));
        setHeightProportion(RandomGenerator.nextFloat(0.01f,0.02f));
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(speed,delta);
        checkAndHandleBounds();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float positionX = RandomGenerator.nextFloat(worldBounds.getLeft(),worldBounds.getRight());
        float positionY = RandomGenerator.nextFloat(worldBounds.getBottom(),worldBounds.getTop());
        pos.set(positionX,positionY);
    }

    private void checkAndHandleBounds(){
        if (getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        if (getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if (getTop() < worldBounds.getBottom()) setBottom(worldBounds.getTop());

    }

}
