package com.kacstudios.game.windows;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

public class PauseMenuButton extends Group {
    private Image background;
    private Label buttonLabel;
    private Label.LabelStyle buttonLabelStyle;
    private Slider privateButtonSlider;

    public PauseMenuButton(String buttonText, int x, int y) {
        background = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(
                                304,
                                48,
                                16,
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
        buttonLabel = new Label(buttonText, buttonLabelStyle);
        buttonLabel.setX(152 - (buttonLabel.getWidth() / 2));
        buttonLabel.setY(24 - (buttonLabel.getHeight() / 2));
        addActor(buttonLabel);
    }

    public PauseMenuButton(String buttonText, int x, int y, int fontSize, Color fontColor, Color backgroundColor) {
        background = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(
                        304,
                        48,
                        16,
                        backgroundColor
                )
                )
        );
        setWidth(background.getWidth());
        setHeight(background.getHeight());
        setX(x);
        setY(y);
        addActor(background);

        buttonLabelStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", fontSize), fontColor);
        buttonLabel = new Label(buttonText, buttonLabelStyle);
        buttonLabel.setX(152 - (buttonLabel.getWidth() / 2));
        buttonLabel.setY(24 - (buttonLabel.getHeight() / 2));
        addActor(buttonLabel);
    }

    public PauseMenuButton(String buttonText, Slider buttonSlider, int x, int y) {
        background = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(
                        304,
                        48,
                        16,
                        Color.WHITE
                )
                )
        );
        setWidth(background.getWidth());
        setHeight(background.getHeight());
        setX(x);
        setY(y);
        addActor(background);

        buttonLabelStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 12), Color.BLACK);
        buttonLabel = new Label(buttonText, buttonLabelStyle);
        buttonLabel.setX(16);
        buttonLabel.setY(24 - (buttonLabel.getHeight() / 2));
        addActor(buttonLabel);

        privateButtonSlider = buttonSlider;
        privateButtonSlider.setX(background.getWidth() - buttonSlider.getWidth() - 16);
        privateButtonSlider.setY(24 - (buttonSlider.getHeight() / 2));
        addActor(privateButtonSlider);
    }

    public Slider getPrivateButtonSlider() {
        return privateButtonSlider;
    }

    public void setButtonLabelText(String newText) { buttonLabel.setText(newText); }
}
