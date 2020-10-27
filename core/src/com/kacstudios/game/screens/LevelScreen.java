package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.actors.Farmer;
import com.kacstudios.game.actors.Tractor;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.grid.plants.CornPlant;
import com.kacstudios.game.inventoryItems.CornPlantItem;
import com.kacstudios.game.inventoryItems.IInventoryItem;
import com.kacstudios.game.inventoryItems.WateringCanItem;
import com.kacstudios.game.overlays.hud.HUD;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.TimeEngine;
import com.kacstudios.game.windows.PauseWindow;


import java.util.ArrayList;
import java.util.List;

public class LevelScreen extends BaseScreen {
    private Farmer farmer;
    private Tractor tractor;
    private List<BaseActor> outOfBoundsArea;
    private Grid grid;
    PauseWindow pauseWindow;
    private HUD hud;

    public void initialize() {
        // placeholder initial inventory
        IInventoryItem[] initialItems = {
                new CornPlantItem(15),
                new WateringCanItem()
        };

//        set background/map limits
        TimeEngine.Init();
        pauseWindow = new PauseWindow(this);
        // TimeEngine.dilateTime(1000); // freeze time
        grid = new Grid(this); // create grid

        hud = new HUD(this, initialItems); // add HUD


//        out of bounds background
//        counter clockwise, starting from the right middle
        outOfBoundsArea = new ArrayList<>();
        outOfBoundsArea.add(new BaseActor(1080, 0, mainStage));
        outOfBoundsArea.add(new BaseActor(1080, 1080, mainStage));
        outOfBoundsArea.add(new BaseActor(0, 1080, mainStage));
        outOfBoundsArea.add(new BaseActor(-1080, 1080, mainStage));
        outOfBoundsArea.add(new BaseActor(-1080, 0, mainStage));
        outOfBoundsArea.add(new BaseActor(-1080, -1080, mainStage));
        outOfBoundsArea.add(new BaseActor(0, -1080, mainStage));
        outOfBoundsArea.add(new BaseActor(1080, -1080, mainStage));
        for (BaseActor baseActor : outOfBoundsArea) {
            baseActor.loadTexture("grass-outofbounds_1080x1080.png");
            baseActor.setSize(1080, 1080);
        }


        //pause button

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();

        Texture buttonTex = new Texture(Gdx.files.internal("button_pause_48x48.png"));
        TextureRegion buttonRegion = new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable(buttonRegion);

        Button PauseButton = new Button(buttonStyle);
//        PauseButton.setColor( Color.CYAN );
        PauseButton.setPosition(20, 650);
        uiStage.addActor(PauseButton);

        PauseButton.addListener(
                (Event e) ->
                {
                    if (!(e instanceof InputEvent))
                        return false;

                    if (!((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    setPaused(true);
                    TimeEngine.pause();
                    pauseWindow.setVisible();

                    return false;
                }
        );

//        add in grid squares

        grid.addGridSquare(2, 2, new CornPlant(false));
        grid.addGridSquare(2, 3, new CornPlant(false));
        grid.addGridSquare(3, 2, new CornPlant(false));
        grid.addGridSquare(3, 3, new CornPlant(false));

        mainStage.addActor(grid); // add grid to stage
//        add in farmer actor
        tractor = new Tractor(100,100,mainStage);
        farmer = new Farmer(20, 20, mainStage);
        tractor.addFarmer(farmer);
    }

    public void update(float dt) {
        //pass
    }

    public boolean keyDown(int keyCode) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            pauseWindow.setVisible();
        if (Gdx.input.isKeyPressed(Input.Keys.B))
            if (tractor.onTractor()) {
                tractor.removeFarmer();
            }
            else {
                tractor.addFarmer(farmer);
            }
        return true;
    }

    public Farmer getFarmer(){
        return farmer;
    }

    public Stage getMainStage(){
        return mainStage;
    }

    public Stage getUIStage(){
        return uiStage;
    }

    public Grid getGrid() {
        return grid;
    }

    public boolean getPaused(){
        return paused;
    }

    public void setPaused(boolean isPaused){
        paused = isPaused;
    }

    public void handleGridClickEvent(GridClickEvent event){
        hud.handleGridClickEvent(event); // pass grid click event to hud for item
    }
}
