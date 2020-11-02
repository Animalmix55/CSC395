package com.kacstudios.game.inventoryItems;

import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;


public class PesticideItem extends IDepleteableItem{
    public PesticideItem(int amount){
        setTexturePath("items/insecticide.png");
        setAmount(amount);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent){
        if(!event.farmerWithinRadius(300)) return;
        if(event.getGridSquare() == null) return;
        if(Plant.class.isAssignableFrom(event.getGridSquare().getClass())){
            //ACTION LOGIC
            Plant target = (Plant) event.getGridSquare();
            if(!target.getInsect()) return;

            if(target.insecticideAmount == 1){ target.setInsect(false); }
            else{ target.insecticideAmount--; }

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
}
