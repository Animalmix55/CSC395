package com.kacstudios.game.grid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.grid.GridSquare;
import java.time.LocalDateTime;

public class WaterSource extends GridSquare{

    public WaterSource() {
        setTexture("grid_blue.png");
    }

    @Override
    public void clickFunction(LocalDateTime time)
    {
        //pass
    }
}
