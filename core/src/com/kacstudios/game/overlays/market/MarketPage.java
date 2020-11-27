package com.kacstudios.game.overlays.market;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.actors.ScrollableGroup;
import com.kacstudios.game.inventoryItems.*;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

import java.util.ArrayList;

public class MarketPage extends Group {
    private ScrollableGroup scrollableGroup = new ScrollableGroup();
    private static int headerHeight = 30;
    private static Label backButton = new Label("Back",
            new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Bold.ttf", 25), Color.WHITE));
    private static Label.LabelStyle itemNameStyle =
            new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Regular.ttf", 20), Color.WHITE);
    private ArrayList<ShopItemRow> shopItemRows = new ArrayList<>();

    private Market parent;
    private int padding;
    private static Texture circleTexture;

    public MarketPage(MarketPage.Data pageMeta, Market parent) {
        this.parent = parent;
        this.padding = parent.getPadding();

        // set dimensions
        setWidth(parent.getWidth());
        setHeight(parent.getHeight());

        if (circleTexture == null) circleTexture = new Texture(ShapeGenerator.createCircle((int) getHeight()/2 - padding - headerHeight, Color.WHITE));;
        backButton.setPosition(padding, getHeight() - padding - headerHeight + (headerHeight - backButton.getHeight())/2);

        // add back listener
        backButton.addCaptureListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parent.loadMainPage();
            }
        });


        Group container = new Group();
        scrollableGroup.setWidth(getWidth());
        scrollableGroup.setHeight(getHeight() - 2 * padding - headerHeight);

        // create buy/sell interface
        int rowBottomY = padding;
        for (ShopItem item: pageMeta.items) {
            ShopItemRow row = new ShopItemRow(item, this);
            row.setPosition(0, rowBottomY);
            container.addActor(row);
            shopItemRows.add(row);
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
        descriptionLabel.setWidth(container.getWidth() - circle.getWidth() - 3 * padding);
        descriptionLabel.pack();
        descriptionLabel.setWidth(container.getWidth() - circle.getWidth() - 3 * padding);

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

    /**
     * To be called when the economy updates, to avoid having a ton of update listers.
     */
    public void economyUpdated() {
        shopItemRows.forEach(r -> r.economyUpdated());
    }

    /**
     * To be called when the inventory updates, to avoid having a ton of update listers.
     */
    public void inventoryUpdated() {
        shopItemRows.forEach(r -> r.inventoryUpdated());
    }

    private static class Pages {
        private static final MarketPage.Data CORN = new MarketPage.Data(1, "Corn", "A cereal grain first domesticated by indigenous peoples in southern Mexico about 10,000 years ago. The leafy stalk of the plant produces pollen inflorescences and separate ovuliferous inflorescences called ears that yield kernels or seeds, which are fruits.", new ShopItem[] {
                new ShopItem(new CornSeedItem(), ShopItem.ItemAccessibility.Both, 2, 1),
                new ShopItem(new CornPlantItem(), ShopItem.ItemAccessibility.CanSell, 0, 10)
        });

        private static final MarketPage.Data BLUEBERRY = new MarketPage.Data(1, "Blueberry", "Flowering plants with blue or purple berries. They are classified in the section Cyanococcus within the genus Vaccinium. Vaccinium also includes cranberries, bilberries, huckleberries and Madeira blueberries. Commercial blueberries, both wild and cultivated, are all native to North America.", new ShopItem[]{
                new ShopItem(new BlueberriesSeedItem(), ShopItem.ItemAccessibility.Both, 4, 2),
                new ShopItem(new BlueberriesPlantItem(), ShopItem.ItemAccessibility.CanSell, 0, 30)
        });

        private static final MarketPage.Data TRACTOR = new MarketPage.Data(0, "Tractor", "An engineering vehicle specifically designed to deliver a high tractive effort at slow speeds, for the purposes of hauling a trailer or machinery such as that used in agriculture.", new ShopItem[]{
                new ShopItem(new BasicTractorItem(), ShopItem.ItemAccessibility.Both, 40000, 15000)
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
        private MarketPage page;

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

    @Override
    public Market getParent() {
        return parent;
    }
}
