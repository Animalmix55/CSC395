package com.kacstudios.game.grid;

public class OversizeGridSquare extends GridSquare {
    private int width;
    private int height;

    public OversizeGridSquare(int width, int height) {
        super();
        this.width = width;
        this.height = height;

        setHeight(Grid.squareSideLength * height);
        setWidth(Grid.squareSideLength * height);
    }

    public int getGridSquareWidth() {
        return width;
    }
    public int getGridSquareHeight() {
        return height;
    }
}
