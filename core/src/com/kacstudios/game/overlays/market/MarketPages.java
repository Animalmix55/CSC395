package com.kacstudios.game.overlays.market;

import com.kacstudios.game.inventoryItems.CornPlantItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarketPages {
    public static final MarketPage CORN = new MarketPage(1, "Test", "test", new ArrayList<ShopItem>(Arrays.asList(
            new ShopItem(new CornPlantItem(), ShopItem.ItemAccessibility.Both, 100, 10)
    )));
}
