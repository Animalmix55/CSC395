package com.kacstudios.game.disasters;

import com.kacstudios.game.grid.plants.Plant;

public class FireDisaster extends PropagatingDisaster{

    public FireDisaster(Plant impl) {
        super(impl,
                loadAnimationUnsetFromFiles(
                        new String[]{"disaster-textures/fire-1.png", "disaster-textures/fire-2.png"}, 0.1f,true),
                10,
                .25f,
                .4f
        );

        // removes itself occasionally if the host is watered.
        if(impl.getWatered() && generateRandom(0, 5) < 5) impl.setDisaster(null);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
    }

    @Override
    public Disaster createInstance(Plant target) {
        return new FireDisaster(target);
    }
}
