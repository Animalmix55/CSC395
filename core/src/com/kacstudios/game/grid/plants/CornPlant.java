package com.kacstudios.game.grid.plants;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class CornPlant extends Plant {
    public CornPlant(boolean collides) {
        super(collides);
        setGrowthTextures(new String[]{"TestSeed.png", "grid_blank.png", "grid_red.png", "grid_blue.png", "grid_green.png"});
        setGrowthTime(100l);
    }
}
