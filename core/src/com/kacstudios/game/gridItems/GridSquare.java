package com.kacstudios.game.gridItems;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kacstudios.game.actors.BaseActor;

import java.time.LocalDateTime;

public class GridSquare extends BaseActor {

    private boolean collideWithPlayer;

    public GridSquare(float x, float y, Stage s, boolean collides)
    {
        super(x,y,s);
        collideWithPlayer = collides;
        loadTexture("soil.png");
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

    public void clickFunction(LocalDateTime dateTime) {
        System.out.println("I was clicked!");
    }

}
