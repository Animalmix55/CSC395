package com.kacstudios.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Farmer extends BaseActor {
    public boolean remove;

    public Farmer(float x, float y, Stage s)
    {
        super(x,y,s);

        String[] filenames =
                {"farmer-1.png", "farmer-2.png", "farmer-3.png",
                        "farmer-4.png", "farmer-5.png", "farmer-6.png"};

        loadAnimationFromFiles(filenames, 0.1f, true);

        setAcceleration(1000);
        setMaxSpeed(200);
        setDeceleration(1000);

        setBoundaryPolygon(8);
    }

    public void act(float dt)
    {
        super.act( dt );
        if (remove) {
            addAction( Actions.fadeOut(1) );
            addAction( Actions.after( Actions.removeActor() ) );
            return;
        }
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

        if ( getSpeed() > 0 )
            setRotation( getMotionAngle() );

        boundToWorld();

        alignCamera();
    }

}
