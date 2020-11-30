package com.kacstudios.game.actors.Farmer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;

public class FarmerAnimationFactory {
    private static DirectionalTextures textures = null;
    public enum Direction {
        up,
        left,
        right,
        down
    }
    public enum CustomizationPart {
        head,
        shirt,
        pants
    }

    /**
     * Returns the farmer textures loaded
     * @return
     */
    public static DirectionalTextures getTextures() {
        if(textures == null) loadTextures();
        return textures;
    }

    private static void loadTextures() {
        textures = new DirectionalTextures();
        TextureAtlas atlas = new TextureAtlas("farmer/Farmer.atlas");
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();

        for (int i = 0; i < regions.size; i++){
            TextureAtlas.AtlasRegion region = regions.get(i);

            ArrayList<FarmerTexture> targetTextures = new ArrayList<>();
            if (region.name.contains("front")) {
                targetTextures.add(textures.front);
            }
            if (region.name.contains("back")) {
                targetTextures.add(textures.back);
            }
            if (region.name.contains("left")) {
                targetTextures.add(textures.left);
            }
            if (region.name.contains("right")) {
                targetTextures.add(textures.right);
            }

            //remove the trailing directional explanations
            String[] regionNameSlices = region.name.split("-");
            region.name = String.join("-", Arrays.copyOfRange(regionNameSlices, 0, regionNameSlices.length - 1));

            if (region.name.contains("pants")) {
                targetTextures.forEach(t -> {
                    t.pants.add(region);
                });
            }
            if (region.name.contains("shirt")) {
                targetTextures.forEach(t -> {
                    t.shirts.add(region);
                });
            }
            if (region.name.contains("skin")) {
                targetTextures.forEach(t -> {
                    t.skinKeyframes.add(region);
                });
            }

            if (region.name.contains("head")) {
                targetTextures.forEach(t -> {
                    t.heads.add(region);
                });
            }
        }
    }

    private static Animation<TextureRegion> createAnimation(Color pantColor, TextureAtlas.AtlasRegion pants,
                                              Color skinColor, ArrayList<TextureAtlas.AtlasRegion> bodyKeyframes,
                                              Color shirtColor, TextureRegion shirt,
                                              Color headColor, TextureRegion head, boolean waddle) {
        ArrayList<TextureAtlas.AtlasRegion> bodyTextures = expandAnimation(bodyKeyframes);
        TextureRegion[] animationKeyframes = new TextureRegion[bodyTextures.size()];

        for (int i = 0; i < bodyTextures.size(); i++) {
            TextureRegion bodyTexture = bodyTextures.get(i);
            bodyTexture.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA4444, 100, 100, false);

            Matrix4 m = new Matrix4();
            m.setToOrtho2D(0, 0, buffer.getWidth(), buffer.getHeight());
            SpriteBatch batch = new SpriteBatch();
            batch.setProjectionMatrix(m);
            float rotationAngle = waddle? getRotation(bodyTextures.size(), i) : 0;

            buffer.begin();
            Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            Sprite pantsSprite = new Sprite(pants);
            pantsSprite.setColor(pantColor);
            pantsSprite.setOrigin(50, 0);
            pantsSprite.setRotation(rotationAngle);
            pantsSprite.draw(batch);

            Sprite shirtSprite = new Sprite(shirt);
            shirtSprite.setColor(shirtColor);
            shirtSprite.setOrigin(50, 0);
            shirtSprite.setRotation(rotationAngle);
            shirtSprite.draw(batch);

            Sprite skinSprite = new Sprite(bodyTexture);
            skinSprite.setColor(skinColor);
            skinSprite.setOrigin(50, 0);
            skinSprite.setRotation(rotationAngle);
            skinSprite.draw(batch);

            if(head != null) {
                Sprite headSprite = new Sprite(head);
                headSprite.setColor(headColor);
                headSprite.setOrigin(50, 0);
                headSprite.setRotation(rotationAngle);
                headSprite.draw(batch);
            }

            batch.end();
            buffer.end();

            TextureRegion returnTexture = new TextureRegion(buffer.getColorBufferTexture());
            returnTexture.flip(false, true);


            animationKeyframes[i] = returnTexture;
        }

        Animation<TextureRegion> animation = new Animation<>(0.1f, animationKeyframes);

