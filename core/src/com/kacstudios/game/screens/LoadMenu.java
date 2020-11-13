package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.games.BaseGame;
import com.kacstudios.game.games.FarmaniaGame;

import com.kacstudios.game.grid.plants.BlueberriesPlant;
import com.kacstudios.game.grid.plants.CornPlant;
import com.kacstudios.game.grid.plants.Plant;

import com.kacstudios.game.inventoryItems.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadMenu extends BaseScreen {

    // list of buttons for saves (Save #1, Save #2, etc)
    private TextButton[] levelButtons;
    private TextButton backButton;

    // used for cycling files in and out to be used in file streams
    private File temporarySaveFile;

    // used for iteration through save file lines
    private Scanner fileScanner;
    private String fileLine;
    private String[] splitFileLine;

    public void initialize() {
        // set background/map limits
        BaseActor farmBaseActor = new BaseActor(0,0,mainStage);
        farmBaseActor.loadTexture("background_3.png");
        farmBaseActor.setSize(1280,720);
        BaseActor.setWorldBounds(farmBaseActor);

        // add back button to top left of screen
        backButton = new TextButton("Back", BaseGame.textButtonStyle);
        backButton.setPosition(20,650);
        uiStage.addActor(backButton);
        backButton.addListener(
                (Event e) -> {
                    if ( !(e instanceof InputEvent) ) return false;
                    if ( !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) ) return false;
                    FarmaniaGame.setActiveScreen( new MainMenu() );
                    return true;
                }
        );

        // create list of level saves, and grey out those that don't exist
        levelButtons = new TextButton[5];
        for (int levelNum=0;levelNum<5;levelNum++) {
            levelButtons[levelNum] = new TextButton(String.format("Save #%d", levelNum+1),BaseGame.textButtonStyle);
            levelButtons[levelNum].setPosition(540,540-(100*levelNum));
            temporarySaveFile = new File(String.format("core/assets/saves/grid%d.mcconnell",levelNum+1));

            // if save file doesn't exist upon first look, grey out the text and never add an event listener
            // otherwise, add the event listener to load the clicked save file
            if (!temporarySaveFile.exists()) levelButtons[levelNum].setStyle(BaseGame.textButtonStyleGray);
            else {
                int finalLevelNum = levelNum+1; // needed because of use in lambda function
                levelButtons[levelNum].addListener(
                        (Event e) -> {
                            if (!(e instanceof InputEvent)) return false;
                            if (!((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) return false;
                            loadLevel(finalLevelNum);
                            return true;
                        }
                );
            }
        }

        // render buttons to load menu screen
        for (TextButton individualButton : levelButtons) {
            uiStage.addActor(individualButton);
        }

    }

    public void update(float dt) {}

    // remove on final release (allows for going back during loading process)
    public boolean keyDown(int keyCode) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            FarmaniaGame.setActiveScreen( new MainMenu() );
        return false;
    }

    public void loadLevel(int levelNumber) {
        int levelWidth;
        int levelHeight;

        Plant tempPlant;
        List<Plant> plants = new ArrayList<Plant>();
        IInventoryItem tempItem;
        IDepleteableItem tempDepleteableItem;
        List<IInventoryItem> items = new ArrayList<IInventoryItem>();

        System.out.println(String.format("Called to load level %d",levelNumber));
        try {
            // get rid of level choice, back button, and replace with loading indicator
            for (TextButton individualButton : levelButtons) individualButton.setVisible(false);
            backButton.setVisible(false);
            TextButton loadingIndicator = new TextButton("Loading", BaseGame.textButtonStyle);
            loadingIndicator.setPosition(540,340);
            uiStage.addActor(loadingIndicator);

            // open grid save file
            temporarySaveFile = new File(String.format("core/assets/saves/grid%d.mcconnell",levelNumber));
            fileScanner = new Scanner(temporarySaveFile);

            // get grid size, store in variables
            fileLine = fileScanner.nextLine();
            splitFileLine = fileLine.split(",");
            levelWidth = Integer.parseInt(splitFileLine[0]);
            levelHeight = Integer.parseInt(splitFileLine[1]);

            // iterate through rest of grid squares in save file
            while (fileScanner.hasNextLine()) {
                fileLine = fileScanner.nextLine();
                splitFileLine = fileLine.split(",");

                // check what type of GridSquare the current line is
                switch (splitFileLine[2]) {
                    case "plant":
                        // check what type of Plant the current line is, and create specific plant type
                        switch (splitFileLine[3]) {
                            case "corn":
                                tempPlant = new CornPlant();
                                break;
                            case "blueberries":
                                tempPlant = new BlueberriesPlant();
                                break;
                            default:
                                tempPlant = null;
                                break;
                        }

                        // FOR ALL PLANTS
                        if (splitFileLine[4].equals("t")) tempPlant.setWatered(true); // set plant to be watered if saved as watered
                        tempPlant.setGrowthPercentage(Float.parseFloat(splitFileLine[7])); // restore plant growth progress
                        tempPlant.setSavedX(Integer.parseInt(splitFileLine[0])); // set placement x coordinate
                        tempPlant.setSavedY(Integer.parseInt(splitFileLine[1])); // set placement y coordinate
                        plants.add(tempPlant);
                        break;
                }
            }

            // load inventory items save file into scanner
            temporarySaveFile = new File(String.format("core/assets/saves/inventory%d.mcconnell",levelNumber));
            fileScanner.close();
            fileScanner = new Scanner(temporarySaveFile);

            // iterate through lines in items save file
            while (fileScanner.hasNextLine()) {
                fileLine = fileScanner.nextLine();
                splitFileLine = fileLine.split(",");

                // check what type of item is being loaded
                switch (splitFileLine[0]) {
                    // INVENTORY ITEMS
                    case "II":
                        // specify which item is being created
                        switch(splitFileLine[1]) {
                            case "BasicTractor":
                                tempItem = new BasicTractorItem(Integer.parseInt(splitFileLine[2]));
                                break;
                            case "CornPlant":
                                tempItem = new CornPlantItem(Integer.parseInt(splitFileLine[2]));
                                break;
                            default:
                                tempItem = null;
                        }
                        items.add(tempItem);
                        break;

                    // DEPLETABLE ITEMS
                    case "ID":
                        switch(splitFileLine[1]) {
                            case "Pesticide":
                                tempDepleteableItem = new PesticideItem(Integer.parseInt(splitFileLine[2]));
                                break;
                            case "WateringCan":
                                tempDepleteableItem = new WateringCanItem(Integer.parseInt(splitFileLine[2]));
                                break;
                            default:
                                tempDepleteableItem = null;
                        }
                        tempDepleteableItem.setDepletionPercentage(Float.parseFloat(splitFileLine[3]));
                        items.add(tempDepleteableItem);
                        break;
                }


            }

            LevelScreen level = new LevelScreen(levelWidth, levelHeight, plants, items);
            FarmaniaGame.setActiveScreen(level);

        }
        catch (Exception e) { e.printStackTrace(); }
        finally { fileScanner.close(); }
    }

}
