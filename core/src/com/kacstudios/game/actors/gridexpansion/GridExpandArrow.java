package com.kacstudios.game.actors.gridexpansion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.utilities.Economy;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

import java.awt.*;

public class GridExpandArrow extends Group {
    private Color arrowColor = new Color(1, 1, 1, .6f);
    private Texture rightArrow = new Texture(
            ShapeGenerator.createEquilateralTriangle(40, arrowColor, ShapeGenerator.Direction.right));
    private Texture leftArrow = new Texture(
            ShapeGenerator.createEquilateralTriangle(40, arrowColor, ShapeGenerator.Direction.left));
    private Texture upArrow = new Texture(
            ShapeGenerator.createEquilateralTriangle(40, arrowColor, ShapeGenerator.Direction.up));
    private Texture downArrow = new Texture(
            ShapeGenerator.createEquilateralTriangle(40, arrowColor, ShapeGenerator.Direction.down));
    private static BitmapFont font = FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 20);

    private int price;
    private Image arrow;
    private boolean hovered = false;
    private Label costLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
    ShapeGenerator.Direction direction;

    public GridExpandArrow(int price, ShapeGenerator.Direction direction) {
        this.price = price;
        this.direction = direction;

        costLabel.setVisible(false);
        addActor(costLabel);

        // set image
        switch (direction) {
            case left:
                arrow = new Image(leftArrow);
                break;
            case right:
                arrow = new Image(rightArrow);
                break;
            case up:
                arrow = new Image(upArrow);
                break;
            case down:
                arrow = new Image(downArrow);
                break;
        }

        setPrice(price);
        addActor(arrow);
        setWidth(arrow.getWidth());
        setHeight(arrow.getHeight());

        arrow.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick();
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        Vector2 cursorPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 localCoords = screenToLocalCoordinates(cursorPos);

        if (localCoords.x > arrow.getX() && localCoords.x < arrow.getRight() &&
                localCoords.y > arrow.getY() && localCoords.y < arrow.getTop()) {
            if (!hovered) setHovered(true);
        }
        else if (hovered) setHovered(false);
    }

    private void setHovered(boolean isHovered) {
        hovered = isHovered;

        costLabel.setVisible(isHovered);

        if (hovered) {
            if(Economy.getMoney() >= price) arrow.setColor(Color.GREEN);
            else arrow.setColor(Color.RED);
        }
        else arrow.setColor(Color.WHITE);
    }

    public void setPrice(int price) {
        this.price = price;
        costLabel.setText(String.format("$%d.00", price));
        costLabel.pack();

        switch (direction) {
            case left:
                costLabel.setPosition(-costLabel.getWidth() - 10, (arrow.getHeight() - costLabel.getHeight())/2);
                break;
            case right:
                costLabel.setPosition(arrow.getWidth() + 10, (arrow.getHeight() - costLabel.getHeight())/2);
                break;
            case up:
                costLabel.setPosition((arrow.getWidth() - costLabel.getWidth())/2, -costLabel.getHeight() - 10);
                break;
            case down:
                costLabel.setPosition((arrow.getWidth() - costLabel.getWidth())/2, arrow.getHeight() + 10);
                break;
        }
    }

    public int getPrice() {
        return price;
    }

    public void onClick() {
        //stub
    }
}
