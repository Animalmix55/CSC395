package com.kacstudios.game.inventoryItems;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.GridClickEvent;

public abstract class IInventoryItem {
    public static class Amount {
        static int Unlimited = -1;
    }
    private Integer amount = Amount.Unlimited;
    private String displayName = null;
    private String description = null;
    private String texturePath = null;

    /**
     * A function to be called when a given inventory item is deployed onto the map, not necessarily in a  (when the object is
     * equipped and the map is clicked), the deloyer will pass in coordinates
     * and the level onto which the object is being deployed.
     * @param event the grid click event that signifies what grid square was selected
     */
    public abstract void onDeployment(GridClickEvent event);

    /**
     * Returns the sprite for a specific item's icon.
     * @return an icon sprite (can be animated)
     */
    public String getTexturePath(){
        return texturePath;
    }

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

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setTexturePath(String path){
        this.texturePath = path;
    }
}
