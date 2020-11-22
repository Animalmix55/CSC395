package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kacstudios.game.actors.gridexpansion.GridExpandPrompt;
import com.kacstudios.game.actors.PlayableActor;
import com.kacstudios.game.disasters.InsectDisaster;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.grid.plants.CornPlant;
import com.kacstudios.game.actors.Farmer.Farmer;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.inventoryItems.*;
import com.kacstudios.game.overlays.hud.HUD;
import com.kacstudios.game.overlays.market.Market;
import com.kacstudios.game.utilities.Economy;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.TimeEngine;
import com.kacstudios.game.windows.PauseMenu;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class LevelScreen extends BaseScreen {
    private Farmer farmer;
    private List<PlayableActor> addedActors;
    private Grid grid;
    private HUD hud;
    private Market market;
    private PauseMenu pauseMenu;

    private boolean loadingFromSave;
    private int gridWidth;
    private int gridHeight;
    private List<Plant> savedPlants;
    private Object[] objectItems;
    private IInventoryItem[] savedInventoryItems;
    private String savedTime;
    private int saveFileNum = -1;


    // new level
    public LevelScreen() {
        super(false);
        loadingFromSave = false;
        gridWidth = 8;
        gridHeight = 8;
        initialize();
    }

    // loading level from save
    public LevelScreen(int width, int height, List<Plant> plantsToImport, List<IInventoryItem> itemsToImport, String timeImport) {
        super(false);
        loadingFromSave = true;
        gridWidth = width;
        gridHeight = height;
        savedPlants = plantsToImport;
        objectItems = itemsToImport.toArray();
        savedInventoryItems = new IInventoryItem[objectItems.length];
        for (int i=0;i<objectItems.length;i++) {
            savedInventoryItems[i] = (IInventoryItem) objectItems[i];
        }
        savedTime = timeImport;
        initialize();
    }

    public void initialize() {
        // placeholder initial inventory
        IInventoryItem[] initialItems = {
                new CornPlantItem(100),
                new WateringCanItem(3),
                new BasicTractorItem(40),
                new PesticideItem(5),
                new BlueberriesPlantItem(100)
        };
        if (loadingFromSave) TimeEngine.Init( LocalDateTime.parse(savedTime) );
        else TimeEngine.Init();
        Economy.Init();
        Economy.addMoney(100000000); // for testing

        grid = new Grid(gridHeight, gridWidth, this);
        CornPlant test = new CornPlant();
        test.setDisaster(new InsectDisaster(test));

        if (loadingFromSave) {
            for (Plant currentPlant : savedPlants) {
                grid.addGridSquare(currentPlant.getSavedX(), currentPlant.getSavedY(), currentPlant);
            }
            hud = new HUD(this, savedInventoryItems);
        }
        else {
            hud = new HUD(this, initialItems);
        }

//        market = new Market(this); // add market overlay
        pauseMenu = new PauseMenu(this);

        //pause button

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();

        Texture buttonTex = new Texture(Gdx.files.internal("button_pause_48x48.png"));
        TextureRegion buttonRegion = new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable(buttonRegion);

        Button PauseButton = new Button(buttonStyle);
        PauseButton.setPosition(20, 650);
        uiStage.addActor(PauseButton);

        market = new Market(this) {
            @Override
            public void onOpen() {
                hud.toggleMarketButton(true);
            }

            @Override
            public void onClose() {
                hud.toggleMarketButton(false);
            }
        };
        market.setVisible(false);

        PauseButton.addListener(
                (Event e) ->
                {
                    if (!(e instanceof InputEvent))
                        return false;

                    if (!((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    setPaused(true);
                    TimeEngine.pause();
                    pauseMenu.setVisible(true);
                    pauseMenu.setMenu_pause();

                    return false;
                }
        );

        mainStage.addActor(grid); // add grid to stage
//      add in farmer actor
        farmer = new Farmer(20, 20, mainStage);
        addedActors = new ArrayList<PlayableActor>();
        new GridExpandPrompt(this);
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
        // escape key used for pause/going back in pause menu
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            if (pauseMenu.getCurrentMenu() != null) {
                if (pauseMenu.getCurrentMenu() == "pause") {
                    setPaused(false);
                    TimeEngine.resume();
                    pauseMenu.setMenu_resume();
                    pauseMenu.setVisible(false);
                }
                else {
                    pauseMenu.setMenu_pause();
                }
            }
            else {
                setPaused(true);
                TimeEngine.pause();
                pauseMenu.setMenu_pause();
                pauseMenu.setVisible(true);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            System.out.println(TimeEngine.getDateTime());
        }
        return true;
    }

    public Farmer getFarmer(){
        return farmer;
    }

    public List<PlayableActor> getAddedActors() { return addedActors; }

    public void addSecondaryActor(PlayableActor actor) { addedActors.add(actor); }

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

    public HUD getHud() {
        return hud;
    }

    public PauseMenu getPauseMenu() { return pauseMenu; }
    public void openMarket(boolean isOpen) {
        market.setVisible(isOpen);
    }
}
