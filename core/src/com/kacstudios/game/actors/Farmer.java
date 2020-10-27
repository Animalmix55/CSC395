package com.kacstudios.game.actors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.utilities.TimeEngine;

import java.awt.*;

public class Farmer extends BaseActor {
    public boolean remove;
    Animation<TextureRegion> leftAnimation;
    Animation<TextureRegion> rightAnimation;
    Animation<TextureRegion> upAnimation;
    Animation<TextureRegion> downAnimation;
    int maxSpeed = 200;
    int prevKey = -1;

    MoveToAction action;

    public Farmer(float x, float y, Stage s) {
        super(x, y, s);

        String[] downAnimationFiles =
                {"farmer-1.png", "farmer-2.png", "farmer-3.png",
                        "farmer-4.png", "farmer-5.png", "farmer-6.png", "farmer-7.png", "farmer-8.png"};

        String[] leftMovementFiles = {"farmer-left-1.png", "farmer-left-2.png", "farmer-left-3.png", "farmer-left-4.png", "farmer-left-5.png", "farmer-left-6.png", "farmer-left-7.png", "farmer-left-8.png"};
        String[] rightMovementFiles = {"farmer-right-1.png", "farmer-right-2.png", "farmer-right-3.png", "farmer-right-4.png", "farmer-right-5.png", "farmer-right-6.png", "farmer-right-7.png", "farmer-right-8.png"};
        String[] upMovementFiles = {"farmer-up-1.png", "farmer-up-2.png", "farmer-up-3.png", "farmer-up-4.png", "farmer-up-5.png", "farmer-up-6.png", "farmer-up-7.png", "farmer-up-8.png"};

        downAnimation = loadAnimationFromFiles(downAnimationFiles, 0.1f, true);
        leftAnimation = loadAnimationUnsetFromFiles(leftMovementFiles, 0.1f, true);
        rightAnimation = loadAnimationUnsetFromFiles(rightMovementFiles, 0.1f, true);
        upAnimation = loadAnimationUnsetFromFiles(upMovementFiles, 0.1f, true);

        setAcceleration(1000);
        setMaxSpeed(maxSpeed);
        setDeceleration(1000);

        setBoundaryPolygon(8);
    }

//to get coordinates do this.getx()

    public static double  getAngleFromPoint(Point firstPoint, Point secondPoint) {

        if((secondPoint.x > firstPoint.x)) {//above 0 to 180 degrees

            return (Math.atan2((secondPoint.x - firstPoint.x), (firstPoint.y - secondPoint.y)) * 180 / Math.PI);

        }
        else if((secondPoint.x < firstPoint.x)) {//above 180 degrees to 360/0

            return 360 - (Math.atan2((firstPoint.x - secondPoint.x), (firstPoint.y - secondPoint.y)) * 180 / Math.PI);

        }//End if((secondPoint.x > firstPoint.x) && (secondPoint.y <= firstPoint.y))

        return Math.atan2(0 ,0);

    }//End public float getAngleFromPoint(Point firstPoint, Point secondPoint)
    public static double angleBetweenTwoPointsWithFixedPoint(double point1X, double point1Y,
                                                             double point2X, double point2Y,
                                                             double fixedX, double fixedY) {

        double angle1 = Math.atan2(point1Y - fixedY, point1X - fixedX);
        double angle2 = Math.atan2(point2Y - fixedY, point2X - fixedX);

        double result= angle1 - angle2;
        System.out.println(angle1);
        System.out.println(angle2);

        return angle2;
    }




    public void moveTo(float x, float y){


        // System.out.println("Old location:"+getX()+","+getY());
        //  System.out.println("new location:"+x+","+y);

        double angle=Math.toDegrees(angleBetweenTwoPointsWithFixedPoint(getX(),getY(),x,y,getX(),getY()));
        // System.out.println("Angle is:"+angle);
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
        addAction(action);

        double distance = Math.sqrt(Math.pow(Math.abs(x-getX()), 2) + Math.pow(Math.abs(y-getY()), 2));

        action.setDuration((float)distance / maxSpeed);
    }


    public void act(float dt) {
        super.act(dt);
        // configure sprite direction

        if (Gdx.input.isKeyPressed(Keys.W)) {
            if (prevKey != Keys.W) setAnimation(upAnimation);
            prevKey = Keys.W;
        } else if (Gdx.input.isKeyPressed(Keys.D)) {
            if (prevKey != Keys.D) setAnimation(rightAnimation);
            prevKey = Keys.D;
        } else if (Gdx.input.isKeyPressed(Keys.A)) {
            if (prevKey != Keys.A) setAnimation(leftAnimation);
            prevKey = Keys.A;
        } else if (Gdx.input.isKeyPressed(Keys.S)) {
            if (prevKey != Keys.S) setAnimation(downAnimation);
            prevKey = Keys.S;
        }

        if (remove) {
            addAction(Actions.fadeOut(1));
            addAction(Actions.after(Actions.removeActor()));
            return;
        }

        // configure acceleration
        if (Gdx.input.isKeyPressed(Keys.A))
            accelerateAtAngle(180);//left
        if (Gdx.input.isKeyPressed(Keys.D))
            accelerateAtAngle(0);//right
        if (Gdx.input.isKeyPressed(Keys.W))
            accelerateAtAngle(90);//up
        if (Gdx.input.isKeyPressed(Keys.S))
            accelerateAtAngle(270);//down


        applyPhysics(dt);
        if(action!=null) {
            if(!action.isComplete())
                setAnimationPaused(false);
            else
                setAnimationPaused(!isMoving());
        }
        else setAnimationPaused(!isMoving());

        boundToWorld();

        alignCamera();
    }
}
