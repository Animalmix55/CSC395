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
import com.kacstudios.game.utilities.Global;
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
    private Window windowsetting;

    public void initialize() {

//        set background/map limits
        BaseActor farmBaseActor = new BaseActor(0,0,mainStage);
        farmBaseActor.loadTexture("grass_1080x1080.png");
        farmBaseActor.setSize(1080,1080);
        BaseActor.setWorldBounds(farmBaseActor);
        TimeEngine.Init();
        // TimeEngine.dilateTime(100); // dilate time

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
                    TimeEngine.pause();
                    window.setVisible(true);



                    //FarmaniaGame.setActiveScreen(new Pause(this));

                    return false;
                }
        );


        //pause menu overlay buttons


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

                    TimeEngine.resume();
                    togglePaused();
                    return true;
                }
        );

        final TextButton SaveButton = new TextButton("Save", skin);
        SaveButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    return ((InputEvent) e).getType().equals(InputEvent.Type.touchDown);
                }
        );

        final TextButton SettingsButton = new TextButton("Settings", skin);
        SettingsButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                        return false;
                    window.setVisible(false);
                    windowsetting.setVisible(true);

                    return true;
                }
        );

        final TextButton ExitButton = new TextButton("Exit to Main", skin);
        ExitButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    TimeEngine.resume();
                    togglePaused();
                    FarmaniaGame.setActiveScreen( new MainMenu() );
                    return true;
                }
        );


        //pause menu settings

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        windowsetting = new Window("Paused", skin);
        windowsetting.setVisible(false);
        windowsetting.setMovable(false);

            ////////////sliders
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        final Label Gamelabel = new Label("Game Volume: " + Global.GameVolume, skin);
        Gamelabel.setColor(Color.WHITE);
        //Gamelabel.setScale(1.5f);
        Gamelabel.setSize(1,1);
        Gamelabel.setPosition(10, 10);

        final Slider Gameslider = new Slider(0,100,1,true, skin);
        //slider.setBounds(75,300,500,300);
        Gameslider.setValue(Global.GameVolume);
        Gameslider.setPosition(15,30);
        Gameslider.addListener(
                (Event e) ->
                {
                    Gamelabel.setText("Game Volume: " + Math.round(Gameslider.getValue()));
                    Global.GameVolume = Math.round(Gameslider.getValue());
                    return true;
                }
        );


        final Label Musiclabel = new Label("Music Volume: " + Global.MusicVolume, skin);
        Musiclabel.setColor(Color.WHITE);
        Musiclabel.setSize(1,1);
        Musiclabel.setPosition(200, 10);

        final Slider Musicslider = new Slider(0,100,1,true, skin);
        //slider.setBounds(75,300,500,300);
        Musicslider.setValue(Global.MusicVolume);
        //Musicslider.setPosition(155,30);
        Musicslider.setPosition(100,30);
        Musicslider.addListener(
                (Event e) ->
                {
                    Musiclabel.setText("Music Volume: " + Math.round(Musicslider.getValue()));
                    Global.MusicVolume = Math.round(Musicslider.getValue());
                    return true;
                }
        );
        final TextButton SettingsReturnButton = new TextButton("Return", skin);
        //SettingsExitButton.setPosition(400,100);
        SettingsReturnButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                        return false;
                    windowsetting.setVisible(false);
                    window.setVisible(true);
                    return true;
                }
        );

        final TextButton ApplyButton = new TextButton("Apply", skin);
        //SettingsExitButton.setPosition(400,100);
        ApplyButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    return ((InputEvent) e).getType().equals(InputEvent.Type.touchDown);
                }
        );




        //adding buttons to windows in pause menu overlay
        window.add(ResumeButton).width(200).row();
        window.add(SaveButton).width(200).row();
        window.add(SettingsButton).width(200).row();
        window.add(ExitButton).width(200).row();
        window.pack();
        float newWidth = 400, newHeight = 200;
        //setting Window Bounds Center on screen.
        window.setBounds((Gdx.graphics.getWidth() - newWidth ) / 2,(Gdx.graphics.getHeight() - newHeight ) / 2, newWidth , newHeight );

        uiStage.addActor(window);

        //adding buttons and sliders to settings menu
        windowsetting.addActor(Gamelabel);
        windowsetting.addActor(Gameslider);
        windowsetting.addActor(Musiclabel);
        windowsetting.addActor(Musicslider);
        windowsetting.add(ApplyButton);
        windowsetting.add(SettingsReturnButton);
        windowsetting.pack();
        windowsetting.setBounds((Gdx.graphics.getWidth() - newWidth ) / 2,(Gdx.graphics.getHeight() - newHeight ) / 2, newWidth , newHeight );
        uiStage.addActor(windowsetting);


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
        //TimeEngine.act(dt); // do NOT remove this, this makes time work.

        timeLabel.setText("Time: " + TimeEngine.getFormattedString());
        for (GridSquare square : gridSquares) {
            if (square.getCollisionSetting()) {
                farmer.preventOverlap(square);
            }
        }

        for(Plant plant : gridSquares) {
            if(plant.checkIfGrowing())
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
        window.setVisible(false);
    }

}
