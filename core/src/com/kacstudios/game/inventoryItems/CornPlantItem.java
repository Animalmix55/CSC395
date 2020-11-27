package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kacstudios.game.grid.plants.CornPlant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class CornPlantItem extends IInventoryItem {
    private static Texture texture = new Texture("items/corn.png");
    public CornPlantItem(){
        this(1);
    }

    public CornPlantItem(int amount){
        setAmount(amount);
        setDisplayName("Corn");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        // does nothing, only for selling
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new CornPlantItem(amount);
    }
}
