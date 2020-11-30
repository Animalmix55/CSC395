package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.games.BaseGame;
import com.kacstudios.game.games.FarmaniaGame;


public class MainMenu extends BaseScreen {
    public static Texture bg = new Texture("menu-textures/background_2.png");
    public static Texture logo = new Texture("menu-textures/logo-main.png");

    public void initialize() {

//        set background/map limits

        Image background = new Image(bg);
        float scaleFactor = Math.max(mainStage.getWidth() / background.getWidth(), mainStage.getHeight() / background.getHeight());
        background.setScale(scaleFactor);
        mainStage.addActor(background);

        Group logoButtons = new Group();

        TextButton NewButton = new TextButton( "New", BaseGame.textButtonStyle );
        logoButtons.addActor(NewButton);
        NewButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FarmaniaGame.setActiveScreen( new LevelScreen() );
            }
        });


        TextButton LoadButton = new TextButton( "Load", BaseGame.textButtonStyle );
        LoadButton.setPosition(NewButton.getRight() + 60,0);
        logoButtons.addActor(LoadButton);
        LoadButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FarmaniaGame.setActiveScreen( new LoadMenu() );
            }
        });

        TextButton SettingsButton = new TextButton( "Settings", BaseGame.textButtonStyle );
        SettingsButton.setPosition(LoadButton.getRight() + 60,0);
        logoButtons.addActor(SettingsButton);

        SettingsButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FarmaniaGame.setActiveScreen( new Settings() );
            }
        });

        TextButton ExitButton = new TextButton( "Exit", BaseGame.textButtonStyle );
        ExitButton.setPosition(SettingsButton.getRight() + 60,0);
        logoButtons.addActor(ExitButton);

        ExitButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                System.exit(0);
            }
        });

        logoButtons.setWidth(ExitButton.getRight());

        Image logoImage = new Image(logo);
        float logoScaleFactor = Math.min(mainStage.getWidth() / logoImage.getWidth(), mainStage.getHeight() / logoImage.getHeight()) / 1.5f;
        logoImage.setScale(logoScaleFactor);

        logoButtons.addActor(logoImage);
        logoImage.setPosition((logoButtons.getWidth() - logoImage.getWidth() * logoImage.getScaleX())/2,
                ExitButton.getTop() + 100);

        logoButtons.setHeight(logoImage.getY() + logoImage.getHeight() * logoImage.getScaleY());
        logoButtons.setPosition((uiStage.getWidth() - logoButtons.getWidth())/2,
                (uiStage.getHeight() - logoButtons.getHeight())/2);

        uiStage.addActor(logoButtons);
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
