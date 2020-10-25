package com.kacstudios.game.utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.actors.BaseActor;

public class SelectableButton extends Group {
    Image selectedImage;
    Image unselectedImage;
    Actor contents;

    public SelectableButton(float x, float y, Texture selectedTexture, Texture unselectedTexture){
        super();
        selectedImage = new Image(selectedTexture);
        unselectedImage = new Image(unselectedTexture);

        init(x, y);
    }

    private void init(float x, float y){
        setX(x);
        setY(y);

        setWidth(selectedImage.getWidth());
        setHeight(selectedImage.getHeight());
        setBounds(getX(), getY(), getWidth(), getHeight());

        selectedImage.setPosition(0,0);
        unselectedImage.setPosition(0,0);
        selectedImage.setVisible(false);

        this.addActor(selectedImage);
        this.addActor(unselectedImage);

        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick(event, x, y);
            }
        });
    }

    /**
     * Override this method for it to be called on click
     */
    public void onClick(InputEvent event, float x, float y) {
        //pass
    }

    public void setSelected(boolean selected) {
        selectedImage.setVisible(selected);
        unselectedImage.setVisible(!selected);
    }
    public boolean getSelected(){
        return selectedImage.isVisible();
    }

    public void setContents(Actor actor){
        if(contents != null) contents.remove();
        contents = actor;
        if(actor != null) this.addActor(contents);
    }
}
