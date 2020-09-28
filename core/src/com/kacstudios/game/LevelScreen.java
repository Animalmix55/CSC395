package com.kacstudios.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

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
        gridSquares.add(new GridSquare(135,135,mainStage,false));

//        add in farmer actor
        farmer = new Farmer(20,20,mainStage);

//        BROKEN
//        loop through grid squares and activate click functions on each one
//
//        for (GridSquare square : gridSquares) {
//            square.addListener(
//                    (Event e) ->
//                    {
//                        InputEvent ie = (InputEvent)e;
//                        if ( ie.getType().equals(InputEvent.Type.touchDown) )
//                            square.clickFunction();
//                        return false;
//                    }
//            );
//        }

    }








    public void update(float dt) {
        for (GridSquare square : gridSquares) {
            if (square.getCollisionSetting()) {
                farmer.preventOverlap(square);
            }
        }
    }







    public boolean keyDown(int keyCode)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.C)) {
            if (!gridSquares.get(0).getCollisionSetting()) {
                gridSquares.get(0).setTexture("grid_red.png");
                gridSquares.get(0).setCollisionSetting(true);
            }
            else {
                gridSquares.get(0).setTexture("grid_blank.png");
                gridSquares.get(0).setCollisionSetting(false);
            }

        }
        return false;
    }

}
