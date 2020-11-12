package com.kacstudios.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;

public class FarmerAnimationFactory {
    public class FarmerTexture {
        public ArrayList<TextureRegion> skinKeyframes = new ArrayList<>();
        public ArrayList<TextureRegion> shirts = new ArrayList<>();
        public ArrayList<TextureRegion> pants = new ArrayList<>();
    }

    public class DirectionalTextures {
        public FarmerTexture front = new FarmerTexture();
        public FarmerTexture back = new FarmerTexture();
        public FarmerTexture left = new FarmerTexture();
        public FarmerTexture right = new FarmerTexture();
    }

    public DirectionalTextures textures;

    public FarmerAnimationFactory() {
        textures = new DirectionalTextures();
        loadTextures();
    }

    public DirectionalTextures getTextures() {
        return textures;
    }

    public void loadTextures() {
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
                                              Color shirtColor, TextureRegion shirt) {

        ArrayList<TextureRegion> bodyTextures = expandAnimation(bodyKeyframes);
        TextureRegion[] animationKeyframes = new TextureRegion[bodyTextures.size()];

        for (int i = 0; i < bodyTextures.size(); i++) {
            TextureRegion bodyTexture = bodyTextures.get(i);
            FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA4444, 100, 100, false);

            Matrix4 m = new Matrix4();
            m.setToOrtho2D(0, 0, buffer.getWidth(), buffer.getHeight());

            SpriteBatch batch = new SpriteBatch();
            batch.setProjectionMatrix(m);

            buffer.begin();
            Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            batch.setColor(skinColor);
            batch.draw(bodyTexture, 0, 0);
            batch.setColor(pantColor);
            batch.draw(pants, 0, 0);
            batch.setColor(shirtColor);
            batch.draw(shirt, 0, 0);

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
