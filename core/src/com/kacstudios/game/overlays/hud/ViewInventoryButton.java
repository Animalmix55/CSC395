package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ViewInventoryButton extends ItemButton {
    Image ellipsis;
    Image garbageCan;
    public ViewInventoryButton(float x, float y) {
        super(x, y);
        garbageCan = new Image(new Texture("bottombar/inventorybutton_icon_trash.png"));
        garbageCan.setVisible(false); // hidden
        ellipsis = new Image(new Texture("bottombar/inventorybutton_icon_more.png"));
        this.addActor(ellipsis);
        this.addActor(garbageCan);
    }

    public void setDeleteMode(boolean isDelete) {
        ellipsis.setVisible(!isDelete);
        garbageCan.setVisible(isDelete);
    }

    public ViewInventoryButton(){
        this(0 ,0);
    }
}
