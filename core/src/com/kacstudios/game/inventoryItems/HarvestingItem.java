package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.grid.plants.BlueberriesPlant;
import com.kacstudios.game.grid.plants.CornPlant;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class HarvestingItem extends IInventoryItem {
    private static Texture texture = new Texture("items/scythe.png");

    public HarvestingItem(int amount){
        setAmount(amount);
        setDisplayName("Scythe");
        setDeletable(false);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        if (!event.farmerWithinRadius(300)) return;
        if (event.getGridSquare() == null) return;
        if (Plant.class.isAssignableFrom(event.getGridSquare().getClass())) {
            Plant target = (Plant) event.getGridSquare();
            if (target.getFullyGrown() == true){
                parent.getViewer().addItem(target.getHarvestItem(1));
                event.setSquare(null);
            }

        }

        parent.checkItem();
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new HarvestingItem(amount);
    }
}
