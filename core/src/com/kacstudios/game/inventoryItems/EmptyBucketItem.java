package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.disasters.FireDisaster;
import com.kacstudios.game.grid.WaterSource;
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

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        if (!event.farmerWithinRadius(300)) return;
        if (event.getGridSquare() != null && WaterSource.class.isAssignableFrom(event.getGridSquare().getClass())) {
            int amount = getAmount();
            if(amount <= 1) {
                parent.setItem(null);
            } else {
                setAmount(amount - 1);
            }

            parent.getViewer().addItem(new WaterBucketItem(1));
        }
        parent.checkItem();
    }

        @Override
        public Texture getTexture() { return texture; }

        @Override
        public IInventoryItem createNewInstance(int amount) { return new EmptyBucketItem(amount); }

    }