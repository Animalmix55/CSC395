package com.kacstudios.game.overlays.pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.kacstudios.game.games.FarmaniaGame;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.screens.MainMenu;
import com.kacstudios.game.sounds.GameSounds;
import com.kacstudios.game.utilities.ShapeGenerator;
import com.kacstudios.game.utilities.TimeEngine;

import static com.kacstudios.game.screens.LoadMenu.saveLevel;

public class PauseMenu extends Group {
    private final LevelScreen screen;

    private final int pauseMenuWidth = 324;
    private final int pauseMenuHeight = 242;
    private final int saveMenuHeight = 358;

    private final int pauseMenuButtonWidth = 304;
    private final int pauseMenuButtonHeight = 48;

    private Image defaultBackground = new Image(new Texture(ShapeGenerator.createRoundedRectangle(pauseMenuWidth, pauseMenuHeight,
            16, new Color(0,0,0,.5f))));

    private Image saveBackground = new Image(new Texture(ShapeGenerator.createRoundedRectangle(pauseMenuWidth, saveMenuHeight, 16,
                        new Color(0,0,0,.5f))));

    PauseMenuButton[] pauseButtonsArray;
    Group pauseButtons = new Group();
    Group saveButtons = new Group();
    Group optionsButtons = new Group();
    Group exitButtons = new Group();

    private final PauseMenuButton options_gameVolumeSlider;
    private final PauseMenuButton options_musicVolumeSlider;

    private String currentPauseMenu;

