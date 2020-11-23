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
    FarmerTextureData textureData = new FarmerTextureData();

    public class FarmerTextureData {
        public String headName;
        public Color headColor;
        public String shirtName;
        public Color shirtColor;
        public String pantsName;
        public Color pantsColor;
        public Color skinColor;
    }

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
        Color defaultHeadColor = Color.BLACK;
        DirectionalTextures textures = FarmerAnimationFactory.getTextures();

        setTextureData( null, defaultHeadColor, textures.back.shirts.get(0).name, defaultShirtColor,
                textures.back.pants.get(0).name, defaultPantColor, defaultSkinColor);
        FarmerAnimationFactory.updateFarmerTextures(this);
        setAnimationDirection(Direction.down);
    }

    public void setTextureData(FarmerTextureData data) {
        textureData = data;
    }
    private void setTextureData(String headName, Color headColor, String shirtName, Color shirtColor,
                               String pantsName, Color pantsColor, Color skinColor) {
        textureData.headColor = headColor;
        textureData.headName = headName;
        textureData.pantsColor = pantsColor;
        textureData.pantsName = pantsName;
        textureData.shirtName = shirtName;
        textureData.shirtColor = shirtColor;
        textureData.skinColor = skinColor;
    }

    public FarmerTextureData getTextureData() {
        return textureData;
    }

    public void updateTextures() {
        FarmerAnimationFactory.updateFarmerTextures(this);
    }
}
