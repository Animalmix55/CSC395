package com.kacstudios.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.utilities.TimeEngine;

import java.util.ArrayList;

/**
 * An actor which has 4 directional animations and can take control of the camera
 */
public class PlayableActor extends BaseActor {
    public enum Direction {
        left,
        right,
        up,
        down
    }
    private boolean isFocused;

    private Vector2 prevPos = new Vector2(0, 0);

    private Animation<TextureRegion> leftAnimation;
    private Animation<TextureRegion> rightAnimation;
    private Animation<TextureRegion> upAnimation;
    private Animation<TextureRegion> downAnimation;
    private int prevKey = Input.Keys.D;
    private MoveToAction action;

    public PlayableActor(float x, float y, Stage s, boolean focused) {
        super(x, y, s);
        this.isFocused = focused;

        PlayableActor actor = this;

        s.addListener(new ClickListener(Input.Buttons.RIGHT){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(isFocused) {
                    super.clicked(event, x, y);
                    float clickX = Gdx.input.getX();
                    float clickY = Gdx.input.getY();
                    Vector3 translatedLocation = s.getCamera().unproject(new Vector3(clickX, clickY, 0));
                    actor.moveTo(translatedLocation.x - actor.getWidth() / 2, translatedLocation.y - actor.getHeight() / 2);
                }
            }
        });
    }

    public void setDirectionalAnimationPaths(String[] left, String[] right, String[] up, String[] down){
        downAnimation = loadAnimationFromFiles(down, 0.1f, true);
        leftAnimation = loadAnimationUnsetFromFiles(left, 0.1f, true);
        rightAnimation = loadAnimationUnsetFromFiles(right, 0.1f, true);
        upAnimation = loadAnimationUnsetFromFiles(up, 0.1f, true);
    }

    public void setDirectionalAnimations(Animation<TextureRegion> left, Animation<TextureRegion> right, Animation<TextureRegion> up, Animation<TextureRegion> down){
        downAnimation = down;
        leftAnimation = left;
        rightAnimation = right;
        upAnimation = up;
    }

    public static double angleBetweenTwoPointsWithFixedPoint(double point1X, double point1Y,
                                                             double point2X, double point2Y,
                                                             double fixedX, double fixedY) {
        double angle1 = Math.atan2(point1Y - fixedY, point1X - fixedX);
        double angle2 = Math.atan2(point2Y - fixedY, point2X - fixedX);
        double result= angle1 - angle2;
        return angle2;
    }

    public void moveTo(float x, float y){
        double angle=Math.toDegrees(angleBetweenTwoPointsWithFixedPoint(getX(),getY(),x,y,getX(),getY()));

        if(angle<=45 && angle >=-45){
            setAnimation(rightAnimation);
            accelerateAtAngle(0);
        }
        else
        if(angle>45 && angle<=135){
            setAnimation(upAnimation);
            accelerateAtAngle(90);
        }
        else if (angle>135 || angle <-135){
            setAnimation(leftAnimation);
            accelerateAtAngle(180);
        }
        else {
            setAnimation(downAnimation);
            accelerateAtAngle(270);
        }


        action= new MoveToAction();
        action.setPosition(x,y);
        action.setActor(this);

        double distance = Math.sqrt(Math.pow(Math.abs(x-getX()), 2) + Math.pow(Math.abs(y-getY()), 2));

        action.setDuration((float)distance / getMaxSpeed());
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        if (isFocused) {
            // configure sprite direction
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                if (prevKey != Input.Keys.W) setAnimation(upAnimation);
                prevKey = Input.Keys.W;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (prevKey != Input.Keys.D) setAnimation(rightAnimation);
                prevKey = Input.Keys.D;
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (prevKey != Input.Keys.A) setAnimation(leftAnimation);
                prevKey = Input.Keys.A;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                if (prevKey != Input.Keys.S) setAnimation(downAnimation);
                prevKey = Input.Keys.S;
            }

            // configure acceleration
            if (Gdx.input.isKeyPressed(Input.Keys.A))
                accelerateAtAngle(180);
            if (Gdx.input.isKeyPressed(Input.Keys.D))
                accelerateAtAngle(0);
            if (Gdx.input.isKeyPressed(Input.Keys.W))
                accelerateAtAngle(90);
            if (Gdx.input.isKeyPressed(Input.Keys.S))
                accelerateAtAngle(270);

            if(action != null) action.act(dt * TimeEngine.getDilation()); // update action

            applyPhysics(dt);
            boundToWorld();
            alignCamera();

            if(action != null) {
                if(!action.isComplete()) {
                    setAnimationPaused(false);
                }
                else
                    setAnimationPaused(!isMoving());
            }
            else setAnimationPaused(!isMoving());
        }

        prevPos.x = getX();
        prevPos.y = getY();
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public boolean getFocused() {
        return isFocused;
    }

    public int getPrevKey() {
        return prevKey;
    }

    public void setAnimationDirection(Direction direction) {
        switch (direction) {
            case up:
                setAnimation(upAnimation);
                break;
            case down:
                setAnimation(downAnimation);
                break;
            case left:
                setAnimation(leftAnimation);
                break;
            case right:
                setAnimation(rightAnimation);
                break;
        }
    }

    public Direction getAnimationDirection() {
        Animation<TextureRegion> animation = getAnimation();

        if (animation == leftAnimation) return Direction.left;
        else if (animation == rightAnimation) return Direction.right;
        else if (animation == downAnimation) return Direction.down;
        else return Direction.up;
    }

    @Override
    public Vector2 preventOverlap(BaseActor other) {
        Vector2 vector = super.preventOverlap(other);
        if(vector != null && action != null) {
            removeAction(action);
            action = null;
        }

        return vector;
    }
}
