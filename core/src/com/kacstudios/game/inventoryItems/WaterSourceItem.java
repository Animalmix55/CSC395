package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.WaterSource;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class WaterSourceItem extends IInventoryItem{
    private static Texture texture = new Texture("items/water.png");
    public WaterSourceItem(){
        this(1);
    }

    public WaterSourceItem(int amount){
        super(300, true);
        setAmount(amount);
        setDisplayName("Pond");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        event.setSquare(new WaterSource());
        int amount = getAmount();
        amount--;
        setAmount(Math.max(amount, 0));

        if(amount == 0){
            parent.setItem(null);
        }

        parent.checkItem();
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new WaterSourceItem(amount);
    }

    protected boolean isBlocked(GridClickEvent event) {
        GridSquare target = event.getGridSquare();
        return target != null;
    }
}
