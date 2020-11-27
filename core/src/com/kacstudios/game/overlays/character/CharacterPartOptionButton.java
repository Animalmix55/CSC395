package com.kacstudios.game.overlays.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.utilities.ShapeGenerator;

public class CharacterPartOptionButton extends Group {
    Image selectedBg;
    Image unselectedBg;
    Image contents;

    public static final int sideLength = 52;

    Texture unselectedTexture = new Texture(ShapeGenerator.createRoundedRectangle(sideLength, sideLength, sideLength/2,
            new Color(1, 1, 1, .3f)));
    Texture selectedTexture = new Texture(ShapeGenerator.createRoundedRectangle(sideLength, sideLength, sideLength/2, Color.WHITE));


    // preset button
    public CharacterPartOptionButton(int x, int y, TextureRegion contentTexture) {
        setHeight(sideLength);
        setWidth(sideLength);
        setX(x);
        setY(y);

        Pixmap selectedBackground = ShapeGenerator.extractPixmapFromTextureRegion(contentTexture);

        int minX = selectedBackground.getWidth();
        int maxX = 0;
        int minY = selectedBackground.getHeight();
        int maxY = 0;

        for (int yPix = 0; yPix < selectedBackground.getHeight(); yPix++) {
            for (int xPix = 0; xPix < selectedBackground.getWidth(); xPix++) {
                Color color = new Color(selectedBackground.getPixel(xPix, yPix));

                if(color.a != 0){
                    if (yPix < minY) minY = yPix;
                    else if (yPix > maxY) maxY = yPix;

                    if (xPix < minX) minX = xPix;
                    else if (xPix > maxX) maxX = xPix;
                }
            }
        } // white out pixmap

        // some buffer
        maxY += 2;
        maxX += 2;
        minX -= 2;
        minY -= 2;

        int bgHeight = maxY - minY;
        int bgWidth = maxX - minX;

        Pixmap croppedBackground = new Pixmap(bgWidth, bgHeight, Pixmap.Format.RGBA8888);
        croppedBackground.drawPixmap(selectedBackground, -minX, -minY);

        selectedBg = new Image(selectedTexture);
        unselectedBg = new Image(unselectedTexture);
        addActor(selectedBg);
        addActor(unselectedBg);
        selectedBg.setVisible(false);

        contents = new Image(new Texture(croppedBackground));
        contents.setScale((sideLength - 7) / (contents.getHeight() > contents.getWidth()? contents.getHeight() : contents.getWidth()));
        contents.setPosition((getWidth() - contents.getWidth() * contents.getScaleX())/2,
                (getHeight() - contents.getHeight() * contents.getScaleY())/2);

        addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick();
            }
        });

        addActor(contents);
    }

    public void setSelected(boolean isSelected) {
        selectedBg.setVisible(isSelected);
        unselectedBg.setVisible(!isSelected);
    }

    public void onClick() {
        // pass
    }

    @Override
    public void setColor(Color color) {
        contents.setColor(color);
    }

    public boolean isSelected() {
        return selectedBg.isVisible();
    }
}
