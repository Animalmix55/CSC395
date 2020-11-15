package com.kacstudios.game.overlays.market;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
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
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.Economy;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

import java.util.ArrayList;

public class MarketPage extends Group {
    private ScrollableGroup scrollableGroup = new ScrollableGroup();
    private MarketPage.Data pageData;
    private Market parent;
    private int padding;
    private static int headerHeight = 30;
    private static Label backButton = null;
    private static Texture greenBox = null;
    private static Texture greenBoxHover = new Texture(ShapeGenerator.createRoundedRectangle(105, 50, 20,
            new Color(0f, 1f, 0f, .7f)));
    private static Texture redBox = null;
    private static Texture redBoxHover = new Texture(ShapeGenerator.createRoundedRectangle(105, 50, 20,
            new Color(	1f, 0f, 0f, .7f)));
    private static Texture circleTexture = null;
    private static Label.LabelStyle itemNameStyle =
            new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 20), Color.WHITE);

    public class ShopItemRow extends Group {
        ShopItem item;
        public ShopItemRow(ShopItem item) {
            setHeight(greenBox.getHeight());
            setWidth(MarketPage.this.getWidth());
            this.item = item;

            ArrayList<Group> rows = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                if (!(i == 0 && item.isPurchasable()) && !(i == 1 && item.isSellable())) continue;
                Group container = new Group();

                Label itemNameLabel = new Label(String.format(i == 0? "Buy %ss:" : "Sell %ss:", item.getWrappedItem().getDisplayName()), itemNameStyle);
                itemNameLabel.setPosition(padding,
                        (redBox.getHeight() - itemNameLabel.getHeight()) / 2);
                container.addActor(itemNameLabel);

                // quantity stuffs
                int price = i == 0? item.getBuyPrice() : item.getSellPrice();
                PriceBox priceBox = new PriceBox(price, i == 0? PriceBox.PriceBoxType.Buy : PriceBox.PriceBoxType.Sell);
                QuantityBox quantityBox = new QuantityBox() {
                    @Override
                    public void onQuantityChange(int quantity) {
                        priceBox.updatePrice(price * quantity);
                    }
                };

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
                if(!parent.getScreen().getHud().getInventoryViewer().addItem(item.createItemInstance(quantity)))
                    Economy.addMoney(quantity * item.getBuyPrice()); // return money if add fails
            }
        }

        public void onSell(int quantity) {
            if(parent.getScreen().getHud().getInventoryViewer().removeItem(item.getWrappedItem().getClass(), quantity)) {
                Economy.addMoney(quantity * item.getSellPrice());
            }
        }
    }
    public static class PriceBox extends Group {
        public enum PriceBoxType {
            Sell,
            Buy
        }
        private int price;
        private static Texture disabledTexture = new Texture(ShapeGenerator.createRoundedRectangle(greenBox.getWidth(),
                greenBox.getHeight(), 20, new Color(1f, 1f, 1f, .2f)));
        private PriceBoxType type;
        private Image hoverImage;
        private Image regularImage;
        private Image disabled;
        private Label priceLabel;

        public PriceBox(int price, PriceBoxType type) {
            this.price = price;
            this.type = type;

            switch (type) {
                case Buy:
                    regularImage = new Image(redBox);
                    hoverImage = new Image(redBoxHover);
                    Economy.subscribeToUpdate(() -> {
                        if(Economy.getMoney() < this.price) setDisabled(true);
                        else setDisabled(false);
                    });
                    break;
                case Sell:
                    regularImage = new Image(greenBox);
                    hoverImage = new Image(greenBoxHover);
                    break;
            }
            setHeight(regularImage.getHeight());
            setWidth(regularImage.getWidth());

            disabled = new Image(disabledTexture);
            priceLabel = new Label(String.format("$%s", price), itemNameStyle);
            priceLabel.setHeight(getHeight());
            priceLabel.setWidth(getWidth());
            priceLabel.setAlignment(Align.center);

            hoverImage.setVisible(false);
            disabled.setVisible(false);
            addActor(regularImage);
            addActor(hoverImage);
            addActor(disabled);
            addActor(priceLabel);

            if(Economy.getMoney() < price) setDisabled(true); // disable on instantiation

            addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onClick();
                }
            });
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if(disabled.isVisible()) return; // don't hover if disabled
            Vector2 mousePos = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            if(mousePos.x > 0 && mousePos.y > 0 && mousePos.x < this.getWidth() && mousePos.y < this.getHeight()) {
                hoverImage.setVisible(true);
                regularImage.setVisible(false);
            } else if (!regularImage.isVisible()){
                hoverImage.setVisible(false);
                regularImage.setVisible(true);
            }
        }

        public void updatePrice(int price) {
            priceLabel.setText(String.format("$%s", price));
            this.price = price;
            if (Economy.getMoney() < price) setDisabled(true);
            else setDisabled(false);
        }

        public void setDisabled(boolean isDisabled) {
            regularImage.setVisible(!isDisabled);
            hoverImage.setVisible(!isDisabled);
            disabled.setVisible(isDisabled);
        }

        public void onClick() {
            // stub
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
        scrollableGroup.setHeight(getHeight() - 2 * padding - headerHeight);

        initStaticAssets(); // init anything static here

        //BEGIN DRAWING THE CONTENTS OF THE SCROLL WINDOW

        // create buy/sell interface
        int rowBottomY = padding;
        for (ShopItem item: pageMeta.items) {
            ShopItemRow row = new ShopItemRow(item);
            row.setPosition(0, rowBottomY);
            container.addActor(row);
            rowBottomY = rowBottomY + (int) row.getHeight() + padding;
        }

        container.setHeight(rowBottomY + scrollableGroup.getHeight());
        container.setWidth(getWidth());

        // draw circle
        Image circle = new Image(circleTexture);
        circle.setPosition(padding, container.getHeight() - circle.getHeight());
        container.addActor(circle);

        // draw icon
        Image icon = new Image(pageMeta.icon);
        icon.scaleBy(circle.getWidth() / icon.getWidth() - 1);
        icon.setPosition(circle.getX() + (circle.getWidth() - icon.getWidth() * icon.getScaleX())/2,
                circle.getY() + (circle.getHeight() - icon.getHeight() * icon.getScaleY())/2);
        container.addActor(icon);

        Group titleDescGroup = new Group();

        // draw title and desc
        Label.LabelStyle titleStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Bold.ttf", 50), Color.WHITE);
        Label titleLabel = new Label(pageMeta.getTitle(), titleStyle);
        Label.LabelStyle descriptionStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 20), Color.WHITE);
        Label descriptionLabel = new Label(pageMeta.getDescription(), descriptionStyle);
        descriptionLabel.setWrap(true);
        descriptionLabel.setWidth(container.getWidth() - circle.getWidth() - 2 * padding);
        descriptionLabel.pack();
        descriptionLabel.setWidth(container.getWidth() - circle.getWidth() - 2 * padding);

        titleDescGroup.setHeight(descriptionLabel.getHeight() + padding + titleLabel.getHeight());
        titleDescGroup.setWidth(container.getWidth());

        titleLabel.setPosition(0, titleDescGroup.getHeight() - titleLabel.getHeight());
        titleDescGroup.addActor(titleLabel);
        descriptionLabel.setPosition(0, 0);
        titleDescGroup.addActor(descriptionLabel);

        titleDescGroup.setPosition(circle.getX() + circle.getWidth() + padding,
                circle.getY() + Math.abs(circle.getHeight() - titleDescGroup.getHeight())/2);
        container.addActor(titleDescGroup);

        scrollableGroup.setContentGroup(container);
        addActor(scrollableGroup);

        addActor(backButton);
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
        private static final MarketPage.Data CORN = new MarketPage.Data(0, "Corn", "Ut justo sapien, dapibus vel suscipit et, tristique at velit. Aenean rhoncus sem neque, nec consequat ipsum gravida sed. Maecenas nunc neque, pretium vitae libero id, sollicitudin dapibus orci.", new ShopItem[]{
                new ShopItem(new CornPlantItem(), ShopItem.ItemAccessibility.Both, 100, 10),
                new ShopItem(new CornPlantItem(), ShopItem.ItemAccessibility.CanSell, 100, 10)
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
