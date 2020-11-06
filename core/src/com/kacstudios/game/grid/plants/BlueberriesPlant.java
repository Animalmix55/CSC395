package com.kacstudios.game.grid.plants;

public class BlueberriesPlant extends Plant {
    public BlueberriesPlant() {
        super(new String[]{"grid_blank.png","blueberries-1.png", "blueberries-2.png", "blueberries-3.png"},
                "blueberries-dead.png");
        setGrowthTime(20l);
    }
}
