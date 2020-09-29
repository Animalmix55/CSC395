package com.kacstudios.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Plant extends GridSquare{

    public Plant(float x, float y, Stage s, boolean collides) {
        super(x, y, s, collides);
        //setTexture("TestSeed.png");

        //add variables here to be set by another function
    }

    @Override
    public void clickFunction() {
        setTexture("TestSeed.png");
    }

    public void checkStatus() {
        int startTime = 0;
        int currentTime = 1000;
        int harvestTime = 2000;
        int timeElapsed = currentTime - startTime;
        int progress = timeElapsed/harvestTime;

        if(progress >= .25)
        {
            loadTexture("Plant1.png");
        }
        if(progress >= .50)
        {
            loadTexture("Plant2.png");
        }
        if(progress >= .75)
        {
            loadTexture("Plant3.png");
        }
        if(progress == .1)
        {
            loadTexture("Plant4.png");
        }
    }

}
