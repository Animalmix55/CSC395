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
import com.kacstudios.game.actors.Farmer;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.inventoryItems.*;
import com.kacstudios.game.overlays.hud.HUD;
import com.kacstudios.game.overlays.market.Market;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.TimeEngine;
import com.kacstudios.game.windows.PauseWindow;

public class LevelScreen extends BaseScreen {
    private Farmer farmer;
    private Grid grid;
    PauseWindow pauseWindow;
    private HUD hud;
    private Market market;

    public void initialize() {
        // placeholder initial inventory
        IInventoryItem[] initialItems = {
                new CornPlantItem(15),
                new WateringCanItem(3),
                new BasicTractorItem(40),
                new PesticideItem(5),
                new BlueberriesPlantItem(5)
        };
        TimeEngine.Init();
        pauseWindow = new PauseWindow(this);
        grid = new Grid(10, 10, this); // create grid

        hud = new HUD(this, initialItems); // add HUD

        market = new Market(this); // add market overlay

        //pause button

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();

        Texture buttonTex = new Texture(Gdx.files.internal("button_pause_48x48.png"));
        TextureRegion buttonRegion = new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable(buttonRegion);

        Button PauseButton = new Button(buttonStyle);
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

        mainStage.addActor(grid); // add grid to stage
//      add in farmer actor
        farmer = new Farmer(20, 20, mainStage);
    }

    public void update(float dt) {

    }

    @Override
    public void render(float dt) {
        // Propagates time dilations to DeltaTime
        TimeEngine.act(dt);
        super.render(dt * TimeEngine.getDilation());
    }

    public boolean keyDown(int keyCode) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            pauseWindow.setVisible();

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
