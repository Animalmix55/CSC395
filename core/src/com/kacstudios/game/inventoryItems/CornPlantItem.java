package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kacstudios.game.grid.plants.CornPlant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class CornPlantItem extends IInventoryItem {
    public CornPlantItem(){
        setTexturePath("items/corn.png");
    }

    public CornPlantItem(int amount){
        this();
        setAmount(amount);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {

        if(!event.farmerWithinRadius(300)) return; // must be within 300 pixels
        if(event.getGridSquare() == null) {
            event.setSquare(new CornPlant(false));
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
