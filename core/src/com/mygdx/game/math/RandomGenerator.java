package com.mygdx.game.math;

import java.util.Random;

public class RandomGenerator {
    private static final Random random = new Random();
    public static float nextFloat(float min,float max){
        return random.nextFloat()*(max-min) + min;
    }
}
