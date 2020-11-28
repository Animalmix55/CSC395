package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.games.BaseGame;
import com.kacstudios.game.games.FarmaniaGame;
import com.kacstudios.game.grid.plants.BlueberriesPlant;
import com.kacstudios.game.inventoryItems.BasicTractorItem;
import com.kacstudios.game.inventoryItems.BlueberriesPlantItem;
import com.kacstudios.game.inventoryItems.CornPlantItem;
import com.kacstudios.game.utilities.Setting;

import java.util.Set;


//public class Settings extends ApplicationAdapter{
public class Settings extends BaseScreen {
    //private List<Slider> slider;
    private Skin skin;
    private Stage stage;
    private Slider Gameslider;
    private Slider Musicslider;

    public void initialize() {
        skin = new Skin(Gdx.files.internal("misc/uiskin.json"));

        final Label Gamelabel = new Label("Game Volume: " + Setting.GameVolume, skin);
        Gamelabel.setColor(Color.WHITE);
        //Gamelabel.setScale(3f);
        Gamelabel.setPosition(225, 200);

        final Slider Gameslider = new Slider(0,100,1,true, skin);
        //slider.setBounds(75,300,500,300);
        Gameslider.setValue(Setting.GameVolume);
        Gameslider.setPosition(275,250);
        Gameslider.addCaptureListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                Gamelabel.setText("Game Volume: " + Math.round(Gameslider.getValue()));
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                Setting.GameVolume = Math.round(Gameslider.getValue());
                Setting.saveGlobalSettingsToFile();
            }
        });


        final Label Musiclabel = new Label("Music Volume: " + Setting.MusicVolume, skin);
        Musiclabel.setColor(Color.WHITE);
        Musiclabel.setPosition(425, 200);

        final Slider Musicslider = new Slider(0,100,1,true, skin);
        //slider.setBounds(75,300,500,300);
        Musicslider.setValue(Setting.MusicVolume);
        Musicslider.setPosition(475,250);
        Musicslider.addCaptureListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                Musiclabel.setText("Music Volume: " + Math.round(Musicslider.getValue()));
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                Setting.MusicVolume = Math.round(Musicslider.getValue());
                Setting.saveGlobalSettingsToFile();
            }
        });


        uiStage.addActor(Gamelabel);
        uiStage.addActor(Gameslider);
        uiStage.addActor(Musiclabel);
        uiStage.addActor(Musicslider);
        //Gdx.input.setInputProcessor(stage);





//        set background/map limits
        BaseActor farmBaseActor = new BaseActor(0,0,mainStage);
//        farmBaseActor.loadTexture("MainMenu.jpg");
        farmBaseActor.loadTexture("menu-textures/background_2.png");
        farmBaseActor.setSize(1280,720);
        BaseActor.setWorldBounds(farmBaseActor);

        TextButton RestoreButton = new TextButton( "Restore", BaseGame.textButtonStyle );
        RestoreButton.setPosition(880,60);
        uiStage.addActor(RestoreButton);
        RestoreButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Setting.MusicVolume = 50;
                Setting.GameVolume = 50;
                Musicslider.setValue(Setting.MusicVolume);
                Musiclabel.setText("Music Volume: " + Setting.MusicVolume);
                Gameslider.setValue(Setting.GameVolume);
                Gamelabel.setText("Game Volume: " + Setting.GameVolume);
                Setting.saveGlobalSettingsToFile();
            }
        });

        TextButton ExitButton = new TextButton( "Exit", BaseGame.textButtonStyle );
        ExitButton.setPosition(1095,60);
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

        return false;
    }


}
