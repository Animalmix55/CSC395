package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.grid.plants.PotatoesPlant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class PotatoesPlantItem extends IInventoryItem {
    private static Texture texture = new Texture("items/potatoes.png");
    public PotatoesPlantItem(){
        this(1);
    }

    public PotatoesPlantItem(int amount){
        super(300, true);
        setAmount(amount);
        setDisplayName("Potato");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        event.setSquare(new PotatoesPlant());
        int amount = getAmount();
        amount--;
        setAmount(amount >= 0 ? amount : 0);

        if (amount == 0) { // amount of -1 signifies unlimited
            parent.setItem(null); // remove from button
        }

        parent.checkItem(); // update button display amount
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new PotatoesPlantItem(amount);
    }

    @Override
    protected boolean isBlocked(GridClickEvent event) {
        GridSquare target = event.getGridSquare();
        return target != null;
    }
}
