package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.disasters.FireDisaster;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.inventoryItems.WaterBucketItem;

public class EmptyBucketItem extends IInventoryItem{

    private static Texture texture = new Texture("items/EmptyBucket.png");

    public EmptyBucketItem() {this(1);}
    public EmptyBucketItem(int amount){
        setAmount(amount);
        setDisplayName("Empty Bucket");
    }

    // THIS ITEM IS NOT FUNCTIONING AT THE MOMENT, MUST ADD A WATER SPACE ON THE GRID TO REFILL
    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        if (!event.farmerWithinRadius(300)) return;
        if (event.getGridSquare() == null) {
            //ACTION LOGIC
            parent.setItem(null);

            //CHANGE QUANTITIES LOGIC

        }
        parent.checkItem();
    }


        @Override
        public Texture getTexture() { return texture; }

        @Override
        public IInventoryItem createNewInstance(int amount) { return new EmptyBucketItem(amount); }

    }