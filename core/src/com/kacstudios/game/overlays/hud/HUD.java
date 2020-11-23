package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.inventoryItems.IInventoryItem;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.*;

public class HUD extends Group {
    LevelScreen screen;
    Image background;
    Label time;
    Label date;
    Label money;
    CustomizeFarmerButton customizeFarmerButton;
    MarketButton market;
    InventoryViewer inventoryViewer;

    public HUD(LevelScreen inputScreen){
        screen = inputScreen;
        background = new Image(new Texture("bottombar/background.png"));

        this.setWidth(background.getWidth());
        this.setHeight(background.getHeight());
        this.setBounds(0, 0, getWidth(), getHeight());
        this.addActor(background);
        this.setX((screen.getUIStage().getWidth() - 1280)/2); // center

        //ADD TIME

        time = new Label(TimeEngine.getFormattedString("HH:mm"),
                new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Bold.ttf", 20), Color.WHITE));
        time.setY(this.getHeight()-time.getHeight()-10);
        time.setX(15);

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

        // adding label for money

        money = new Label(TimeEngine.getFormattedString("$" + Economy.getMoney() + ".00"),
                new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Bold.ttf", 20), Color.WHITE));
        money.setX(tripleTimeButton.getX() + tripleTimeButton.getWidth() + 22);
        money.setY((background.getHeight() - money.getHeight())/2);

        Economy.subscribeToUpdate(() -> {
            money.setText("$" + Economy.getMoney() + ".00");
            money.pack();
            customizeFarmerButton.setX(money.getWidth() + money.getX() + 10);
            market.setX(customizeFarmerButton.getWidth() + customizeFarmerButton.getX());
        }); // subscribe to updates in balance

        this.addActor(money);

        //ADD CUSTOMIZATION BUTTON
        customizeFarmerButton = new CustomizeFarmerButton() {
            @Override
            public void onClick(InputEvent event, float x, float y) {
                screen.openCustomization(!getSelected());
            }
        };
        customizeFarmerButton.setX(money.getWidth() + money.getX() + 10);
        customizeFarmerButton.setY((background.getHeight() - customizeFarmerButton.getHeight()) / 2);
        addActor(customizeFarmerButton);

        //ADD MARKET BUTTON
        market = new MarketButton(this);
        market.setX(customizeFarmerButton.getWidth() + customizeFarmerButton.getX());
        market.setY((background.getHeight() - market.getHeight()) / 2);

        this.addActor(market);

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

    public InventoryViewer getInventoryViewer() {
        return inventoryViewer;
    }

    public void toggleMarketButton(boolean isOpen) {
        market.setSelected(isOpen);
    }

    public void toggleCustomizationButton(boolean isOpen) {
        customizeFarmerButton.setSelected(isOpen);
    }

    public LevelScreen getScreen() {
        return screen;
    }
}
