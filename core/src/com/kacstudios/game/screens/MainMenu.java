package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.games.BaseGame;
import com.kacstudios.game.games.FarmaniaGame;


public class MainMenu extends BaseScreen {

    public void initialize() {

//        set background/map limits
        BaseActor farmBaseActor = new BaseActor(0,0,mainStage);
//        farmBaseActor.loadTexture("MainMenu.jpg");
        farmBaseActor.loadTexture("menu-textures/background_2.png");
        farmBaseActor.setSize(1280,720);
        BaseActor.setWorldBounds(farmBaseActor);

        BaseActor logoBaseActor = new BaseActor(190,330,mainStage);
        logoBaseActor.loadTexture("menu-textures/logo-main.png");
        uiStage.addActor(logoBaseActor);



        TextButton NewButton = new TextButton( "New", BaseGame.textButtonStyle );
        NewButton.setPosition(280,200);
        uiStage.addActor(NewButton);


        NewButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    FarmaniaGame.setActiveScreen( new LevelScreen() );
                    return true;
                }
        );


        TextButton LoadButton = new TextButton( "Load", BaseGame.textButtonStyle );
        LoadButton.setPosition(440,200);
        uiStage.addActor(LoadButton);

        LoadButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    FarmaniaGame.setActiveScreen( new LoadMenu() );
                    return true;
                }
        );

        TextButton SettingsButton = new TextButton( "Settings", BaseGame.textButtonStyle );
        SettingsButton.setPosition(610,200);
        uiStage.addActor(SettingsButton);

        SettingsButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                        return false;
                    //new test();
                    FarmaniaGame.setActiveScreen( new Settings() );
                    return true;
                }
        );

        TextButton ExitButton = new TextButton( "Exit", BaseGame.textButtonStyle );
        ExitButton.setPosition(870,200);
        uiStage.addActor(ExitButton);

        ExitButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    System.exit(0);
                    return true;
                }
        );

    }








    public void update(float dt) {

    }







    public boolean keyDown(int keyCode)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();
        return false;
    }


}
