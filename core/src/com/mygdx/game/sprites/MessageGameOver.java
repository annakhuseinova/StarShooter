package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.basic.ActionListener;
import com.mygdx.game.basic.BasicScaledTouchUpButton;
import com.mygdx.game.basic.Sprite;
import com.mygdx.game.math.Rect;

public class MessageGameOver extends Sprite{


    public MessageGameOver(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
        setHeightProportion(0.07f);
        setBottom(0.09f);
    }
    @Override
    public void resize(Rect worldBounds){
        setBottom(worldBounds.getBottom());
        setRight(worldBounds.getRight());

    }
}
