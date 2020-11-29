package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.actors.Farmer.Farmer;
import com.kacstudios.game.grid.Grid;
import com.kacstudios.game.grid.GridSquare;
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
        super(300, true, 3, 3);
        setAmount(amount);
        setDisplayName("Barn");
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        event.setSquare(new BarnStructure());

        int amount = getAmount();
        amount--;
        setAmount(Math.max(amount, 0));

        if(amount == 0){ // amount of -1 signifies unlimited
            parent.setItem(null); // remove from button
            hoverImage.remove();
        }

        parent.checkItem(); // update button display amount
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
    protected boolean isBlocked(GridClickEvent event) {
        GridSquare target = event.getGridSquare();
        return target != null || !event.getScreen().getGrid()
                .hasClearanceFor(event.getGridLocation().x, event.getGridLocation().y, getGridSquareWidth(), getGridSquareHeight());
    }
}
