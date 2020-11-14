package com.kacstudios.game.overlays.market;

import com.kacstudios.game.inventoryItems.IInventoryItem;

public class ShopItem {
    public enum ItemAccessibility {
        CanBuy,
        CanSell,
        Both
    }

    private IInventoryItem item;
    private int buyPrice;
    private int sellPrice;

    private ItemAccessibility accessibility;

    /**
     * Wraps the item in a container that specifies how the item exists in a given shop page.
     *
     * @param itemInstance An instance of the item.
     * @param accessibility How the item will be accessed in the shop.
     * @param buyPrice The price of the item.
     * @param sellPrice The sell value of the item.
     */
    public ShopItem(IInventoryItem itemInstance, ItemAccessibility accessibility, int buyPrice, int sellPrice) {
        item = itemInstance;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.accessibility = accessibility;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    /**
     * Wraps the item in a container that specifies how the item exists in a given shop page.
     * If accessibility is set to both, will duplicate the buy and sell price. Otherwise, only the desired
     * option will be given that price.
     *
     * @param itemInstance An instance of the item.
     * @param accessibility How the item will be accessed in the shop.
     * @param price The cost/price (see above) of the item.
     */
    public ShopItem(IInventoryItem itemInstance, ItemAccessibility accessibility, int price) {
        this(itemInstance, accessibility, price, price);
    }

    /**
     * Creates a new item instance with the given amount
     * @param amount
     * @return item
     */
    public IInventoryItem createItemInstance(int amount) {
        return item.createNewInstance(amount);
    }

    /**
     * Returns the underlying item without creating any new objects.
     * @return
     */
    public IInventoryItem getWrappedItem() {
        return item;
    }

    public ItemAccessibility getAccessibility() {
        return accessibility;
    }

    public boolean isPurchasable() {
        return getAccessibility() == ItemAccessibility.Both || getAccessibility() == ItemAccessibility.CanBuy;
    }

    public boolean isSellable() {
        return getAccessibility() == ItemAccessibility.Both || getAccessibility() == ItemAccessibility.CanSell;
    }
}
