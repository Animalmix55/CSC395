package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.GridClickEvent;

public class WateringCanItem extends IDepleteableItem {
    public WateringCanItem(){
        setTexturePath("items/watering_can.png");
        setAmount(3);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        float newPercent = getDepletionPercentage() + 0.10f;
        setDepletionPercentage(newPercent <= 1? newPercent : 1);

        if(getDepletionPercentage() >= 1){
            if(getAmount() > 1) {
                setDepletionPercentage(0);
                setAmount(getAmount() - 1);
            }
            else parent.setItem(null);
        }

        parent.checkItem();
    }
}
