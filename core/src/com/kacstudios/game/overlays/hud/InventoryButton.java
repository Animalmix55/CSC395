package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class InventoryButton extends Actor {
    Texture selectedImage = new Texture("bottombar/inventorybutton_background_selected.png");
    Texture unselectedImage = new Texture("bottombar/inventorybutton_background_unselected.png");
    boolean isSelected = false;

    public InventoryButton(float x, float y){
        super();

        setX(x);
        setY(y);

        setWidth(selectedImage.getWidth());
        setHeight(selectedImage.getHeight());
        setBounds(getX(), getY(), getWidth(), getHeight());

        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick();
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(isSelected? selectedImage : unselectedImage, getX(), getY());

        super.draw(batch, parentAlpha);
    }

    public void onClick() {
        System.out.println("Clicked");
        //isSelected = !isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
