package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.inventoryItems.IInventoryItem;
import com.kacstudios.game.utilities.GridClickEvent;

import java.util.ArrayList;

public class InventoryViewer extends Group {
    private int rows = 3;
    private int columns = 9;
    private ItemButton[][] itemButtons = new ItemButton[columns][rows];
    private boolean isExpanded = false;
    private Image background;

    public InventoryViewer(){
        background = new Image(new Texture("bottombar/background-inventory-extension.png"));
        background.setY(64);
        background.setVisible(false);
        this.addActor(background);

        // init buttons
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                if(x == columns - 1 && y == 0) continue; // skip for more button
                ItemButton temp = new ItemButton(true);
                temp.setX(8 + x * temp.getWidth());
                temp.setY(y * temp.getHeight());
                temp.setVisible(false);
                itemButtons[x][y] = temp;
                if (y == 0) itemButtons[x][y].setVisible(true); // always add hotbar
                this.addActor(itemButtons[x][y]);
            }
        }

        setWidth(background.getWidth());

        // init ViewInventoryButton
        ViewInventoryButton moreButton = new ViewInventoryButton(){
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
                else if(event.getTarget().getParent().getClass() == ItemButton.class)
                    target = (ItemButton) event.getTarget().getParent();

                if(target == null) return;

                for(int j = 0; j < columns - 1; j++){
                    if(itemButtons[j][0] == target) itemButtons[j][0].setSelected(true);
                    else itemButtons[j][0].setSelected(false);
                };

            }
        });
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
        for(int i = 0; i < columns - 2; i++){
            if(itemButtons[i][0].getSelected()){
                itemButtons[i][0].onUseItem(event);
            }
        }
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
    }
}
