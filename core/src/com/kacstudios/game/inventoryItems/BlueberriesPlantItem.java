package com.kacstudios.game.inventoryItems;

import com.kacstudios.game.grid.plants.BlueberriesPlant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class BlueberriesPlantItem extends IInventoryItem {
    public BlueberriesPlantItem(){
        setTexturePath("items/blueberries.png");
    }

    public BlueberriesPlantItem(int amount){
        this();
        setAmount(amount);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {

        if(!event.farmerWithinRadius(300)) return; // must be within 300 pixels
        if(event.getGridSquare() == null) {
            event.setSquare(new BlueberriesPlant());
            int amount = getAmount();
            amount--;
            setAmount(amount >= 0 ? amount : 0);

            if(amount == 0){ // amount of -1 signifies unlimited
                parent.setItem(null); // remove from button
            }

            parent.checkItem(); // update button display amount
        }
    }
}
