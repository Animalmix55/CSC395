package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kacstudios.game.inventoryItems.IInventoryItem;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.SelectableButton;

public class ItemButton extends SelectableButton {
    private boolean isHotItem = false;
    private IInventoryItem item;
    private AmountLabel amountLabel;

    private class AmountLabel extends Label {
        private ShapeRenderer shapeRenderer = new ShapeRenderer();
        private int radius = 10;
        private ItemButton button;

        public AmountLabel(CharSequence text, LabelStyle style, ItemButton parent) {
            super(text, style);
            this.setAlignment(Align.center);
            setWidth(radius);
            setHeight(radius);
            this.button = parent;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if(!isVisible()) return; // dont bother
            batch.end();
            float shapeX = Math.abs(getWidth() - 2*radius)/2;
            float shapeY = Math.abs(getHeight() - 2*radius)/2;

            Actor parent = this;
            do{
                shapeX += parent.getX();
                shapeY += parent.getY();
            } while ((parent = parent.getParent()) != null);

            shapeRenderer.setColor(button.getSelected()? Color.GRAY : Color.WHITE);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(shapeX + 1, shapeY, radius); // not sure why the +1, but it works
            shapeRenderer.end();
            batch.begin();

            super.draw(batch, parentAlpha);
        }
    }

    /**
     * Builds selectable button with default rectangular shape
     * @param x x coord
     * @param y y coord
     */
    public ItemButton(float x, float y){
        super(x, y, new Texture("bottombar/inventorybutton_background_selected.png"),
                new Texture("bottombar/inventorybutton_background_unselected.png"));

        Label.LabelStyle amountStyle = new Label.LabelStyle();
        amountStyle.font = FarmaniaFonts.generateFont("OpenSans.ttf", 10);
        amountLabel = new AmountLabel("", amountStyle, this);
        amountLabel.setPosition(3, 14);
        amountLabel.setVisible(false);
        this.addActor(amountLabel);
        amountLabel.setColor(Color.BLACK);
    }

    public ItemButton(float x, float y, boolean isHotItem){
        this(x, y);
        this.isHotItem = isHotItem;
    }

    public ItemButton(boolean isHotItem){
        this(0, 0, isHotItem);
    }

    public ItemButton(){
        this(0, 0);
    }

    public void setItem(IInventoryItem item){
        this.item = item;

        if(item != null) {
            setContents(new Image(new Texture(item.getTexturePath())));
            if(item.getAmount() > 1){
                amountLabel.setText(item.getAmount());
                amountLabel.setVisible(true);
            } // only write if exceeds 1
            else amountLabel.setVisible(false);
        }
        else {
            amountLabel.setVisible(false);
            setContents(null);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        if (selected) amountLabel.setColor(Color.WHITE);
        else amountLabel.setColor(Color.BLACK);
    }

    public boolean getIsHotItem(){
        return isHotItem;
    }

    /**
     * Calls the onDeployment event handler on the item
     * @param event
     */
    public void onUseItem(GridClickEvent event){
        if(item != null){
            item.onDeployment(event);
            int amount = item.getAmount();

            if(amount == 0){ // amount of -1 signifies unlimited
                setItem(null);
            }
            else if (amount != -1) amountLabel.setText(amount);
        };
    }

    public IInventoryItem getItem() {
        return item;
    }
}
