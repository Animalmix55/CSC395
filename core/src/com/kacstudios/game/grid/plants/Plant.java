package com.kacstudios.game.grid.plants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.disasters.Disaster;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.disasters.InsectDisaster;
import com.kacstudios.game.utilities.TimeEngine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Plant extends GridSquare {

    private Image drySoil;
    private Image wetSoil;

    private float growthPercentage = 0;
    private float growthRateModifier = 1;

    private int savedX;
    private int savedY;

    private Boolean fullyGrown = false;
    private Boolean isDead = false;
    private Boolean isProtected = false;
    private long growthTime = 60;
    private LocalDateTime startTime = TimeEngine.getDateTime();
    private ArrayList<Image> growthImages = new ArrayList<>();
    private Image deadImage;
    private Disaster disaster;

    private String plantType; // used when saving game to define what type of plant is being saved

    public Plant(String[] growthTexturePaths, String deadTexturePath) {
        super();
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

    public void setDisaster(Disaster disaster) {
        if(this.disaster != null) this.disaster.remove();
        this.disaster = disaster;

        if(this.disaster != null) addActor(disaster);
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

            for (int i = numTextures - 1; i > 0; i--){
                growthImages.get(i).setVisible(false);
            }

            if(fullyGrown) { // if grown, don't look for other textures
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

    public float getGrowthRateModifier() {
        return growthRateModifier;
    }

    public void resetGrowthRateModifier(){
        this.growthRateModifier = 1;
    }

    public Disaster getDisaster() {
        return disaster;
    }

    /**
     * Kills the plant
     */
    public void setDead(boolean isDead) {
        if(isDead) {
            this.isDead = true;
            for (int i = 0; i < growthImages.size(); i++){
                growthImages.get(i).setVisible(false);
            }
            deadImage.setVisible(true);
        }
        else
            this.isDead = false;
    }

    public Boolean getDead() {
        return isDead;
    }

    public float getGrowthPercentage() {
        return growthPercentage;
    }

    public void setGrowthPercentage(float newPercentage) {
        growthPercentage = newPercentage;
    }

    public void setSavedX(int x) { savedX = x; }

    public void setSavedY(int y) { savedY = y; }

    public int getSavedX() { return savedX; }

    public int getSavedY() { return savedY; }

    public void setPlantName(String plantName) { plantType = plantName; }

    public String getPlantName() { return plantType; }
}