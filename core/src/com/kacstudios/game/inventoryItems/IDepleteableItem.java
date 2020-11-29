package com.kacstudios.game.inventoryItems;

public abstract class IDepleteableItem extends IInventoryItem {
    private float depletionPercentage = 0.00f;

    public IDepleteableItem(int radius, boolean showHover, int squareWidth, int squareHeight) {
        super(radius, showHover, squareWidth, squareHeight);
    }

    public IDepleteableItem(int radius, boolean showHover) {
        super(radius, showHover);
    }

    public IDepleteableItem(boolean setHoverSizeToTarget, int radius) {
        super(true, 300);
    }

    public IDepleteableItem() {
        super();
    }

        /**
         * Gets how depleted the item is to display its depletion. 100% is completely depleted
         * @return the depletion percentage (0.00-1.00)
         */
    public float getDepletionPercentage(){
        return depletionPercentage;
    }

    /**
     * Sets the depletion percentage of a given item in the inventory.
     */
    public void setDepletionPercentage(float percentage){
        depletionPercentage = percentage;
    }
}
