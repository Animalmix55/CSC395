package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Texture;
import com.kacstudios.game.utilities.SelectableButton;

public class HotItemButton extends SelectableButton {
    /**
     * Builds selectable button with default rectangular shape
     * @param x x coord
     * @param y y coord
     */
    public HotItemButton(float x, float y){
        super(x, y, new Texture("bottombar/inventorybutton_background_selected.png"),
                new Texture("bottombar/inventorybutton_background_unselected.png"));
    }
}
