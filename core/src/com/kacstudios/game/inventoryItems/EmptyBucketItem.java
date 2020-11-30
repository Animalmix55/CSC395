package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.disasters.FireDisaster;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.WaterSource;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.sounds.GameSounds;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.inventoryItems.WaterBucketItem;

public class EmptyBucketItem extends IInventoryItem{

    private static Texture texture = new Texture("items/EmptyBucket.png");

    public EmptyBucketItem() {this(1);}
    public EmptyBucketItem(int amount){
        super(true, 300);
        setAmount(amount);
        setDisplayName("Empty Bucket");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        GameSounds.fillBucketSound.play(false);

        int amount = getAmount();
        if(amount <= 1) {
            parent.setItem(null);
        } else {
            setAmount(amount - 1);
        }

        parent.getViewer().addItem(new WaterBucketItem(1));

        parent.checkItem();
    }

        @Override
        public Texture getTexture() { return texture; }

        @Override
        public IInventoryItem createNewInstance(int amount) { return new EmptyBucketItem(amount); }

    protected boolean isBlocked(GridClickEvent event) {
        GridSquare target = event.getGridSquare();
        return target == null || !WaterSource.class.isAssignableFrom(target.getClass());
    }
}