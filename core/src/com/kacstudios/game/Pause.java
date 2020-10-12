package com.kacstudios.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


public class Pause extends BaseScreen {

    //this allows to save current state of game when return back from pause
    private LevelScreen level;
    public Pause(LevelScreen _level){
        level = _level;
    }


    public void initialize() {

//        set background/map limits
        BaseActor farmBaseActor = new BaseActor(0,0,mainStage);
        farmBaseActor.loadTexture("MainMenu.jpg");
        farmBaseActor.setSize(1024,768);
        BaseActor.setWorldBounds(farmBaseActor);



        TextButton SaveButton = new TextButton( "Save", BaseGame.textButtonStyle );
        //SaveButton.setLayoutEnabled(true);
        SaveButton.setPosition(200,300);
        uiStage.addActor(SaveButton);

        SaveButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    return true;
                }
        );

        TextButton SettingsButton = new TextButton( "Settings", BaseGame.textButtonStyle );
        SettingsButton.setPosition(400,300);
        uiStage.addActor(SettingsButton);

        SettingsButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                        return false;
                    FarmaniaGame.setActiveScreen( new PauseSettings(this) );
                    return true;
                }
        );

        TextButton ExitButton = new TextButton( "Exit", BaseGame.textButtonStyle );
        ExitButton.setPosition(690,300);
        uiStage.addActor(ExitButton);

        ExitButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    FarmaniaGame.setActiveScreen( new MainMenu() );
                    return true;
                }
        );

    }


    public void update(float dt) {

    }







    public boolean keyDown(int keyCode)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            FarmaniaGame.setActiveScreen( level );
        return false;
    }


}
