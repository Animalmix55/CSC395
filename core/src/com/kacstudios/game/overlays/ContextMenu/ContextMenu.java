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

import java.time.Duration;
import java.time.LocalDateTime;

public class ContextMenu extends Group {
    private static Texture optionBackground = new Texture(ShapeGenerator.createRoundedRectangle(150, 30, 10,
            new Color(255, 255, 255, .3f)));

    private static Texture optionBackgroundHovered = new Texture(ShapeGenerator.createRoundedRectangle(150, 30, 10,
            new Color(255, 255, 255, .7f)));

    Label.LabelStyle labelStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 15), Color.WHITE);
    LocalDateTime openTime;

    private class ContextMenuOptionLine extends Group {
        private Image hoverBg;
        private Image bg;
        private Label label;
        private boolean wasHovered = false;

        public ContextMenuOptionLine(ContextMenuOption option) {
            this.setHeight(optionBackground.getHeight());
            this.setWidth(optionBackground.getWidth());

            bg = new Image(optionBackground);
            hoverBg  = new Image(optionBackgroundHovered);
            hoverBg.setVisible(false);

            this.addActor(hoverBg);
            this.addActor(bg);
            this.addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setOpen(false);
                    option.onClick.run();
                }
            });

            label = new Label(option.getDisplayText(), labelStyle);
            label.setWidth(this.getWidth());
            label.setHeight(this.getHeight());
            label.setAlignment(Align.center);
            this.addActor(label);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            Vector2 globalCursorPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 localCursorPos = screenToLocalCoordinates(globalCursorPos);

            boolean isHovered = localCursorPos.x > 0 && localCursorPos.y > 0 && localCursorPos.x < getWidth() && localCursorPos.y < getHeight();
            if(isHovered != wasHovered) {
                bg.setVisible(!isHovered);
                hoverBg.setVisible(isHovered);
                if(isHovered) label.setColor(Color.BLACK);
                else label.setColor(Color.WHITE);

                wasHovered = isHovered;
            }
        }
    }

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
            ContextMenuOptionLine optionGroup = new ContextMenuOptionLine(options[i]);

            optionGroup.setPosition(0, i * (optionBackground.getHeight() + 5) + 5);
            optionGroup.setX(5);

            addActor(optionGroup);
        }
        setOpen(false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(isOpen()) {
            if(Duration.between(openTime, LocalDateTime.now()).getSeconds() > 5) setOpen(false);
            else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                Vector2 coords = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

                if(coords.x < 0 || coords.x > getWidth() || coords.y < 0 || coords.y > getHeight()) setOpen(false);
            }
        }
    }

    public void setOpen(boolean isOpen) {
        setVisible(isOpen);
        if(isOpen) openTime = LocalDateTime.now();
        else openTime = null;
    }

    public boolean isOpen() {
        return openTime != null;
    }
}
