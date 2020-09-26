package com.kacstudios.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GridSquare extends BaseActor {

    private boolean collideWithPlayer;

    public GridSquare(float x, float y, Stage s, boolean collides)
    {
        super(x,y,s);
        collideWithPlayer = collides;
        loadTexture("grid_blank.png");
    }

    public void setTexture(String path) {
        try {
            Animation<TextureRegion> loadNewTexture = loadTexture(path);
            setAnimation(loadNewTexture);
        }
        catch (Exception ie) {
            System.out.println(ie);
        }
    }

    public void setCollisionSetting(boolean collides) {
        collideWithPlayer = collides;
    }

    public boolean getCollisionSetting() {
        return collideWithPlayer;
    }

    public void clickFunction() {
        System.out.println("I was clicked!");
    }

}
