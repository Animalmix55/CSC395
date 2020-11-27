package com.kacstudios.game.grid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.BufferUtils;
import com.kacstudios.game.actors.BaseActor;

import com.kacstudios.game.disasters.FireDisaster;
import com.kacstudios.game.disasters.InsectDisaster;
import com.kacstudios.game.grid.plants.BlueberriesPlant;
import com.kacstudios.game.grid.plants.CornPlant;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.TimeEngine;
import com.kacstudios.game.actors.PlayableActor;
import com.kacstudios.game.utilities.ShapeGenerator;

import java.nio.IntBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Grid extends Group {

    private Texture gridSquareBgLight;
    private Texture gridSquareBgDark;

    private DisasterSpawner spawner;

    private ArrayList<ArrayList<Image>> gridSquareImages;

    private GridSquare[][] gridSquares;
    private LevelScreen screen;
    private final Integer squareSideLength = 135;
    private ArrayList<Image> outOfBoundsArea;
    private int width;
    private static final Texture backgroundTexture = new Texture("grass-outofbounds_1080x1080.png");
    private int height;
    private LocalDateTime timeSinceDisaster;
    private boolean isDisasterTime = false;
    private boolean isTopLeftDark = true;

    public Grid(int height, int width, LevelScreen levelScreen){
        gridSquareBgDark = new Texture("grid-grass-dark.png");
        gridSquareBgLight = new Texture("grid-grass-light.png");

        this.screen = levelScreen;
        setStage(levelScreen.getMainStage());

        buildGrid(height, width, isTopLeftDark);

        timeSinceDisaster = TimeEngine.getDateTime();

        this.addCaptureListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createGridEvent(x, y);
            }
        });
        levelScreen.getUIStage().addActor(this);

        // register disasters with the spawner
        spawner = new DisasterSpawner(this);
        spawner.registerDisaster(FireDisaster::new, 500);
        spawner.registerDisaster(InsectDisaster::new, 150);
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
        spawner.act();

        for(int x = 0; x < gridSquares.length; x++) {
            for (int y = 0; y < gridSquares[x].length; y++) {
                if(gridSquares[x][y] == null) continue;

                if (gridSquares[x][y].getCollisionSetting()) {
                    screen.getFarmer().preventOverlap(gridSquares[x][y]);
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Camera camera = getStage().getCamera();
        float cameraHeight = camera.viewportHeight;
        float cameraWidth = camera.viewportWidth;
        float cameraX = camera.position.x - cameraWidth/2;
        float cameraY = camera.position.y - cameraHeight/2;

        boolean columnPlaced = false;
        for(int xIndex = 0; xIndex < gridSquareImages.size(); xIndex++){
            Image currentSquare = gridSquareImages.get(xIndex).get(0);
            float x = currentSquare.getX();
            float y = currentSquare.getY();
            float height = currentSquare.getHeight();
            float width = currentSquare.getWidth();
            Rectangle cameraRectangle = new Rectangle(cameraX, cameraY, cameraWidth, cameraHeight);
            Rectangle imageRectangle = new Rectangle(x, 0, width, getHeight());

            if(!cameraRectangle.overlaps(imageRectangle)) continue; // dont try if the space isn't promising

            int columnHeight = gridSquareImages.get(xIndex).size();
            int yIndex = 0;
            boolean imagesPlaced = false;
            do {
                imageRectangle = new Rectangle(x, y, width, height);
                if(cameraRectangle.overlaps(imageRectangle)) {
                    currentSquare.draw(batch, parentAlpha); // render
                    imagesPlaced = true;
                }

                yIndex++;
                if(yIndex >= columnHeight) break; // dont go outside bounds
                currentSquare = gridSquareImages.get(xIndex).get(yIndex);
                x = currentSquare.getX();
                y = currentSquare.getY();
                height = currentSquare.getHeight();
                width = currentSquare.getWidth();
            } while (true);

            if(imagesPlaced) columnPlaced = true;
            else if (columnPlaced) break; // don't bother checking further right...
        }
        super.draw(batch, parentAlpha);
    }

    public int getGridWidth() {
        return width;
    }

    public int getGridHeight() {
        return height;
    }

    /**
     * Adds a grid square to the game in the specified <b>GRID COORDINATE</b>. This does not use pixels.
     * Grid coordinates are measured starting from the bottom left of the grid (0,0).
     * @param x the x coordinate of the grid square
     * @param y the y coordinate of the grid square
     * @param square the square to add.
     */
    public void addGridSquare(int x, int y, GridSquare square){
        if(gridSquares[x][y] != null) gridSquares[x][y].remove(); // remove old square
        gridSquares[x][y] = square;

        if(square != null) {
            square.setX(squareSideLength * x);
            square.setY(squareSideLength * y);
            this.addActor(square); // register gridSquare
            square.setParent(this, new GridVector(x, y));
        }
    }

    /**
     * Returns the grid square at a specific location in the grid
     * @param x the zero-based x coordinate of the square in the grid
     * @param y the zero-based y coordinate of the square in the grid
     * @return the GridSquare or null if there is no square in that location.
     */
    public GridSquare getSquare(int x, int y){
        if(x >= width || y >= height) return null; // don't access an illegal position
        return gridSquares[x][y];
    }

    public GridSquare getSquare(GridVector position){
        return getSquare(position.x, position.y);
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

        IntBuffer intBuffer = BufferUtils.newIntBuffer(16);
        Gdx.gl20.glGetIntegerv(Gdx.gl20.GL_MAX_TEXTURE_SIZE, intBuffer);
        int maxTextureSize = intBuffer.get();

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

        int imageY = 0;
        Texture sideTexture = null;
        while (imageY < height) {
            int imageHeight = (height - imageY) > maxTextureSize? maxTextureSize : ((int) height - imageY);

            if(sideTexture == null || imageHeight != sideTexture.getHeight()) {
                Pixmap container = new Pixmap((int) textureWidth, imageHeight, Pixmap.Format.RGB888);
                for (int y = 0; y < imageHeight; y = y + (int) textureHeight) {
                    container.drawPixmap(backgroundImage, 0, y);
                }

                sideTexture = new Texture(container);
            }
            Image leftImage = new Image(sideTexture);
            Image rightImage = new Image(sideTexture);
            leftImage.setPosition(width, imageY);
            rightImage.setPosition(-textureWidth, imageY);

            imageY += imageHeight; // increment height
            outOfBoundsArea.add(leftImage);
            outOfBoundsArea.add(rightImage);
        }

        // end generating side texture

        // generate top/bottom texture

        int imageX = 0;
        Texture topBottomTexture = null;

        while (imageX < width) {
            int imageWidth = (width - imageX) > maxTextureSize? maxTextureSize : ((int) width - imageX);

            if(topBottomTexture == null || imageWidth != sideTexture.getWidth()) {
                Pixmap container2 = new Pixmap(imageWidth, (int) textureHeight, Pixmap.Format.RGB888);
                for (int x = 0; x < imageWidth; x = x + (int) textureWidth) {
                    container2.drawPixmap(backgroundImage, x, 0);
                }

                topBottomTexture = new Texture(container2);
            }
            Image topImage = new Image(topBottomTexture);
            Image bottomImage = new Image(topBottomTexture);
            topImage.setPosition(imageX, height);
            bottomImage.setPosition(imageX, -textureHeight);

            imageX += imageWidth;

            outOfBoundsArea.add(topImage);
            outOfBoundsArea.add(bottomImage);
        }

        // end generate top/bottom texture
        for (Image image: outOfBoundsArea){
            addActor(image);
        }

        backgroundImage.dispose();
    }

    private void buildGridBackground(int height, int width, boolean isTopLeftDark){
        gridSquareImages = new ArrayList<>();

        gridSquareBgLight.getTextureData().prepare();
        gridSquareBgDark.getTextureData().prepare();
        Pixmap dark = gridSquareBgDark.getTextureData().consumePixmap();
        Pixmap light = gridSquareBgLight.getTextureData().consumePixmap();

        int imageX = 0;

        int pixelHeight = height * squareSideLength;
        int pixelWidth = width * squareSideLength;

        IntBuffer intBuffer = BufferUtils.newIntBuffer(16);
        Gdx.gl20.glGetIntegerv(Gdx.gl20.GL_MAX_TEXTURE_SIZE, intBuffer);
        int maxTextureSize = intBuffer.get() / 4; // don't go overboard
        int xIndexOffset = 0;
        Texture textureResult = null; // holds the images temporarily

        int imageGridX = 0;
        while (imageX < pixelWidth) {
            gridSquareImages.add(new ArrayList<>());
            int imageWidth;

            imageWidth = (width * squareSideLength - imageX) < maxTextureSize? (width * squareSideLength - imageX) :
                    (maxTextureSize / (squareSideLength * 2)) * (squareSideLength * 2); // round

            int imageY = 0;
            int yIndexOffset = 0;
            while (imageY < pixelHeight) {
                int imageHeight = (height * squareSideLength - imageY) < maxTextureSize ? (height * squareSideLength - imageY) :
                        (maxTextureSize / (squareSideLength * 2)) * (squareSideLength * 2); // round
                if(textureResult == null || textureResult.getHeight() != imageHeight || textureResult.getWidth() != imageWidth){
                    Pixmap container = new Pixmap(imageWidth, imageHeight, Pixmap.Format.RGBA8888);

                    for (int x = 0; x < imageWidth; x = x + squareSideLength) {
                        int yIndex = yIndexOffset;
                        for (int y = 0; y < imageHeight; y = y + squareSideLength) {
                            if ((xIndexOffset + yIndex + (isTopLeftDark? 1 : 0)) % 2 == 0) container.drawPixmap(dark, x, y);
                            else container.drawPixmap(light, x, y);
                            yIndex++;
                        }
                        xIndexOffset++;
                    }
                    textureResult = new Texture(container);
                }
                Image image = new Image(textureResult);
                image.setPosition(imageX, imageY);
                gridSquareImages.get(imageGridX).add(image);

                imageY += imageHeight;
            }
            imageGridX++;
            imageX += imageWidth;
        }

        dark.dispose();
        light.dispose();
    }

    public void expandGrid(ShapeGenerator.Direction direction) {
        int newHeight = getGridHeight();
        int newWidth = getGridWidth();
        GridSquare[][] oldGrid = gridSquares;

        if(direction == ShapeGenerator.Direction.up || direction == ShapeGenerator.Direction.left)
            isTopLeftDark = !isTopLeftDark; // keep weird motion effect from occuring

        switch (direction) {
            case up:
            case down:
                newHeight++;
                break;
            case right:
            case left:
                newWidth++;
                break;
        }
        buildGrid(newHeight, newWidth, isTopLeftDark);

        int xOffset = 0;
        int yOffset = 0;
        switch (direction) {
            case down:
                screen.getFarmer().setY(screen.getFarmer().getY() + squareSideLength); // shift farmer
                yOffset++;
                break;
            case left:
                screen.getFarmer().setX(screen.getFarmer().getX() + squareSideLength); // shift farmer
                xOffset++;
                break;
        }

        // rebuild array and reposition squares
        for (int x = 0; x < oldGrid.length; x++) {
            for (int y = 0; y < oldGrid[x].length; y++) {
                GridSquare actor = oldGrid[x][y];
                addGridSquare(x + xOffset, y + yOffset, actor);
            }
        }

        // move any non-squares on map
        if(xOffset != 0 || yOffset != 0) {
            List<Actor> addedActors = screen.getAddedActors();
            for (Actor addedActor : addedActors) {
                addedActor.setPosition(addedActor.getX() + squareSideLength * xOffset,
                        addedActor.getY() + squareSideLength * yOffset);
            }
        }
    }

    public void buildGrid(int height, int width, boolean isTopLeftDark) {
        clearChildren();
        outOfBoundsArea = new ArrayList<>();
        gridSquares = new GridSquare[width][height]; // wipes the contents of grid
        this.height = height;
        this.width = width;

        setHeight(height * squareSideLength);
        setWidth(width * squareSideLength);

        buildGridBackground(height, width, isTopLeftDark);
        BaseActor.setWorldBounds(getWidth(), getHeight());

        createOutOfBoundsArea();
    }
}
