package com.kacstudios.game.grid.plants;

public class CornPlant extends Plant {
    public CornPlant() {
        super(new String[]{"grid_blank.png","corn-1.png", "corn-2.png", "corn-3.png"},
                "corn-dead.png");
        setGrowthTime(20l);
        setDryGrowthRateModifier(.2f); // grows at 20% speed when dry
        setSecondsToDry(20);
    }
}
