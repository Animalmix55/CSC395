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

    /**
     * Returns the farmer textures loaded
     * @return
     */
    public static DirectionalTextures getTextures() {
        if(textures == null) loadTextures();
        return textures;
    }

    public static void loadTextures() {
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
        }
    }

    public static Animation<TextureRegion> createAnimation(Color pantColor, TextureRegion pants,
                                              Color skinColor, ArrayList<TextureRegion> bodyKeyframes,
                                              Color shirtColor, TextureRegion shirt, boolean waddle) {
        ArrayList<TextureRegion> bodyTextures = expandAnimation(bodyKeyframes);
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

    private static Pixmap extractPixmapFromTextureRegion(TextureRegion textureRegion) {
        TextureData textureData = textureRegion.getTexture().getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }
        Pixmap pixmap = new Pixmap(
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight(),
                textureData.getFormat()
        );
        pixmap.drawPixmap(
                textureData.consumePixmap(), // The other Pixmap
                0, // The target x-coordinate (top left corner)
                0, // The target y-coordinate (top left corner)
                textureRegion.getRegionX(), // The source x-coordinate (top left corner)
                textureRegion.getRegionY(), // The source y-coordinate (top left corner)
                textureRegion.getRegionWidth(), // The width of the area from the other Pixmap in pixels
                textureRegion.getRegionHeight() // The height of the area from the other Pixmap in pixels
        );
        return pixmap;
    }

    /**
     * Formats a 5-frame body animation into an 8-frame animation by duplicating necessary frames.
     * Given frames 1-5, they will be exported as 1, 2, 3, 2, 1, 4, 5, 4.
     *
     * If the animation is not 5 frames long, returns the same array.
     * @param bodyAnimation
     * @return
     */
    private static ArrayList<TextureRegion> expandAnimation(ArrayList<TextureRegion> bodyAnimation){
        if(bodyAnimation.size() != 5) return bodyAnimation;

        TextureRegion[] regions = new TextureRegion[8];
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
}
