package com.kacstudios.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.List;

public class LevelScreen extends BaseScreen {
    private Farmer farmer;
    private List<GridSquare> gridSquares;

    public void initialize() {

//        set background/map limits
        BaseActor farmBaseActor = new BaseActor(0,0,mainStage);
        farmBaseActor.loadTexture("grass_1080x1080.png");
        farmBaseActor.setSize(1080,1080);
        BaseActor.setWorldBounds(farmBaseActor);



//        add in grid square
        gridSquares = new ArrayList<>();
        gridSquares.add(new GridSquare(135,135,mainStage));
//        add in farmer actor
        farmer = new Farmer(20,20,mainStage);

    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.C)) {
            gridSquares.get(0).setTexture("grid_red.png");
        }
    }

}
