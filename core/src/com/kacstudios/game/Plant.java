package com.kacstudios.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Plant extends GridSquare{

    private LocalDateTime startTime;
    private Boolean isPlanted = false;
    private Boolean fullyGrown = false;
    private long growthTime = 60;

    public Plant(float x, float y, Stage s, boolean collides) {
        super(x, y, s, collides);

        //setTexture("TestSeed.png");

        //add variables here to be set by another function
    }

    /**
     *  Overrides the clickFunction of GridSquare.
     *  Upon a click of an empty GridSquare, the time of the click is recorded and the seed texture is set.
     */
    @Override
    public void clickFunction(LocalDateTime time)
    {
        if(!isPlanted)
        {
            startTime = time;
            System.out.println(time);
            setTexture("TestSeed.png");
            isPlanted = true;
        }
    }

    /**
     *  Gives us the ability to set the growthTime of specific plants.
     */
    public void setGrowthTime(Long gTime)
    {
        growthTime = gTime;
    }

    /**
     * Called within the update() of LevelScreen, this checks to see the growth percentage of the plant
     *
     */
    public void checkStatus() {
        if(isPlanted)
        {
            Long elapsedTime = TimeEngine.getSecondsSince(startTime);
            if(elapsedTime >= (growthTime*.25))
            {
                setTexture("grid_blank.png");
            }
            if(elapsedTime >= (growthTime*.50))
            {
                setTexture("grid_red.png");
            }
            if(elapsedTime >= (growthTime*.75))
            {
                setTexture("grid_blue.png");
            }
            if(elapsedTime >= growthTime)
            {
                setTexture("grid_green.png");
                fullyGrown = true;
            }
        }
    }

}
