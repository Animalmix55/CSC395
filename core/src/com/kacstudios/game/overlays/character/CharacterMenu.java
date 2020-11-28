package com.kacstudios.game.overlays.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.actors.Farmer.Farmer;
import com.kacstudios.game.actors.Farmer.FarmerAnimationFactory;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.ShapeGenerator;

public class CharacterMenu extends Group {
    private Farmer farmer;
    private Image background;
    private Image char_preview_background;
    private Image char_preview;
    private Farmer.FarmerTextureData tempTextureData;
    public static final int charMenuWidth = 356;
    public static final int charMenuHeight = 384;
    private static Texture backgroundTexture = new Texture(ShapeGenerator.createRoundedRectangle(
                charMenuWidth,
                charMenuHeight,
                16,
                new Color(0,0,0,.5f)
        )
    );

    private boolean isDirty = false;

    Group mainButtons = new Group();

    CharacterMenuPage head;
    CharacterMenuPage shirt;
    CharacterMenuPage pants;
    CharacterMenuPage skin;

    public CharacterMenu(Farmer farmer) {
        setWidth(charMenuWidth);
        setHeight(charMenuHeight);

        tempTextureData = farmer.getTextureData();
        this.farmer = farmer;
        background = new Image(backgroundTexture);
        addActor(background);

        char_preview_background = new Image( new Texture("menu-textures/customization_preview_background.png") );
        char_preview_background.setY(getHeight() - char_preview_background.getHeight());
        addActor(char_preview_background);

        updatePreview();
        char_preview.setPosition(char_preview_background.getX() +
                (char_preview_background.getWidth() - char_preview.getWidth())/2,
                char_preview_background.getY() + (char_preview_background.getHeight() - char_preview.getHeight())/2);

        CharacterMenuButton main_skinButton = new CharacterMenuButton("Skin", 0,15);
        main_skinButton.setX((getWidth() - main_skinButton.getWidth())/2);
        main_skinButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenu_skin();
            }
        });
        mainButtons.addActor(main_skinButton);
        addActor(mainButtons);

        CharacterMenuButton main_pantsButton = new CharacterMenuButton("Pants", 0,(int) main_skinButton.getTop() + 10);
        main_pantsButton.setX((getWidth() - main_pantsButton.getWidth())/2);
        main_pantsButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { setMenu_pants();
            }
        });
        mainButtons.addActor(main_pantsButton);

        CharacterMenuButton main_shirtButton = new CharacterMenuButton("Shirt", 0,(int) main_pantsButton.getTop() + 10);
        main_shirtButton.setX((getWidth() - main_shirtButton.getWidth())/2);
        main_shirtButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { setMenu_shirt();
            }
        });
        mainButtons.addActor(main_shirtButton);

        CharacterMenuButton main_headButton = new CharacterMenuButton("Head", 0,(int) main_shirtButton.getTop() + 10);
        main_headButton.setX((getWidth() - main_headButton.getWidth())/2);
        main_headButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { setMenu_head();
            }
        });
        mainButtons.addActor(main_headButton);

        Image closeButton = new Image(new Texture(ShapeGenerator.createCloseButton(20, Color.BLACK, Color.WHITE)));
        closeButton.setPosition(getWidth() - closeButton.getWidth() / 2, getHeight() - closeButton.getHeight() / 2);
        closeButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeMenu();
            }
        });

        addActor(closeButton);

        closeMenu();
    }

    public void closeMenu() {
        setVisible(false);
        if (head != null) head.remove();
        if (shirt != null) shirt.remove();
        if (pants != null) pants.remove();
        if (skin != null) skin.remove();

        if(isDirty) {
            farmer.setTextureData(tempTextureData);
            farmer.updateTextures();
            isDirty = false;
        }

        onClose();
    }

    public void setMenu_default() {
        tempTextureData = farmer.getTextureData();
        updatePreview();
        setVisible(true);
        mainButtons.setVisible(true);
        onOpen();
    }

    public void setMenu_head() {
        mainButtons.setVisible(false);
        head = new CharacterMenuPage(
                tempTextureData.headColor, FarmerAnimationFactory.CustomizationPart.head, tempTextureData.headName, false) {
            @Override
            public void onClose() {
                this.remove();
                setMenu_default();
            }

            @Override
            public void onChange(Color color, String selection) {
                tempTextureData.headColor = color;
                tempTextureData.headName = selection;

                updatePreview();
            }
        };
        head.setX((getWidth() - head.getWidth())/2);
        addActor(head);
    }

    public void setMenu_shirt() {
        mainButtons.setVisible(false);
        shirt = new CharacterMenuPage(tempTextureData.shirtColor, FarmerAnimationFactory.CustomizationPart.shirt, tempTextureData.shirtName) {
            @Override
            public void onClose() {
                this.remove();
                setMenu_default();
            }
            @Override
            public void onChange(Color color, String selection) {
                tempTextureData.shirtColor = color;
                tempTextureData.shirtName = selection;

                updatePreview();
            }
        };
        shirt.setX((getWidth() - shirt.getWidth())/2);
        addActor(shirt);
    }

    public void setMenu_skin() {
        mainButtons.setVisible(false);
        skin = new CharacterMenuPage(tempTextureData.skinColor, null, null) {
            @Override
            public void onClose() {
                this.remove();
                setMenu_default();
            }

            @Override
            public void onChange(Color color, String selection) {
                tempTextureData.skinColor = color;

                updatePreview();
            }
        };
        skin.setX((getWidth() - skin.getWidth())/2);
        addActor(skin);
    }

    public void setMenu_pants() {
        mainButtons.setVisible(false);
        pants = new CharacterMenuPage(tempTextureData.pantsColor, FarmerAnimationFactory.CustomizationPart.pants, tempTextureData.pantsName) {
            @Override
            public void onClose() {
                this.remove();
                setMenu_default();
            }

            @Override
            public void onChange(Color color, String selection) {
                tempTextureData.pantsColor = color;
                tempTextureData.pantsName = selection;

                updatePreview();
            }
        };
        pants.setX((getWidth() - pants.getWidth())/2);
        addActor(pants);
    }

    public void onOpen() {
        //pass
    }

    public void onClose() {
        //pass
    }

    private void updatePreview() {
        float x = 0;
        float y = 0;

        if (char_preview != null) {
            x = char_preview.getX();
            y = char_preview.getY();

            char_preview.remove();
        }

        char_preview = new Image(FarmerAnimationFactory.generatePreviewFrame(tempTextureData));
        char_preview.setPosition(x, y);
        addActor(char_preview);
        isDirty = true;
    }
}
