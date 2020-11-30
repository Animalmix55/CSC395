package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.actors.Farmer.Farmer;
import com.kacstudios.game.disasters.Disaster;
import com.kacstudios.game.games.BaseGame;
import com.kacstudios.game.games.FarmaniaGame;

import com.kacstudios.game.grid.GridSquare;
import com.kacstudios.game.grid.GridVector;
import com.kacstudios.game.grid.OversizeGridSquare;
import com.kacstudios.game.grid.plants.Plant;

import com.kacstudios.game.inventoryItems.*;
import com.kacstudios.game.overlays.hud.ItemButton;
import com.kacstudios.game.utilities.Economy;
import com.kacstudios.game.utilities.ShapeGenerator;
import com.kacstudios.game.utilities.TimeEngine;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;


public class LoadMenu extends BaseScreen {

    private LevelScreen level;

    // list of buttons for saves (Save #1, Save #2, etc)
    private TextButton[] levelButtons;
    private TextButton backButton;

    public static Texture bg = new Texture("menu-textures/background_3.png");
    // used for cycling files in and out to be used in file streams
    private File temporarySaveFile;

    // used for iteration through save file lines
    private Scanner fileScanner;
    private String fileLine;
    private String[] splitFileLine;
    private static FileWriter fileWriter;

    public void initialize() {
        // set background/map limits
        Image background = new Image(bg);
        float scaleFactor = Math.max(mainStage.getWidth() / background.getWidth(), mainStage.getHeight() / background.getHeight());
        background.setScale(scaleFactor);
        mainStage.addActor(background);

        // add back button to top left of screen
        backButton = new TextButton("Back", BaseGame.textButtonStyle);
        backButton.setPosition(20,uiStage.getHeight() - backButton.getHeight() - 20);
        uiStage.addActor(backButton);
        backButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FarmaniaGame.setActiveScreen( new MainMenu() );
            }
        });

        Group buttons = new Group();
        buttons.setWidth(300);
        // create list of level saves, and grey out those that don't exist
        levelButtons = new TextButton[5];
        for (int i = 0; i < 5; i++) {
            int levelNum = 5 - i;
            TextButton button = new TextButton(String.format("Save #%d", levelNum),BaseGame.textButtonStyle);
            button.setWidth(buttons.getWidth());
            button.align(Align.center);

            levelButtons[i] = button;
            levelButtons[i].setPosition(0, (i > 0? levelButtons[i - 1].getTop() + 30 : 0));
            buttons.addActor(button);
            temporarySaveFile = new File(String.format("core/assets/saves/grid%d.mcconnell", levelNum));

            // if save file doesn't exist upon first look, grey out the text and never add an event listener
            // otherwise, add the event listener to load the clicked save file
            if (!temporarySaveFile.exists()) button.setStyle(BaseGame.textButtonStyleGray);
            else {
                button.addCaptureListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        loadLevel(levelNum);
                    }
                });
            }
        }

        buttons.setHeight(levelButtons[levelButtons.length - 1].getTop());
        buttons.setPosition((uiStage.getWidth() - buttons.getWidth())/2,
                (uiStage.getHeight() - buttons.getHeight())/2);
        uiStage.addActor(buttons);
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
        GridSquare tempSquare;
        List<Plant> plants = new ArrayList<Plant>();
        List<GridSquare> miscSquares = new ArrayList<GridSquare>();
        IInventoryItem tempItem;
        List<IInventoryItem> items = new ArrayList<IInventoryItem>();
        Farmer.FarmerTextureData customization = new Farmer.FarmerTextureData();
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

            // init time engine, set time
            TimeEngine.Init( LocalDateTime.parse(splitFileLine[2]) );

            // load money
            money = Integer.parseInt(splitFileLine[3]);

            // iterate through rest of grid squares in save file
            while (fileScanner.hasNextLine()) {
                fileLine = fileScanner.nextLine();
                splitFileLine = fileLine.split(",");

                // check what type of GridSquare the current line is
                switch (splitFileLine[2]) {
                    case "plant":
                        // check what type of Plant the current line is, and create specific plant type
                        String className = splitFileLine[3];
                        try {
                            Class<?> plantClass = Class.forName(className);
                            if(Plant.class.isAssignableFrom(plantClass)) {
                                tempPlant = (Plant) plantClass.getDeclaredConstructor().newInstance();

                                // FOR ALL PLANTS
                                if (splitFileLine[4].equals("t"))
                                    tempPlant.setWatered(true); // set plant to be watered if saved as watered
                                tempPlant.setGrowthPercentage(Float.parseFloat(splitFileLine[7])); // restore plant growth progress
                                tempPlant.setGridCoords(new GridVector(Integer.parseInt(splitFileLine[0]), Integer.parseInt(splitFileLine[1]))); // set placement y coordinate

                                if (!splitFileLine[5].equals("0")) {
                                    String disasterClassName = splitFileLine[5];
                                    try {
                                        Class<?> disasterClass = Class.forName(disasterClassName);
                                        if (Disaster.class.isAssignableFrom(disasterClass)) {
                                            tempPlant.setDisaster( (Disaster) disasterClass.getDeclaredConstructor(Plant.class).newInstance(tempPlant) );
                                            if (!splitFileLine[6].equals("0")) tempPlant.getDisaster().setDisasterProgress( LocalDateTime.parse(splitFileLine[6]) );
                                        }
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (splitFileLine[8].equals("t")) tempPlant.setDead(true);
                                plants.add(tempPlant);
                            }
                        } catch (Exception e) {
                            // if the class is bad and the reflection fails, just bail
                        }
                        break;
                    case "gridsquare":
                        className = splitFileLine[3];
                        try {
                            Class<?> gridSquareClass = Class.forName(className);
                            tempSquare = (GridSquare) gridSquareClass.getDeclaredConstructor().newInstance();
                            tempSquare.setGridCoords(new GridVector(Integer.parseInt(splitFileLine[0]),
                                    Integer.parseInt(splitFileLine[1]))); // set placement y coordinate
                            miscSquares.add(tempSquare);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

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
                boolean isDepletable = splitFileLine[0] == "ID";

                // specify which item is being created
                String className = splitFileLine[1];
                int amount = Integer.parseInt(splitFileLine[2]);

                try {
                    Class<?> itemClass = Class.forName(className);
                    if(IInventoryItem.class.isAssignableFrom(itemClass)) {
                        tempItem = (IInventoryItem) itemClass.getDeclaredConstructor(int.class).newInstance(amount);

                        items.add(tempItem);

                        if (isDepletable)
                            ((IDepleteableItem) tempItem).setDepletionPercentage(Float.parseFloat(splitFileLine[3]));
                    }
                } catch (Exception e) {
                    // pass, this happens if the class is invalid or a constructor doesn't exist
                }
            }
            // switch over to actors file
            temporarySaveFile = new File(String.format("core/assets/saves/actors%d.mcconnell",levelNumber));
            fileScanner.close();
            fileScanner = new Scanner(temporarySaveFile);

            // pull farmer location and save into variable
            fileLine = fileScanner.nextLine();
            splitFileLine = fileLine.split(",");
            float farmerCoordinateX = Float.parseFloat(splitFileLine[1]);
            float farmerCoordinateY = Float.parseFloat(splitFileLine[2]);

            // create level, set previously stored farmer location
            level = new LevelScreen(levelWidth, levelHeight, plants, items, miscSquares);
            level.getFarmer().setX(farmerCoordinateX);
            level.getFarmer().setY(farmerCoordinateY);

            // set money
            Economy.setMoney( money );

            // set current level indicator within pause menu
            level.getPauseMenu().saveMenu_setCurrentLevel(levelNumber);

            // iterate other existing actors and place them accordingly
            while (fileScanner.hasNextLine()) {
                fileLine = fileScanner.nextLine();
                splitFileLine = fileLine.split(",");
                String className = splitFileLine[0];
                try {
                    Class<?> secondaryActorClass = Class.forName(className);
                    if(Actor.class.isAssignableFrom(secondaryActorClass)) {
                        Constructor<?> constructor = secondaryActorClass.getConstructor(float.class, float.class, LevelScreen.class);

                        level.addSecondaryActor( (Actor) constructor.newInstance(Float.parseFloat(splitFileLine[1]), Float.parseFloat(splitFileLine[2]), level));
                    }
                } catch (Exception e) {
                    // pass, only occurs when the casting/reflection fails.
                }
            }

            // set farmer customization
            temporarySaveFile = new File(String.format("core/assets/saves/farmer%d.mcconnell",levelNumber));
            fileScanner.close();
            fileScanner = new Scanner(temporarySaveFile);
            customization.headName = fileScanner.nextLine();
            customization.headColor = Color.valueOf( fileScanner.nextLine() );
            customization.shirtName = fileScanner.nextLine();
            customization.shirtColor = Color.valueOf( fileScanner.nextLine() );
            customization.pantsName = fileScanner.nextLine();
            customization.pantsColor = Color.valueOf( fileScanner.nextLine() );
            customization.skinColor = Color.valueOf( fileScanner.nextLine() );
            fileScanner.close();
            level.getFarmer().setTextureData(customization);
            level.getFarmer().updateTextures();


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
                    if ( Plant.class.isAssignableFrom(loadedSquares[column][row].getClass()) ) {
                        tempLine = new String[9];
                        tempLine[0] = String.valueOf(column);
                        tempLine[1] = String.valueOf(row);
                        tempLine[2] = "plant";
                        tempPlant = (Plant) loadedSquares[column][row];
                        tempLine[3] = tempPlant.getClass().getName();
                        if (tempPlant.getWatered()) tempLine[4] = "t";
                        else tempLine[4] = "f";
                        // create disaster class
                        if (tempPlant.getDisaster() != null) tempLine[5] = tempPlant.getDisaster().getClass().getCanonicalName();
                        else tempLine[5] = "0";
                        // set disaster progress
                        if (tempPlant.getDisaster() != null) tempLine[6] = tempPlant.getDisaster().getDisasterProgress().toString();
                        else tempLine[6] = "0";
                        // end disaster code
                        tempLine[7] = String.valueOf(tempPlant.getGrowthPercentage());
                        if (tempPlant.getDead()) tempLine[8] = "t";
                        else tempLine[8] = "f";
                    }
                    else {
                        GridSquare square = loadedSquares[column][row];
                        if(square.getGridCoords().x != column || square.getGridCoords().y != row) {
                            continue; // for oversize squares, skip over saving several times
                        }

                        tempLine = new String[4];
                        tempLine[0] = String.valueOf(column);
                        tempLine[1] = String.valueOf(row);
                        tempLine[2] = "gridsquare";
                        tempLine[3] = loadedSquares[column][row].getClass().getName();
                    }

                    // convert line to string, add to lines to write
                    String arrayConvertedToString = "";
                    for (String element : tempLine) {
                        arrayConvertedToString += element+",";
                    }
                    arrayConvertedToString = arrayConvertedToString.substring(0,arrayConvertedToString.length()-1);
                    gridLinesToWrite.add(arrayConvertedToString);
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
        actorsLinesToWrite.add( String.format("%s,%f,%f", Farmer.class.getCanonicalName(), screen.getFarmer().getX(),screen.getFarmer().getY()) );
        for (int i=0;i<screen.getAddedActors().size();i++) {
            actorsLinesToWrite.add( String.format("%s,%f,%f", screen.getAddedActors().get(i).getClass().getCanonicalName(),
                    screen.getAddedActors().get(i).getX(), screen.getAddedActors().get(i).getY() ) );
        }

        // save farmer customization
        String[] farmerState = screen.getFarmer().getFarmerTextureSaveState();

        // set file names according to level number
        File gridSaveFile = new File(String.format("core/assets/saves/grid%d.mcconnell",levelNumber));
        File inventorySaveFile = new File(String.format("core/assets/saves/inventory%d.mcconnell",levelNumber));
        File actorsSaveFile = new File(String.format("core/assets/saves/actors%d.mcconnell",levelNumber));
        File farmerSaveFile = new File(String.format("core/assets/saves/farmer%d.mcconnell",levelNumber));

        // write to save files
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
            fileWriter = new FileWriter(farmerSaveFile);
            for (String farmerLine : farmerState) {
                fileWriter.write(farmerLine + "\n");
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
        String itemName = item.getClass().getCanonicalName();
        IDepleteableItem tempDepletion;
        String retString;
        if(IDepleteableItem.class.isAssignableFrom(item.getClass())) {
            tempDepletion = (IDepleteableItem) item;
            retString = String.format("%s,%s,%s,%s","ID",itemName,item.getAmount(),tempDepletion.getDepletionPercentage());
        } else {
            retString = String.format("%s,%s,%s","II",itemName,item.getAmount());
        }

        return retString;
    }

}
