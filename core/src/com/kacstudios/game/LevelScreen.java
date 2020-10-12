package com.kacstudios.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LevelScreen extends BaseScreen {
    private Farmer farmer;
    private List<Plant> gridSquares;
    private List<BaseActor> outOfBoundsArea;
    private TimeEngine time;
    private Label timeLabel;

    public void initialize() {

//        set background/map limits
        BaseActor farmBaseActor = new BaseActor(0,0,mainStage);
        farmBaseActor.loadTexture("grass_1080x1080.png");
        farmBaseActor.setSize(1080,1080);
        BaseActor.setWorldBounds(farmBaseActor);
        time = new TimeEngine();
        // time.dilateTime(2) // double time

        timeLabel = new Label("Time:", BaseGame.labelStyle);
        timeLabel.setX(0);
        timeLabel.setY(0);

        uiStage.addActor(timeLabel);

//        out of bounds background
//        counter clockwise, starting from the right middle
        outOfBoundsArea = new ArrayList<>();
        outOfBoundsArea.add( new BaseActor(1080,0,mainStage) );
        outOfBoundsArea.add( new BaseActor(1080,1080,mainStage) );
        outOfBoundsArea.add( new BaseActor(0,1080,mainStage) );
        outOfBoundsArea.add( new BaseActor(-1080,1080,mainStage) );
        outOfBoundsArea.add( new BaseActor(-1080,0,mainStage) );
        outOfBoundsArea.add( new BaseActor(-1080,-1080,mainStage) );
        outOfBoundsArea.add( new BaseActor(0,-1080,mainStage) );
        outOfBoundsArea.add( new BaseActor(1080,-1080,mainStage) );
        for (BaseActor baseActor : outOfBoundsArea) {
            baseActor.loadTexture("grass-outofbounds_1080x1080.png");
            baseActor.setSize(1080,1080);
        }



//        add in grid squares
        gridSquares = new ArrayList<>();
        gridSquares.add(new Plant(135,135,mainStage,false));
        gridSquares.add(new Plant(270,135,mainStage,false));
        gridSquares.add(new Plant(135,270,mainStage,false));
        gridSquares.add(new Plant(270,270,mainStage,false));


//        add in farmer actor
        farmer = new Farmer(20,20,mainStage);


//        loop through grid squares and activate click functions on each one
        for (GridSquare square : gridSquares) {
            square.addListener(
                    (Event e) ->
                    {
                        InputEvent ie = (InputEvent)e;
                        if ( ie.getType().equals(InputEvent.Type.touchDown) )
                            square.clickFunction(time.getDateTime());
                        return false;
                    }
            );
        }

    }

    public void update(float dt) {
        time.timeHook(dt); // do NOT remove this, this makes time work.

        timeLabel.setText("Time: " + time.getFormattedString());
        for (GridSquare square : gridSquares) {
            if (square.getCollisionSetting()) {
                farmer.preventOverlap(square);
            }
        }

        for(Plant plant : gridSquares) {
            plant.checkStatus(time.getDateTime());
        }
    }

    public boolean keyDown(int keyCode)
    {
//        if (Gdx.input.isKeyPressed(Input.Keys.C)) {
//            if (!gridSquares.get(0).getCollisionSetting()) {
//                gridSquares.get(0).setTexture("soil.png");
//                gridSquares.get(0).setCollisionSetting(true);
//            }
//            else {
//                gridSquares.get(0).setTexture("grid_blank.png");
//                gridSquares.get(0).setCollisionSetting(false);
//            }
//
//        }
        return false;
    }

}
