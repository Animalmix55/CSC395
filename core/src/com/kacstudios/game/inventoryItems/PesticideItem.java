package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.disasters.FireDisaster;
import com.kacstudios.game.disasters.InsectDisaster;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;


public class PesticideItem extends IDepleteableItem{

    private static Texture texture = new Texture("items/insecticide.png");
    public PesticideItem(int amount){
        super(true, 300);
        setAmount(amount);
        setDisplayName("Pesticide");
    }

    public PesticideItem() {
        this(1);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent){
        //ACTION LOGIC
        Plant target = (Plant) event.getGridSquare();
        if (target.getDisaster() != null && target.getDisaster().getClass() == InsectDisaster.class) {
            InsectDisaster disaster = (InsectDisaster) target.getDisaster();
            if(disaster == null) return;
            if(disaster.getInsecticideAmount() == 1){ target.setDisaster(null); }
            else{ disaster.setInsecticideAmount(disaster.getInsecticideAmount() - 1); }



        //CHANGE QUANTITIES LOGIC
            float newPercent = getDepletionPercentage() + 0.05f;
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
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new PesticideItem(amount);
    }

    @Override
    protected boolean isBlocked(GridClickEvent event) {
        GridSquare target = event.getGridSquare();
        return target == null || !Plant.class.isAssignableFrom(target.getClass()) || ((Plant) target).getDisaster() == null ||
                ((Plant) target).getDisaster().getClass() != InsectDisaster.class;
    }
}
