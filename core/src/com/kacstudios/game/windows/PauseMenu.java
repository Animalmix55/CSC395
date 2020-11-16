package com.kacstudios.game.windows;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.ShapeGenerator;

public class PauseMenu extends Group {
    private LevelScreen screen;
    private Image background;

    private final int pauseMenuWidth = 324;
    private final int pauseMenuX = 640 - (pauseMenuWidth/2);
    private final int pauseMenuHeight = 242;

    private final int pauseMenuButtonWidth = 304;
    private final int pauseMenuButtonX = 640 - (pauseMenuButtonWidth/2);
    private final int pauseMenuButtonHeight = 48;

    Group pauseButtons = new Group();

    public PauseMenu(LevelScreen inputScreen) {
        screen = inputScreen;
        this.setWidth(pauseMenuWidth);
        this.setHeight(pauseMenuHeight);
        this.setBounds(0,0,pauseMenuWidth,pauseMenuHeight);

        background = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(
                        pauseMenuWidth,
                        pauseMenuHeight,
                        16,
                        new Color(0,0,0,.5f)
                )
                )
        );
        background.setX(pauseMenuX);
        background.setY(360 - (pauseMenuHeight/2)); // center
        addActor(background);

        // 58 apart on Y coordinate
        PauseMenuButton resumeButton = new PauseMenuButton("Resume", pauseMenuButtonX, (447 - (pauseMenuButtonHeight/2)));
        pauseButtons.addActor(resumeButton);
        PauseMenuButton saveButton = new PauseMenuButton("Save", pauseMenuButtonX, (389 - (pauseMenuButtonHeight/2)));
        pauseButtons.addActor(saveButton);
        PauseMenuButton optionsButton = new PauseMenuButton("Options", pauseMenuButtonX, (331 - (pauseMenuButtonHeight/2)));
        pauseButtons.addActor(optionsButton);
        PauseMenuButton exitButton = new PauseMenuButton("Exit Game", pauseMenuButtonX, (273 - (pauseMenuButtonHeight/2)));
        pauseButtons.addActor(exitButton);


        addActor(pauseButtons);
        screen.getUIStage().addActor(this);
    }
}
