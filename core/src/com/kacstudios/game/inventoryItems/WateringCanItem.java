package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.GridClickEvent;

public class WateringCanItem extends IDepleteableItem {
    public WateringCanItem(){
        setTexturePath("items/watering_can.png");
        setBreakable(false);
    }

    @Override
    public void onDeployment(GridClickEvent event) {
        //pass
    }
}
