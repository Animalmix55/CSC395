package com.kacstudios.game.actors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.screens.LevelScreen;

import java.awt.geom.Point2D;

public class Tractor extends PlayableActor {
    private Farmer farmer;
    private boolean hasResized = true;
    private LevelScreen screen;

    public Tractor (float x, float y, LevelScreen s) {
        super(x,y,s.getMainStage(), false);
        screen = s;
        String[] leftMovementFiles = {"tractor-left-1.png"};
        String[] rightMovementFiles = {"tractor-right-1.png"};
        String[] upMovementFiles = {"tractor-up-1.png"};
        String[] downMovementFiles = {"tractor-down-1.png"};

        Animation<TextureRegion> leftAnimation = loadAnimationUnsetFromFiles(leftMovementFiles, 0.1f, true);
        Animation<TextureRegion> rightAnimation = loadAnimationFromFiles(rightMovementFiles,0.1f,true);
        Animation<TextureRegion> upAnimation = loadAnimationUnsetFromFiles(upMovementFiles,0.1f,true);
        Animation<TextureRegion> downAnimation = loadAnimationUnsetFromFiles(downMovementFiles,0.1f,true);
        setAcceleration(2000);
        setMaxSpeed(625);
        setDeceleration(5000);

        setDirectionalAnimations(leftAnimation, rightAnimation, upAnimation, downAnimation);
        setBoundaryPolyCustom(new float[]{0,0, getWidth(),0, getWidth(),getHeight(), 0,getHeight()});
    }

    @Override
    public void onClick() {
        if (getFocused()) removeFarmer();
        else {
            if (getDistanceFromFarmer() < 200) addFarmer();
        };
    }

    public void act(float dt) {
        super.act(dt);
        if (farmer != null) {
            if(getFocused()){
                setAnimationPaused(!isMoving());

                Direction currentDirection = getAnimationDirection();
                if(farmer.getAnimationDirection() != currentDirection) {
                    farmer.setAnimationDirection(currentDirection);
                    setBoundaryPolyCustom(new float[]{89, 0, getWidth() - 89, 0, getWidth() - 89, getHeight(), 89, getHeight()});
                }

                // every frame
                switch (currentDirection) { // run once per direction
                    case down:
                    case up:
                        farmer.setPosition(this.getX() + (getWidth() - farmer.getWidth()) / 2, this.getY() + 70);
                        break;
                    case right:
                        farmer.setPosition(this.getX() + 35, this.getY() + 70);
                        break;
                    case left:
                        farmer.setPosition(this.getX() + 115, this.getY() + 70);
                        break;
                }
            }
            else {
                farmer.preventOverlap(this);
                if(!hasResized &&
                        (getAnimationDirection() == Direction.left || getAnimationDirection() == Direction.right)) {
                    setBoundaryPolyCustom(new float[]{0, 0, getWidth(), 0, getWidth(), getHeight(), 0, getHeight()});
                    hasResized = true;
                }

            }
        }
    }

    /**
     * Sets the tractor status as being actively used, which disables player movement, matches player speed to tractor speed, and mounts the player onto the tractor
     */
    private void addFarmer() {
        this.setFocused(true);
        farmer.setFocused(false);
        farmer.setMaxSpeed(625);
    }

    /**
     * Sets the tractor status as inactive, re-enabling player movement and resetting speed to normal
     * Also forces tractor speed to 0, so that it doesn't slide when getting back on tractor for second time usage
     */
    private void removeFarmer() {
        hasResized = false;
        farmer.setFocused(true);
        farmer.setMaxSpeed(200);
        this.setSpeed(0);
        this.setFocused(false);
    }

    /**
     * Sets the farmer within the Tractor class so that it can be referred to within Tractor
     * @param farmer that should be mounted onto tractor when in use
     */
    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    /**
     * @return Pixel distance between tractor and farmer
     */
    private double getDistanceFromFarmer() {
        return Point2D.distance(
                farmer.getX()+(farmer.getWidth()/2),
                farmer.getY()+(farmer.getHeight()/2),
                this.getX()+(this.getWidth()/2),
                this.getY()+(this.getHeight()/2)
        );
    }
}
