package com.kacstudios.game.overlays.market;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kacstudios.game.actors.ScrollableGroup;
import com.kacstudios.game.inventoryItems.BasicTractorItem;
import com.kacstudios.game.inventoryItems.BlueberriesPlantItem;
import com.kacstudios.game.inventoryItems.CornPlantItem;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

public class MarketPage extends Group {
    private ScrollableGroup scrollableGroup = new ScrollableGroup();
    private MarketPage.Data pageData;
    private Market parent;
    private int padding;
    private static int headerHeight = 30;
    private static Label backButton = null;
    private static Texture greenBox = null;
    private static Texture redBox = null;
    private static Texture circleTexture = null;
    private static Label.LabelStyle itemNameStyle =
            new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 20), Color.WHITE);

    public class ShopItemRow extends Group {
        public Label sellQuantity;

        public ShopItemRow(ShopItem item) {
            setHeight(greenBox.getHeight());
            setWidth(MarketPage.this.getWidth());

            float yTop = getHeight();
            if(item.isPurchasable()) {
                // price stuffs
                Image buyPriceBox = new Image(redBox);
                buyPriceBox.setPosition(getWidth() - padding - buyPriceBox.getWidth(), yTop - buyPriceBox.getHeight());
                addActor(buyPriceBox);

                Label priceLabel = new Label(String.format("$%d", item.getBuyPrice()), itemNameStyle);
                priceLabel.setPosition(buyPriceBox.getX(), buyPriceBox.getY());
                priceLabel.setHeight(buyPriceBox.getHeight());
                priceLabel.setWidth(buyPriceBox.getWidth());
                priceLabel.setAlignment(Align.center);
                addActor(priceLabel);
                // end price stuffs

                // quantity stuffs
                QuantityBox quantityBox = new QuantityBox() {
                    @Override
                    public void onQuantityChange(int quantity) {
                        priceLabel.setText(String.format("$%d", item.getBuyPrice() * quantity));
                    }
                };
                quantityBox.setPosition(buyPriceBox.getX() - padding - quantityBox.getWidth(), buyPriceBox.getY());
                addActor(quantityBox);
                // end quantity stuffs

                Label itemNameLabel = new Label(String.format("Buy %ss:", item.getWrappedItem().getDisplayName()), itemNameStyle);
                itemNameLabel.setPosition(quantityBox.getX() - padding - itemNameLabel.getWidth(),
                        yTop - quantityBox.getHeight() + (quantityBox.getHeight() - itemNameLabel.getHeight())/2);
                addActor(itemNameLabel);

                yTop -= buyPriceBox.getHeight() + padding;
            }

            if(item.isSellable()) {
                Image sellPriceBox = new Image(greenBox);
                sellPriceBox.setPosition(getWidth() - padding - sellPriceBox.getWidth(), yTop - sellPriceBox.getHeight());
                addActor(sellPriceBox);

                Label priceLabel = new Label(String.format("$%d", item.getSellPrice()), itemNameStyle);
                priceLabel.setPosition(sellPriceBox.getX(), sellPriceBox.getY());
                priceLabel.setHeight(sellPriceBox.getHeight());
                priceLabel.setWidth(sellPriceBox.getWidth());
                priceLabel.setAlignment(Align.center);
                addActor(priceLabel);

                // quantity stuffs
                QuantityBox quantityBox = new QuantityBox() {
                    @Override
                    public void onQuantityChange(int quantity) {
                        priceLabel.setText(String.format("$%d", item.getSellPrice() * quantity));
                    }
                };
                quantityBox.setPosition(sellPriceBox.getX() - padding - quantityBox.getWidth(), sellPriceBox.getY());
                addActor(quantityBox);
                // end quantity stuffs

                Label itemNameLabel = new Label(String.format("Sell %ss:", item.getWrappedItem().getDisplayName()), itemNameStyle);
                itemNameLabel.setPosition(quantityBox.getX() - padding - itemNameLabel.getWidth(),
                        yTop - quantityBox.getHeight() + (quantityBox.getHeight() - itemNameLabel.getHeight())/2);
                addActor(itemNameLabel);
            }
        }
    }

    public MarketPage(MarketPage.Data pageMeta, Market parent) {
        pageData = pageMeta;
        this.parent = parent;
        this.padding = parent.getPadding();

        // set dimensions
        setWidth(parent.getWidth());
        setHeight(parent.getHeight());

        Group container = new Group();
        scrollableGroup.setWidth(getWidth());
        scrollableGroup.setHeight(getHeight());

        container.setHeight(getHeight());

        initStaticAssets(); // init anything static here

        // draw circle
        Image circle = new Image(circleTexture);
        circle.setPosition(padding, padding);
        container.addActor(circle);

        container.addActor(backButton);

        // draw title
        Label.LabelStyle titleStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Bold.ttf", 50), Color.WHITE);
        Label titleLabel = new Label(pageMeta.getTitle(), titleStyle);
        titleLabel.setPosition(circle.getX() + circle.getWidth() + padding,
                circle.getY() + circle.getHeight() - titleLabel.getHeight());

        container.addActor(titleLabel);

        // draw description
        Label.LabelStyle descriptionStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 20), Color.WHITE);
        Label descriptionLabel = new Label(pageMeta.getDescription(), descriptionStyle);
        descriptionLabel.setPosition(circle.getX() + circle.getWidth() + padding,
                circle.getY() + circle.getHeight() - titleLabel.getHeight() - descriptionLabel.getHeight() - padding);

        container.addActor(descriptionLabel);

        // create buy/sell interface
        int rowTopY = (int)circle.getY() + (int)circle.getHeight();
        for (ShopItem item: pageMeta.items) {
            ShopItemRow row = new ShopItemRow(item);
            row.setPosition(getWidth() - row.getWidth(), rowTopY - row.getHeight());
            container.addActor(row);
            rowTopY = rowTopY - (int) row.getHeight() - padding;
        }

        scrollableGroup.setContentGroup(container);
        addActor(scrollableGroup);
    }

    private void initStaticAssets() {
        if(backButton == null) { // init back button
            Label.LabelStyle backStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Bold.ttf", 25), Color.WHITE);
            backButton = new Label("Back", backStyle);
            backButton.setPosition(padding, getHeight() - padding - headerHeight + (headerHeight - backButton.getHeight())/2);

            // add back listener
            backButton.addCaptureListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    parent.loadMainPage();
                }
            });
        }

        if(redBox == null) {
            redBox = new Texture(ShapeGenerator.createRoundedRectangle(105, 50, 20,
                    new Color(	1f, 0f, 0f, .4f)));
        }

        if(greenBox == null) {
            greenBox = new Texture(ShapeGenerator.createRoundedRectangle(105, 50, 20,
                    new Color(0f, 1f, 0f, .4f)));
        }

        if(circleTexture == null) {
            circleTexture = new Texture(ShapeGenerator.createCircle((int) getHeight()/2 - padding - headerHeight, Color.WHITE));
        }
    }

    // ADDITIONAL CLASSES FOR DEFINING PAGE DATA AND PAGES BELOW...

    private static class Pages {
        private static final MarketPage.Data CORN = new MarketPage.Data(0, "Corn", "test", new ShopItem[]{
                new ShopItem(new CornPlantItem(), ShopItem.ItemAccessibility.Both, 100, 10)
        });

        private static final MarketPage.Data BLUEBERRY = new MarketPage.Data(0, "Blueberry", "test", new ShopItem[]{
                new ShopItem(new BlueberriesPlantItem(), ShopItem.ItemAccessibility.Both, 300, 30)
        });

        private static final MarketPage.Data TRACTOR = new MarketPage.Data(0, "Tractor", "test", new ShopItem[]{
                new ShopItem(new BasicTractorItem(), ShopItem.ItemAccessibility.Both, 1000, 300)
        });

        private static final MarketPage.Data[] PAGES = new MarketPage.Data[] {
                CORN, BLUEBERRY, TRACTOR
        };

        public static MarketPage.Data[] getPages() {
            return PAGES;
        }
    }
    /**
     * Defines the data contained within a MarketPage
     */
    public static class Data {
        private Texture icon;
        private String title;
        private String description;
        private ShopItem[] items;

        public Data(Texture icon, String title,
                              String description, ShopItem[] items) {
            this.icon = icon;
            this.title = title;
            this.description = description;
            this.items = items;
        }

        public Data(int iconIndex, String title,
                              String description, ShopItem[] items) {
            this.icon = items[iconIndex].getWrappedItem().getTexture();
            this.title = title;
            this.description = description;
            this.items = items;
        }

        public Texture getIcon() {
            return icon;
        }

        public ShopItem[] getItems() {
            return items;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Returns a list of all registered MarketPage Data packets.
     * @return
     */
    public static MarketPage.Data[] getPages() {
        return Pages.getPages();
    }
}
