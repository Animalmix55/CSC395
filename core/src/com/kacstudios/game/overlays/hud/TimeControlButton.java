package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.kacstudios.game.utilities.SelectableButton;
import com.kacstudios.game.utilities.TimeEngine;

import java.sql.Time;

public class TimeControlButton extends SelectableButton {
    public enum ButtonType {
        Pause,
        Play,
        Double,
        Triple
    }

    private ButtonType type;

    public TimeControlButton(ButtonType type) {
        super(0, 0, new Texture("bottombar/timebutton_background_selected.png"),
                new Texture("bottombar/timebutton_background_unselected.png"));

        this.type = type;

        switch(type){
            case Pause:
                setAnimation(loadTexture("bottombar/timebutton_icon_pause.png"));
                break;
            case Play:
                setAnimation(loadTexture("bottombar/timebutton_icon_play.png"));
                break;
            case Double:
                setAnimation(loadTexture("bottombar/timebutton_icon_2x.png"));
                break;
            case Triple:
                setAnimation(loadTexture("bottombar/timebutton_icon_3x.png"));
                break;
        }
    }

    @Override
    public void onClick(InputEvent event, float x, float y) {
        switch(type){
            case Pause:
                TimeEngine.pause();
                break;
            case Play:
                TimeEngine.resume();
                break;
            case Double:
                TimeEngine.dilateTime(2);
                break;
            case Triple:
                TimeEngine.dilateTime(3);
                break;
        }

        setSelected(true);
    }

    @Override
    public void act(float dt) {
        switch (type) {
            case Pause:
                setSelected(TimeEngine.getDilation() == 0);
                break;
            case Play:
                setSelected(TimeEngine.getDilation() == 1);
                break;
            case Double:
                setSelected(TimeEngine.getDilation() == 2);
                break;
            case Triple:
                setSelected(TimeEngine.getDilation() == 3);
                break;
        }
        super.act(dt);
    }
}