    public PauseMenu(LevelScreen inputScreen) {
        screen = inputScreen;

        // CODE FOR ADDING DEFAULT MENU ASSETS (INITIAL PAUSE MENU)
        float pauseMenuX = (inputScreen.getUIStage().getWidth() - pauseMenuWidth)/2;
        int pauseMenuButtonX = (int)((float) (pauseMenuWidth - pauseMenuButtonWidth))/2;

        setX(pauseMenuX);
        setY((inputScreen.getUIStage().getHeight() - pauseMenuHeight)/2);

        setHeight(pauseMenuHeight);
        setWidth(pauseMenuWidth);
        addActor(defaultBackground);

        PauseMenuButton exitButton = new PauseMenuButton("Exit Game", pauseMenuButtonX, 10);
        exitButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_exit();
            }
        });
        pauseButtons.addActor(exitButton);
        addActor(pauseButtons);

        PauseMenuButton optionsButton = new PauseMenuButton("Options", pauseMenuButtonX, (int) exitButton.getTop() + 10);
        optionsButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_options();
            }
        });
        pauseButtons.addActor(optionsButton);

        PauseMenuButton saveButton = new PauseMenuButton("Save", pauseMenuButtonX, (int) optionsButton.getTop() + 10);
        saveButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_save();
            }
        });
        pauseButtons.addActor(saveButton);

        // 58 apart on Y coordinate
        PauseMenuButton resumeButton = new PauseMenuButton("Resume", pauseMenuButtonX, (int) saveButton.getTop() + 10);
        resumeButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_resume();
                TimeEngine.resume();
            }
        });
        pauseButtons.addActor(resumeButton);






        // CODE FOR ADDING SAVE MENU ASSETS
        pauseButtonsArray = new PauseMenuButton[5];
        saveBackground.setPosition(0, -(saveBackground.getHeight() - getHeight())/2);

        for (int i = 4; i >= 0; i--) {
            pauseButtonsArray[i] = new PauseMenuButton(String.format("Save #%d",i+1), pauseMenuButtonX,  (int) saveBackground.getY() + 10 + (4 - i) * (pauseMenuButtonHeight + 10));
            saveButtons.addActor(pauseButtonsArray[i]);
            int finalI = i;
            pauseButtonsArray[i].addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    saveLevel(screen, finalI +1);
                    saveMenu_setCurrentLevel(finalI +1);
                    setMenu_pause();
                }
            });
        }

        PauseMenuButton save_backButton = new PauseMenuButton("Back", pauseMenuButtonX, (int) pauseButtonsArray[0].getTop() + 10);
        save_backButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_pause();
            }
        });
        saveButtons.addActor(save_backButton);
        saveButton.setWidth(getWidth());
        saveButton.setHeight(save_backButton.getTop() + 10);

        addActor(saveBackground);
        addActor(saveButtons);







        // CODE FOR ADDING OPTIONS MENU ASSETS
        // no background is added here since the options menu will be the same size as the default menu size
        PauseMenuButton options_saveSettings = new PauseMenuButton("Return", pauseMenuButtonX, 10);
        options_saveSettings.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_pause();
            }
        });
        optionsButtons.addActor(options_saveSettings);

        PauseMenuButton options_resetToDefault = new PauseMenuButton("Reset to Default", pauseMenuButtonX,
                (int) options_saveSettings.getTop() + 10);
        options_resetToDefault.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSounds.setDefaults();
                updateSliders();
            }
        });
        optionsButtons.addActor(options_resetToDefault);


        options_gameVolumeSlider = new PauseMenuButton(
                "Sound Volume",
                new Slider(0,1,.01f,false, new Skin(Gdx.files.internal("misc/uiskin.json"))),
                pauseMenuButtonX,
                (int) options_resetToDefault.getTop() + 10
        );
        options_gameVolumeSlider.getPrivateButtonSlider().setValue(GameSounds.getSfxVolume());
        options_gameVolumeSlider.getPrivateButtonSlider().addCaptureListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setGameVolume(options_gameVolumeSlider.getPrivateButtonSlider().getValue());
                GameSounds.saveGlobalSettingsToFile();
            }
        });
        options_gameVolumeSlider.getPrivateButtonSlider().addCaptureListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                setGameVolume(options_gameVolumeSlider.getPrivateButtonSlider().getValue());
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                setGameVolume(options_gameVolumeSlider.getPrivateButtonSlider().getValue());
                GameSounds.saveGlobalSettingsToFile();
            }
        });

        optionsButtons.addActor(options_gameVolumeSlider);

        options_musicVolumeSlider = new PauseMenuButton(
                "Music Volume",
                new Slider(0,1,.01f,false, new Skin(Gdx.files.internal("misc/uiskin.json"))),
                pauseMenuButtonX,
                (int) options_gameVolumeSlider.getTop() + 10
        );
        options_musicVolumeSlider.getPrivateButtonSlider().setValue(GameSounds.getMusicVolume());
        options_musicVolumeSlider.getPrivateButtonSlider().addCaptureListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMusicVolume(options_musicVolumeSlider.getPrivateButtonSlider().getValue());
                GameSounds.saveGlobalSettingsToFile();
            }
        });
        options_musicVolumeSlider.getPrivateButtonSlider().addCaptureListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                setMusicVolume(options_musicVolumeSlider.getPrivateButtonSlider().getValue());
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                setMusicVolume(options_musicVolumeSlider.getPrivateButtonSlider().getValue());
                GameSounds.saveGlobalSettingsToFile();
            }
        });


        optionsButtons.addActor(options_musicVolumeSlider);

        addActor(optionsButtons);







        // CODE FOR ADDING EXIT MENU ASSETS
        // no background is added here since the exit menu will be the same size as the default menu size

        PauseMenuButton exit_cancelButton = new PauseMenuButton("No", pauseMenuButtonX, 10);
        exit_cancelButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_pause();
            }
        });
        exitButtons.addActor(exit_cancelButton);

        PauseMenuButton exit_confirmButton = new PauseMenuButton("Yes", pauseMenuButtonX, (int) exit_cancelButton.getTop() + 10);
        exit_confirmButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TimeEngine.resume();
                FarmaniaGame.setActiveScreen(new MainMenu());
            }
        });
        exitButtons.addActor(exit_confirmButton);

        PauseMenuButton confirmLine2 = new PauseMenuButton("Are you sure you want to continue?",pauseMenuButtonX, (int) exit_confirmButton.getTop() + 10,
                16, Color.WHITE, Color.CLEAR);
        exitButtons.addActor(confirmLine2);

        PauseMenuButton confirmLine1 = new PauseMenuButton("Unsaved changes will be lost",pauseMenuButtonX,
                (int) confirmLine2.getTop() + 10, 16, Color.WHITE, Color.CLEAR);
        exitButtons.addActor(confirmLine1);

        addActor(exitButtons);








        // set everything invisible here, so that when it's added into the levelscreen, the pause menu doesn't show up by default

        saveBackground.setVisible(false);
        saveButtons.setVisible(false);
        optionsButtons.setVisible(false);
        exitButtons.setVisible(false);

        setVisible(false);

        // add to screen UI
        screen.getUIStage().addActor(this);
    }

    /**
     * Sets pause menu as being invisible by setting the default menu invisible
     */
    public void setMenu_resume() {
        setVisible(false);
        setCurrentMenu(null);
    }

    /**
     * Sets the pause menu to the default, starting pause menu (resume, save, options, exit game)
     * Function works by setting all other existing actors to invisible, and then setting itself to being visible
     */
    public void setMenu_pause() {
        // set save menu invisible
        saveButtons.setVisible(false);
        saveBackground.setVisible(false);
        // set options menu invisible
        optionsButtons.setVisible(false);
        // set exit menu invisible
        exitButtons.setVisible(false);
        // set original menu visible
        defaultBackground.setVisible(true);
        pauseButtons.setVisible(true);
        setVisible(true);
        setCurrentMenu("pause");
    }

    /**
     * Sets the pause menu to showing the save menu (back, save 1, save 2, etc.)
     * Function works by setting default menu ( setMenu_pause() ) to invisible, and then setting itself to being visible
     */
    public void setMenu_save() {
        // set default menu invisible
        pauseButtons.setVisible(false);
        defaultBackground.setVisible(false);
        // set save menu visible
        saveButtons.setVisible(true);
        saveBackground.setVisible(true);
        setCurrentMenu("save");
    }


    /**
     * Sets the pause menu to showing the options menu (volume, reset to default)
     * Function works by setting default menu ( setMenu_pause() ) to invisible, and then setting itself to being visible
     */
    public void setMenu_options() {
        updateSliders();
        // set default menu invisible, but keep background visible since it is used in options menu
        defaultBackground.setVisible(true);
        pauseButtons.setVisible(false);
        // set options menu buttons visible
        optionsButtons.setVisible(true);
        setCurrentMenu("options");
    }



    /**
     * Sets the pause menu to showing the exit menu (Are you sure you want to exit?)
     * Function works by setting default menu ( setMenu_pause() ) to invisible, and then setting itself to being visible
     */
    public void setMenu_exit() {
        // set default menu invisible, but keep background visible since it is used in exit menu
        defaultBackground.setVisible(true);
        pauseButtons.setVisible(false);
        // set exit menu buttons visible
        exitButtons.setVisible(true);
        setCurrentMenu("exit");
    }

    public void setCurrentMenu(String menu) { currentPauseMenu = menu; }

    public String getCurrentMenu() { return currentPauseMenu; }

    public void saveMenu_setCurrentLevel(int currentLevel) {
        // reset all buttons to not show current level
        for (int i=0;i<5;i++) {
            pauseButtonsArray[i].setButtonLabelText( String.format("Save #%d",i+1) );
        }
        pauseButtonsArray[currentLevel-1].setButtonLabelText( String.format("Save #%d (current)",currentLevel) );
    }

    private void setGameVolume(float volume) {
        options_gameVolumeSlider.setButtonLabelText(String.format("Sound Volume: %d%%", Math.round(volume * 100)));
        if(GameSounds.getSfxVolume() != volume) GameSounds.setSfxVolume(volume);
        if(options_gameVolumeSlider.getPrivateButtonSlider().getValue() != volume)
            options_gameVolumeSlider.getPrivateButtonSlider().setValue(volume);
    }

    private void setMusicVolume(float volume) {
        options_musicVolumeSlider.setButtonLabelText(String.format("Music Volume: %d%%", Math.round(volume * 100)));
        if(GameSounds.getMusicVolume() != volume) GameSounds.setMusicVolume(volume);
        if(options_musicVolumeSlider.getPrivateButtonSlider().getValue() != volume)
            options_musicVolumeSlider.getPrivateButtonSlider().setValue(volume);
    }

    private void updateSliders() {
        setMusicVolume(GameSounds.getMusicVolume());
        setGameVolume(GameSounds.getSfxVolume());
    }
}
