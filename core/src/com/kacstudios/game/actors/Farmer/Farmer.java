package com.kacstudios.game.actors.Farmer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kacstudios.game.actors.PlayableActor;

public class Farmer extends PlayableActor {
    Animation<TextureRegion> leftAnimation;
    Animation<TextureRegion> rightAnimation;
    Animation<TextureRegion> upAnimation;
    Animation<TextureRegion> downAnimation;

    public Farmer(float x, float y, Stage s)
    {
        super(x,y,s, true);

        setDefaultAnimations();
        setAcceleration(1000);
        setMaxSpeed(200);
        setDeceleration(1000);
        setBoundaryPolygon(8);
    }

    public void setDefaultAnimations() {
        Color defaultPantColor = new Color(.173f, .384f, .667f, 1);
        Color defaultShirtColor = new Color(	.749f, .129f, .129f, 1);
//        Color defaultSkinColor = Color.WHITE;
        Color defaultSkinColor = new Color(	.553f, .333f, .141f, 1);


        downAnimation = FarmerAnimationFactory.createAnimation(
                defaultPantColor, FarmerAnimationFactory.getTextures().front.pants.get(0),
                defaultSkinColor, FarmerAnimationFactory.getTextures().front.skinKeyframes,
                defaultShirtColor, FarmerAnimationFactory.getTextures().front.shirts.get(0),
                true
        );
        upAnimation = FarmerAnimationFactory.createAnimation(
                defaultPantColor, FarmerAnimationFactory.getTextures().back.pants.get(0),
                defaultSkinColor, FarmerAnimationFactory.getTextures().back.skinKeyframes,
                defaultShirtColor, FarmerAnimationFactory.getTextures().back.shirts.get(0),
                true
        );
        rightAnimation = FarmerAnimationFactory.createAnimation(
                defaultPantColor, FarmerAnimationFactory.getTextures().right.pants.get(0),
                defaultSkinColor, FarmerAnimationFactory.getTextures().right.skinKeyframes,
                defaultShirtColor, FarmerAnimationFactory.getTextures().right.shirts.get(0),
                false
        );
        leftAnimation = FarmerAnimationFactory.createAnimation(
                defaultPantColor, FarmerAnimationFactory.getTextures().left.pants.get(0),
                defaultSkinColor, FarmerAnimationFactory.getTextures().left.skinKeyframes,
                defaultShirtColor, FarmerAnimationFactory.getTextures().left.shirts.get(0),
                false
        );
        setDirectionalAnimations(leftAnimation, rightAnimation, upAnimation, downAnimation);
        setAnimationDirection(Direction.down);
    }
}
