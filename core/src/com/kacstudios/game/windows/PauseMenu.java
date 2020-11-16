package com.kacstudios.game.windows;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.ShapeGenerator;

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

    Group pauseButtons = new Group();
    Group saveButtons = new Group();

    public PauseMenu(LevelScreen inputScreen) {
        screen = inputScreen;
        setWidth(pauseMenuWidth);
        setHeight(pauseMenuHeight);
        setBounds(0,0,pauseMenuWidth,pauseMenuHeight);

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
        pauseButtons.addActor(optionsButton);
        PauseMenuButton exitButton = new PauseMenuButton("Exit Game", pauseMenuButtonX, (273 - (pauseMenuButtonHeight/2)));
        pauseButtons.addActor(exitButton);

        addActor(pauseButtons);






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
        for (int i=0;i<5;i++) {
            saveButtons.addActor(
                    new PauseMenuButton(String.format("Save %d",i+1), pauseMenuButtonX, (447-(58*i) - (pauseMenuButtonHeight/2)))
            );
        }

        addActor(saveButtons);



        // set everything invisible here
        saveButtons.setVisible(false);
        saveBackground.setVisible(false);

        // add to screen UI
        screen.getUIStage().addActor(this);
    }

    public void setMenu_resume() {
        defaultBackground.setVisible(false);
        pauseButtons.setVisible(false);
    }

    public void setMenu_pause() {
        // set save menu invisible
        saveButtons.setVisible(false);
        saveBackground.setVisible(false);
        // set original menu visible
        defaultBackground.setVisible(true);
        pauseButtons.setVisible(true);
    }

    public void setMenu_save() {
        // set default menu invisible
        defaultBackground.setVisible(false);
        pauseButtons.setVisible(false);
        // set save menu visible
        saveButtons.setVisible(true);
        saveBackground.setVisible(true);
    }
}
