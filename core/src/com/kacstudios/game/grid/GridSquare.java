package com.kacstudios.game.grid;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.utilities.TimeEngine;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class GridSquare extends BaseActor {

    private boolean collideWithPlayer = false;
    private GridVector gridCoords;
    private Grid grid;

    private int savedX;
    private int savedY;

    public GridSquare()
    {
        super();

        // add event
        this.addListener(
            (Event e) ->
            {
                InputEvent ie = (InputEvent)e;
                if ( ie.getType().equals(InputEvent.Type.touchDown) )
                    this.clickFunction(TimeEngine.getDateTime());
                return false;
            }
        );
    }

    public void setParent(Grid grid, GridVector position) {
        this.grid = grid;
        this.gridCoords = position;
    }

    public Grid getGrid() {
        return grid;
    }

    public GridVector getGridCoords() {
        return gridCoords;
    }

    /**
     * Returns all adjacent NON-NULL squares
     * @return
     */
    public ArrayList<GridSquare> getAdjacentSquares() {
        int minX = Math.max(gridCoords.x - 1, 0);
        int maxX = Math.min(gridCoords.x + 1, grid.getGridHeight() - 1);
        int minY = Math.max(gridCoords.y - 1, 0);
        int maxY = Math.min(gridCoords.y + 1, grid.getGridWidth() - 1);

        ArrayList<GridSquare> squares = new ArrayList<>();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if(x == gridCoords.x && y == gridCoords.y) continue; // skip square itself

                GridSquare target = grid.getSquare(x, y);
                if(target != null) squares.add(target);
            }
        }

        return squares;
    }

    public void setCollideWithPlayer(boolean collideWithPlayer) {
        this.collideWithPlayer = collideWithPlayer;
    }

    public void setTexture(String path) {
        try {
            Animation<TextureRegion> loadNewTexture = loadTexture(path);
            setAnimation(loadNewTexture);
        }
        catch (Exception ie) {
            System.out.println(ie);
        }
    }

    public void setCollisionSetting(boolean collides) {
        collideWithPlayer = collides;
    }

    public boolean getCollisionSetting() {
        return collideWithPlayer;
    }

    public void clickFunction(LocalDateTime dateTime) {
        System.out.println("I was clicked!");
    }

    public void setSavedX(int x) { savedX = x; }

    public void setSavedY(int y) { savedY = y; }

    public int getSavedX() { return savedX; }

    public int getSavedY() { return savedY; }

    @Override
    public void act(float dt) {
        super.act(dt);
    }
}
