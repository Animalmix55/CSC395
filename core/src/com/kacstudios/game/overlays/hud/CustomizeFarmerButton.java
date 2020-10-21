package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.kacstudios.game.utilities.SelectableButton;

public class CustomizeFarmerButton extends SelectableButton {
    public CustomizeFarmerButton(float x, float y) {
        super(x, y, new Texture("bottombar/button_edit_character_selected.png"),
                new Texture("bottombar/button_edit_character.png"));
    }

    public CustomizeFarmerButton() {
        this(0, 0);
    }

    @Override
    public void onClick(InputEvent event, float x, float y) {
        setSelected(!getSelected());
    }
}
