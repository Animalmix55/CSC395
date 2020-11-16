package com.kacstudios.game.inventoryItems;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.GridClickEvent;

public abstract class IInventoryItem {
    private Integer amount = 1;
    private String displayName = null;
    private String description = null;

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
    public abstract void onDeployment(GridClickEvent event, ItemButton parent);

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
}
