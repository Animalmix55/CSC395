package com.kacstudios.game.utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.actors.BaseActor;

public class SelectableButton extends BaseActor {
    Texture selectedImage;
    Texture unselectedImage;
    boolean isSelected = false;

    public SelectableButton(float x, float y, Texture selectedTexture, Texture unselectedTexture){
        super();
        selectedImage = selectedTexture;
        unselectedImage = unselectedTexture;
        init(x, y);
    }

    private void init(float x, float y){
        setX(x);
        setY(y);

        setWidth(selectedImage.getWidth());
        setHeight(selectedImage.getHeight());
        setBounds(getX(), getY(), getWidth(), getHeight());

        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick(event, x, y);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(isSelected? selectedImage : unselectedImage, getX(), getY());

        super.draw(batch, parentAlpha);
    }

    /**
     * Override this method for it to be called on click
     */
    public void onClick(InputEvent event, float x, float y) {
        //pass
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public boolean getSelected(){
        return isSelected;
    }
}
