package com.kacstudios.game.overlays.market;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.ShapeGenerator;

public class Market extends Group {
    private LevelScreen screen;
    private Image background;
    private Image closeButton;

    /**
     * The number of buttons wide
     */
    private final int gridWidth = 14;
    /**
     * Number of buttons tall
     */
    private final int gridHeight = 4;
    /**
     * Pixels of padding around the frame
     */
    private final int padding = 15;

    Group mainPage = new Group();
    MarketPage currentPage = null;
    MarketPageButton[][] buttons = new MarketPageButton[gridWidth][gridHeight];

    private static class MarketPageButton extends Group {
        private MarketPage.Data page;
        private static Texture texture;
        public MarketPageButton(MarketPage.Data page) {
            this();
            this.page = page;
            addActor(new Image(page.getIcon()));
        }

        public MarketPageButton() {
            if(texture == null) texture = ItemButton.getUnselectedTexture(); // reuse texture for efficiency
            setHeight(texture.getHeight());
            setWidth(texture.getWidth());
            addActor(new Image(texture)); // add background

            addCaptureListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onClick();
                }
            });
        }

        public void onClick() {
            // to be implemented
        }

        /**
         * Used for positioning before instantiating
         * @return
         */
        public static float getSideLength() {
            if(texture == null) texture = ItemButton.getUnselectedTexture();
            return texture.getHeight();
        }

        /**
         * Returns the MarketPage implementation associated with the button, or null if there isn't one.
         * @return
         */
        public MarketPage.Data getPage() {
            return page;
        }
    }

    public Market(LevelScreen inputScreen) {
        int buttonSideLength = (int)MarketPageButton.getSideLength();
        screen = inputScreen;
        Stage stage = screen.getUIStage();
        background = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(
                        gridWidth * buttonSideLength + 2 * padding,
                        (gridHeight + 1) * buttonSideLength + 2 * padding, 20, new Color(0, 0, 0, .7f))));
        setWidth(background.getWidth());
        setHeight(background.getHeight());

        // Configure Main Page Group
        mainPage.setHeight(getHeight());
        mainPage.setWidth(getWidth());

        addActor(background);

        setPosition((stage.getWidth() - getWidth())/2, (stage.getHeight() - getHeight())/ 2);

        // CONTENTS
        MarketPage.Data[] pages = MarketPage.getPages();
        Market parent = this;
        int i = 0;
        for (int y = gridHeight - 1; y >= 0; y--) {
            for (int x = 0; x < gridWidth; x++) {
                MarketPage.Data page;
                MarketPageButton button;
                if(i < pages.length) {
                    page = pages[i];
                    button = new MarketPageButton(page) {
                        @Override
                        public void onClick() {
                            loadMarketPage(new MarketPage(page, parent));
                        }
                    };
                    i++;
                } else {
                    button = new MarketPageButton();
                }

                button.setX(padding + x * buttonSideLength);
                button.setY(padding + y * buttonSideLength);
                mainPage.addActor(button);
                buttons[x][y] = button;
            }
        }

        // END CONTENTS

        // ADD LABELS

        Label.LabelStyle titleStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Bold.ttf", 40), Color.WHITE);
        Label label = new Label("Farmania Market", titleStyle);
        label.setPosition(10 + padding, padding + gridHeight * buttonSideLength + (padding + buttonSideLength - label.getHeight())/2 - 10);
        mainPage.addActor(label);

        Label.LabelStyle descriptionStyle = new Label.LabelStyle(FarmaniaFonts.generateFont("fonts/OpenSans-Bold.ttf", 15), Color.WHITE);
        Label descLabel = new Label("Buy and sell items to further expand your farm\nClick items for more options", descriptionStyle);
        descLabel.setAlignment(Align.right);
        descLabel.setPosition(getWidth() - descLabel.getWidth() - padding, padding + gridHeight * buttonSideLength +
                (padding + buttonSideLength - label.getHeight())/2);

        mainPage.addActor(descLabel);
        // END LABELS

        addActor(mainPage);

        closeButton =
                new Image(new Texture(ShapeGenerator.createCloseButton(20, Color.BLACK, Color.WHITE)));
        closeButton.setPosition(getWidth() - closeButton.getWidth() / 2, getHeight() - closeButton.getHeight() / 2);
        closeButton.addCaptureListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });
        addActor(closeButton);

        stage.addActor(this); // add Market to screen

        loadMarketPage(new MarketPage(MarketPage.getPages()[0], this));
    }

    /**
     * Opens the main market page and closes and other page open within it.
     */
    public void loadMainPage() {
        if(currentPage != null) {
            currentPage.remove();
            currentPage = null;
        }

        mainPage.setVisible(true);
    }

    public void loadMarketPage(MarketPage page) {
        currentPage = page;
        addActorBefore(mainPage, page);
        mainPage.setVisible(false);
    }

    public int getPadding() {
        return padding;
    }

    public LevelScreen getScreen() {
        return screen;
    }
}
