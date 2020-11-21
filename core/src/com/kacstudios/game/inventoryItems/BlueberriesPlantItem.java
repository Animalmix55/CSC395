package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.grid.plants.BlueberriesPlant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.Setting;

public class BlueberriesPlantItem extends IInventoryItem {
    private static Texture texture = new Texture("items/blueberries.png");
    public static Music PlantingSound;
    public static float volume= Setting.GameVolume*0.01f;

    public BlueberriesPlantItem(){
        this(1);
    }

    public BlueberriesPlantItem(int amount){
        setAmount(amount);
        setDisplayName("Blueberry Seed");
        setInventoryItemType("II");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {

        if(!event.farmerWithinRadius(300)) return; // must be within 300 pixels
        if(event.getGridSquare() == null) {
            event.setSquare(new BlueberriesPlant());
            int amount = getAmount();
            amount--;
            setAmount(amount >= 0 ? amount : 0);

            if(amount == 0){ // amount of -1 signifies unlimited
                parent.setItem(null); // remove from button
            }
            PlantingSound = Gdx.audio.newMusic(Gdx.files.internal("SoundEffects/planting-sounds.ogg"));
            PlantingSound.setVolume(volume);
            PlantingSound.play();
            parent.checkItem(); // update button display amount
        }
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new BlueberriesPlantItem(amount);
    }
}
