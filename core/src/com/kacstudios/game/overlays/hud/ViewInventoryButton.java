package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ViewInventoryButton extends ItemButton {
    Image ellipsis;
    public ViewInventoryButton(float x, float y) {
        super(x, y);
        ellipsis = new Image(new Texture("bottombar/inventorybutton_icon_more.png"));
        this.addActor(ellipsis);
    }

    public ViewInventoryButton(){
        this(0 ,0);
    }
}
