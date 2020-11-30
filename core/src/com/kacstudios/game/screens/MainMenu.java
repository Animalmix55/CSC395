package com.kacstudios.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.kacstudios.game.actors.BaseActor;
import com.kacstudios.game.actors.Farmer.DirectionalTextures;
import com.kacstudios.game.actors.Farmer.Farmer;
import com.kacstudios.game.actors.Farmer.FarmerAnimationFactory;
import com.kacstudios.game.games.BaseGame;
import com.kacstudios.game.games.FarmaniaGame;
import com.kacstudios.game.utilities.FarmaniaFonts;

import java.util.Random;


public class MainMenu extends BaseScreen {
    public static Texture bg = new Texture("menu-textures/background_2.png");
    public static Texture logo = new Texture("menu-textures/logo-main.png");
    public static Animation<TextureRegion> farmerFront;
    public static Animation<TextureRegion> farmerRight;

    private static final int farmerVelocity = 40;
    private static final int buttonGap = 100;
    private BaseActor farmer;
    private Farmer.FarmerTextureData textureData;

    private boolean pauseFarmer;
    private String[] skinColors;


    public void initialize() {
        pauseFarmer = false;
        textureData = new Farmer.FarmerTextureData();
        skinColors = new String[] {
                "#8D5524",
                "#C68642",
                "#E0AC69",
                "#F1C27D",
                "#FFDBAC"
        };

        generateNewFarmer();

//        set background/map limits
        Image background = new Image(bg);
        float scaleFactor = Math.max(mainStage.getWidth() / background.getWidth(), mainStage.getHeight() / background.getHeight());
        background.setScale(scaleFactor);
        mainStage.addActor(background);

        Group logoButtons = new Group();
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(BaseGame.textButtonStyle);
        style.font.getData().setScale(1.4f);

        TextButton NewButton = new TextButton( "New", BaseGame.textButtonStyle );
        logoButtons.addActor(NewButton);
        NewButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FarmaniaGame.setActiveScreen( new LevelScreen() );
            }
        });


        TextButton LoadButton = new TextButton( "Load", BaseGame.textButtonStyle );
        LoadButton.setPosition(NewButton.getRight() + buttonGap,0);
        logoButtons.addActor(LoadButton);
        LoadButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FarmaniaGame.setActiveScreen( new LoadMenu() );
            }
        });

        TextButton SettingsButton = new TextButton( "Settings", BaseGame.textButtonStyle );
        SettingsButton.setPosition(LoadButton.getRight() + buttonGap,0);
        logoButtons.addActor(SettingsButton);

        SettingsButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FarmaniaGame.setActiveScreen( new Settings() );
            }
        });

        TextButton ExitButton = new TextButton( "Exit", BaseGame.textButtonStyle );
        ExitButton.setPosition(SettingsButton.getRight() + buttonGap,0);
        logoButtons.addActor(ExitButton);

        ExitButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                System.exit(0);
            }
        });

        logoButtons.setWidth(ExitButton.getRight());

        Image logoImage = new Image(logo);
        float logoScaleFactor = Math.min(mainStage.getWidth() / logoImage.getWidth(), mainStage.getHeight() / logoImage.getHeight()) / 1.8f;
        logoImage.setScale(logoScaleFactor);

        logoButtons.addActor(logoImage);
        logoImage.setPosition((logoButtons.getWidth() - logoImage.getWidth() * logoImage.getScaleX())/2,
                ExitButton.getTop() + 100);

        logoButtons.setHeight(logoImage.getY() + logoImage.getHeight() * logoImage.getScaleY());
        logoButtons.setPosition((uiStage.getWidth() - logoButtons.getWidth())/2,
                (uiStage.getHeight() - logoButtons.getHeight())/2);

        uiStage.addActor(logoButtons);

        farmer = new BaseActor();
        farmer.setAnimation(farmerRight);
        uiStage.addActor(farmer);

        farmer.addCaptureListener(new DragListener() {
            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                super.dragStart(event, x, y, pointer);
                pauseFarmer = true;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                pauseFarmer = false;
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                Vector2 coords = MainMenu.this.uiStage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                farmer.setPosition(coords.x - farmer.getWidth()/2, coords.y - farmer.getHeight()/2);
            }
        });

        farmer.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if(getTapCount() == 2) {
                    generateNewFarmer();
                }
            }
        });
    }




    private void generateNewFarmer() {
        Random rand = new Random();

        DirectionalTextures textures = FarmerAnimationFactory.getTextures();
        textureData.headColor = new Color(rand.nextFloat() / 2f + 0.5f, rand.nextFloat() / 2f + 0.5f,
                rand.nextFloat() / 2f + 0.5f, 1);
        textureData.pantsColor = new Color(rand.nextFloat() / 2f + 0.5f, rand.nextFloat() / 2f + 0.5f,
                rand.nextFloat() / 2f + 0.5f, 1);
        textureData.shirtColor = new Color(rand.nextFloat() / 2f + 0.5f, rand.nextFloat() / 2f + 0.5f,
                rand.nextFloat() / 2f + 0.5f, 1);

        textureData.headName = textures.front.heads.get(rand.nextInt(textures.front.heads.size())).name;
        textureData.shirtName = textures.front.shirts.get(rand.nextInt(textures.front.shirts.size())).name;
        textureData.pantsName = textures.front.pants.get(rand.nextInt(textures.front.pants.size())).name;
        textureData.skinColor = Color.valueOf(skinColors[rand.nextInt(skinColors.length)]);


        farmerRight = FarmerAnimationFactory.generatePreviewAnimation(FarmerAnimationFactory.Direction.right, textureData);
        farmerFront = FarmerAnimationFactory.generatePreviewAnimation(FarmerAnimationFactory.Direction.down, textureData);
    }



    public void update(float dt) {
        if(!pauseFarmer) {
            if (farmer.getAnimation() != farmerRight) {
                farmer.setAnimation(farmerRight);
                farmer.setAnimationPaused(false);
            }
            farmer.setX(farmer.getX() + farmerVelocity * dt);
            if(farmer.getX() > uiStage.getWidth()) farmer.setX(-farmer.getWidth());
        }
        else {
            if (farmer.getAnimation() != farmerFront) {
                farmer.setAnimation(farmerFront);
                farmer.setAnimationPaused(true);
            }
        }
    }







    public boolean keyDown(int keyCode)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();
        return false;
    }


}
