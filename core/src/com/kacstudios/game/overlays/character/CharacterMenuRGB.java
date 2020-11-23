package com.kacstudios.game.overlays.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

public class CharacterMenuRGB extends Group {

    private Image[] backgrounds;
    private Label.LabelStyle buttonLabelStyle;
    private String[] labelPrefixes;
    private Label label_r, label_g, label_b;
    private Label[] labels;
    private Slider slider_r, slider_g, slider_b;
    private Slider[] sliders;

    private final int buttonWidth = 332;
    private final int buttonHeight = 48;
    private final int buttonCornerRadius = 16;

    public CharacterMenuRGB(int x, int y) {
        // y value should be set to bottom bar y value
        backgrounds = new Image[3];
        setWidth(buttonWidth);

        buttonLabelStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 24), Color.BLACK);

        for (int i=0;i<3;i++) {
            backgrounds[i] = new Image(
                    new Texture(ShapeGenerator.createRoundedRectangle(
                            buttonWidth,
                            buttonHeight,
                            buttonCornerRadius,
                            Color.WHITE
                    )
                    )
            );
            backgrounds[i].setX(x);
            backgrounds[i].setY(y+(58*i));
            addActor(backgrounds[i]);
        }

        labelPrefixes = new String[]{"Blue","Green","Red"};
        label_r = new Label(labelPrefixes[2],buttonLabelStyle);
        label_g = new Label(labelPrefixes[1],buttonLabelStyle);
        label_b = new Label(labelPrefixes[0],buttonLabelStyle);
        labels = new Label[3];
        labels[0] = label_b;
        labels[1] = label_g;
        labels[2] = label_r;
        for (int i=0;i<3;i++) {
            labels[i].setX(x+16);
            labels[i].setY(y+8+(58*i));
            addActor(labels[i]);
        }

        slider_r = new Slider(0,1,.01f,false, new Skin(Gdx.files.internal("uiskin.json")));
        slider_g = new Slider(0,1,.01f,false, new Skin(Gdx.files.internal("uiskin.json")));
        slider_b = new Slider(0,1,.01f,false, new Skin(Gdx.files.internal("uiskin.json")));
        sliders = new Slider[3];
        sliders[0] = slider_b;
        sliders[1] = slider_g;
        sliders[2] = slider_r;
        for (int i=0;i<3;i++) {
            int finali = i;
            sliders[i].setX(x+32+sliders[i].getWidth());
            sliders[i].setY(y+16+(58*i));
            sliders[i].addCaptureListener(new DragListener() {
                @Override
                public void drag(InputEvent event, float x, float y, int pointer) {
                    labels[finali].setText( String.format("%s (%d%%)",labelPrefixes[finali], (int) (sliders[finali].getValue() * 100)) );
                    onSliderChange(slider_r.getValue(), slider_g.getValue(), slider_b.getValue());
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer) {
                    onStopSliding(slider_r.getValue(), slider_g.getValue(), slider_b.getValue());
                }
            });
            sliders[i].addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    labels[finali].setText( String.format("%s (%d%%)",labelPrefixes[finali], (int) (sliders[finali].getValue() * 100)) );
                    onSliderChange(slider_r.getValue(), slider_g.getValue(), slider_b.getValue());
                    onStopSliding(slider_r.getValue(), slider_g.getValue(), slider_b.getValue());
                }
            });
            addActor(sliders[i]);
        }

    }

    public float[] getSliderValues() {
        return new float[]{slider_r.getValue(),slider_g.getValue(),slider_b.getValue()};
    }

    public void setSliders(float r, float g, float b) {
        slider_r.setValue(r);
        slider_g.setValue(g);
        slider_b.setValue(b);

        for (int i = 0; i < sliders.length; i++) {
            labels[i].setText(String.format("%s (%d%%)", labelPrefixes[i], (int) (sliders[i].getValue() * 100)));
        }
    }

    public void onSliderChange(float r, float g, float b) {
        // pass
    }

    public void onStopSliding(float r, float g, float b) {
        // pass
    }
}
