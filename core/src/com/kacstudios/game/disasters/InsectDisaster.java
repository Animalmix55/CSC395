package com.kacstudios.game.disasters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.utilities.TimeEngine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class InsectDisaster extends PropagatingDisaster {

    private int insecticideAmount;

    public InsectDisaster(Plant impl) {
        super(impl,
                loadAnimationUnsetFromFiles(
                        new String[]{"pest-1.png", "pest-2.png", "pest-3.png", "pest-4.png"}, 0.1f,true),
                40,
                .25f,
                .3f
        );
        insecticideAmount = generateRandom(0, 5);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
    }

    public int getInsecticideAmount() {
        return insecticideAmount;
    }

    public void setInsecticideAmount(int insecticideAmount) {
        this.insecticideAmount = insecticideAmount;
    }
}