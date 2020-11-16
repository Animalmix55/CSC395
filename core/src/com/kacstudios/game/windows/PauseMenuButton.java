package com.kacstudios.game.windows;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

public class PauseMenuButton extends Group {
    private Image background;
    private String text;
    private Label buttonLabel;

    public PauseMenuButton(String buttonText, int x, int y) {
        text = buttonText;
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

        Label.LabelStyle buttonLabelStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 24), Color.BLACK);
        buttonLabel = new Label(buttonText, buttonLabelStyle);
        buttonLabel.setX(152 - (buttonLabel.getWidth() / 2));
        buttonLabel.setY(24 - (buttonLabel.getHeight() / 2));
        addActor(buttonLabel);
    }
}
