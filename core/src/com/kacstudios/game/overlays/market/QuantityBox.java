package com.kacstudios.game.overlays.market;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

public class QuantityBox extends Group {
    private static Texture quantityBox = new Texture(ShapeGenerator.createRoundedRectangle(80, 50, 20,
            new Color(1f, 1f, 1f, .4f)));
    private static Texture upArrow = new Texture(
            ShapeGenerator.createEquilateralTriangle(15, new Color(0, 0, 0, .7f), ShapeGenerator.Direction.up)
    );
    private static Texture downArrow = new Texture(
            ShapeGenerator.createEquilateralTriangle(15,  new Color(0, 0, 0, .7f), ShapeGenerator.Direction.down)
    );
    private Label QuantityLabel;
    private static Label.LabelStyle itemNameStyle =
            new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 20), Color.WHITE);
    private Image upArrowImage;
    private Image downArrowImage;

    private boolean downClicked = false;
    private float clickSeconds = 0;
    private float clickSecondsAtLastUpdate = 0;
    private boolean upClicked = false;


    public QuantityBox() {
        setHeight(quantityBox.getHeight());
        setWidth(quantityBox.getWidth());

        Image quantityBoxImage = new Image(quantityBox);
        addActor(quantityBoxImage);

        QuantityLabel = new Label("1", itemNameStyle);
        QuantityLabel.setWidth(quantityBoxImage.getWidth());
        QuantityLabel.setHeight(quantityBoxImage.getHeight());
        QuantityLabel.setAlignment(Align.center);
        QuantityLabel.setPosition(quantityBoxImage.getX(), quantityBoxImage.getY());
        addActor(QuantityLabel);

        upArrowImage = new Image(upArrow);
        upArrowImage.setPosition(getWidth() - upArrowImage.getWidth() - 5, getHeight()/2 + 5);
        upArrowImage.addCaptureListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upClicked = false;
                clickSeconds = 0;
                clickSecondsAtLastUpdate = 0;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upClicked = true;
                setQuantity(getQuantity() + 1);
                return true;
            }
        });
        addActor(upArrowImage);

        downArrowImage = new Image(downArrow);
        downArrowImage.setPosition(getWidth() - downArrowImage.getWidth() - 5, getHeight()/2 - 5 - downArrowImage.getHeight());
        downArrowImage.addCaptureListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downClicked = false;
                clickSeconds = 0;
                clickSecondsAtLastUpdate = 0;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downClicked = true;
                setQuantity(getQuantity() - 1);
                return true;
            }
        });
        addActor(downArrowImage);
        onQuantityChange(getQuantity()); // ensure state is up to date
    }

    private void setQuantity(int quantity) {
        int currentQuantity = getQuantity();
        if (quantity < 1) QuantityLabel.setText(1);
        else QuantityLabel.setText(quantity);

        // call hook
        if(currentQuantity != getQuantity()) onQuantityChange(getQuantity());
    }

    public int getQuantity() {
        return Integer.parseInt(QuantityLabel.getText().toString());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 globalCursorPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 localCursorPos = screenToLocalCoordinates(globalCursorPos);

        if(localCursorPos.x > 0 && localCursorPos.y > 0 && localCursorPos.x < getWidth() && localCursorPos.y < getHeight()) {
            upArrowImage.setVisible(true);
            downArrowImage.setVisible(true);
        } else {
            upArrowImage.setVisible(false);
            downArrowImage.setVisible(false);
        }

        if(upClicked || downClicked) {
            clickSeconds += delta;

            int stepSize = clickSeconds > 3? (clickSeconds > 10? 40 : 20) : 5;
            if(clickSeconds - clickSecondsAtLastUpdate > .5) {
                if(upClicked) setQuantity(getQuantity() + stepSize);
                else setQuantity(getQuantity() - stepSize);
                clickSecondsAtLastUpdate = clickSeconds;
            }
        }
    }

    /**
     * Called whenever the quantity inside of the box changes
     * @param quantity
     */
    public void onQuantityChange(int quantity) {
        // stub to be implemented
    }
}
