package com.kacstudios.game.disasters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.utilities.TimeEngine;

import java.time.LocalDateTime;
import java.util.Random;

public abstract class Disaster extends BaseActor {
    protected Plant plant;
    protected LocalDateTime startTime;
    protected float plantGrowthModifier;
    protected float secondsToKill;

    /**
     * Creates a disaster actor
     * @param parent The plant that the disaster is placed on top of.
     * @param animation The animation the disaster should superimpose over the plant.
     * @param secondsToKill The amount of seconds needed to cause the plant to die.
     * @param plantGrowthModifier The growth affect factor that will be multiplied into the factor currently
     *                            affecting the target plant.
     */
    public Disaster(Plant parent,
                    Animation<TextureRegion> animation,
                    float secondsToKill,
                    float plantGrowthModifier
    ) {
        this.plant = parent;
        this.startTime = TimeEngine.getDateTime();
        this.secondsToKill = secondsToKill;
        this.plantGrowthModifier = plantGrowthModifier;
        setAnimation(animation);
        startTime = TimeEngine.getDateTime();
        plant.setGrowthRateModifier(plant.getGrowthRateModifier() * plantGrowthModifier);
    }

    /**
     * generates a number from 1-5
     */
    public static int generateRandom(int min, int max){
        Random rand = new Random();
        return Math.round((max-min) * rand.nextFloat() + min);
    }

    @Override
    public boolean remove() {
        plant.setGrowthRateModifier(plant.getGrowthRateModifier() / plantGrowthModifier); // undo multiplication
        return super.remove();
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        if(TimeEngine.getSecondsSince(startTime) > secondsToKill)
        {
            plant.setDead(true);
            plant.setDisaster(null);
        }
    }
}
