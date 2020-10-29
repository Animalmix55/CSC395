package com.kacstudios.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.awt.geom.Point2D;

public class Tractor extends BaseActor {
    private Farmer farmer;
    private boolean active = false;
    Animation<TextureRegion> leftAnimation;
    Animation<TextureRegion> rightAnimation;
    Animation<TextureRegion> upAnimation;
    Animation<TextureRegion> downAnimation;
    int prevKey = -1;


    public Tractor (float x, float y, Stage s) {
        super(x,y,s);
        String[] leftMovementFiles = {"tractor-left-1.png"};
        String[] rightMovementFiles = {"tractor-right-1.png"};
        String[] upMovementFiles = {"tractor-up-1.png"};
        String[] downMovementFiles = {"tractor-down-1.png"};

        leftAnimation = loadAnimationUnsetFromFiles(leftMovementFiles, 0.1f, true);
        rightAnimation = loadAnimationFromFiles(rightMovementFiles,0.1f,true);
        upAnimation = loadAnimationUnsetFromFiles(upMovementFiles,0.1f,true);
        downAnimation = loadAnimationUnsetFromFiles(downMovementFiles,0.1f,true);
        setAcceleration(2000);
        setMaxSpeed(625);
        setDeceleration(5000);

        setBoundaryPolyCustom(new float[]{0,0, getWidth(),0, getWidth(),getHeight(), 0,getHeight()});

//        adds click listener so that if the player is within 200 pixels of the tractor, they can click it to be added onto the tractor
//        if the player is already on the tractor, then they are removed from the tractor
        this.addListener(
                (Event e) ->
                {
                    if (!(e instanceof InputEvent))
                        return false;

                    if (!((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    if (active) removeFarmer();
                    else {
                        if (getDistanceFromFarmer() < 200) addFarmer();
                    };

                    return false;
                }
        );

    }

    public void act(float dt) {

        super.act(dt);
        if (farmer != null) {
            if (active) {
                if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                    if(prevKey != Input.Keys.UP) {
                        setAnimation(upAnimation);
                    };
                    prevKey = Input.Keys.UP;
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    if(prevKey != Input.Keys.RIGHT) {
                        setAnimation(rightAnimation);
                    }
                    prevKey = Input.Keys.RIGHT;
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    if(prevKey != Input.Keys.LEFT) {
                        setAnimation(leftAnimation);
                    }
                    prevKey = Input.Keys.LEFT;
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    if (prevKey != Input.Keys.DOWN) {
                        setAnimation(downAnimation);
                    }
                    prevKey = Input.Keys.DOWN;
                }

                if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) accelerateAtAngle(180);
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) accelerateAtAngle(0);
                if (Gdx.input.isKeyPressed(Input.Keys.UP)) accelerateAtAngle(90);
                if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) accelerateAtAngle(270);

                applyPhysics(dt);

                switch (prevKey) {
                    case Input.Keys.LEFT:
                        farmer.setPosition(this.getX() + 115, this.getY() + 70);
                        setBoundaryPolyCustom(new float[]{0,0, getWidth(),0, getWidth(),getHeight(), 0,getHeight()});
                        break;
                    case Input.Keys.RIGHT:
                        farmer.setPosition(this.getX() + 35, this.getY() + 70);
                        setBoundaryPolyCustom(new float[]{0,0, getWidth(),0, getWidth(),getHeight(), 0,getHeight()});
                        break;
                    case Input.Keys.UP:
                    case Input.Keys.DOWN:
                        setBoundaryPolyCustom(new float[]{89,0, getWidth()-89,0, getWidth()-89,getHeight(), 89,getHeight()});
                        farmer.setPosition(this.getX() + 75, this.getY() + 70);
//                    farmer.setPosition(this.getX(), this.getY());
//                    farmer.setPosition(this.getX() + 75, this.getY() + 50);
                        break;
                }

                setAnimationPaused(!isMoving());

                boundToWorld();

                alignCamera();
            }
            else {
                farmer.preventOverlap(this);
            }
        }
    }

    /**
     * Sets the tractor status as being actively used, which disables player movement, matches player speed to tractor speed, and mounts the player onto the tractor
     */
    private void addFarmer() {
        active = true;
        farmer.setMovement(false);
        farmer.setMaxSpeed(625);

    }

    /**
     * Sets the tractor status as inactive, re-enabling player movement and resetting speed to normal
     * Also forces tractor speed to 0, so that it doesn't slide when getting back on tractor for second time usage
     */
    private void removeFarmer() {
        farmer.setMovement(true);
        farmer.setMaxSpeed(200);
        this.setSpeed(0);
        active = false;
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
