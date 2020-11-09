package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.screens.LevelScreen;

public class Market extends Group {
    LevelScreen screen;
    Image background;

    public Market(LevelScreen inputScreen) {
        screen = inputScreen;
        background = new Image(new Texture("market/background.png"));

        this.setWidth(background.getWidth());
        this.setHeight(background.getHeight());
        this.setBounds(0,0,getWidth(),getHeight());

        
    }
}
