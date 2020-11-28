package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class DeleteItem extends IInventoryItem{
    private static Texture texture = new Texture("items/shovel.png");

    public DeleteItem(int amount){
        setAmount(amount);
        setDisplayName("Shovel");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        if (!event.farmerWithinRadius(300)) return;
        if (event.getGridSquare() != null) {
            event.setSquare(null);
        }

        parent.checkItem();
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new DeleteItem(amount);
    }
}
