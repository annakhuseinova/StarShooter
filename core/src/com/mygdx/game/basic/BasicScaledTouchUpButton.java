package com.mygdx.game.basic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BasicScaledTouchUpButton extends Sprite {

    private int pointer;
    private boolean isPressed;
    private float pressScale;
    private ActionListener actionListener;

    public BasicScaledTouchUpButton(TextureRegion region, ActionListener actionListener, float pressScale) {
        super(region);
        this.actionListener = actionListener;
        this.pressScale = pressScale;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (isPressed || !isMe(touch)){
            return false;
        }

       this.pointer = pointer;
        scale = pressScale;
        this.isPressed = true;
        return true;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (this.pointer != pointer ||!isPressed){
            return false;
        }
        if (isMe(touch)){
           actionListener.actionPerformed(this);
           return true;
        }
        isPressed = false;
        scale = 1f;
        return false;
    }
}
