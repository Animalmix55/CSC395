package com.kacstudios.game.disasters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.utilities.TimeEngine;

import java.time.LocalDateTime;
import java.util.Random;

public class InsectDisaster extends BaseActor {

    //for insect noise
    //public boolean isactive true then sound loop on

    public int insecticideAmount;
    public LocalDateTime attackStartTime;
    public boolean isActive = false;
    public boolean isComplete = false;
    Animation<TextureRegion> insectAnimation;
    Plant plant;

    public InsectDisaster(Plant impl) {
        insecticideAmount = generateRandom();
        plant = impl;
        String[] insectAnimationFiles = {"pest-1.png", "pest-2.png", "pest-3.png", "pest-4.png"};

        setVisible(false);
        attackStartTime = TimeEngine.getDateTime();
        insectAnimation = loadAnimationFromFiles(insectAnimationFiles, 0.1f,true);
    }

    public int getInsecticideAmount() {
        return insecticideAmount;
    }

    public void setInsecticideAmount(int insecticideAmount) {
        this.insecticideAmount = insecticideAmount;
    }

    //generates a number from 1-5
    public static int generateRandom(){
        Random rand = new Random();
        int upperbound = 4;
        return rand.nextInt(upperbound) + 1;
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        if(plant.getGrowthPercentage() > .5 && !isVisible()) setActive(true);

        if(isActive && !isComplete){
            // Kills plant if insects not killed in 10 seconds
            if(TimeEngine.getSecondsSince(attackStartTime) > 10.0)
            {
                plant.setDead(true);
                isComplete = true;
                plant.setInsect(null);
            }
        }

    }

    /**
     * Officially begins the disaster
     * @param active
     */
    public void setActive(boolean active) {
        isActive = active;
        setVisible(true);
        attackStartTime = TimeEngine.getDateTime();
        plant.setGrowthRateModifier(.25f);
        isComplete = false;
    }

    public void endDisaster(){
        plant.setGrowthRateModifier(1);
        plant.setInsect(null);
    }
}