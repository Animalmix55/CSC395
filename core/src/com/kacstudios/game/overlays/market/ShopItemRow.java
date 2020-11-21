package com.kacstudios.game.overlays.market;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.Economy;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

import java.util.ArrayList;

/**
 * A single row typically used within a MarketPage which provides an interface for wrapping a given ShopItem.
 *
 * Generates a label, a quantity button, and a purchase/sell button. Arranged horizontally.
 */
public class ShopItemRow extends Group {
    ShopItem item;
    private static Label.LabelStyle itemNameStyle =
            new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 20), Color.WHITE);
    ArrayList<PriceBox> priceBoxes = new ArrayList<>();
    ArrayList<QuantityBox> quantityBoxes = new ArrayList<>();
    private MarketPage parent;
    private LevelScreen screen;

    public ShopItemRow(ShopItem item, MarketPage parent) {
        int padding = parent.getParent().getPadding();
        setHeight(PriceBox.height);
        setWidth(parent.getWidth());
        this.parent = parent;
        screen = parent.getParent().getScreen();
        this.item = item;

        ArrayList<Group> rows = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            if (!(i == 0 && item.isPurchasable()) && !(i == 1 && item.isSellable())) continue;
            Group container = new Group();

            Label itemNameLabel = new Label(String.format(i == 0? "Buy %ss:" : "Sell %ss:", item.getWrappedItem().getDisplayName()), itemNameStyle);
            itemNameLabel.setPosition(padding,
                    (getHeight() - itemNameLabel.getHeight()) / 2);
            container.addActor(itemNameLabel);

            // quantity stuffs
            int price = i == 0? item.getBuyPrice() : item.getSellPrice();
            PriceBox priceBox = new PriceBox(price, i == 0? PriceBox.PriceBoxType.Buy : PriceBox.PriceBoxType.Sell);

            int finalI1 = i;
            QuantityBox quantityBox = new QuantityBox() {
                @Override
                public void onQuantityChange(int quantity) {
                    priceBox.updatePrice(price * quantity);
                    if(finalI1 == 1) {
                        if (this.getQuantity() >
                                screen.getHud().getInventoryViewer().getAmount(item.getWrappedItem().getClass())) {
                            priceBox.setDisabled(true);
                        }
                        else priceBox.setDisabled(false);
                    }
                }
            };

            quantityBoxes.add(quantityBox);

            priceBoxes.add(priceBox);

            int finalI = i;
            priceBox.addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(finalI == 0) onBuy(quantityBox.getQuantity());
                    else onSell(quantityBox.getQuantity());
                }
            });

            quantityBox.setPosition(itemNameLabel.getX() + itemNameLabel.getWidth() + padding, 0);
            container.addActor(quantityBox);
            // end quantity stuffs

            // site price position
            priceBox.setPosition(quantityBox.getRight() + padding, 0);
            container.addActor(priceBox);

            container.setWidth(priceBox.getRight() + padding);
            container.setHeight(priceBox.getHeight());

            container.setX(i == 0? 0 : getWidth() - container.getWidth());
            rows.add(container);
            addActor(container);
        }
    }

    public void onBuy(int quantity) {
        if(Economy.removeMoney(quantity * item.getBuyPrice())) {
            if(!screen.getHud().getInventoryViewer().addItem(item.createItemInstance(quantity)))
                Economy.addMoney(quantity * item.getBuyPrice()); // return money if add fails
        }
    }

    public void onSell(int quantity) {
        if(screen.getHud().getInventoryViewer().removeItem(item.getWrappedItem().getClass(), quantity)) {
            Economy.addMoney(quantity * item.getSellPrice());
        }
    }

    /**
     * To be called when the economy updates
     */
    public void economyUpdated() {
        priceBoxes.forEach(b -> b.economyUpdated());
    }

    /**
     * To be called when the economy updates
     */
    public void inventoryUpdated() {
        int count = quantityBoxes.size();
        for (int i = 0; i < count; i++) {
            if (priceBoxes.get(i).getType() == PriceBox.PriceBoxType.Sell) {
                if(quantityBoxes.get(i).getQuantity() >
                        screen.getHud().getInventoryViewer().getAmount(item.getWrappedItem().getClass())) {
                    priceBoxes.get(i).setDisabled(true);
                }
                else priceBoxes.get(i).setDisabled(false);
            }
        }
    }
}
