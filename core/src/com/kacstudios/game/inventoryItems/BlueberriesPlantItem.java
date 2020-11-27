package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.grid.plants.BlueberriesPlant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class BlueberriesPlantItem extends IInventoryItem {
    private static Texture texture = new Texture("items/blueberries.png");
    public BlueberriesPlantItem(){
        this(1);
    }

    public BlueberriesPlantItem(int amount){
        setAmount(amount);
        setDisplayName("Blueberries");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        // does nothing
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new BlueberriesPlantItem(amount);
    }
}
