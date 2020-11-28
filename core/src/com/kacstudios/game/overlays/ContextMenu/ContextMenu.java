package com.kacstudios.game.overlays.ContextMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;
import com.kacstudios.game.utilities.TimeEngine;

import java.time.LocalDateTime;

public class ContextMenu extends Group {
    private static Texture optionBackground = new Texture(ShapeGenerator.createRoundedRectangle(200, 40, 10,
            new Color(255, 255, 255, .3f)));
    Label.LabelStyle labelStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 30), Color.WHITE);
    LocalDateTime openTime;

    public static class ContextMenuOption {
        private String displayText;
        private Runnable onClick;
        public ContextMenuOption(String displayText, Runnable onClick) {
            this.displayText = displayText;
            this.onClick = onClick;
        }

        public Runnable getOnClick() {
            return onClick;
        }

        public String getDisplayText() {
            return displayText;
        }
    }
    public ContextMenu(int x, int y, ContextMenuOption[] options) {
        setPosition(x, y);
        int size = options.length;
        setHeight((optionBackground.getHeight() + 5) * size + 5);
        setWidth(optionBackground.getWidth() + 10);

        Image background =
                new Image(new Texture(ShapeGenerator.createRoundedRectangle((int) getWidth(), (int) getHeight(), 10,
                        new Color(0, 0, 0, .5f))));

        addActor(background);
        addCaptureListener(new ClickListener()); // catches events

        for (int i = 0; i < size; i++) {
            Group optionGroup = new Group();

            optionGroup.setPosition(0, i * (optionBackground.getHeight() + 5) + 5);
            optionGroup.setHeight(optionBackground.getHeight());
            optionGroup.setX(5);
            optionGroup.setWidth(getWidth());

            ContextMenuOption option = options[i];
            optionGroup.addActor(new Image(optionBackground));
            optionGroup.addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setOpen(false);
                    option.onClick.run();
                }
            });

            Label label = new Label(option.getDisplayText(), labelStyle);
            label.setWidth(optionGroup.getWidth());
            label.setHeight(optionGroup.getHeight());
            label.setAlignment(Align.center);
            optionGroup.addActor(label);

            addActor(optionGroup);
        }
        setOpen(false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(isOpen()) {
            if(TimeEngine.getSecondsSince(openTime) > 5) setOpen(false);
            else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                Vector2 coords = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

                if(coords.x < 0 || coords.x > getWidth() || coords.y < 0 || coords.y > getHeight()) setOpen(false);
            }
        }
    }

    public void setOpen(boolean isOpen) {
        setVisible(isOpen);
        if(isOpen) openTime = TimeEngine.getDateTime();
        else openTime = null;
    }

    public boolean isOpen() {
        return openTime != null;
    }
}
