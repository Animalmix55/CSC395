package com.kacstudios.game.overlays.character;

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
    Group headButtons = new Group();
    Group shirtButtons = new Group();
    Group skinButtons = new Group();
    Group pantsButtons = new Group();

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
        main_headButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { setMenu_head();
            }
        });
        mainButtons.addActor(main_headButton);
        CharacterMenuButton main_shirtButton = new CharacterMenuButton("Shirt",charMenuButtonX,296);
        main_shirtButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { setMenu_shirt();
            }
        });
        mainButtons.addActor(main_shirtButton);
        CharacterMenuButton main_pantsButton = new CharacterMenuButton("Pants",charMenuButtonX,238);
        main_pantsButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { setMenu_pants();
            }
        });
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











        CharacterMenuRGB head_rgbMenu = new CharacterMenuRGB(charMenuButtonX, 180);
        // set rgb values here
        headButtons.addActor(head_rgbMenu);

        CharacterMenuButton head_backButton = new CharacterMenuButton("Back",charMenuButtonX, 354, true, true);
        head_backButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_default();
            }
        });
        headButtons.addActor(head_backButton);

        CharacterMenuButton head_button1 = new CharacterMenuButton(charMenuButtonX+221,354,"farmer/menu/plain.png");
        headButtons.addActor(head_button1);

        CharacterMenuButton head_button2 = new CharacterMenuButton(charMenuButtonX+285,354,"farmer/menu/plaid.png");
        headButtons.addActor(head_button2);

        addActor(headButtons);














        CharacterMenuRGB shirt_rgbMenu = new CharacterMenuRGB(charMenuButtonX, 180);
        // set rgb values here
        shirtButtons.addActor(shirt_rgbMenu);

        CharacterMenuButton shirt_backButton = new CharacterMenuButton("Back",charMenuButtonX, 354, true, true);
        shirt_backButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_default();
            }
        });
        shirtButtons.addActor(shirt_backButton);

        CharacterMenuButton shirt_plainButton = new CharacterMenuButton(charMenuButtonX+221,354,"farmer/menu/plain.png");
        shirtButtons.addActor(shirt_plainButton);

        CharacterMenuButton shirt_plaidButton = new CharacterMenuButton(charMenuButtonX+285,354,"farmer/menu/plaid.png");
        shirtButtons.addActor(shirt_plaidButton);

        addActor(shirtButtons);









        CharacterMenuRGB skin_rgbMenu = new CharacterMenuRGB(charMenuButtonX,180);
        // set rgb values here
        skinButtons.addActor(skin_rgbMenu);

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









        CharacterMenuRGB pants_rgbMenu = new CharacterMenuRGB(charMenuButtonX,180);
        // set rgb values here
        pantsButtons.addActor(pants_rgbMenu);

        CharacterMenuButton pants_backButton = new CharacterMenuButton("Back",charMenuButtonX, 354, true, true);
        pants_backButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_default();
            }
        });
        pantsButtons.addActor(pants_backButton);

        CharacterMenuButton pants_pantsLabel = new CharacterMenuButton("Pants",charMenuButtonX+(charMenuButtonWidth/2)+6,354, false, true);
        pantsButtons.addActor(pants_pantsLabel);

        addActor(pantsButtons);






        closeMenu();
        screen.getUIStage().addActor(this);
    }

    public void closeMenu() {
        char_preview_background.setVisible(false);
        headButtons.setVisible(false);
        shirtButtons.setVisible(false);
        skinButtons.setVisible(false);
        pantsButtons.setVisible(false);
        background.setVisible(false);
        mainButtons.setVisible(false);
        char_preview_background.setVisible(false);
    }

    public void setMenu_default() {
        // set all other menu assets invisible here
        char_preview_background.setVisible(true);
        headButtons.setVisible(false);
        shirtButtons.setVisible(false);
        skinButtons.setVisible(false);
        pantsButtons.setVisible(false);
        // set original menu visible
        background.setVisible(true);
        mainButtons.setVisible(true);
    }

    public void setMenu_head() {
        mainButtons.setVisible(false);
        headButtons.setVisible(true);
    }

    public void setMenu_shirt() {
        mainButtons.setVisible(false);
        shirtButtons.setVisible(true);
    }

    public void setMenu_skin() {
        mainButtons.setVisible(false);
        skinButtons.setVisible(true);
    }

    public void setMenu_pants() {
        mainButtons.setVisible(false);
        pantsButtons.setVisible(true);
    }
}
