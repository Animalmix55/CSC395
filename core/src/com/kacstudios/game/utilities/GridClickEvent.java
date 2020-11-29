package com.kacstudios.game.utilities;

import com.badlogic.gdx.math.Vector2;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.GridVector;
import com.kacstudios.game.screens.LevelScreen;

public class GridClickEvent {
    private float x;
    private float y;

    private GridVector gridCoords;

    private Grid grid;
    private LevelScreen screen;

    public GridClickEvent(float x, float y, LevelScreen screen) {
        this.grid = screen.getGrid();

        Vector2 coords = this.grid.localToScreenCoordinates(new Vector2(x, y));
        this.x = coords.x;
        this.y = coords.y;

        this.gridCoords = new GridVector((int)x / grid.getSquareSideLength(),
                (int) y / grid.getSquareSideLength());
        this.screen = screen;
    }

    /**
     * Returns the location of the grid clicked in <b>GRID COORDINATES</b>
     * @return Grid coordinate vector
     */
    public GridVector getGridLocation(){
        return gridCoords;
    }

    /**
     * Returns the location of the original click event on LevelScreen
     * @return Vector2 containing the click coords
     */
    public Vector2 getEventCoords(){
        return new Vector2(x, y);
    }

    /**
     * Returns the current Grid Square clicked or null if none existed.
     * @return GridSquare clicked or null
     */
    public GridSquare getGridSquare(){
        return grid.getGridSquares()[gridCoords.x][gridCoords.y]; // could be null
    }

    public void setSquare(GridSquare square){
        grid.addGridSquare(gridCoords.x, gridCoords.y, square);
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
        double squareX = grid.getSquareSideLength() * ((float)gridCoords.x + .5);
        double squareY = grid.getSquareSideLength() * ((float)gridCoords.y + .5);

        return (squareX + radius >= getFarmerLocation().x &&
                squareX - radius <= getFarmerLocation().x) &&
                (squareY + radius >= getFarmerLocation().y &&
                squareY - radius <= getFarmerLocation().y);
    }

    /**
     * Checks to see if there is room for an oversize gridSquare at the place clicked
     * @param width
     * @param height
     * @return
     */
    public boolean hasClearanceFor(int width, int height) {
        int startX = getGridLocation().x;
        int startY = getGridLocation().y;

        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + width; y++) {
                if (grid.getSquare(x, y) != null) return false;
            }
        }

        return true;
    }

    public LevelScreen getScreen() {
        return screen;
    }
}
