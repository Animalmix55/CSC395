package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.games.BaseGame;
import com.kacstudios.game.games.FarmaniaGame;
import com.kacstudios.game.sounds.GameSounds;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

import java.awt.*;

public class Settings extends BaseScreen {
    //private List<Slider> slider;

    private static BitmapFont font = FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 35);
    private static Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

    private static Skin skin = new Skin(Gdx.files.internal("misc/uiskin.json"));
    private Slider gameVolumeSlider;
    private Slider musicVolumeSlider;

    private Label gameVolumeLabel;
    private Label musicVolumeLabel;

    public void initialize() {
        Group volumeSliders = new Group();

        gameVolumeLabel = new Label("Sound Volume: 100%", labelStyle);
        gameVolumeLabel.setColor(Color.WHITE);
        gameVolumeLabel.setAlignment(Align.center);
        gameVolumeLabel.pack();

        gameVolumeSlider = new Slider(0,1,.01f,false, skin);
        gameVolumeSlider.setValue(GameSounds.getSfxVolume());
        gameVolumeSlider.setWidth(300);

        gameVolumeLabel.setWidth(gameVolumeSlider.getWidth());
        gameVolumeLabel.setPosition(Math.abs(gameVolumeLabel.getWidth() - gameVolumeSlider.getWidth()) / 2,
                gameVolumeSlider.getTop() + 10);

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
        gameVolumeSlider.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setGameVolume(gameVolumeSlider.getValue());
                GameSounds.saveGlobalSettingsToFile();
            }
        });
        setGameVolume(GameSounds.getSfxVolume()); //update label

        musicVolumeLabel = new Label("Music Volume: 100%", labelStyle);
        musicVolumeLabel.pack();
        musicVolumeLabel.setAlignment(Align.center);
        musicVolumeLabel.setColor(Color.WHITE);

        musicVolumeSlider = new Slider(0,1,.01f,false, skin);
        musicVolumeSlider.setWidth(300);
        musicVolumeLabel.setWidth(musicVolumeSlider.getWidth());
        musicVolumeSlider.setValue(GameSounds.getMusicVolume());
        musicVolumeSlider.setPosition(0,gameVolumeLabel.getTop() + 60);
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
        musicVolumeSlider.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMusicVolume(musicVolumeSlider.getValue());
                GameSounds.saveGlobalSettingsToFile();
            }
        });

        musicVolumeLabel.setPosition(Math.abs(musicVolumeLabel.getWidth() - musicVolumeSlider.getWidth()) / 2,
                musicVolumeSlider.getTop() + 10);

        setMusicVolume(GameSounds.getMusicVolume()); // update slider
        volumeSliders.setWidth(gameVolumeSlider.getWidth());

        if(Gdx.graphics.supportsDisplayModeChange()) {
            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(BaseGame.textButtonStyle);
            style.font = font;

            TextButton fsButton = new TextButton(String.format("Toggle %s", Gdx.graphics.isFullscreen() ? "Windowed" : "Fullscreen"),
                    style);

            fsButton.pack();

            fsButton.addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (!Gdx.graphics.isFullscreen()) Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    else
                        Gdx.graphics.setWindowedMode((int)(Gdx.graphics.getDisplayMode().width/1.25),
                                (int)(Gdx.graphics.getDisplayMode().height/1.25));

                    FarmaniaGame.setActiveScreen(new Settings());
                }
            });

            fsButton.setPosition((volumeSliders.getWidth() - fsButton.getWidth())/2, musicVolumeLabel.getTop() + 60);
            volumeSliders.addActor(fsButton);

            volumeSliders.setHeight(fsButton.getTop());
        } else {
            volumeSliders.setHeight(musicVolumeLabel.getTop());
        }

        volumeSliders.setPosition((this.uiStage.getWidth() - volumeSliders.getWidth())/2,
                (this.uiStage.getHeight() - volumeSliders.getHeight())/2);

        volumeSliders.addActor(gameVolumeLabel);
        volumeSliders.addActor(gameVolumeSlider);
        volumeSliders.addActor(musicVolumeLabel);
        volumeSliders.addActor(musicVolumeSlider);

        this.uiStage.addActor(volumeSliders);


//        set background/map limits
        Image background = new Image(LoadMenu.bg);
        float scaleFactor = Math.max(mainStage.getWidth() / background.getWidth(), mainStage.getHeight() / background.getHeight());
        background.setScale(scaleFactor);
        mainStage.addActor(background);

        TextButton ExitButton = new TextButton( "Exit", BaseGame.textButtonStyle );
        ExitButton.setPosition(uiStage.getWidth() - ExitButton.getWidth() - 60,60);
        uiStage.addActor(ExitButton);
        ExitButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FarmaniaGame.setActiveScreen( new MainMenu() );
            }
        });

        TextButton RestoreButton = new TextButton( "Restore", BaseGame.textButtonStyle );
        RestoreButton.setPosition(ExitButton.getX() - RestoreButton.getWidth() - 60,60);
        uiStage.addActor(RestoreButton);
        RestoreButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSounds.setDefaults();
                updateSliders();
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
