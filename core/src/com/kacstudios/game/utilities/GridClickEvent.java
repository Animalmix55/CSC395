package com.kacstudios.game.utilities;

import com.badlogic.gdx.math.Vector2;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.GridVector;
import com.kacstudios.game.screens.LevelScreen;

public class GridClickEvent {
    private int x;
    private int y;
    private Grid grid;
    private LevelScreen screen;

    public GridClickEvent(int x, int y, LevelScreen screen) {
        this.x = x;
        this.y = y;
        this.grid = screen.getGrid();
        this.screen = screen;
    }

    /**
     * Returns the location of the grid clicked in <b>GRID COORDINATES</b>
     * @return Grid coordinate vector
     */
    public GridVector getLocation(){
        return new GridVector(x, y);
    }

    /**
     * Returns the current Grid Square clicked or null if none existed.
     * @return GridSquare clicked or null
     */
    public GridSquare getGridSquare(){
        return grid.getGridSquares()[x][y]; // could be null
    }

    public void setSquare(GridSquare square){
        grid.addGridSquare(x, y, square);
    }

    public Vector2 getFarmerLocation() {
        return screen.getFarmer().localToStageCoordinates(
                new Vector2(screen.getFarmer().getOriginX(), screen.getFarmer().getOriginY()));
    }

    /**
     * Returns if the farmer is within a certain pixel radius of the plot
     * @param radius
     * @return
     */
    public boolean farmerWithinRadius(float radius) {
        double squareX = grid.getSquareSideLength() * ((float)x + .5);
        double squareY = grid.getSquareSideLength() * ((float)y + .5);

        return (squareX + radius >= getFarmerLocation().x &&
                squareX - radius <= getFarmerLocation().x) &&
                (squareY + radius >= getFarmerLocation().y &&
                squareY - radius <= getFarmerLocation().y);
    }

    public LevelScreen getScreen() {
        return screen;
    }
}
