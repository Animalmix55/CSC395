package com.kacstudios.game.grid;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

import java.util.ArrayList;

public class Grid extends Group {

    private Texture gridSquareBgLight;
    private Texture gridSquareBgDark;

    private GridSquare[][] gridSquares;
    private LevelScreen screen;
    private final Integer squareSideLength = 135;
    private ArrayList<Image> outOfBoundsArea;
    private int width;
    private static final Texture backgroundTexture = new Texture("grass-outofbounds_1080x1080.png");
    private int height;

    public Grid(int height, int width, LevelScreen levelScreen){
        gridSquareBgDark = new Texture("grid-grass-dark.png");
        gridSquareBgLight = new Texture("grid-grass-light.png");

        this.height = height;
        this.width = width;

        gridSquareBgLight.getTextureData().prepare();
        gridSquareBgDark.getTextureData().prepare();

        outOfBoundsArea = new ArrayList<>();
        Actor background = new Image(buildGridBackground(height, width));
        this.addActor(background);
        this.setHeight(background.getHeight());
        this.setWidth(background.getWidth());
        BaseActor.setWorldBounds(this.getWidth(), this.getHeight());

        this.screen = levelScreen;
        setStage(levelScreen.getMainStage());

        levelScreen.getUIStage().addActor(this);
        gridSquares = new GridSquare[width][height];

        this.addCaptureListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createGridEvent(x, y);
            }
        });
        createOutOfBoundsArea();
    }

    private void createGridEvent(float x, float y){
        GridClickEvent gridSquareItemEvent = new GridClickEvent(x, y, this.screen);
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

    /**
     * Creates an imagine actor for the specific location requested for a boundary background,
     * does not add it to the screen.
     * @param x
     * @param y
     * @return
     */
    private Image createBoundaryBackground(float x, float y){
        Image temp = new Image(backgroundTexture);
        temp.setPosition(x, y);
        return temp;
    }

    private void createOutOfBoundsArea(){
        if(outOfBoundsArea != null){
            for (int i = 0; i < outOfBoundsArea.size(); i++){
                outOfBoundsArea.get(i).remove();
            }
        }

        float textureHeight = backgroundTexture.getHeight();
        float textureWidth = backgroundTexture.getWidth();

        outOfBoundsArea = new ArrayList<>();
        float height = getHeight();
        float width = getWidth();

        // build corners
        outOfBoundsArea.add(createBoundaryBackground(-textureWidth, height));
        outOfBoundsArea.add(createBoundaryBackground(-textureWidth, -textureHeight));
        outOfBoundsArea.add(createBoundaryBackground(width, -textureHeight));
        outOfBoundsArea.add(createBoundaryBackground(width, height));

        // generate side texture
        backgroundTexture.getTextureData().prepare();
        Pixmap backgroundImage = backgroundTexture.getTextureData().consumePixmap();

        Pixmap container = new Pixmap((int)textureWidth, (int)height, Pixmap.Format.RGB888);
        for(int y = 0; y < height; y = y + (int)textureHeight){
            container.drawPixmap(backgroundImage, 0, y);
        }

        Texture sideTexture = new Texture(container);
        Image leftImage = new Image(sideTexture);
        Image rightImage = new Image(sideTexture);
        leftImage.setPosition(width, 0);
        rightImage.setPosition(-textureWidth, 0);

        outOfBoundsArea.add(leftImage);
        outOfBoundsArea.add(rightImage);

        // end generating side texture

        // generate top/bottom texture

        Pixmap container2 = new Pixmap((int)width, (int)textureHeight, Pixmap.Format.RGB888);
        for(int x = 0; x < width; x = x + (int)textureWidth){
            container2.drawPixmap(backgroundImage, x, 0);
        }

        Texture topBottomTexture = new Texture(container2);
        Image topImage = new Image(topBottomTexture);
        Image bottomImage = new Image(topBottomTexture);
        topImage.setPosition(0, height);
        bottomImage.setPosition(0, -textureHeight);

        outOfBoundsArea.add(topImage);
        outOfBoundsArea.add(bottomImage);

        // end generate top/bottom texture
        for (Image image: outOfBoundsArea){
            screen.getMainStage().addActor(image);
        }

        backgroundImage.dispose();
    }

    private Texture buildGridBackground(int height, int width){
        Pixmap dark = gridSquareBgDark.getTextureData().consumePixmap();
        Pixmap light = gridSquareBgLight.getTextureData().consumePixmap();

        Pixmap container = new Pixmap(width * squareSideLength, height * squareSideLength, Pixmap.Format.RGBA8888);

        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++) {
                if((x + y) % 2 == 0) container.drawPixmap(dark, x * squareSideLength, y * squareSideLength);
                else container.drawPixmap(light, x * squareSideLength, y * squareSideLength);
            }
        }
        Texture textureResult = new Texture(container);

        dark.dispose();
        light.dispose();

        return textureResult;
    }
}
