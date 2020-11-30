package com.kacstudios.game.actors.gridexpansion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.actors.Farmer.Farmer;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.Economy;
import com.kacstudios.game.utilities.ShapeGenerator;
import com.kacstudios.game.utilities.TimeEngine;

public class GridExpandPrompt extends Group {
    private LevelScreen screen;
    /**
     * The min distance from the side to see the prompt
     */
    private final int radius = 100;
    GridExpandArrow left;
    GridExpandArrow right;
    GridExpandArrow top;
    GridExpandArrow bottom;

    public final int perSquarePrice = 500;

    public GridExpandPrompt(LevelScreen screen) {
        this.screen = screen;

        Grid grid = screen.getGrid();
        left = new GridExpandArrow(grid.getGridHeight() * perSquarePrice, ShapeGenerator.Direction.left) {
            @Override
            public void onClick() {
                if(TimeEngine.getDilation() == 0 || !Economy.removeMoney(getPrice())) return;
                grid.expandGrid(ShapeGenerator.Direction.left);
                top.setPrice(grid.getGridWidth() * perSquarePrice);
                bottom.setPrice(grid.getGridWidth() * perSquarePrice);
            }
        };
        right = new GridExpandArrow(grid.getGridHeight() * perSquarePrice, ShapeGenerator.Direction.right) {
            @Override
            public void onClick() {
                if(TimeEngine.getDilation() == 0 || !Economy.removeMoney(getPrice())) return;
                grid.expandGrid(ShapeGenerator.Direction.right);
                top.setPrice(grid.getGridWidth() * perSquarePrice);
                bottom.setPrice(grid.getGridWidth() * perSquarePrice);
            }
        };
        top = new GridExpandArrow(grid.getGridWidth() * perSquarePrice, ShapeGenerator.Direction.up) {
            @Override
            public void onClick() {
                if(TimeEngine.getDilation() == 0 || !Economy.removeMoney(getPrice())) return;
                grid.expandGrid(ShapeGenerator.Direction.up);
                left.setPrice(grid.getGridHeight() * perSquarePrice);
                right.setPrice(grid.getGridHeight() * perSquarePrice);
            }
        };
        bottom = new GridExpandArrow(grid.getGridHeight() * perSquarePrice, ShapeGenerator.Direction.down) {
            @Override
            public void onClick() {
                if(TimeEngine.getDilation() == 0 || !Economy.removeMoney(getPrice())) return;
                grid.expandGrid(ShapeGenerator.Direction.down);
                left.setPrice(grid.getGridHeight() * perSquarePrice);
                right.setPrice(grid.getGridHeight() * perSquarePrice);
            }
        };

        addActor(left);
        addActor(right);
        addActor(top);
        addActor(bottom);

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
            if(left.isVisible()) left.setVisible(false);
        }
        else {
            left.setVisible(true);
            left.setPosition(-50 - left.getWidth(), farmerPos.y - left.getHeight() / 2);
        }

        // bottom
        if(farmerPos.y > radius) {
            if(bottom.isVisible()) bottom.setVisible(false);
        }
        else {
            bottom.setVisible(true);
            bottom.setPosition(farmerPos.x - bottom.getWidth() / 2, -50 - bottom.getHeight());
        }

        // right
        if(maxX - farmerPos.x > radius) {
            if(right.isVisible()) right.setVisible(false);
        }
        else {
            right.setVisible(true);
            right.setPosition(maxX + 50, farmerPos.y - right.getHeight()/2);
        }

        // top
        if(maxY - farmerPos.y > radius) {
            if(top.isVisible()) top.setVisible(false);
        }
        else {
            top.setVisible(true);
            top.setPosition(farmerPos.x - top.getWidth() / 2, maxY + 50);
        }
    }
}
