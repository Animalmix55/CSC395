package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.plants.BlueberriesPlant;
import com.kacstudios.game.grid.plants.CornPlant;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.sounds.GameSounds;
import com.kacstudios.game.utilities.GridClickEvent;

public class HarvestingItem extends IInventoryItem {
    private static Texture texture = new Texture("items/scythe.png");

    public HarvestingItem(int amount){
        super(true, 300);
        setAmount(amount);
        setDisplayName("Scythe");
        setDeletable(false);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        GameSounds.harvestSound.play(false);

        Plant target = (Plant) event.getGridSquare();
        parent.getViewer().addItem(target.getHarvestItem(1));
        event.setSquare(null);

        parent.checkItem();
        GameSounds.plantingSound.play(false);
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new HarvestingItem(amount);
    }

    protected boolean isBlocked(GridClickEvent event) {
        GridSquare target = event.getGridSquare();
        return target == null || !Plant.class.isAssignableFrom(target.getClass()) ||
                ((Plant) target).getDead() || !((Plant) target).getFullyGrown();
    }
}
