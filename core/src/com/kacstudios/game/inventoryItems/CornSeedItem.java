package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.plants.CornPlant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.sounds.GameSounds;
import com.kacstudios.game.utilities.GridClickEvent;

public class CornSeedItem extends IInventoryItem {
    private static Texture texture = new Texture("items/corn-seeds.png");
    public CornSeedItem(){
        this(1);
    }

    public CornSeedItem(int amount){
        super(300, true, 1, 1);
        setAmount(amount);
        setDisplayName("Corn Seed");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        GameSounds.plantingSound.play(false);

        event.setSquare(new CornPlant());
        int amount = getAmount();
        amount--;
        setAmount(Math.max(amount, 0));

        if(amount == 0){ // amount of -1 signifies unlimited
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
        return new CornSeedItem(amount);
    }

    protected boolean isBlocked(GridClickEvent event) {
        GridSquare target = event.getGridSquare();
        return target != null;
    }
}
