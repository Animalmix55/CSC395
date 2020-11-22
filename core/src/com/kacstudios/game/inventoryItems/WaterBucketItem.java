package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.disasters.FireDisaster;
import com.kacstudios.game.disasters.InsectDisaster;
import com.kacstudios.game.grid.WaterSource;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.SelectableButton;
import com.kacstudios.game.inventoryItems.EmptyBucketItem;


public class WaterBucketItem extends IDepleteableItem{

    private static Texture texture1 = new Texture("items/EmptyBucket.png");
    private static Texture texture2 = new Texture("items/WaterBucket.png");
    private boolean isEmpty = false;

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
            if (isEmpty) return;
            Plant target = (Plant) event.getGridSquare();
            if (target.getDisaster() != null && target.getDisaster().getClass() == FireDisaster.class) {
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
                    }
                    else {
                        parent.setContents(new Image(texture1));
                        isEmpty = true;
                    }

                }
            }
        }

        if(!event.farmerWithinRadius(300)) return;
        if(event.getGridSquare() == null) return;
        if(WaterSource.class.isAssignableFrom(event.getGridSquare().getClass())) {
            setDepletionPercentage(0);
            isEmpty = false;
            parent.setContents(new Image(texture2));
        }
        parent.checkItem();
    }


    @Override
    public Texture getTexture() { return texture2; }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new WaterBucketItem(amount);
    }

}
