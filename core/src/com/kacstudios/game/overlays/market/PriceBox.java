package com.kacstudios.game.overlays.market;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kacstudios.game.utilities.Economy;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

public class PriceBox extends Group {
    public static final int height = 50;
    public static final int width = 105;
    private static final int cornerRadius = 20;

    private static final Texture greenBox = new Texture(ShapeGenerator.createRoundedRectangle(width, height, cornerRadius,
            new Color(0f, 1f, 0f, .4f)));
    private static final Texture greenBoxHover = new Texture(ShapeGenerator.createRoundedRectangle(width, height, cornerRadius,
            new Color(0f, 1f, 0f, .7f)));
    private static final Texture disabledTexture = new Texture(ShapeGenerator.createRoundedRectangle(width, height, cornerRadius,
            new Color(1f, 1f, 1f, .2f)));
    private static final Label.LabelStyle priceBoxTextStyle =
            new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 20), Color.WHITE);

    private int price;
    private PriceBoxType type;
    private Image hoverImage;
    private Image regularImage;
    private Image disabled;
    private Label priceLabel;

    public enum PriceBoxType {
        Sell,
        Buy
    }

    public PriceBox(int price, PriceBoxType type) {
        this.price = price;
        this.type = type;

        regularImage = new Image(greenBox);
        hoverImage = new Image(greenBoxHover);

        setHeight(regularImage.getHeight());
        setWidth(regularImage.getWidth());

        disabled = new Image(disabledTexture);
        priceLabel = new Label(String.format("$%s", price), priceBoxTextStyle);
        priceLabel.setHeight(getHeight());
        priceLabel.setWidth(getWidth());
        priceLabel.setAlignment(Align.center);

        hoverImage.setVisible(false);
        disabled.setVisible(false);
        addActor(regularImage);
        addActor(hoverImage);
        addActor(disabled);
        addActor(priceLabel);

        if(type == PriceBoxType.Buy && Economy.getMoney() < price) setDisabled(true); // disable on instantiation

        addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick();
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(disabled.isVisible()) return; // don't hover if disabled
        Vector2 mousePos = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        if(mousePos.x > 0 && mousePos.y > 0 && mousePos.x < this.getWidth() && mousePos.y < this.getHeight()) {
            hoverImage.setVisible(true);
            regularImage.setVisible(false);
        } else if (!regularImage.isVisible()){
            hoverImage.setVisible(false);
            regularImage.setVisible(true);
        }
    }

    public void updatePrice(int price) {
        priceLabel.setText(String.format("$%s", price));
        this.price = price;
        if (type == PriceBoxType.Buy) {
            if (Economy.getMoney() < price)
                setDisabled(true);
            else setDisabled(false);
        }
    }

    public void setDisabled(boolean isDisabled) {
        regularImage.setVisible(!isDisabled);
        hoverImage.setVisible(false);
        disabled.setVisible(isDisabled);
    }

    public void onClick() {
        // stub
    }

    /**
     * To be called when the economy is updated
     */
    public void economyUpdated() {
        updatePrice(price); // checks button again
    }

    public PriceBoxType getType() {
        return type;
    }
}

