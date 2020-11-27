package com.kacstudios.game.grid.plants;

public class CornPlant extends Plant {
    public CornPlant() {
        super(new String[]{"grid-textures/grid_blank.png","plant-textures/corn-1.png",
                        "plant-textures/corn-2.png", "plant-textures/corn-3.png"},
                "plant-textures/corn-dead.png");
        setGrowthTime(20l);
        setDryGrowthRateModifier(.2f); // grows at 20% speed when dry
        setSecondsToDry(20);
    }
}
