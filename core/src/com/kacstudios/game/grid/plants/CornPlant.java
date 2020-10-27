package com.kacstudios.game.grid.plants;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class CornPlant extends Plant {
    public CornPlant() {
        super(new String[]{"soil.png","corn-1.png", "corn-2.png", "corn-3.png", "corn-4.png"},
                "corn-dead.png");
        setGrowthTime(20l);
    }
}
