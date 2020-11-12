package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.games.BaseGame;
import com.kacstudios.game.games.FarmaniaGame;

import java.io.File;

public class LoadMenu extends BaseScreen {

    private TextButton[] levelButtons;
    private File temporarySaveFile;

    public void initialize() {
        // set background/map limits
        BaseActor farmBaseActor = new BaseActor(0,0,mainStage);
        farmBaseActor.loadTexture("background_3.png");
        farmBaseActor.setSize(1280,720);
        BaseActor.setWorldBounds(farmBaseActor);

        // create list of level saves, and grey out those that don't exist
        levelButtons = new TextButton[5];
        for (int levelNum=0;levelNum<5;levelNum++) {
            levelButtons[levelNum] = new TextButton(String.format("Save #%d", levelNum+1),BaseGame.textButtonStyle);
            levelButtons[levelNum].setPosition(540,540-(100*levelNum));
            temporarySaveFile = new File(String.format("core/assets/saves/grid%d.save",levelNum+1));
            if (!temporarySaveFile.exists()) levelButtons[levelNum].setStyle(BaseGame.textButtonStyleGray);
            else { // if a save file exists for the given save

            }
        }




        for (TextButton individualButton : levelButtons) {
            uiStage.addActor(individualButton);
        }

    }








    public void update(float dt) {

    }







    public boolean keyDown(int keyCode)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            FarmaniaGame.setActiveScreen( new MainMenu() );
        return false;
    }


}
