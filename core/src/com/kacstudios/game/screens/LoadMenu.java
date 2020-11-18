package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.actors.Tractor;
import com.kacstudios.game.games.BaseGame;
import com.kacstudios.game.games.FarmaniaGame;

import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.plants.BlueberriesPlant;
import com.kacstudios.game.grid.plants.CornPlant;
import com.kacstudios.game.grid.plants.Plant;

import com.kacstudios.game.inventoryItems.*;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.Economy;
import com.kacstudios.game.utilities.TimeEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;


public class LoadMenu extends BaseScreen {

    private LevelScreen level;

    // list of buttons for saves (Save #1, Save #2, etc)
    private TextButton[] levelButtons;
    private TextButton backButton;

    // used for cycling files in and out to be used in file streams
    private File temporarySaveFile;

    // used for iteration through save file lines
    private Scanner fileScanner;
    private String fileLine;
    private String[] splitFileLine;
    private static FileWriter fileWriter;

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
        String time;
        int money;

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
            time = splitFileLine[2];
            money = Integer.parseInt(splitFileLine[3]);

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
                        if (splitFileLine[8].equals("t")) tempPlant.setDead(true);
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
                            case "Basic Tractor":
                                tempItem = new BasicTractorItem(Integer.parseInt(splitFileLine[2]));
                                break;
                            case "Corn Seed":
                                tempItem = new CornPlantItem(Integer.parseInt(splitFileLine[2]));
                                break;
                            case "Blueberry Seed":
                                tempItem = new BlueberriesPlantItem(Integer.parseInt(splitFileLine[2]));
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
                            case "Watering Can":
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

            temporarySaveFile = new File(String.format("core/assets/saves/actors%d.mcconnell",levelNumber));
            fileScanner.close();
            fileScanner = new Scanner(temporarySaveFile);

            fileLine = fileScanner.nextLine();
            splitFileLine = fileLine.split(",");
            float farmerCoordinateX = Float.parseFloat(splitFileLine[1]);
            float farmerCoordinateY = Float.parseFloat(splitFileLine[2]);

            level = new LevelScreen(levelWidth, levelHeight, plants, items, time);
            level.getFarmer().setX(farmerCoordinateX);
            level.getFarmer().setY(farmerCoordinateY);
            Economy.setMoney( money );


            while (fileScanner.hasNextLine()) {
                fileLine = fileScanner.nextLine();
                splitFileLine = fileLine.split(",");
                switch (splitFileLine[0]) {
                    case "BasicTractor":
                        level.addSecondaryActor( new Tractor( Float.parseFloat(splitFileLine[1]), Float.parseFloat(splitFileLine[2]), level ) );
                        break;
                }
            }


