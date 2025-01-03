package com.kacstudios.game.actors.Farmer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kacstudios.game.actors.PlayableActor;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.sounds.GameSounds;

import java.util.logging.Level;

public class Farmer extends PlayableActor {
    long soundId;
    FarmerTextureData textureData = new FarmerTextureData();

    public static class FarmerTextureData {
        public FarmerTextureData() {}
        public String headName;
        public Color headColor;
        public String shirtName;
        public Color shirtColor;
        public String pantsName;
        public Color pantsColor;
        public Color skinColor;
    }

    public Farmer(float x, float y, LevelScreen screen)
    {
        super(x,y,screen, true);

        setDefaultAnimations();
        setAcceleration(1000);
        setMaxSpeed(200);
        setDeceleration(1000);
        setBoundaryPolygon(8);

        soundId = GameSounds.walkingSound.play(true);
        GameSounds.wateringSound.pause(soundId);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        if(isMoving() || (getAction() != null && !getAction().isComplete())) GameSounds.walkingSound.resume(soundId);
        else GameSounds.walkingSound.pause(soundId);
    }

    public void setDefaultAnimations() {
        Color defaultPantColor = new Color(.173f, .384f, .667f, 1);
        Color defaultShirtColor = new Color(	.749f, .129f, .129f, 1);
//        Color defaultSkinColor = Color.WHITE;
        Color defaultSkinColor = new Color(	1f, .811f, .666f, 1);
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

    public String[] getFarmerTextureSaveState() {
        return new String[]{
                textureData.headName,
                textureData.headColor.toString(),
                textureData.shirtName,
                textureData.shirtColor.toString(),
                textureData.pantsName,
                textureData.pantsColor.toString(),
                textureData.skinColor.toString()
        };
    }

    public void updateTextures() {
        FarmerAnimationFactory.updateFarmerTextures(this);
    }
}
