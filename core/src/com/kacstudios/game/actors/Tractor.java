package com.kacstudios.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

import javax.xml.stream.FactoryConfigurationError;

public class Tractor extends BaseActor {
    private boolean remove;
    private Farmer farmer;
    Animation<TextureRegion> leftAnimation;
    Animation<TextureRegion> rightAnimation;
    Animation<TextureRegion> upAnimation;
    Animation<TextureRegion> downAnimation;
    int prevKey = -1;


    public Tractor (float x, float y, Stage s) {
        super(x,y,s);
        String[] leftMovementFiles = {"tractor-left-1.png"};
        String[] rightMovementFiles = {"tractor-right-1.png"};
        String[] upMovementFiles = {"tractor-right-1.png"};
        String[] downMovementFiles = {"tractor-right-1.png"};

        leftAnimation = loadAnimationUnsetFromFiles(leftMovementFiles, 0.1f, true);
        rightAnimation = loadAnimationUnsetFromFiles(rightMovementFiles,0.1f,true);
        upAnimation = loadAnimationUnsetFromFiles(upMovementFiles,0.1f,true);
        downAnimation = loadAnimationUnsetFromFiles(downMovementFiles,0.1f,true);
        setAcceleration(1000);
        setMaxSpeed(500);
        setDeceleration(1000);

        setBoundaryPolygon(8);
    }

    public void act(float dt) {

        super.act(dt);

        if (farmer != null) {
//            set farmer position on chair
//            need to change with direction


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
                    farmer.setPosition(this.getX() + 105, this.getY() + 70);
                    break;
                case Input.Keys.RIGHT:
                    farmer.setPosition(this.getX() + 25, this.getY() + 70);
                    break;
                case Input.Keys.UP:
                    farmer.setPosition(this.getX() + 25, this.getY() + 70);
                    break;
                case Input.Keys.DOWN:
                    farmer.setPosition(this.getX() + 25, this.getY() + 70);
                    break;
            }

            setAnimationPaused(!isMoving());

            boundToWorld();

            alignCamera();
        }
    }


    public void addFarmer(Farmer farmer) {
        this.farmer = farmer;
        farmer.setMovement(false);
        farmer.setMaxSpeed(500);

    }

    public void removeFarmer() {
        farmer.setMovement(true);
        farmer.setMaxSpeed(200);
        this.farmer = null;
    }

    public boolean onTractor() {
        return this.farmer != null;
    }
}