            FarmaniaGame.setActiveScreen(level);

        }
        catch (Exception e) { e.printStackTrace(); }
        finally { fileScanner.close(); }
    }

    public static void saveLevel(LevelScreen screen, int levelNumber) {
        // save grid
        GridSquare[][] loadedSquares = screen.getGrid().getGridSquares();
        Plant tempPlant;
        String[] tempLine;
        ArrayList<String> gridLinesToWrite = new ArrayList<String>(); // lines that will be iterated when grid file is opened to write
        gridLinesToWrite.add(String.format("%d,%d,%s,%d", screen.getGrid().getGridWidth(), screen.getGrid().getGridHeight(), TimeEngine.getDateTime().toString(), Economy.getMoney() ));
        for (int column=0;column<loadedSquares.length;column++) {
            for (int row=0;row<loadedSquares[column].length;row++) {
                if (loadedSquares[column][row] != null) {
                    // do this for every grid square:
                    if ( loadedSquares[column][row].getClass().getName().startsWith("com.kacstudios.game.grid.plants") ) {
                        tempLine = new String[9];
                        tempLine[0] = String.valueOf(column);
                        tempLine[1] = String.valueOf(row);
                        tempLine[2] = "plant";
                        tempPlant = (Plant) loadedSquares[column][row];
                        tempLine[3] = tempPlant.getPlantName();
                        if (tempPlant.getWatered()) tempLine[4] = "t";
                        else tempLine[4] = "f";
                        // insert disaster save code here
                        tempLine[5] = "0";
                        tempLine[6] = "0";
                        // end disaster code
                        tempLine[7] = String.valueOf(tempPlant.getGrowthPercentage());
                        if (tempPlant.getDead()) tempLine[8] = "t";
                        else tempLine[8] = "f";
                        String arrayConvertedToString = "";
                        for (String element : tempLine) {
                            arrayConvertedToString += element+",";
                        }
                        arrayConvertedToString = arrayConvertedToString.substring(0,arrayConvertedToString.length()-1);
                        gridLinesToWrite.add(arrayConvertedToString);
                    }

                }
            }
        }


        // save inventory
        ArrayList<String> inventoryLinesToWrite = new ArrayList<String>(); // lines that will be iterated when grid file is opened to write
        String tempInventoryLine;

        ItemButton[][] loadedInventory = screen.getHud().getInventoryViewer().getItemButtons();
        for (int column=0;column<loadedInventory.length;column++) {
            for (int row=0;row<loadedInventory[column].length;row++) {
                if (loadedInventory[column][row].getItem() != null) {
                    tempInventoryLine = getInventorySaveLine(loadedInventory[column][row]);
                    inventoryLinesToWrite.add(tempInventoryLine);
                }
            }
        }


        // save actors
        ArrayList<String> actorsLinesToWrite = new ArrayList<String>();
        String tempActorsLine;
        actorsLinesToWrite.add( String.format("Farmer,%f,%f",screen.getFarmer().getX(),screen.getFarmer().getY()) );
        for (int i=0;i<screen.getAddedActors().size();i++) {
            actorsLinesToWrite.add( String.format("%s,%f,%f", screen.getAddedActors().get(i).getActorName(), screen.getAddedActors().get(i).getX(), screen.getAddedActors().get(i).getY() ) );
        }


        File gridSaveFile = new File(String.format("core/assets/saves/grid%d.mcconnell",levelNumber));
        File inventorySaveFile = new File(String.format("core/assets/saves/inventory%d.mcconnell",levelNumber));
        File actorsSaveFile = new File(String.format("core/assets/saves/actors%d.mcconnell",levelNumber));

        try {
            fileWriter = new FileWriter(gridSaveFile);
            for (int i=0;i<gridLinesToWrite.size();i++) {
                fileWriter.write(gridLinesToWrite.get(i) + "\n");
            }
            fileWriter.close();
            fileWriter = new FileWriter(inventorySaveFile);
            for (int i=0;i<inventoryLinesToWrite.size();i++) {
                fileWriter.write(inventoryLinesToWrite.get(i) + "\n");
            }
            fileWriter.close();
            fileWriter = new FileWriter(actorsSaveFile);
            for (int i=0;i<actorsLinesToWrite.size();i++) {
                fileWriter.write(actorsLinesToWrite.get(i) + "\n");
            }
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Converts a given item button to the proper line to be written into the save file
     * @param itemButton
     * @return String containing exact line to be added to file
     */
    private static String getInventorySaveLine(ItemButton itemButton) {
        IInventoryItem item = itemButton.getItem();
        String itemName = item.getDisplayName();
        String itemType = item.getInventoryItemType();
        IDepleteableItem tempDepletion;
        String retString;
        switch(itemType) {
            case "II":
                retString = String.format("%s,%s,%s","II",itemName,item.getAmount());
                break;
            case "ID":
                tempDepletion = (IDepleteableItem) item;
                retString = String.format("%s,%s,%s,%s","ID",itemName,item.getAmount(),tempDepletion.getDepletionPercentage());
                break;
            default:
                retString = "";
                break;
        }
        return retString;
    }

}
