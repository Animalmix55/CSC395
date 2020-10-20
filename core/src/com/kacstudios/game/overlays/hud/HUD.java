package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.TimeEngine;
import sun.font.TextLabel;

import java.util.Arrays;

public class HUD extends Group {
    LevelScreen screen;
    Image background;
    InventoryButton[] hotButtons = new InventoryButton[8];
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
            hotButtons[i] = new InventoryButton(getWidth() - 64*i, 0);
            this.addActor(hotButtons[i]);
        }

        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Actor target = event.getTarget();

                if(target.getClass().getSimpleName().equals("InventoryButton")){ // is inventory item
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

        screen.getUIStage().addActor(this); // add to screen
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time.setText(TimeEngine.getFormattedString("HH:mm a"));
    }
}
