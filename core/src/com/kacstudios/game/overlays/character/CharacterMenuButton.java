package com.kacstudios.game.overlays.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

public class CharacterMenuButton extends Group {
    private Image outline;
    private Image background;
    private Label buttonLabel;
    private Label.LabelStyle buttonLabelStyle;

    private final int buttonWidth = 332;
    private final int buttonWidthHalf = 160;
    private final int buttonHeight = 48;
    private final int buttonCornerRadius = 16;

    // standard full width button, white background
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

    // customizable width/background button
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

    // preset button
    public CharacterMenuButton(int x, int y, String texturePath) {
        outline = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(
                        52,
                        52,
                        16,
                        Color.WHITE
                )
                )
        );
        outline.setX(-2);
        outline.setY(-2);
        outline.setVisible(false);
        addActor(outline);
        background = new Image( new Texture( texturePath ) );
        setX(x);
        setY(y);
        background.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (outline.isVisible()) outline.setVisible(false);
                else outline.setVisible(true);
            }
        });
        addActor(background);
    }


}
