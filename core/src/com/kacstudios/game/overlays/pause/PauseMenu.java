package com.kacstudios.game.overlays.pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.games.FarmaniaGame;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.screens.MainMenu;
import com.kacstudios.game.utilities.Setting;
import com.kacstudios.game.utilities.ShapeGenerator;
import com.kacstudios.game.utilities.TimeEngine;

import static com.kacstudios.game.screens.LoadMenu.saveLevel;

public class PauseMenu extends Group {
    private LevelScreen screen;
    private Image defaultBackground;
    private Image saveBackground;

    private final int pauseMenuWidth = 324;
    private final int pauseMenuX = 640 - (pauseMenuWidth/2);
    private final int pauseMenuHeight = 242;
    private final int saveMenuHeight = 358;

    private final int pauseMenuButtonWidth = 304;
    private final int pauseMenuButtonX = 640 - (pauseMenuButtonWidth/2);
    private final int pauseMenuButtonHeight = 48;

    PauseMenuButton[] pauseButtonsArray;
    Group pauseButtons = new Group();
    Group saveButtons = new Group();
    Group optionsButtons = new Group();
    Group exitButtons = new Group();

    private String currentPauseMenu;

    public PauseMenu(LevelScreen inputScreen) {
        screen = inputScreen;

        // CODE FOR ADDING DEFAULT MENU ASSETS (INITIAL PAUSE MENU)
        defaultBackground = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(
                        pauseMenuWidth,
                        pauseMenuHeight,
                        16,
                        new Color(0,0,0,.5f)
                )
                )
        );
        defaultBackground.setX(pauseMenuX);
        defaultBackground.setY(360 - (pauseMenuHeight/2)); // center
        addActor(defaultBackground);

        // 58 apart on Y coordinate
        PauseMenuButton resumeButton = new PauseMenuButton("Resume", pauseMenuButtonX, (447 - (pauseMenuButtonHeight/2)));
        resumeButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_resume();
                TimeEngine.resume();
                inputScreen.setPaused(false);
            }
        });
        pauseButtons.addActor(resumeButton);
        PauseMenuButton saveButton = new PauseMenuButton("Save", pauseMenuButtonX, (389 - (pauseMenuButtonHeight/2)));
        saveButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_save();
            }
        });
        pauseButtons.addActor(saveButton);
        PauseMenuButton optionsButton = new PauseMenuButton("Options", pauseMenuButtonX, (331 - (pauseMenuButtonHeight/2)));
        optionsButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_options();
            }
        });
        pauseButtons.addActor(optionsButton);
        PauseMenuButton exitButton = new PauseMenuButton("Exit Game", pauseMenuButtonX, (273 - (pauseMenuButtonHeight/2)));
        exitButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_exit();
            }
        });
        pauseButtons.addActor(exitButton);
        addActor(pauseButtons);







        // CODE FOR ADDING SAVE MENU ASSETS
        saveBackground = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(
                        pauseMenuWidth,
                        saveMenuHeight,
                        16,
                        new Color(0,0,0,.5f)
                )
                )
        );
        saveBackground.setX(pauseMenuX);
        saveBackground.setY(360 - (saveMenuHeight/2)); // center
        addActor(saveBackground);

        PauseMenuButton save_backButton = new PauseMenuButton("Back", pauseMenuButtonX, (505 - (pauseMenuButtonHeight/2)));
        save_backButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_pause();
            }
        });
        saveButtons.addActor(save_backButton);
        pauseButtonsArray = new PauseMenuButton[5];
        for (int i=0;i<5;i++) {
            pauseButtonsArray[i] = new PauseMenuButton(String.format("Save #%d",i+1), pauseMenuButtonX, (447-(58*i) - (pauseMenuButtonHeight/2)));
        }
        for (int i=0;i<5;i++) {
            saveButtons.addActor(
                    pauseButtonsArray[i]
            );
        }
        for (int i=0;i<5;i++) {
            int finalI = i;
            saveButtons.getChildren().get(i+1).addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    saveLevel(screen,finalI+1);
                    saveMenu_setCurrentLevel(finalI+1);
                    setMenu_pause();
                }
            });
        }
        addActor(saveButtons);







        // CODE FOR ADDING OPTIONS MENU ASSETS
        // no background is added here since the options menu will be the same size as the default menu size
        PauseMenuButton options_gameVolumeSlider = new PauseMenuButton(
                "Game Volume",
                new Slider(0,100,1,false, new Skin(Gdx.files.internal("uiskin.json"))),
                pauseMenuButtonX,
                (447 - (pauseMenuButtonHeight/2))
        );
        options_gameVolumeSlider.getPrivateButtonSlider().setValue(Setting.GameVolume);
        options_gameVolumeSlider.getPrivateButtonSlider().addCaptureListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Setting.GameVolume = Math.round(options_gameVolumeSlider.getPrivateButtonSlider().getValue());
                Setting.saveGlobalSettingsToFile();
            }
        });
        optionsButtons.addActor(options_gameVolumeSlider);

        PauseMenuButton options_musicVolumeSlider = new PauseMenuButton(
                "Music Volume",
                new Slider(0,100,1,false, new Skin(Gdx.files.internal("uiskin.json"))),
                pauseMenuButtonX,
                (389 - (pauseMenuButtonHeight/2))
        );
        options_musicVolumeSlider.getPrivateButtonSlider().setValue(Setting.MusicVolume);
        options_musicVolumeSlider.getPrivateButtonSlider().addCaptureListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Setting.MusicVolume = Math.round(options_musicVolumeSlider.getPrivateButtonSlider().getValue());
                Setting.saveGlobalSettingsToFile();
            }
        });

        optionsButtons.addActor(options_musicVolumeSlider);
        PauseMenuButton options_resetToDefault = new PauseMenuButton("Reset to Default", pauseMenuButtonX, (331 - (pauseMenuButtonHeight / 2)));
        options_resetToDefault.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Setting.GameVolume = 50;
                Setting.MusicVolume = 50;
                options_gameVolumeSlider.getPrivateButtonSlider().setValue(Setting.GameVolume);
                options_musicVolumeSlider.getPrivateButtonSlider().setValue(Setting.MusicVolume);
                Setting.saveGlobalSettingsToFile();

            }
        });
        optionsButtons.addActor(options_resetToDefault);

        PauseMenuButton options_saveSettings = new PauseMenuButton("Return", pauseMenuButtonX, (273 - (pauseMenuButtonHeight / 2)));
        options_saveSettings.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_pause();
            }
        });
        optionsButtons.addActor(options_saveSettings);
        addActor(optionsButtons);







        // CODE FOR ADDING EXIT MENU ASSETS
        // no background is added here since the exit menu will be the same size as the default menu size
        PauseMenuButton confirmLine1 = new PauseMenuButton("Unsaved changes will be lost",pauseMenuButtonX, (447 - (pauseMenuButtonHeight/2)), 16, Color.WHITE, Color.CLEAR);
        exitButtons.addActor(confirmLine1);
        PauseMenuButton confirmLine2 = new PauseMenuButton("Are you sure you want to continue?",pauseMenuButtonX, (389 - (pauseMenuButtonHeight/2)), 16, Color.WHITE, Color.CLEAR);
        exitButtons.addActor(confirmLine2);

        PauseMenuButton exit_confirmButton = new PauseMenuButton("Yes", pauseMenuButtonX, (331 - (pauseMenuButtonHeight/2)));
        exit_confirmButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TimeEngine.resume();
                screen.setPaused(false);
                FarmaniaGame.setActiveScreen(new MainMenu());
            }
        });
        exitButtons.addActor(exit_confirmButton);
        PauseMenuButton exit_cancelButton = new PauseMenuButton("No", pauseMenuButtonX, (273 - (pauseMenuButtonHeight/2)));
        exit_cancelButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_pause();
            }
        });
        exitButtons.addActor(exit_cancelButton);
        addActor(exitButtons);








        // set everything invisible here, so that when it's added into the levelscreen, the pause menu doesn't show up by default
        defaultBackground.setVisible(false);
        pauseButtons.setVisible(false);
        saveButtons.setVisible(false);
        saveBackground.setVisible(false);
        optionsButtons.setVisible(false);
        exitButtons.setVisible(false);

        // add to screen UI
        screen.getUIStage().addActor(this);
    }

    /**
     * Sets pause menu as being invisible by setting the default menu invisible
     */
    public void setMenu_resume() {
        defaultBackground.setVisible(false);
        pauseButtons.setVisible(false);
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
        setCurrentMenu("pause");
    }

    /**
     * Sets the pause menu to showing the save menu (back, save 1, save 2, etc.)
     * Function works by setting default menu ( setMenu_pause() ) to invisible, and then setting itself to being visible
     */
    public void setMenu_save() {
        // set default menu invisible
        defaultBackground.setVisible(false);
        pauseButtons.setVisible(false);
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
}
