package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.grid.plants.BlueberriesPlant;
import com.kacstudios.game.grid.structures.BarnStructure;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class BarnItem extends IInventoryItem {
    private static Texture texture = new Texture("items/barn.png");
    public BarnItem(){
        this(1);
    }

    public BarnItem(int amount){
        setAmount(amount);
        setDisplayName("Barn");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {

        if(!event.farmerWithinRadius(300)) return; // must be within 300 pixels
        if(event.getGridSquare() == null) {

            if(!event.hasClearanceFor(3, 3)) return;

            event.setSquare(new BarnStructure());

            int amount = getAmount();
            amount--;
            setAmount(amount >= 0 ? amount : 0);

            if(amount == 0){ // amount of -1 signifies unlimited
                parent.setItem(null); // remove from button
            }

            parent.checkItem(); // update button display amount
        }
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new BarnItem(amount);
    }
}
