package com.kacstudios.game.gridItems.plants;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class CornPlant extends Plant {
    public CornPlant(float x, float y, Stage s, boolean collides) {
        super(x, y, s, collides);
        setGrowthTextures(new String[]{"grid_blank.png", "grid_red.png", "grid_blue.png", "grid_green.png"});
    }
}
