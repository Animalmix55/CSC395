package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.kacstudios.game.actors.Tractor;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class BasicTractorItem extends IInventoryItem {
    private static Texture texture = new Texture("items/tractor-1.png");
    public static Music TractorSound;
    public static float volume=0;

    public BasicTractorItem(int amount) {
        setAmount(amount);
        setDisplayName("Basic Tractor");
        setInventoryItemType("II");
    }

    public BasicTractorItem() {
        this(1);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        TractorSound = Gdx.audio.newMusic(Gdx.files.internal("SoundEffects/tractor-engine.ogg"));
        TractorSound.setLooping(true);
        TractorSound.setVolume(volume);
        TractorSound.play();

        if(!event.farmerWithinRadius(300)) return;
        if(getAmount() > 0) {
            setAmount(getAmount() - 1);
            Vector2 clickLoc = event.getEventCoords();

            Tractor tractor = new Tractor(clickLoc.x, clickLoc.y, event.getScreen());
            tractor.setX(clickLoc.x - tractor.getWidth()/2);
            tractor.setY(clickLoc.y - tractor.getHeight()/2);
            event.getScreen().getAddedActors().add(tractor);
            if (getAmount() == 0) parent.setItem(null); // remove from inventory if none left
            parent.checkItem();
        }
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new BasicTractorItem(amount);
    }
}
