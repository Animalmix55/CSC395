package com.kacstudios.game.grid.structures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.grid.OversizeGridSquare;

public class BarnStructure extends OversizeGridSquare {
    private static Texture barnTexture = new Texture("buildings/farm.png");

    public BarnStructure() {
        super(3, 3);
        setBoundaryPolyCustom(new float[] {
            33, 33,
            33, 242,
            112, 349,
            202, 371,
            287, 349,
            368, 242,
            368, 33
        });
        setCollideWithPlayer(true);

        addActor(new Image(barnTexture));
    }

    public static Texture getTexture() {
        return barnTexture;
    }
}
