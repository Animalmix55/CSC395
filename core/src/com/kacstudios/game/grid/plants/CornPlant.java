package com.kacstudios.game.grid.plants;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class CornPlant extends Plant {
    public CornPlant(boolean collides) {
        super(collides);
        setGrowthTextures(new String[]{"grid_blank.png","corn-1.png", "corn-2.png", "corn-3.png", "corn-4.png"});
        setGrowthTime(20l);
    }
}
