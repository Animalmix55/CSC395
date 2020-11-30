package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.kacstudios.game.actors.Tractor;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class BasicTractorItem extends IInventoryItem {
    private static Texture texture = new Texture("items/basic-tractor.png");
    public BasicTractorItem(int amount) {
        setAmount(amount);
        setDisplayName("Basic Tractor");
    }

    public BasicTractorItem() {
        this(1);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        if(!event.farmerWithinRadius(300)) return;
        if(getAmount() > 0) {
            setAmount(getAmount() - 1);
            Vector2 clickLoc = event.getScreen().getMainStage().screenToStageCoordinates(event.getEventCoords());

            Tractor tractor = new Tractor(clickLoc.x, clickLoc.y, event.getScreen());
            tractor.setX(clickLoc.x - tractor.getWidth()/2);
            tractor.setY(clickLoc.y - tractor.getHeight()/2);
            event.getScreen().getAddedActors().add(tractor);
            if (getAmount() == 0) parent.setItem(null); // remove from inventory if none left
            parent.checkItem();
        }
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new BasicTractorItem(amount);
    }

    @Override
    public void onEquippedChange(boolean isEquipped, Grid grid) {
        super.onEquippedChange(isEquipped, grid);
    }
}
