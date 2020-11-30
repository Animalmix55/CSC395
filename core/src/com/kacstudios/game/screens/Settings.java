package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.games.BaseGame;
import com.kacstudios.game.games.FarmaniaGame;
import com.kacstudios.game.sounds.GameSounds;

public class Settings extends BaseScreen {
    //private List<Slider> slider;

    private Skin skin;
    private Slider gameVolumeSlider;
    private Slider musicVolumeSlider;

    private Label gameVolumeLabel;
    private Label musicVolumeLabel;

    public void initialize() {
        skin = new Skin(Gdx.files.internal("misc/uiskin.json"));

        gameVolumeLabel = new Label("", skin);
        gameVolumeLabel.setColor(Color.WHITE);
        //Gamelabel.setScale(3f);
        gameVolumeLabel.setPosition(225, 200);

        gameVolumeSlider = new Slider(0,1,.01f,true, skin);
        //slider.setBounds(75,300,500,300);
        gameVolumeSlider.setValue(GameSounds.getSfxVolume());
        gameVolumeSlider.setPosition(275,250);
        gameVolumeSlider.addCaptureListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                setGameVolume(gameVolumeSlider.getValue());
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                setGameVolume(gameVolumeSlider.getValue());
                GameSounds.saveGlobalSettingsToFile();
            }
        });
        setGameVolume(GameSounds.getSfxVolume()); //update label

        musicVolumeLabel = new Label("", skin);
        musicVolumeLabel.setColor(Color.WHITE);
        musicVolumeLabel.setPosition(425, 200);

        musicVolumeSlider = new Slider(0,1,.01f,true, skin);
        //slider.setBounds(75,300,500,300);
        musicVolumeSlider.setValue(GameSounds.getMusicVolume());
        musicVolumeSlider.setPosition(475,250);
        musicVolumeSlider.addCaptureListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                setMusicVolume(musicVolumeSlider.getValue());
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                setMusicVolume(musicVolumeSlider.getValue());
                GameSounds.saveGlobalSettingsToFile();
            }
        });

        setMusicVolume(GameSounds.getMusicVolume()); // update slider

        uiStage.addActor(gameVolumeLabel);
        uiStage.addActor(gameVolumeSlider);
        uiStage.addActor(musicVolumeLabel);
        uiStage.addActor(musicVolumeSlider);

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
                GameSounds.setDefaults();
                updateSliders();
            }
        });

        TextButton ExitButton = new TextButton( "Exit", BaseGame.textButtonStyle );
        ExitButton.setPosition(1095,60);
        uiStage.addActor(ExitButton);

        ExitButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FarmaniaGame.setActiveScreen( new MainMenu() );
            }
        });

        updateSliders();
    }


    public void update(float dt) {
        //pass
    }

    private void setGameVolume(float volume) {
        gameVolumeLabel.setText(String.format("Sound Volume: %d%%", Math.round(volume * 100)));
        if(GameSounds.getSfxVolume() != volume) GameSounds.setSfxVolume(volume);
        if(gameVolumeSlider.getValue() != volume) gameVolumeSlider.setValue(volume);
    }

    private void setMusicVolume(float volume) {
        musicVolumeLabel.setText(String.format("Music Volume: %d%%", Math.round(volume * 100)));
        if(GameSounds.getMusicVolume() != volume) GameSounds.setMusicVolume(volume);
        if(musicVolumeSlider.getValue() != volume) musicVolumeSlider.setValue(volume);
    }

    private void updateSliders() {
        setMusicVolume(GameSounds.getMusicVolume());
        setGameVolume(GameSounds.getSfxVolume());
    }

}
