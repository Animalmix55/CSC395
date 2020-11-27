package com.kacstudios.game.grid.plants;

import com.kacstudios.game.inventoryItems.BlueberriesPlantItem;
import com.kacstudios.game.inventoryItems.CornPlantItem;

public class BlueberriesPlant extends Plant {
    public BlueberriesPlant() {
        super(new String[]{"grid-textures/grid_blank.png","plant-textures/blueberries-1.png",
                        "plant-textures/blueberries-2.png", "plant-textures/blueberries-3.png"},
                "plant-textures/blueberries-dead.png");
        setGrowthTime(20l);
        setDryGrowthRateModifier(.2f); // grows at 20% speed when dry
        setSecondsToDry(20);

        setHarvestItemConstructor(BlueberriesPlantItem::new);
    }
}
