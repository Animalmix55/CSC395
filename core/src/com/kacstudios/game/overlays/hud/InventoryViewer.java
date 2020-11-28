package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.inventoryItems.IInventoryItem;
import com.kacstudios.game.utilities.GridClickEvent;
import com.kacstudios.game.utilities.TimeEngine;

import java.lang.reflect.Array;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;

public class InventoryViewer extends Group {
    private int rows = 3;
    private int columns = 9;
    private ItemButton[][] itemButtons = new ItemButton[columns][rows];
    private boolean isExpanded = false;
    private Image background;
    private boolean mouseDown = false;
    private LocalDateTime mouseDownTime;
    private ItemButton mouseTarget;
    private IInventoryItem dragItem;
    private Image dragItemImage;
    private ArrayList<Runnable> updateListeners = new ArrayList<>();

    public InventoryViewer(){
        background = new Image(new Texture("bottombar/background-inventory-extension.png"));
        background.setY(64);
        background.setVisible(false);
        this.addActor(background);

        // init buttons
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                if(x == columns - 1 && y == 0) continue; // skip for more button
                ItemButton temp = new ItemButton(y == 0, this);
                temp.setX(8 + x * temp.getWidth());
                temp.setY(y * temp.getHeight());
                temp.setVisible(false);
                itemButtons[x][y] = temp;
                if (y == 0) itemButtons[x][y].setVisible(true); // always add hotbar
                this.addActor(itemButtons[x][y]);
            }
        }


        itemButtons[0][0].setSelected(true); // select default first slot
        setWidth(background.getWidth());

        // init ViewInventoryButton
        ViewInventoryButton moreButton = new ViewInventoryButton(this){
            @Override
            public void onClick(InputEvent event, float x, float y) {
                toggleViewer();
            }
        };

        moreButton.setX(itemButtons[columns-2][0].getWidth() + itemButtons[columns-2][0].getX());
        itemButtons[columns-1][0] = moreButton;
        this.addActor(itemButtons[columns-1][0]);

        // ADD HOTBAR LISTENER
        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ItemButton target = null;
                if(event.getTarget().getClass() == ItemButton.class)
                    target = (ItemButton) event.getTarget();
                else if(event.getTarget().getParent() != null && event.getTarget().getParent().getClass() == ItemButton.class)
                    target = (ItemButton) event.getTarget().getParent();

                if(target == null || !target.getIsHotItem()) return;

                for(int j = 0; j < columns - 1; j++){
                    if(itemButtons[j][0] == target) itemButtons[j][0].setSelected(true);
                    else itemButtons[j][0].setSelected(false);
                };
            }
        });

        // ADD DRAG LISTENER
        this.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!isExpanded) return true;

                mouseDown = true;
                mouseDownTime = LocalDateTime.now(); // use system time
                if(event.getTarget().getClass() == ItemButton.class) mouseTarget = (ItemButton) event.getTarget();
                else mouseTarget = (ItemButton) event.getTarget().getParent();

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                mouseDown = false;
                mouseDownTime = null;

                if(dragItemImage == null) return;
                boolean placed = false;

                float gridX = dragItemImage.getX() + dragItemImage.getWidth()/2;
                float gridY = dragItemImage.getY() + dragItemImage.getHeight()/2;

                for(ItemButton[] column: itemButtons){
                    for(ItemButton itemButton : column){
                        if(gridX < itemButton.getX()) continue;
                        if(gridX > itemButton.getX() + itemButton.getWidth()) continue;

                        if(gridY < itemButton.getY()) continue;
                        if(gridY > itemButton.getY() + itemButton.getHeight()) continue;

                        IInventoryItem temp = itemButton.getItem();
                        itemButton.setItem(dragItem);
                        if(temp != null) mouseTarget.setItem(temp);
                        placed = true;

                        break;
                    }
                }

                ViewInventoryButton viewInventoryButton = (ViewInventoryButton) itemButtons[columns-1][0];
                viewInventoryButton.setDeleteMode(false);

                if(!placed) { // put the item back where it was...
                    mouseTarget.setItem(dragItem);
                }
                else if (viewInventoryButton.getItem() != null){
                    viewInventoryButton.setItem(null);
                    if(!dragItem.isDeletable()) mouseTarget.setItem(dragItem);
                    else informSubscribers();
                }

                dragItemImage.remove();
                dragItemImage = null;
                dragItem = null;

                mouseTarget = null;
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(mouseDown && Duration.between(mouseDownTime, LocalDateTime.now()).getSeconds() > 0.5) { // hold down
            if(dragItem == null){ // remove item from button
                dragItem = mouseTarget.getItem();
                mouseTarget.setItem(null); // remove item
                dragItemImage = new Image(dragItem.getTexture());
                mouseTarget.getParent().addActor(dragItemImage); // add drag item to stage

                if(dragItem != null && dragItem.isDeletable()) {
                    ViewInventoryButton viewInventoryButton = (ViewInventoryButton) itemButtons[columns - 1][0];
                    viewInventoryButton.setDeleteMode(true);
                }
            }

            Vector2 globalCursorPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());

            Vector2 localCoordinates = this.screenToLocalCoordinates(globalCursorPos);
            dragItemImage.setX(localCoordinates.x - dragItemImage.getWidth()/2);
            dragItemImage.setY(localCoordinates.y - dragItemImage.getHeight()/2);
        }
    }

    private void toggleViewer(){
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                if(y != 0) itemButtons[x][y].setVisible(!isExpanded); // add other rows
            }
        }
        background.setVisible(!isExpanded);
        isExpanded = !isExpanded;
    }

    public void onUseItem(GridClickEvent event){
        for(int i = 0; i < columns - 1; i++){
            if(itemButtons[i][0].getSelected()){
                itemButtons[i][0].onUseItem(event);
            }
        }
        informSubscribers();
    }

    private IInventoryItem[] compressItems(IInventoryItem[] items) {
        ArrayList<IInventoryItem> newItems = new ArrayList<>();
        IInventoryItem[] results = new IInventoryItem[columns * rows];
        for(IInventoryItem item: items){
            boolean add = true;
            for(IInventoryItem newItem : newItems) {
                if(newItem.getClass() == item.getClass()) {
                    newItem.setAmount(newItem.getAmount() + item.getAmount());
                    add = false;
                    break;
                }
            };
            if(add) newItems.add(item); // otherwise add it
        }

        return newItems.toArray(results);
    }

    public void setItems(IInventoryItem[] items){
        IInventoryItem[] fixedItems = compressItems(items);
        int numPlaced = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                if(x == columns - 1 && y == 0) continue; //skip add button
                itemButtons[x][y].setItem(fixedItems[numPlaced]);
                numPlaced++;
            }
        }
        informSubscribers();
    }


    public ItemButton[][] getItemButtons() {
        return itemButtons;
    }

    /**
     * Returns the amount of items whose types can be assigned to the given parameter type
     * @param type
     * @return
     */
    public int getAmount(Class<? extends IInventoryItem> type) {
        int amount = 0;
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++){
                IInventoryItem item = itemButtons[x][y].getItem();
                if (item != null && type.isAssignableFrom(item.getClass()))
                    amount += itemButtons[x][y].getItem().getAmount();
            }
        }

        return amount;
    }

    public boolean removeItem(Class<? extends IInventoryItem> type, int amount) {
        if(getAmount(type) < amount) return false;

        int amountLeft = amount;
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++){
                IInventoryItem item = itemButtons[x][y].getItem();
                if (item != null && type.isAssignableFrom(item.getClass())) {
                    if (item.getAmount() > amountLeft) {
                        item.setAmount(item.getAmount() - amountLeft);
                        itemButtons[x][y].checkItem(); // update button
                        informSubscribers();
                        return true;
                    }
                    else if (item.getAmount() == amountLeft) {
                        itemButtons[x][y].setItem(null); // remove item
                        informSubscribers();
                        return true;
                    }
                    else {
                        amountLeft -= item.getAmount();
                        item.setAmount(0);
                        itemButtons[x][y].checkItem(); // update button
                    }
                }

            }
        }

        informSubscribers();
        return true;
    }

    /**
     * Adds an item to the inventory
     * @param item
     * @return a boolean noting if the item was successfully added to the inventory. This can fail if the inventory is full
     */
    public boolean addItem(IInventoryItem item) {
        ItemButton firstEmpty = null;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                ItemButton currentButton = itemButtons[x][y];
                if(currentButton.getItem() == null) {
                    if (firstEmpty == null) firstEmpty = currentButton;
                    continue;
                }
                IInventoryItem currentItem = itemButtons[x][y].getItem();
                if(currentItem.getClass() == item.getClass()) {
                    currentItem.setAmount(item.getAmount() + currentItem.getAmount()); // add on to existing item
                    currentButton.checkItem(); // update button

                    informSubscribers();
                    return true;
                }
            }
        }

        if(firstEmpty == null) return false;

        firstEmpty.setItem(item);
        informSubscribers();
        return true;
    }

    /**
     * Provides a function the inventory which is run every time it updates.
     * @param onChange
     */
    public void addUpdateListener(Runnable onChange) {
        updateListeners.add(onChange);
    }

    /**
     * Sends off an event to any objects subscribed to inventory content changes
     */
    private void informSubscribers() {
        updateListeners.forEach(u -> {
            try {
                u.run();
            } catch (Exception e) {
                updateListeners.remove(u);
            }
        });
    }
}
