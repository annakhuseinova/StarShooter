package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.basic.ActionListener;
import com.mygdx.game.basic.BasicScaledTouchUpButton;
import com.mygdx.game.math.Rect;

public class ExitButton extends BasicScaledTouchUpButton {

    public ExitButton(TextureAtlas atlas, ActionListener actionListener) {
        super(atlas.findRegion("btExit"),actionListener,0.9f);
        setHeightProportion(0.12f);
    }

    @Override
    public void resize(Rect worldBounds){
        setBottom(worldBounds.getBottom());
        setRight(worldBounds.getRight());

    }
}
