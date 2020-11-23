package com.kacstudios.game.overlays.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

import java.nio.ByteBuffer;

public class CharacterMenuButton extends Group {
    private Image outline;
    private Image background;
    private Label buttonLabel;
    private Label.LabelStyle buttonLabelStyle;

    private static final int buttonWidth = 332;
    private static final int buttonWidthHalf = 160;
    private static final int buttonHeight = 48;
    private static final int buttonCornerRadius = 16;

    private static Texture fullBackgroundTexture = new Texture(ShapeGenerator.createRoundedRectangle(
            buttonWidth,
            buttonHeight,
            buttonCornerRadius,
            Color.WHITE
    ));

    private static Texture halfBackgroundTexture = new Texture(ShapeGenerator.createRoundedRectangle(
            buttonWidthHalf,
            buttonHeight,
            buttonCornerRadius,
            Color.WHITE
    ));


    // standard full width button, white background
    public CharacterMenuButton(String buttonText, int x, int y) {
        background = new Image(fullBackgroundTexture);
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
        if (!half) background = new Image(fullBackgroundTexture);
        else background = new Image(halfBackgroundTexture);

        background.setVisible(backgroundVisible);
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

    public void onClick() {
        // pass
    }


}
