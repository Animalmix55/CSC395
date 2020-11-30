package com.kacstudios.game.disasters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.sounds.GameSounds;
import com.kacstudios.game.utilities.TimeEngine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class InsectDisaster extends PropagatingDisaster {

    private int insecticideAmount;
    private long soundId;

    public InsectDisaster(Plant impl) {
        super(impl,
                loadAnimationUnsetFromFiles(
                        new String[]{"disaster-textures/pest-1.png", "disaster-textures/pest-2.png",
                                "disaster-textures/pest-3.png", "disaster-textures/pest-4.png"}, 0.1f,true),
                30,
                .5f,
                .25f
        );
        insecticideAmount = generateRandom(1, 5);

        soundId = GameSounds.insectSound.play(true);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
    }

    @Override
    public Disaster createInstance(Plant target) {
        return new InsectDisaster(target);
    }

    @Override
    public boolean remove() {
        GameSounds.insectSound.stop(soundId);
        return super.remove();
    }

    public int getInsecticideAmount() {
        return insecticideAmount;
    }

    public void setInsecticideAmount(int insecticideAmount) {
        this.insecticideAmount = insecticideAmount;
    }
}