package com.kacstudios.game.overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.screens.LevelScreen;

public class HUD extends Group {
    LevelScreen screen;
    Image background;
    Image roundedButton;

    public HUD(LevelScreen inputScreen){
        screen = inputScreen;
        background = new Image(new Texture("bottombar/background.png"));
        this.addActor(background);

        this.setX((screen.getUIStage().getWidth() - 1280)/2); // center

        screen.getUIStage().addActor(this); // add to screen
    }
}
