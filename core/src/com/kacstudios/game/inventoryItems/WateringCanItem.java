package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.sounds.GameSounds;
import com.kacstudios.game.utilities.GridClickEvent;

public class WateringCanItem extends IDepleteableItem {
    private static Texture texture = new Texture("items/watering-can.png");
    public WateringCanItem(int amount){
        super(true, 300);
        setAmount(amount);
        setDisplayName("Watering Can");
    }

    public WateringCanItem() {
        this(1);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        GameSounds.wateringSound.play(false);

        //ACTION LOGIC
        Plant target = (Plant) event.getGridSquare();
        target.setWatered(true);

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


        parent.checkItem();
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new WateringCanItem(amount);
    }

    @Override
    protected boolean isBlocked(GridClickEvent event) {
        GridSquare target = event.getGridSquare();
        return target == null || !Plant.class.isAssignableFrom(target.getClass()) || ((Plant) target).getWatered();
    }
}
