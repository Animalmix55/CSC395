package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.kacstudios.game.utilities.SelectableButton;

public class MarketButton extends SelectableButton {
    private static Texture selectedTexture = new Texture("bottombar/button_market_selected.png");
    private static Texture unselectedTexture = new Texture("bottombar/button_market.png");
    private HUD hud;

    public MarketButton(float x, float y, HUD hud) {
        super(x, y, selectedTexture,
                unselectedTexture);
        this.hud = hud;
    }

    public MarketButton(HUD hud) {
        this(0, 0, hud);
    }

    @Override
    public void onClick(InputEvent event, float x, float y) {
        hud.getScreen().openMarket(!getSelected());
    }
}
