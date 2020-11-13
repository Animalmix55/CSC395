package com.kacstudios.game.overlays.market;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class MarketPage {
    private Texture icon;
    private String title;
    private String description;
    private ArrayList<ShopItem> items = new ArrayList<>();

    public MarketPage(Texture icon, String title,
                      String description, ArrayList<ShopItem> items) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.items = items;
    }

    public MarketPage(int iconIndex, String title,
                      String description, ArrayList<ShopItem> items) {
        this.icon = items.get(iconIndex).getWrappedItem().getTexture();
        this.title = title;
        this.description = description;
        this.items = items;
    }

    public Texture getIcon() {
        return icon;
    }

    public ArrayList<ShopItem> getItems() {
        return items;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
