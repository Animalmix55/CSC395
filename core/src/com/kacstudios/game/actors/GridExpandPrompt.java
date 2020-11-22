package com.kacstudios.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.actors.Farmer.Farmer;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.ShapeGenerator;

import java.util.ArrayList;

public class GridExpandPrompt extends Group {
    private LevelScreen screen;
    /**
     * The min distance from the side to see the prompt
     */
    private final int radius = 100;

    private Color arrowColor = new Color(1, 1, 1, .6f);
    private Texture rightArrow = new Texture(
            ShapeGenerator.createEquilateralTriangle(40, arrowColor, ShapeGenerator.Direction.right));
    private Texture leftArrow = new Texture(
            ShapeGenerator.createEquilateralTriangle(40, arrowColor, ShapeGenerator.Direction.left));
    private Texture upArrow = new Texture(
            ShapeGenerator.createEquilateralTriangle(40, arrowColor, ShapeGenerator.Direction.up));
    private Texture downArrow = new Texture(
            ShapeGenerator.createEquilateralTriangle(40, arrowColor, ShapeGenerator.Direction.down));

    private class ExpandArrows {
        Image left = null;
        Image right = null;
        Image top = null;
        Image bottom = null;
    }

    ExpandArrows arrows = new ExpandArrows();

    public GridExpandPrompt(LevelScreen screen) {
        this.screen = screen;
        screen.getMainStage().addActor(this);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        Farmer farmer = screen.getFarmer();

        Vector2 farmerPos = new Vector2(farmer.getX() + farmer.getWidth()/2, farmer.getY() + farmer.getHeight()/2);
        int maxX = (int) screen.getGrid().getWidth();
        int maxY = (int) screen.getGrid().getHeight();

        // left
        if(farmerPos.x > radius) {
            if(arrows.left != null) {
                arrows.left.remove();
                arrows.left = null;
            }
        }
        else {
            if (arrows.left == null) {
                arrows.left = new Image(leftArrow);
                addActor(arrows.left);
            }
            arrows.left.setPosition(-50 - leftArrow.getWidth(), farmerPos.y - rightArrow.getHeight()/2);
        }

        // bottom
        if(farmerPos.y > radius) {
            if(arrows.bottom != null) {
                arrows.bottom.remove();
                arrows.bottom = null;
            }
        }
        else {
            if (arrows.bottom == null) {
                arrows.bottom = new Image(downArrow);
                addActor(arrows.bottom);
            }
            arrows.bottom.setPosition(farmerPos.x - downArrow.getWidth()/2, -50 - downArrow.getHeight());
        }

        // right
        if(maxX - farmerPos.x > radius) {
            if(arrows.right != null) {
                arrows.right.remove();
                arrows.right = null;
            }
        }
        else {
            if(arrows.right == null) {
                arrows.right = new Image(rightArrow);
                addActor(arrows.right);
            }
            arrows.right.setPosition(maxX + 50, farmerPos.y - rightArrow.getHeight()/2);
        }

        // top
        if(maxY - farmerPos.y > radius) {
            if(arrows.top != null) {
                arrows.top.remove();
                arrows.top = null;
            }
        }
        else {
            if (arrows.top == null) {
                arrows.top = new Image(upArrow);
                addActor(arrows.top);
            }
            arrows.top.setPosition(farmerPos.x - upArrow.getWidth()/2, maxY + 50);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
