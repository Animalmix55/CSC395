package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.disasters.InsectDisaster;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;


public class PesticideItem extends IDepleteableItem{

    private static Texture texture = new Texture("items/insecticide.png");
    public PesticideItem(int amount){
        setAmount(amount);
        setDisplayName("Pesticide");
    }

    public PesticideItem() {
        this(1);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent){
        if(!event.farmerWithinRadius(300)) return;
        if(event.getGridSquare() == null) return;
        if(Plant.class.isAssignableFrom(event.getGridSquare().getClass())){
            //ACTION LOGIC
            Plant target = (Plant) event.getGridSquare();
            InsectDisaster disaster = (InsectDisaster) target.getDisaster();

            if(disaster == null) return;

            if(disaster.getInsecticideAmount() == 1){ target.setDisaster(null); }
            else{ disaster.setInsecticideAmount(disaster.getInsecticideAmount() - 1); }

            //CHANGE QUANTITIES LOGIC
            float newPercent = getDepletionPercentage() + 0.10f;
            setDepletionPercentage(newPercent <= 1? newPercent : 1);

            if(getDepletionPercentage() >= 1){
                if(getAmount() > 1) {
                    setDepletionPercentage(0);
                    setAmount(getAmount() - 1);
                }
                else parent.setItem(null);
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
}
