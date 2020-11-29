package com.kacstudios.game.grid.plants;

import com.kacstudios.game.inventoryItems.PotatoesPlantItem;

public class PotatoesPlant extends Plant {
    public PotatoesPlant() {
        super(new String[]{"grid-textures/grid_blank.png","plant-textures/potatoes-1.png",
                        "plant-textures/potatoes-2.png", "plant-textures/potatoes-3.png"},
                "plant-textures/potatoes-dead.png");
        setGrowthTime(20l);
        setDryGrowthRateModifier(.2f); // grows at 20% speed when dry
        setSecondsToDry(20);

        setHarvestItemConstructor(amount -> new PotatoesPlantItem(5 * amount));
    }
}
