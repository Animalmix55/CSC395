package com.kacstudios.game.grid;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.grid.plants.Plant;
import java.util.Random;

public class InsectDisaster extends BaseActor {

    Animation<TextureRegion> insectAnimation;

    public InsectDisaster(Plant impl) {
        Plant parent = (Plant) getParent();

        String[] insectAnimationFiles = {"pest-1.png", "pest-2.png", "pest-3.png", "pest-4.png"};

        insectAnimation = loadAnimationFromFiles(insectAnimationFiles, 0.1f,true);
    }

    //generates a number from 1-5
    public int generateRandom(){
        Random rand = new Random();
        int upperbound = 4;
        return rand.nextInt(upperbound) + 1;
    }
}