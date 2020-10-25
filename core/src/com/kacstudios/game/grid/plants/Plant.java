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

    private LocalDateTime startTime;
    private Boolean fullyGrown = false;
    private long growthTime = 60;
    private ArrayList<Texture> growthTextures = new ArrayList<>();

    public Plant(boolean collides) {
        super(collides);
        startTime = TimeEngine.getDateTime();

        drySoil = new Image(new Texture("soil.png"));
        wetSoil = new Image(new Texture("soil-wet.png"));
        wetSoil.setVisible(false);

        addActor(drySoil);
        addActor(wetSoil);
    }

    public void setGrowthTextures(String[] textureNames) {
        growthTextures = new ArrayList<>();
        Arrays.stream(textureNames).forEach(t -> growthTextures.add(new Texture(t))); // add all textures
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int elapsedSeconds = (int) TimeEngine.getSecondsSince(startTime);
        int numTextures = growthTextures.size();
        super.draw(batch, parentAlpha);

        if(fullyGrown) { // if grown, don't look for other textures
            batch.draw(growthTextures.get(numTextures-1), getX(), getY());
            return;
        }

        for(int i = numTextures; i > 0; i--){ // if not fully grown, find stage
            if(growthTime * ((float)(i-1)/(numTextures-1)) <= elapsedSeconds) {
                batch.draw(growthTextures.get(i-1), getX(), getY());
                return;
            }
        }
    }

    /**
     * Called within the update() of Grid, this checks to see the growth percentage of the plant
     */
    @Override
    public void act(float dt) {
        super.act(dt);
        if(!fullyGrown)
        {
            int elapsedSeconds = (int) TimeEngine.getSecondsSince(startTime);
            if(elapsedSeconds >= growthTime) {
                fullyGrown = true;
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
}