package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.actors.Farmer;
import com.kacstudios.game.games.BaseGame;
import com.kacstudios.game.games.FarmaniaGame;
import com.kacstudios.game.gridItems.plants.CornPlant;
import com.kacstudios.game.gridItems.GridSquare;
import com.kacstudios.game.gridItems.plants.Plant;
import com.kacstudios.game.utilities.TimeEngine;


import java.util.ArrayList;
import java.util.List;

public class LevelScreen extends BaseScreen {
    private Farmer farmer;
    private List<Plant> gridSquares;
    private List<BaseActor> outOfBoundsArea;
    private Label timeLabel;
    private Skin skin;
    private Window window;

    public void initialize() {

//        set background/map limits
        BaseActor farmBaseActor = new BaseActor(0,0,mainStage);
        farmBaseActor.loadTexture("grass_1080x1080.png");
        farmBaseActor.setSize(1080,1080);
        BaseActor.setWorldBounds(farmBaseActor);
        TimeEngine.Init();
        // TimeEngine.dilateTime(0); // freeze time

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



        //pause button

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();

        Texture buttonTex = new Texture( Gdx.files.internal("button_pause_48x48.png") );
        TextureRegion buttonRegion =  new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        Button PauseButton = new Button( buttonStyle );
//        PauseButton.setColor( Color.CYAN );
        PauseButton.setPosition(20,650);
        uiStage.addActor(PauseButton);

        PauseButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    paused = !paused;
                    window.setVisible(true);

                    return false;
                }
        );

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        window = new Window("Paused", skin);
        window.setVisible(false);
        window.setMovable(false);
        final TextButton ResumeButton = new TextButton("Resume", skin);
        ResumeButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    togglePaused();
                    return true;
                }
        );
        window.add(ResumeButton);
        window.pack();
        float newWidth = 400, newHeight = 200;
        window.setBounds((Gdx.graphics.getWidth() - newWidth ) / 2,(Gdx.graphics.getHeight() - newHeight ) / 2, newWidth , newHeight ); //Center on screen.
        uiStage.addActor(window);



//        add in grid square

//        add in grid squares

        gridSquares = new ArrayList<>();
        gridSquares.add(new CornPlant(135,135,mainStage,false));
        gridSquares.add(new CornPlant(270,135,mainStage,false));
        gridSquares.add(new CornPlant(135,270,mainStage,false));
        gridSquares.add(new CornPlant(270,270,mainStage,false));


//        add in farmer actor
        farmer = new Farmer(20,20, mainStage);


//        loop through grid squares and activate click functions on each one
        for (GridSquare square : gridSquares) {
            square.addListener(
                    (Event e) ->
                    {
                        InputEvent ie = (InputEvent)e;
                        if ( ie.getType().equals(InputEvent.Type.touchDown) )
                            square.clickFunction(TimeEngine.getDateTime());
                        return false;
                    }
            );
        }

    }

    public void update(float dt) {
        TimeEngine.act(dt); // do NOT remove this, this makes time work.

        timeLabel.setText("Time: " + TimeEngine.getFormattedString());
        for (GridSquare square : gridSquares) {
            if (square.getCollisionSetting()) {
                farmer.preventOverlap(square);
            }
        }

        for(Plant plant : gridSquares) {
            if(plant.checkIfGrowing() == true)
            {
                plant.checkStatus();
            }
        }
    }

    public boolean keyDown(int keyCode)
    {

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            FarmaniaGame.setActiveScreen(new Pause(this));

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

    private void togglePaused() {
        paused = !paused;
        window.setVisible(paused);
    }

}
