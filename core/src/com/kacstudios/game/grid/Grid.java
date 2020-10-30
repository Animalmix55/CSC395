package com.kacstudios.game.grid;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.GridClickEvent;

public class Grid extends Group {
    private GridSquare[][] gridSquares;
    private LevelScreen screen;
    private Integer squareSideLength = 135;
    private int width = 8;
    private int height = 8;

    public Grid(LevelScreen levelScreen){
        Actor background = new Image(new Texture("grass_1080x1080.png"));
        this.addActor(background);
        this.setHeight(background.getHeight());
        this.setWidth(background.getWidth());
        BaseActor.setWorldBounds(this.getHeight(), this.getWidth());

        screen = levelScreen;
        setStage(levelScreen.getMainStage());

        levelScreen.getUIStage().addActor(this);
        gridSquares = new GridSquare[width][height];

        this.addCaptureListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createGridEvent(x, y);
            }
        });
    }

    private void createGridEvent(float x, float y){
        System.out.println(x + " " + y);
        GridVector coords = new GridVector((int)x / squareSideLength, (int) y / squareSideLength);
        GridClickEvent gridSquareItemEvent = new GridClickEvent(coords.x, coords.y, this.screen);
        screen.handleGridClickEvent(gridSquareItemEvent); // pass the event to the screen
    }

    /**
     * Completes physics calculations regarding grid squares.
     * @param dt deltaTime (seconds)
     */
    @Override
    public void act(float dt){
        super.act(dt);
        for(int x = 0; x < gridSquares.length; x++) {
            for (int y = 0; y < gridSquares[x].length; y++) {
                if(gridSquares[x][y] == null) continue;

                if (gridSquares[x][y].getCollisionSetting()) {
                    screen.getFarmer().preventOverlap(gridSquares[x][y]);
                }
            }
        }
    }

    /**
     * Adds a grid square to the game in the specified <b>GRID COORDINATE</b>. This does not use pixels.
     * Grid coordinates are measured starting from the bottom left of the grid (0,0).
     * @param x the x coordinate of the grid square
     * @param y the y coordinate of the grid square
     * @param square the square to add.
     */
    public void addGridSquare(int x, int y, GridSquare square){
        square.setX(squareSideLength * x);
        square.setY(squareSideLength * y);
        if(gridSquares[x][y] != null) gridSquares[x][y].remove(); // remove old square
        gridSquares[x][y] = square;
        this.addActor(square); // register gridSquare
    }

    /**
     * Removes a grid square at a certain grid coordinate
     * @param x the x coordinate of the grid square
     * @param y the y coordinate of the grid square
     */
    public void removeGridSquare(int x, int y){
        if(gridSquares[x][y] != null){
            gridSquares[x][y].remove(); // remove old square
            gridSquares[x][y] = null;
        }
    }

    public GridSquare[][] getGridSquares(){
        return gridSquares;
    }

    public GridVector getGridCoordinate(Vector2 screenCoordinate){
        return new GridVector((int)screenCoordinate.x / width, (int)screenCoordinate.y / height);
    }
    public GridVector getGridCoordinate(Vector3 screenCoordinate){
        return new GridVector((int)screenCoordinate.x / width, (int)screenCoordinate.y / height);
    }

    public Integer getSquareSideLength() {
        return squareSideLength;
    }
}
