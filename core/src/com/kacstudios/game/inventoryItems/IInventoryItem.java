package com.kacstudios.game.inventoryItems;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.actors.Farmer.Farmer;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.GridVector;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.ShapeGenerator;

public abstract class IInventoryItem {
    private Integer amount = 1;
    private String displayName = null;
    private String description = null;
    private int radius = 300;
    private boolean isDeletable = true;
    private GridVector prevCoord = new GridVector(-1, -1);
    private Vector2 prevFarmerCoord = new Vector2();
    protected Image hoverImage;
    private int gridWidth = 1;
    private int gridHeight = 1;
    private boolean matchHoverToTarget = false;

    private static final Color unsafeColor = new Color(1, 0, 0, .3f);
    private static final Color safeColor = new Color(0, 1, 0, .3f);

    /**
     * @param radius
     * @param isMatchHoverSizeToTarget if the hover box should be 1x1 or match the size to the target
     */
    private IInventoryItem(boolean isMatchHoverSizeToTarget, int radius) {
        this(radius, true, 1, 1);
        this.matchHoverToTarget = isMatchHoverSizeToTarget;
    }

    /**
     * For items that don't place gridsquares or place a 1x1
     * @param radius
     * @param showHover
     */
    public IInventoryItem(int radius, boolean showHover) {
        this(radius, showHover, 1, 1);
    }


    /**
     * For items that need to specify a grid square size greater than 1x1
     * @param radius
     * @param showHover
     * @param squareWidth
     * @param squareHeight
     */
    public IInventoryItem(int radius, boolean showHover, int squareWidth, int squareHeight) {
        if(showHover) {
            hoverImage = new Image(new Texture(ShapeGenerator.createRectangle(squareWidth * Grid.squareSideLength,
                    squareHeight * Grid.squareSideLength, Color.WHITE)));
            hoverImage.setColor(Color.CLEAR);
        }
        this.radius = radius;

        gridWidth = squareWidth;
        gridHeight = squareHeight;
    }

    public IInventoryItem() {
        // pass
    }

    /**
     * To be called when the item is deployed
     * @param event
     * @param parent
     */
    public void deploy(GridClickEvent event, ItemButton parent) {
        if (event.farmerWithinRadius(radius) && !isBlocked(event)) {
            onDeployment(event, parent);
            prevCoord.x = -1;
            prevCoord.y = -1;
        }
    }

    /**
     * A function to be called when a given inventory item is deployed onto the map, not necessarily in a  (when the object is
     * equipped and the map is clicked), the deployer will pass in coordinates
     * and the level onto which the object is being deployed.
     *
     * NOTE: The button is not constantly checking the item for changes, be aware of this and inform the button
     * if the item changes with the appropriate update methods.
     *
     * @param event the grid click event that signifies what grid square was selected
     */
    protected abstract void onDeployment(GridClickEvent event, ItemButton parent);

    protected void setDescription(String description) {
        this.description = description;
    }

    protected void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the sprite for a specific item's icon.
     * @return an icon sprite (can be animated)
     */
    public abstract Texture getTexture();

    /**
     * Returns the display name of a given item (hose, for example)
     * @return display name
     */
    public String getDisplayName(){
        return displayName;
    }

    /**
     * Returns the description of a given inventory item
     * @return the description of the item
     */
    public String getDescription(){
        return description;
    }

    /**
     * Returns the amount of the item in the users' inventory.
     * @return an integer representing the amount.
     */
    public Integer getAmount(){
        return amount;
    }

    /**
     * Returns the amount of a given item in the group.
     * @param amount
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public abstract IInventoryItem createNewInstance(int amount);

    protected void setDeletable(boolean deletable) {
        isDeletable = deletable;
    }

    public boolean isDeletable() {
        return isDeletable;
    }

    /**
     * To be called every frame when selected, can draw on grid
     */
    public void whileEquipped(LevelScreen screen) {
        Grid grid = screen.getGrid();
        if(hoverImage != null) {
            Vector2 screenCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 localCoordinates = grid.screenToLocalCoordinates(screenCoordinates);
            GridVector coord = grid.getGridCoordinate(localCoordinates);
            GridClickEvent gridEvent = new GridClickEvent(localCoordinates.x, localCoordinates.y, screen);
            Vector2 farmerLocation = gridEvent.getFarmerLocation();

            boolean closeToFarmer = gridEvent.farmerWithinRadius(radius);

            if(prevCoord.x != localCoordinates.x || prevCoord.y != localCoordinates.y || !farmerLocation.equals(prevFarmerCoord)) {
                if (closeToFarmer) {
                    if (localCoordinates.x < 0 || coord.x >= grid.getGridWidth() || localCoordinates.y < 0 || coord.y >= grid.getGridHeight())
                        hoverImage.setVisible(false);
                    else if (!hoverImage.isVisible()) {
                        hoverImage.setVisible(true);
                    }

                    if (hoverImage.isVisible()) {
                        hoverImage.setPosition(Grid.squareSideLength * coord.x, Grid.squareSideLength * coord.y);
                        hoverImage.setColor(!isBlocked(gridEvent) ? safeColor : unsafeColor);
                    }
                    prevCoord = coord;
                } else hoverImage.setVisible(false);
                prevFarmerCoord = gridEvent.getFarmerLocation();
            }
        }
    }

    /**
     * Implement to define when the action should be blocked
     * @param event
     * @return
     */
    protected boolean isBlocked(GridClickEvent event) {
        return false;
    }

    /**
     * To be called when the item is equipped and unequipped
     * @param isEquipped
     */
    public void onEquippedChange(boolean isEquipped, Grid grid) {
        if (hoverImage != null) {
            if (isEquipped) grid.addActor(hoverImage);
            else hoverImage.remove();
        }
    }

    public int getGridSquareHeight() {
        return gridHeight;
    }

    public int getGridSquareWidth() {
        return gridWidth;
    }
}
