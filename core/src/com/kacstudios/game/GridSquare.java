package com.kacstudios.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class GridSquare extends BaseActor {

    public GridSquare(float x, float y, Stage s)
    {
        super(x,y,s);
        loadTexture("grid_blank.png");
//        loadTexture("grid_red.png");
    }

    public void setTexture(String path) {
        System.out.println("set");
        try {
            loadTexture("grid_red.png");
        }
        catch (Exception ie) {
            System.out.println(ie);
        }

    }

}
