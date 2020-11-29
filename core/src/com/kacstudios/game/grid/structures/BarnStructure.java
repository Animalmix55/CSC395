package com.kacstudios.game.grid.structures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.grid.OversizeGridSquare;
import com.kacstudios.game.utilities.ShapeGenerator;

public class BarnStructure extends OversizeGridSquare {
    private static Texture barnTexture;

    public BarnStructure() {
        super(2, 2);
        if(barnTexture == null) barnTexture =
                new Texture(ShapeGenerator.createRectangle((int) getWidth(), (int) getHeight(), Color.WHITE));

        addActor(new Image(barnTexture));
    }
}
