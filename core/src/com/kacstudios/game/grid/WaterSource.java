package com.kacstudios.game.grid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.utilities.TimeEngine;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class WaterSource extends GridSquare{

    private static final int pollingSeconds = 1;
    private LocalDateTime lastCheck = TimeEngine.getDateTime();

    public WaterSource() {
        setTexture("grid-textures/water.png");
        setCollideWithPlayer(true);
        setBoundaryRectangle();
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        if(TimeEngine.getSecondsSince(lastCheck) > pollingSeconds) {
            waterAdjacentPlants();
            lastCheck = TimeEngine.getDateTime();
        }
    }

    private void waterAdjacentPlants() {
        ArrayList<GridSquare> adj = getAdjacentSquares();
        for (GridSquare square:
             adj) {
            if (Plant.class.isAssignableFrom(square.getClass())) {
                Plant plant = (Plant) square;
                if (!plant.getWatered()) {
                    plant.setWatered(true);
                }
                plant.setCanDry(false); // never dries
            }
        }
    }

    @Override
    public boolean remove() {
        ArrayList<GridSquare> adj = getAdjacentSquares();
        for (GridSquare square:
                adj) {
            if (Plant.class.isAssignableFrom(square.getClass())) {
                Plant plant = (Plant) square;
                plant.setCanDry(true);
                plant.setWatered(true); // give it a last moment
            }
        }
        return super.remove();
    }
}
