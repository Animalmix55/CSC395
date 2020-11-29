package com.kacstudios.game.inventoryItems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.overlays.ContextMenu.ContextMenu;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.GridClickEvent;

public class DeleteItem extends IInventoryItem{
    private static Texture texture = new Texture("items/shovel.png");

    public DeleteItem(int amount){
        super(true, 300);

        setAmount(amount);
        setDisplayName("Shovel");
        setDeletable(false);
    }

    @Override
    public void onDeployment(GridClickEvent event, ItemButton parent) {
        Vector2 coords = event.getGridSquare().getStage().screenToStageCoordinates(event.getEventCoords());
        GridSquare square = event.getGridSquare();

        ContextMenu menu = new ContextMenu((int) coords.x, (int) coords.y, new ContextMenu.ContextMenuOption[] {
                new ContextMenu.ContextMenuOption("Delete",
                        () -> { if(event.farmerWithinRadius(300)) event.setSquare(null); })
        }) {
            @Override
            public void setOpen(boolean isOpen) {
                super.setOpen(isOpen);
                if(!isOpen) {
                    if(event.getGridSquare() != null) event.getGridSquare().setColor(Color.WHITE);
                    remove();
                }
            }
        };
        square.setColor(new Color(255, 255, 255, .4f));

        menu.setPosition(coords.x - menu.getWidth()/2, coords.y - menu.getHeight()/2);

        menu.setOpen(true);

        square.getStage().addActor(menu);

        parent.checkItem();
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public IInventoryItem createNewInstance(int amount) {
        return new DeleteItem(amount);
    }

    protected boolean isBlocked(GridClickEvent event) {
        GridSquare target = event.getGridSquare();
        return target == null || (Plant.class.isAssignableFrom(target.getClass()) && ((Plant) target).getDisaster() != null);
    }
}
