package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.actors.Farmer.Farmer;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.grid.GridVector;
import com.kacstudios.game.grid.plants.BlueberriesPlant;
import com.kacstudios.game.grid.structures.BarnStructure;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.ShapeGenerator;

public class BarnItem extends IInventoryItem {
    private static Texture texture = new Texture("items/barn.png");
    private static Texture highlightBox = new Texture(ShapeGenerator.createRectangle(Grid.squareSideLength * 3,
            Grid.squareSideLength * 3, Color.WHITE));
    private Image hoverImage = new Image(highlightBox);
    private Color unsafeColor = new Color(1, 0, 0, .3f);
    private Color safeColor = new Color(0, 1, 0, .3f);

    private GridVector prevCoord = new GridVector(0, 0);

    public BarnItem(){
        this(1);
    }

    public BarnItem(int amount){
        setAmount(amount);
        setDisplayName("Barn");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {

        if(!event.farmerWithinRadius(300)) return; // must be within 300 pixels
        if(event.getGridSquare() == null) {

            if(!event.hasClearanceFor(3, 3)) return;

            event.setSquare(new BarnStructure());

            int amount = getAmount();
            amount--;
            setAmount(amount >= 0 ? amount : 0);

            if(amount == 0){ // amount of -1 signifies unlimited
                parent.setItem(null); // remove from button
                hoverImage.remove();
            }

            parent.checkItem(); // update button display amount
        }
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new BarnItem(amount);
    }

    @Override
    public void whileEquipped(Grid grid, Farmer farmer){
        Vector2 screenCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 localCoordinates = grid.screenToLocalCoordinates(screenCoordinates);
        GridVector coord = grid.getGridCoordinate(localCoordinates);

        float dst = farmer.localToScreenCoordinates(new Vector2(0, 0))
                .dst(grid.localToScreenCoordinates(new Vector2(((float)coord.x + .5f) * Grid.squareSideLength,
                        ((float)coord.y + .5f) * Grid.squareSideLength )));

        boolean closeToFarmer = dst <= 300;
        // only check again if the cursor moved into a new gridSquare
        if(coord.x != prevCoord.x || coord.y != prevCoord.y) {
            if (localCoordinates.x < 0 || coord.x >= grid.getGridWidth() || localCoordinates.y < 0 || coord.y >= grid.getGridHeight())
                hoverImage.setVisible(false);
            else if (!hoverImage.isVisible()) hoverImage.setVisible(true);

            if (hoverImage.isVisible()) {
                hoverImage.setPosition(Grid.squareSideLength * coord.x, Grid.squareSideLength * coord.y);
                hoverImage.setColor((grid.hasClearanceFor(coord.x, coord.y, 3, 3)) ? safeColor : unsafeColor);
            }

            prevCoord = coord;
        }

        if (!closeToFarmer) hoverImage.setVisible(false);
        else hoverImage.setVisible(true);
    }

    @Override
    public void onEquippedChange(boolean isEquipped, Grid grid) {
        if(isEquipped) grid.addActor(hoverImage);
        else hoverImage.remove();
    }
}
