package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.math.Vector2;
import com.kacstudios.game.actors.Tractor;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class BasicTractorItem extends IInventoryItem {
    public BasicTractorItem(int amount) {
        setTexturePath("items/tractor-1.png");
        setAmount(amount);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        if(!event.farmerWithinRadius(300)) return;
        if(getAmount() > 0) {
            setAmount(getAmount() - 1);
            Vector2 clickLoc = event.getEventCoords();

            Tractor tractor = new Tractor(clickLoc.x, clickLoc.y, event.getScreen());
            tractor.setX(clickLoc.x - tractor.getWidth()/2);
            tractor.setY(clickLoc.y - tractor.getHeight()/2);
            if (getAmount() == 0) parent.setItem(null); // remove from inventory if none left
        }
    }
}
