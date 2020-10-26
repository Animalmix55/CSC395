package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kacstudios.game.inventoryItems.IDepleteableItem;
import com.kacstudios.game.inventoryItems.IInventoryItem;
import com.kacstudios.game.utilities.FarmaniaFonts;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.SelectableButton;

public class ItemButton extends SelectableButton {
    private boolean isHotItem = false;
    private IInventoryItem item;
    private AmountLabel amountLabel;
    private PercentBar percentBar;

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
    private class PercentBar extends Actor {
        private float percent = 1;
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        public PercentBar(float width){
            setWidth(width);
            setHeight(3);
        }

        public void setPercent(float percent) {
            this.percent = percent;
        }

        public float getPercent() {
            return percent;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if(!isVisible()) return;

            Vector2 coords = this.localToStageCoordinates(new Vector2(0, 0));

            super.draw(batch, parentAlpha);
            batch.end();
            shapeRenderer.setColor(ItemButton.this.getSelected()? Color.GRAY : Color.WHITE);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(coords.x, coords.y, getWidth(), getHeight());
            shapeRenderer.end();

            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(coords.x, coords.y, getWidth()*percent, getHeight());
            shapeRenderer.end();
            batch.begin();
        }

        public void setParent(Group parent){
            super.setParent(parent);
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
        amountLabel.setPosition(3, getHeight() - amountLabel.getHeight() - 7);
        amountLabel.setVisible(false);
        this.addActor(amountLabel);
        amountLabel.setColor(Color.BLACK);
        amountLabel.setText("");

        percentBar = new PercentBar(getWidth() - 25);
        percentBar.setX(getX() + (getWidth() - percentBar.getWidth())/2);
        percentBar.setY(getY() + 18);
        percentBar.setVisible(false);
        percentBar.setParent(this); // emulate setting parent for access to coordinates
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        percentBar.draw(batch, parentAlpha);
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
            Image contents = new Image(new Texture(item.getTexturePath()));
            setContents(contents);
            if(item.getAmount() > 1){
                amountLabel.setText(item.getAmount());
                amountLabel.setVisible(true);
            } // only write if exceeds 1
            else amountLabel.setVisible(false);

            if(IDepleteableItem.class.isAssignableFrom(item.getClass())) { //if the item is depletable
                percentBar.setVisible(true);
                percentBar.setPercent(1 - ((IDepleteableItem) item).getDepletionPercentage());
            }
        }
        else {
            amountLabel.setVisible(false);
            percentBar.setVisible(false);
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
            item.onDeployment(event, this);
        };
    }

    public IInventoryItem getItem() {
        return item;
    }

    /**
     * Updates the amount/percent displayed for a given item in the button.
     * This is not automatically done when items are updates, must be called.
     */
    public void checkItem(){
        if(item == null) {
            amountLabel.setVisible(false);
            percentBar.setVisible(false);

            return;
        }
        if(!amountLabel.getText().equals(item.getAmount().toString())) { // update amount label
            amountLabel.setText(item.getAmount());
            if(item.getAmount() <= 1) amountLabel.setVisible(false);
        }
        if(IDepleteableItem.class.isAssignableFrom(item.getClass())) // update percent label
            percentBar.setPercent(1 - ((IDepleteableItem)item).getDepletionPercentage());
    }
}
