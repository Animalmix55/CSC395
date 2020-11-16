package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.disasters.FireDisaster;
import com.kacstudios.game.disasters.InsectDisaster;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class WaterBucketItem extends IDepleteableItem{

    private static Texture texture = new Texture("items/WaterBucket.png");
    public WaterBucketItem(int amount){
        setAmount(amount);
        setDisplayName("Water Bucket");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        if (!event.farmerWithinRadius(300)) return;
        if (event.getGridSquare() == null) return;
        if (Plant.class.isAssignableFrom(event.getGridSquare().getClass())) {
            //ACTION LOGIC
            Plant target = (Plant) event.getGridSquare();
            FireDisaster disaster = (FireDisaster) target.getDisaster();

            if (disaster == null) return;
            target.setDisaster(null);

            //CHANGE QUANTITIES LOGIC
            float newPercent = getDepletionPercentage() + 0.2f;
            setDepletionPercentage(newPercent <= 1 ? newPercent : 1);

            if (getDepletionPercentage() >= 1) {
                if (getAmount() > 1) {
                    setDepletionPercentage(0);
                    setAmount(getAmount() - 1);
                } else parent.setItem(null);
            }
        }
        parent.checkItem();
    }


    @Override
    public Texture getTexture() { return texture; }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new WaterBucketItem(amount);
    }

}
