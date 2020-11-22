package com.kacstudios.game.overlays.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.ShapeGenerator;

public class CharacterMenu extends Group {
    private LevelScreen screen;
    private Image background;
    private Image char_preview_background;

    private final int charMenuWidth = 356;
    private final int charMenuX = 640 - (charMenuWidth/2);
    private final int charMenuHeight = 384;

    private final int charMenuButtonWidth = 332;
    private final int charMenuButtonX = 640 - (charMenuButtonWidth/2);
    private final int charMenuButtonHeight = 48;

    Group mainButtons = new Group();
    Group skinButtons = new Group();

    public CharacterMenu(LevelScreen inputScreen) {
        screen = inputScreen;

        background = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(
                        charMenuWidth,
                        charMenuHeight,
                        16,
                        new Color(0,0,0,.5f)
                )
                )
        );
        background.setX(charMenuX);
        background.setY(360 - (charMenuHeight/2));
        addActor(background);

        char_preview_background = new Image( new Texture("customization_preview_background.png") );
        char_preview_background.setX(charMenuX);
        char_preview_background.setY(410);
        addActor(char_preview_background);






        CharacterMenuButton main_headButton = new CharacterMenuButton("Head",charMenuButtonX,354);
        mainButtons.addActor(main_headButton);
        CharacterMenuButton main_shirtButton = new CharacterMenuButton("Shirt",charMenuButtonX,296);
        mainButtons.addActor(main_shirtButton);
        CharacterMenuButton main_pantsButton = new CharacterMenuButton("Pants",charMenuButtonX,238);
        mainButtons.addActor(main_pantsButton);
        CharacterMenuButton main_skinButton = new CharacterMenuButton("Skin",charMenuButtonX,180);
        main_skinButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_skin();
            }
        });
        mainButtons.addActor(main_skinButton);
        addActor(mainButtons);
        // debug
        mainButtons.setVisible(false);







        CharacterMenuRGB rgbMenu = new CharacterMenuRGB(charMenuButtonX,180);
        skinButtons.addActor(rgbMenu);
        CharacterMenuButton skin_backButton = new CharacterMenuButton("Back",charMenuButtonX, 354, true, true);
        skin_backButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_default();
            }
        });
        skinButtons.addActor(skin_backButton);
        CharacterMenuButton skin_skinLabel = new CharacterMenuButton("Skin",charMenuButtonX+(charMenuButtonWidth/2)+6,354, false, true);
        skinButtons.addActor(skin_skinLabel);
        addActor(skinButtons);



        screen.getUIStage().addActor(this);
    }

    public void closeMenu() {
        background.setVisible(false);
        mainButtons.setVisible(false);
    }

    public void setMenu_default() {
        // set all other menu assets invisible here
        char_preview_background.setVisible(false);
        // SET CHAR PREVIEW INVISIBLE
        skinButtons.setVisible(false);
        // set original menu visible
        background.setVisible(true);
        mainButtons.setVisible(true);
    }

    public void setMenu_skin() {
        mainButtons.setVisible(false);
        // SET CHAR PREVIEW VISIBLE
        char_preview_background.setVisible(true);
        skinButtons.setVisible(true);
    }
}