        animation.setPlayMode(Animation.PlayMode.LOOP);
        return animation;
    }

    /**
     *
     * @param totalFrames the number of frames
     * @param frameNumber in base 0
     * @return
     */
    private static float getRotation(int totalFrames, int frameNumber) {
        float amplitude = 2;
        double rotation = amplitude * Math.sin(((float)2*frameNumber)/(totalFrames - 1) * Math.PI);
        return (float) rotation;
    }

    /**
     * Formats a 5-frame body animation into an 8-frame animation by duplicating necessary frames.
     * Given frames 1-5, they will be exported as 1, 2, 3, 2, 1, 4, 5, 4.
     *
     * If the animation is not 5 frames long, returns the same array.
     * @param bodyAnimation
     * @return
     */
    private static ArrayList<TextureAtlas.AtlasRegion> expandAnimation(ArrayList<TextureAtlas.AtlasRegion> bodyAnimation){
        if(bodyAnimation.size() != 5) return bodyAnimation;

        TextureAtlas.AtlasRegion[] regions = new TextureAtlas.AtlasRegion[8];
        regions[0] = bodyAnimation.get(0);
        regions[1] = bodyAnimation.get(1);
        regions[2] = bodyAnimation.get(2);
        regions[3] = bodyAnimation.get(1);
        regions[4] = bodyAnimation.get(0);
        regions[5] = bodyAnimation.get(3);
        regions[6] = bodyAnimation.get(4);
        regions[7] = bodyAnimation.get(3);
        return new ArrayList<>(Arrays.asList(regions));
    }

    public static TextureRegion tintTextureRegion(TextureRegion texture, Color color) {
        FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA4444, 100, 100, false);

        Matrix4 m = new Matrix4();
        m.setToOrtho2D(0, 0, buffer.getWidth(), buffer.getHeight());
        
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(m);

        buffer.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.setColor(color);
        batch.draw(texture, 0, 0);
        batch.setColor(Color.WHITE);

        batch.end();
        buffer.end();

        TextureRegion returnTexture = new TextureRegion(buffer.getColorBufferTexture());
        returnTexture.flip(false, true);

        return returnTexture;
    }

    /**
     * Retrieves the first grayscale texture for a given part of the farmer with a region name containing the filter.
     * @param direction
     * @param part
     * @return texture
     */
    private static TextureAtlas.AtlasRegion retrievePart(Direction direction, CustomizationPart part, String filter) {
        ArrayList<TextureAtlas.AtlasRegion> partTextures = retrievePartTextures(part, direction);
        TextureAtlas.AtlasRegion defText;

        if(part == CustomizationPart.head) defText = null; // no defaulting for headwear
        else defText = partTextures.get(0);

        for (TextureAtlas.AtlasRegion texture: partTextures) {
            if(texture.name.equals(filter)) return texture;
            if(texture.name.contains("plain")) defText = texture;
        }
        return defText; // if not found, default
    }

    public static ArrayList<TextureAtlas.AtlasRegion> retrievePartTextures(CustomizationPart part, Direction direction) {
        FarmerTexture folder;
        DirectionalTextures textures = getTextures();

        switch (direction) {
            case left:
                folder = textures.left;
                break;
            case right:
                folder = textures.right;
                break;
            default:
            case down:
                folder = textures.front;
                break;
            case up:
                folder = textures.back;
                break;
        }
        ArrayList<TextureAtlas.AtlasRegion> partTextures;
        switch (part) {
            case head:
                partTextures = folder.heads;
                break;
            case pants:
                partTextures = folder.pants;
                break;
            default:
            case shirt:
                partTextures = folder.shirts;
                break;
        }

        return partTextures;
    }

    public static TextureRegion generatePreviewFrame(Farmer.FarmerTextureData textureData) {
        return generatePreviewAnimation(Direction.down, textureData).getKeyFrame(0);
    }

    public static Animation<TextureRegion> generatePreviewAnimation(Direction direction, Farmer.FarmerTextureData textureData) {
        ArrayList<TextureAtlas.AtlasRegion> skinFrames;
        switch (direction) {
            case right:
                skinFrames = FarmerAnimationFactory.getTextures().right.skinKeyframes;
                break;
            case left:
                skinFrames = FarmerAnimationFactory.getTextures().left.skinKeyframes;
                break;
            case up:
                skinFrames = FarmerAnimationFactory.getTextures().back.skinKeyframes;
                break;
            default:
                skinFrames = FarmerAnimationFactory.getTextures().front.skinKeyframes;
        }

        return FarmerAnimationFactory.createAnimation(
                textureData.pantsColor, retrievePart(direction, CustomizationPart.pants, textureData.pantsName),
                textureData.skinColor, skinFrames,
                textureData.shirtColor, retrievePart(direction, CustomizationPart.shirt, textureData.shirtName),
                textureData.headColor, retrievePart(direction, CustomizationPart.head, textureData.headName),
                false
        );
    }

    public static void updateFarmerTextures(Farmer farmer) {

        Farmer.FarmerTextureData textureData = farmer.getTextureData();

        Animation<TextureRegion> downAnimation = FarmerAnimationFactory.createAnimation(
                textureData.pantsColor, retrievePart(Direction.down, CustomizationPart.pants, textureData.pantsName),
                textureData.skinColor, FarmerAnimationFactory.getTextures().front.skinKeyframes,
                textureData.shirtColor, retrievePart(Direction.down, CustomizationPart.shirt, textureData.shirtName),
                textureData.headColor, retrievePart(Direction.down, CustomizationPart.head, textureData.headName),
                true
        );
        Animation<TextureRegion> upAnimation = FarmerAnimationFactory.createAnimation(
                textureData.pantsColor, retrievePart(Direction.up, CustomizationPart.pants, textureData.pantsName),
                textureData.skinColor, FarmerAnimationFactory.getTextures().back.skinKeyframes,
                textureData.shirtColor, retrievePart(Direction.up, CustomizationPart.shirt, textureData.shirtName),
                textureData.headColor, retrievePart(Direction.up, CustomizationPart.head, textureData.headName),
                true
        );
        Animation<TextureRegion> rightAnimation = FarmerAnimationFactory.createAnimation(
                textureData.pantsColor, retrievePart(Direction.right, CustomizationPart.pants, textureData.pantsName),
                textureData.skinColor, FarmerAnimationFactory.getTextures().right.skinKeyframes,
                textureData.shirtColor, retrievePart(Direction.right, CustomizationPart.shirt, textureData.shirtName),
                textureData.headColor, retrievePart(Direction.right, CustomizationPart.head, textureData.headName),
                false
        );
        Animation<TextureRegion> leftAnimation = FarmerAnimationFactory.createAnimation(
                textureData.pantsColor, retrievePart(Direction.left, CustomizationPart.pants, textureData.pantsName),
                textureData.skinColor, FarmerAnimationFactory.getTextures().left.skinKeyframes,
                textureData.shirtColor, retrievePart(Direction.left, CustomizationPart.shirt, textureData.shirtName),
                textureData.headColor, retrievePart(Direction.left, CustomizationPart.head, textureData.headName),
                false
        );

        farmer.setDirectionalAnimations(leftAnimation, rightAnimation, upAnimation, downAnimation);
    }
}
