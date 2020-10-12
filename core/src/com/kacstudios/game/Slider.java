package com.kacstudios.game;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import javax.swing.*;

public class Slider extends BaseActor {
    public boolean remove;

    public Slider(float x, float y, Stage s)
    {
        super(x,y,s);
        //slider1 = new JSlider(JSlider.VERTICAL, 0,10,0);
        new JSlider(JSlider.VERTICAL, 0,10,0);


    }

    public void act(float dt)
    {
        super.act( dt );

    }

}
