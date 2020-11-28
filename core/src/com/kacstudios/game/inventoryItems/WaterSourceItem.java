package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.grid.WaterSource;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class WaterSourceItem extends IInventoryItem{
    private static Texture texture = new Texture("items/water.png");
    public WaterSourceItem(){
        this(1);
    }

    public WaterSourceItem(int amount){
        setAmount(amount);
        setDisplayName("Water Spigot");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        if(!event.farmerWithinRadius(300)) return;
        if(event.getGridSquare() == null) {
            event.setSquare(new WaterSource());
            int amount = getAmount();
            amount--;
            setAmount(amount >= 0 ? amount : 0);

            if(amount == 0){
                parent.setItem(null);
            }

            parent.checkItem();

        }
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new WaterSourceItem(amount);
    }
}
