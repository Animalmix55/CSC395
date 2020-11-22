package com.kacstudios.game.overlays.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

public class CharacterMenuButton extends Group {
    private Image background;
    private Label buttonLabel;
    private Label.LabelStyle buttonLabelStyle;
    private Slider privateButtonSlider;

    private final int buttonWidth = 332;
    private final int buttonWidthHalf = 160;
    private final int buttonHeight = 48;
    private final int buttonCornerRadius = 16;

    public CharacterMenuButton(String buttonText, int x, int y) {
        background = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(
                        buttonWidth,
                        buttonHeight,
                        buttonCornerRadius,
                        Color.WHITE
                )
                )
        );
        setWidth(background.getWidth());
        setHeight(background.getHeight());
        setX(x);
        setY(y);
        addActor(background);

        buttonLabelStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 24), Color.BLACK);
        buttonLabel = new Label(buttonText,buttonLabelStyle);
        buttonLabel.setX((buttonWidth/2) - (buttonLabel.getWidth() / 2));
        buttonLabel.setY((buttonHeight/2) - (buttonLabel.getHeight() / 2));
        addActor(buttonLabel);
    }

    public CharacterMenuButton(String buttonText, int x, int y, boolean backgroundVisible, boolean half) {
        Color backgroundColor;
        if (backgroundVisible) backgroundColor = Color.WHITE;
        else backgroundColor = Color.CLEAR;

        if (!half) background = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(
                        buttonWidth,
                        buttonHeight,
                        buttonCornerRadius,
                        backgroundColor
                )
                )
        );
        else background = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(
                        buttonWidthHalf,
                        buttonHeight,
                        buttonCornerRadius,
                        backgroundColor
                )
                )
        );
        setWidth(background.getWidth());
        setHeight(background.getHeight());
        setX(x);
        setY(y);
        addActor(background);


        if (backgroundVisible) buttonLabelStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 24), Color.BLACK);
        else buttonLabelStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 24), Color.WHITE);
        buttonLabel = new Label(buttonText,buttonLabelStyle);
        buttonLabel.setX((buttonWidth/2) - (buttonLabel.getWidth() / 2));
        if (half) buttonLabel.setX(buttonLabel.getX()-83);
        buttonLabel.setY((buttonHeight/2) - (buttonLabel.getHeight() / 2));
        addActor(buttonLabel);
    }

}
