package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kacstudios.game.inventoryItems.IInventoryItem;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.TimeEngine;

public class HUD extends Group {
    LevelScreen screen;
    Image background;
    Label time;
    Label date;
    CustomizeFarmerButton customizeFarmerButton;
    InventoryViewer inventoryViewer;

    public HUD(LevelScreen inputScreen){
        screen = inputScreen;
        background = new Image(new Texture("bottombar/background.png"));

        this.setWidth(background.getWidth());
        this.setHeight(background.getHeight());
        this.setBounds(0, 0, getWidth(), getHeight());
        this.addActor(background);
        this.setX((screen.getUIStage().getWidth() - 1280)/2); // center

        //ADD CUSTOMIZATION BUTTON
        customizeFarmerButton = new CustomizeFarmerButton();
        customizeFarmerButton.setX(5);
        customizeFarmerButton.setY((background.getHeight() - customizeFarmerButton.getHeight()) / 2);

        this.addActor(customizeFarmerButton);

        //ADD TIME

        time = new Label(TimeEngine.getFormattedString("HH:mm"),
                new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Bold.ttf", 20), Color.WHITE));
        time.setY(this.getHeight()-time.getHeight()-10);
        time.setX(customizeFarmerButton.getWidth() + customizeFarmerButton.getX() + 5);

        this.addActor(time);

        date = new Label(TimeEngine.getFormattedString("MM/dd/yyyy"),
                new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Bold.ttf", 15), Color.WHITE));
        date.setX(time.getX());
        date.setY(10);

        this.addActor(date);

        // SET UP TIME CONTROL BUTTONS
        TimeControlButton pauseButton = new TimeControlButton(TimeControlButton.ButtonType.Pause);
        pauseButton.setX(date.getX() + date.getWidth() + 15);
        pauseButton.setY((this.getHeight() - pauseButton.getHeight()) / 2);
        this.addActor(pauseButton);

        TimeControlButton playButton = new TimeControlButton(TimeControlButton.ButtonType.Play);
        playButton.setX(pauseButton.getX() + pauseButton.getWidth() + 10);
        playButton.setY((this.getHeight() - playButton.getHeight()) / 2);
        this.addActor(playButton);

        TimeControlButton doubleTimeButton = new TimeControlButton(TimeControlButton.ButtonType.Double);
        doubleTimeButton.setX(playButton.getX() + playButton.getWidth() + 10);
        doubleTimeButton.setY((this.getHeight() - doubleTimeButton.getHeight()) / 2);
        this.addActor(doubleTimeButton);

        TimeControlButton tripleTimeButton = new TimeControlButton(TimeControlButton.ButtonType.Triple);
        tripleTimeButton.setX(doubleTimeButton.getX() + doubleTimeButton.getWidth() + 10);
        tripleTimeButton.setY((this.getHeight() - tripleTimeButton.getHeight()) / 2);
        this.addActor(tripleTimeButton);

        // END TIME CONTROL BUTTONS

        // Set up inventory viewer
        inventoryViewer = new InventoryViewer();
        inventoryViewer.setX(getWidth() - inventoryViewer.getWidth());
        this.addActor(inventoryViewer);

        screen.getUIStage().addActor(this); // add to screen
    }
    public HUD(LevelScreen inputScreen, IInventoryItem[] initialInventoryItems){
        this(inputScreen);
        inventoryViewer.setItems(initialInventoryItems);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        date.setText(TimeEngine.getFormattedString("MM/dd/yyyy"));
        time.setText(TimeEngine.getFormattedString("HH:mm a"));
    }

    public void handleGridClickEvent(GridClickEvent event){
        useItem(event);
    }

    /**
     * Calls the onDeployment method of the item in the selected HUD slot, provided one exists
     * @param event
     */
    public void useItem(GridClickEvent event){
        inventoryViewer.onUseItem(event);
    }

    public InventoryViewer getInventoryViewer() { return inventoryViewer; }
}
