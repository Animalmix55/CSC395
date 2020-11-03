package com.kacstudios.game.grid;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.BufferUtils;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.GridClickEvent;

import java.nio.IntBuffer;
import java.util.ArrayList;

public class Grid extends Group {

    private Texture gridSquareBgLight;
    private Texture gridSquareBgDark;

    private ArrayList<Image> gridSquareImages;

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
        buildGridBackground(height, width);
        this.setHeight(height * squareSideLength);
        this.setWidth(width * squareSideLength);
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
            screen.getMainStage().addActor(image);
        }

        backgroundImage.dispose();
    }

    private void buildGridBackground(int height, int width){
        if(gridSquareImages != null){
            for (int i = 0; i < gridSquareImages.size(); i++){
                gridSquareImages.get(i).remove();
            }
        }
        gridSquareImages = new ArrayList<>();

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

        while (imageX < pixelWidth) {
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
                            if ((xIndexOffset + yIndex) % 2 == 0) container.drawPixmap(dark, x, y);
                            else container.drawPixmap(light, x, y);
                            yIndex++;
                        }
                        xIndexOffset++;
                    }
                    textureResult = new Texture(container);
                }
                Image image = new Image(textureResult);
                image.setPosition(imageX, imageY);

                this.addActor(image);
                gridSquareImages.add(image);

                imageY += imageHeight;
            }
            imageX += imageWidth;
        }

        dark.dispose();
        light.dispose();
    }
}
