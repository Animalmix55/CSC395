package com.kacstudios.game.actors;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Farmer extends PlayableActor {
    Animation<TextureRegion> leftTractorAnimation;
    Animation<TextureRegion> rightTractorAnimation;
    Animation<TextureRegion> upTractorAnimation;
    Animation<TextureRegion> downTractorAnimation;

    Animation<TextureRegion> leftAnimation;
    Animation<TextureRegion> rightAnimation;
    Animation<TextureRegion> upAnimation;
    Animation<TextureRegion> downAnimation;

    FarmerAnimationFactory animationFactory = new FarmerAnimationFactory();

    public Farmer(float x, float y, Stage s)
    {
        super(x,y,s, true);
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

        String[] leftTractor = {"farmer-tractor-left.png"};
        String[] rightTractor = {"farmer-tractor-right.png"};
        String[] upTractor = {"farmer-tractor-up.png"};
        String[] downTractor = {"farmer-tractor-down.png"};

        downTractorAnimation = loadAnimationFromFiles(downTractor, 0.1f, true);
        leftTractorAnimation = loadAnimationUnsetFromFiles(leftTractor, 0.1f, true);
        rightTractorAnimation = loadAnimationUnsetFromFiles(rightTractor, 0.1f, true);
        upTractorAnimation = loadAnimationUnsetFromFiles(upTractor, 0.1f, true);

        setDirectionalAnimationPaths(leftMovementFiles, rightMovementFiles, upMovementFiles, downAnimationFiles);
        setDownAnimation(FarmerAnimationFactory.createAnimation(
                Color.BLUE, animationFactory.getTextures().front.pants.get(0),
                Color.WHITE, animationFactory.getTextures().front.skinKeyframes,
                Color.RED, animationFactory.getTextures().front.shirts.get(0)
        ));


        setAcceleration(1000);
        setMaxSpeed(200);
        setDeceleration(1000);

        setBoundaryPolygon(8);
    }

    public void useTractorAnimations(boolean isOnTractor) {
        if(isOnTractor){
            setDirectionalAnimations(leftTractorAnimation, rightTractorAnimation, upTractorAnimation, downTractorAnimation);
        }
        else {
            setDirectionalAnimations(leftAnimation, rightAnimation, upAnimation, downAnimation);
        }
    }
}
