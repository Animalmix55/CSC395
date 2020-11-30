package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.disasters.FireDisaster;
import com.kacstudios.game.disasters.InsectDisaster;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.WaterSource;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.SelectableButton;
import com.kacstudios.game.inventoryItems.EmptyBucketItem;


public class WaterBucketItem extends IDepleteableItem{

    private static Texture texture2 = new Texture("items/waterbucket.png");
    private boolean isEmpty = false;

    public WaterBucketItem() {this(1);}
    public WaterBucketItem(int amount){
        super(true, 300);
        setAmount(amount);
        setDisplayName("Water Bucket");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
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
                    parent.setItem(null); // self destruct
                }

                parent.getViewer().addItem(new EmptyBucketItem());
            }
        }
        parent.checkItem();
    }


    @Override
    public Texture getTexture() { return texture2; }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new WaterBucketItem(amount);
    }

    @Override
    protected boolean isBlocked(GridClickEvent event) {
        GridSquare target = event.getGridSquare();
        return target == null || !Plant.class.isAssignableFrom(target.getClass()) ||
                ((Plant) target).getDisaster() == null || ((Plant) target).getDisaster().getClass() != FireDisaster.class;
    }
}
