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

        String[] leftTractor = {"farmer-tractor-left.png"};
        String[] rightTractor = {"farmer-tractor-right.png"};
        String[] upTractor = {"farmer-tractor-up.png"};
        String[] downTractor = {"farmer-tractor-down.png"};

        downTractorAnimation = loadAnimationFromFiles(downTractor, 0.1f, true);
        leftTractorAnimation = loadAnimationUnsetFromFiles(leftTractor, 0.1f, true);
        rightTractorAnimation = loadAnimationUnsetFromFiles(rightTractor, 0.1f, true);
        upTractorAnimation = loadAnimationUnsetFromFiles(upTractor, 0.1f, true);

        setDefaultAnimations();
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

    public void setDefaultAnimations() {
        Color defaultPantColor = new Color(.173f, .384f, .667f, 1);
        Color defaultShirtColor = new Color(	.749f, .129f, .129f, 1);
        Color defaultSkinColor = Color.WHITE;

        downAnimation = FarmerAnimationFactory.createAnimation(
                defaultPantColor, animationFactory.getTextures().front.pants.get(0),
                defaultSkinColor, animationFactory.getTextures().front.skinKeyframes,
                defaultShirtColor, animationFactory.getTextures().front.shirts.get(0),
                true
        );
        upAnimation = FarmerAnimationFactory.createAnimation(
                defaultPantColor, animationFactory.getTextures().back.pants.get(0),
                defaultSkinColor, animationFactory.getTextures().back.skinKeyframes,
                defaultShirtColor, animationFactory.getTextures().back.shirts.get(0),
                true
        );
        rightAnimation = FarmerAnimationFactory.createAnimation(
                defaultPantColor, animationFactory.getTextures().right.pants.get(0),
                defaultSkinColor, animationFactory.getTextures().right.skinKeyframes,
                defaultShirtColor, animationFactory.getTextures().right.shirts.get(0),
                false
        );
        leftAnimation = FarmerAnimationFactory.createAnimation(
                defaultPantColor, animationFactory.getTextures().left.pants.get(0),
                defaultSkinColor, animationFactory.getTextures().left.skinKeyframes,
                defaultShirtColor, animationFactory.getTextures().left.shirts.get(0),
                false
        );
        setDirectionalAnimations(leftAnimation, rightAnimation, upAnimation, downAnimation);
    }
}
