package com.kacstudios.game.grid.plants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.utilities.TimeEngine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Plant extends GridSquare {

    private Image drySoil;
    private Image wetSoil;

    private float growthPercentage = 0;
    private float growthRateModifier = 1;

    private Boolean fullyGrown = false;
    private Boolean isDead = false;
    private long growthTime = 60;
    private ArrayList<Image> growthImages = new ArrayList<>();
    private Image deadImage;

    public Plant(String[] growthTexturePaths, String deadTexturePath) {
        super(false);
        drySoil = new Image(new Texture("soil.png"));
        wetSoil = new Image(new Texture("soil-wet.png"));
        deadImage = new Image(new Texture(deadTexturePath));
        deadImage.setVisible(false);
        wetSoil.setVisible(false);

        addActor(drySoil);
        addActor(wetSoil);
        addActor(deadImage);
        setGrowthTextures(growthTexturePaths);
    }

    private void setGrowthTextures(String[] textureNames) {
        growthImages = new ArrayList<>();
        Arrays.stream(textureNames).forEach(t -> {
            Image temp = new Image(new Texture(t));
            temp.setVisible(false);
            growthImages.add(temp);
            addActor(temp);
        }); // add all textures
    }

    /**
     *  Overrides the clickFunction of GridSquare.
     */
    @Override
    public void clickFunction(LocalDateTime time)
    {
        //pass
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
        return !fullyGrown;
    }

    /**
     * Returns the modified rate for how much a plant should grow per second
     * @return
     */
    private float getPercentPerSecond() {
        return ((float)1/growthTime) * growthRateModifier;
    }

    /**
     * Called within the update() of Grid, this checks to see the growth percentage of the plant
     */
    @Override
    public void act(float dt) {
        super.act(dt);

        if(!fullyGrown && !isDead)
        {
            float tempGrowthPercentage = getPercentPerSecond() * dt + growthPercentage; // update growth percentage
            if(tempGrowthPercentage >= 1) {
                fullyGrown = true;
                growthPercentage = 1;
            }
            else growthPercentage = tempGrowthPercentage;

            int numTextures = growthImages.size();

            if(fullyGrown) { // if grown, don't look for other textures
                for (int i = numTextures - 2; i > 0; i--){
                    growthImages.get(i).setVisible(false);
                }
                growthImages.get(numTextures-1).setVisible(true);
            }
            else {
                for(int i = numTextures; i > 0; i--){ // if not fully grown, find stage
                    if(((float)(i-1)/(numTextures-1)) <= growthPercentage) {
                        growthImages.get(i-1).setVisible(true);
                        break;
                    }
                }
            }
        }
    }

    public void setWatered(boolean isWatered){
        wetSoil.setVisible(isWatered);
        drySoil.setVisible(!isWatered);
    }

    public boolean getWatered(){
        return wetSoil.isVisible();
    }

    /**
     * Sets the modifer that determines how quickly the plant should grow.
     * Ex: setting a modifier of .5 makes the plant grow at half speed.
     * setting the modifier as 2 makes the plant grow double speed.
     * @param growthRateModifier
     */
    public void setGrowthRateModifier(float growthRateModifier) {
        this.growthRateModifier = growthRateModifier;
    }

    public void resetGrowthRateModifier(){
        this.growthRateModifier = 1;
    }

    /**
     * Kills the plant
     */
    public void setDead(boolean isDead) {
        if(isDead) {
            for (int i = growthImages.size()-1; i > 0; i--){
                growthImages.get(i).setVisible(false);
            }
            this.isDead = true;
            deadImage.setVisible(true);
        }
        else
            isDead = false;
    }

    public Boolean getDead() {
        return isDead;
    }
}