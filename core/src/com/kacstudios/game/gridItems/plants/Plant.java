package com.kacstudios.game.gridItems.plants;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kacstudios.game.gridItems.GridSquare;
import com.kacstudios.game.utilities.TimeEngine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Plant extends GridSquare {

    private LocalDateTime startTime;
    private Boolean isPlanted = false;
    private Boolean fullyGrown = false;
    private long growthTime = 60;
    private ArrayList<Animation<TextureRegion>> growthTextures = new ArrayList<>();

    public Plant(float x, float y, Stage s, boolean collides) {
        super(x, y, s, collides);
    }

    public void setGrowthTextures(String[] textureNames) {
        growthTextures = new ArrayList<>();
        Arrays.stream(textureNames).forEach(t -> growthTextures.add(this.loadTexture(t))); // add all textures
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
            setTexture("TestSeed.png");
            isPlanted = true;
            fullyGrown = false;
        }
    }

    /**
     *  Gives us the ability to set the growthTime of specific plants.
     */
    public void setGrowthTime(Long gTime)
    {
        growthTime = gTime;
    }

    public Boolean checkIfGrowing()
    {
        return isPlanted;
    }

    /**
     * Called within the update() of LevelScreen, this checks to see the growth percentage of the plant
     *
     */
    public void checkStatus() {
        if(isPlanted && !fullyGrown)
        {
            int elapsedSeconds = (int) TimeEngine.getSecondsSince(startTime);
            int numTextures = growthTextures.size();

            for(int i = 1; i <= numTextures; i++){
                if((int)(growthTime * ((float)i/numTextures)) == elapsedSeconds) {
                    setAnimation(growthTextures.get(i-1));
                    break;
                }
            }

            if(elapsedSeconds >= growthTime) {
                fullyGrown = true;
                isPlanted = false;
            }
        }
    }

}