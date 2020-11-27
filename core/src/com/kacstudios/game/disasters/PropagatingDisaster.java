package com.kacstudios.game.disasters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.plants.Plant;

import java.util.ArrayList;

public abstract class PropagatingDisaster extends Disaster {
    /**
     * Number of times it spreads per second
     */
    private float spreadFrequency;
    private float secondsSinceLastSpread = 0;

    /**
     * Creates a disaster actor
     *
     * @param parent              The plant that the disaster is placed on top of.
     * @param animation           The animation the disaster should superimpose over the plant.
     * @param secondsToKill       The amount of seconds needed to cause the plant to die.
     * @param plantGrowthModifier The growth affect factor that will be multiplied into the factor currently
     * @param spreadRate          The rate amount of potential spreads per second.
     */
    public PropagatingDisaster(Plant parent, Animation<TextureRegion> animation, float secondsToKill, float plantGrowthModifier, float spreadRate) {
        super(parent, animation, secondsToKill, plantGrowthModifier);
        spreadFrequency = spreadRate;
    }

    @Override
    public void act(float dt) {
        secondsSinceLastSpread += dt;

        if (secondsSinceLastSpread > 1/spreadFrequency) {
            ArrayList<GridSquare> adjSquares = plant.getAdjacentSquares();

            if (adjSquares.size() > 0) {
                int rnd = generateRandom(0, adjSquares.size() - 1);
                GridSquare target = adjSquares.get(rnd);
                if (Plant.class.isAssignableFrom(target.getClass())) {
                    Plant targetPlant = (Plant) target;
                    if (targetPlant.getDisaster() == null && !targetPlant.getDead()) {
                        if (plant.getDisaster().getClass() == InsectDisaster.class){
                            targetPlant.setDisaster(new InsectDisaster(targetPlant));}
                        if (plant.getDisaster().getClass() == FireDisaster.class){
                            targetPlant.setDisaster(new FireDisaster((targetPlant)));}
                    }

                }
            }
            secondsSinceLastSpread = 0;
        }

        super.act(dt);
    }
}
