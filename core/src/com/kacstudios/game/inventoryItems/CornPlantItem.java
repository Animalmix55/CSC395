package com.kacstudios.game.inventoryItems;

import com.kacstudios.game.grid.plants.CornPlant;
import com.kacstudios.game.utilities.GridClickEvent;

public class CornPlantItem extends IDepleteableItem {
    public CornPlantItem(){
        setTexturePath("items/corn.png");
        setBreakable(false);
    }

    public CornPlantItem(int amount){
        this();
        setAmount(amount);
    }

    @Override
    public void onDeployment(GridClickEvent event) {
        if(event.getGridSquare() == null) {
            event.setSquare(new CornPlant(false));
            int amount = getAmount();
            amount--;
            setAmount(amount >= 0 ? amount : 0);
        }
    }
}
