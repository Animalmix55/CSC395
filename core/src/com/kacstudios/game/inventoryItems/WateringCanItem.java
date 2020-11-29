package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.Setting;

public class WateringCanItem extends IDepleteableItem {
    private static Texture texture = new Texture("items/watering_can.png");

    public static Music WateringSound;
    public static float volume= Setting.GameVolume*0.01f;


    public WateringCanItem(int amount){
        setAmount(amount);
        setDisplayName("Watering Can");
        setInventoryItemType("ID");
    }

    public WateringCanItem() {
        this(1);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {

        if(!event.farmerWithinRadius(300)) return; // must be within 300 pixels
        if(event.getGridSquare() == null) return; // must already be a grid square
        if(Plant.class.isAssignableFrom(event.getGridSquare().getClass())){ // the square must be a plant
            //ACTION LOGIC
            Plant target = (Plant) event.getGridSquare();
            if(target.getWatered()) return; // the square must not already be watered

            target.setWatered(true);

            WateringSound = Gdx.audio.newMusic(Gdx.files.internal("SoundEffects/watering_effect.ogg"));
            WateringSound.setVolume(volume);
            WateringSound.play();

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
        return new WateringCanItem(amount);
    }
}
