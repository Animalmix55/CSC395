package com.kacstudios.game.utilities;

import com.badlogic.gdx.math.Vector2;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.GridVector;

public class GridClickEvent {
    private int x;
    private int y;
    private Grid grid;

    public GridClickEvent(int x, int y, Grid grid) {
        this.x = x;
        this.y = y;
        this.grid = grid;
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
}
