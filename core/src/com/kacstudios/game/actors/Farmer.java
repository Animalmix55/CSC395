package com.kacstudios.game.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kacstudios.game.actors.BaseActor;

public class Farmer extends BaseActor {
    public boolean remove;
    Animation<TextureRegion> leftAnimation;
    Animation<TextureRegion> rightAnimation;
    Animation<TextureRegion> upAnimation;
    Animation<TextureRegion> downAnimation;
    float previousAngle = -1;
    int prevKey = -1;


    public Farmer(float x, float y, Stage s)
    {
        super(x,y,s);

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
        setMaxSpeed(200);
        setDeceleration(1000);

        setBoundaryPolygon(8);
    }

    public void act(float dt)
    {
        super.act( dt );

        // configure sprite direction
        if(Gdx.input.isKeyPressed(Keys.UP)){
            if(prevKey != Keys.UP) setAnimation(upAnimation);
            prevKey = Keys.UP;
        }
        else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            if(prevKey != Keys.RIGHT) setAnimation(rightAnimation);
            prevKey = Keys.RIGHT;
        }
        else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            if(prevKey != Keys.LEFT) setAnimation(leftAnimation);
            prevKey = Keys.LEFT;
        }
        else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            if (prevKey != Keys.DOWN) setAnimation(downAnimation);
            prevKey = Keys.DOWN;
        }

        if (remove) {
            addAction( Actions.fadeOut(1) );
            addAction( Actions.after( Actions.removeActor() ) );
            return;
        }

        // configure acceleration
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            accelerateAtAngle(180);
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            accelerateAtAngle(0);
        if (Gdx.input.isKeyPressed(Keys.UP))
            accelerateAtAngle(90);
        if (Gdx.input.isKeyPressed(Keys.DOWN))
            accelerateAtAngle(270);

        applyPhysics(dt);

        setAnimationPaused( !isMoving() );

//        if ( getSpeed() > 0 )
//            setRotation( getMotionAngle() );

        boundToWorld();

        alignCamera();
    }

}
