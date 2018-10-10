package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.basic.ActionListener;
import com.mygdx.game.basic.BasicScaledTouchUpButton;
import com.mygdx.game.math.Rect;

public class StartNewGameButton extends BasicScaledTouchUpButton {
    public StartNewGameButton(TextureAtlas atlas, ActionListener actionListener) {
        super(atlas.findRegion("button_new_game"), actionListener, 0.9f);
        setHeightProportion(0.05f);
        setBottom(-0.3f);

    }
    public void resize(Rect worldBounds){
        setBottom(worldBounds.getBottom());
        setRight(worldBounds.getRight());
    }
}
