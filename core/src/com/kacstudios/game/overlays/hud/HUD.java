package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.SelectableButton;
import com.kacstudios.game.utilities.TimeEngine;

public class HUD extends Group {
    LevelScreen screen;
    Image background;
    HotItemButton[] hotButtons = new HotItemButton[8];
    Label time;
    Label date;

    public HUD(LevelScreen inputScreen){
        screen = inputScreen;
        background = new Image(new Texture("bottombar/background.png"));

        this.setWidth(background.getWidth());
        this.setHeight(background.getHeight());
        this.setBounds(0, 0, getWidth(), getHeight());
        this.addActor(background);
        this.setX((screen.getUIStage().getWidth() - 1280)/2); // center

        // init buttons
        for(int i = 0; i < hotButtons.length; i++){
            hotButtons[i] = new HotItemButton(getWidth() - 64*i, 0);
            this.addActor(hotButtons[i]);
        }

        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Actor target = event.getTarget();

                if(target.getClass().getSimpleName().equals(HotItemButton.class.getSimpleName())){ // is inventory item
                    for(int j = 0; j < hotButtons.length; j++){
                        if(hotButtons[j] == target) hotButtons[j].setSelected(true);
                        else hotButtons[j] .setSelected(false);
                    };
                }
            }
        });

        time = new Label(TimeEngine.getFormattedString("HH:mm"),
                new Label.LabelStyle(FarmaniaFonts.generateFont("OpenSans.ttf", 20), Color.WHITE));
        time.setY(this.getHeight()-time.getHeight()-10);
        time.setX(10);

        this.addActor(time);

        date = new Label(TimeEngine.getFormattedString("MM/dd/yyyy"),
                new Label.LabelStyle(FarmaniaFonts.generateFont("OpenSans.ttf", 15), Color.WHITE));
        date.setX(10);
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


        screen.getUIStage().addActor(this); // add to screen
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time.setText(TimeEngine.getFormattedString("HH:mm a"));
    }
}
