package com.kacstudios.game.overlays.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.actors.Farmer.Farmer;
import com.kacstudios.game.actors.Farmer.FarmerAnimationFactory;
import com.kacstudios.game.actors.ScrollableGroup;

import java.util.ArrayList;

public class CharacterMenuPage extends Group {
    Color color;
    ArrayList<TextureAtlas.AtlasRegion> options = new ArrayList<>();
    ArrayList<CharacterPartOptionButton> optionButtons = new ArrayList<>();

    TextureAtlas.AtlasRegion selectedOption = null;

    public CharacterMenuPage(Color color, FarmerAnimationFactory.CustomizationPart part, String defaultOption) {
        this(color, part, defaultOption, true);
    }

    public CharacterMenuPage(Color color, FarmerAnimationFactory.CustomizationPart part, String defaultOption, boolean selectionRequired) {
        if (part != null) options = FarmerAnimationFactory.retrievePartTextures(part, FarmerAnimationFactory.Direction.down);
        this.color = color;

        CharacterMenuRGB rgbMenu = new CharacterMenuRGB(0, 180) {
            @Override
            public void onSliderChange(float r, float g, float b) {
                colorChanged(r, g, b);
            }
        };
        // set rgb values here
        addActor(rgbMenu);

        setWidth(rgbMenu.getWidth());

        CharacterMenuButton backButton = new CharacterMenuButton("Back",0, 354, true, options.size() != 0);
        backButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClose();
            }
        });
        addActor(backButton);

        Group optionsGroup = new Group();
        optionsGroup.setHeight((CharacterPartOptionButton.sideLength + 5) * (int) Math.ceil(options.size() / (float) 2) - 5);
        optionsGroup.setWidth(CharacterPartOptionButton.sideLength * 2 + 5);

        int x = 0;
        for (TextureAtlas.AtlasRegion option : options) {
            CharacterPartOptionButton button = new CharacterPartOptionButton(x % 2 == 0?
                    0 : CharacterPartOptionButton.sideLength + 5,(int) optionsGroup.getHeight() -
                    (x / 2 + 1) * (CharacterPartOptionButton.sideLength + 5) + 5,
                    new TextureRegion(option)){
                @Override
                public void onClick() {
                    boolean wasSelected = isSelected();
                    optionButtons.forEach(b -> b.setSelected(false));
                    if(!wasSelected || selectionRequired) {
                        setSelected(true);
                        selectedOption = option;
                    } else selectedOption = null;

                    onChange(color, selectedOption != null? selectedOption.name : null);
                }
            };

            if(option.name.equals(defaultOption)) {
                button.setSelected(true);
                selectedOption = option;
            }
            optionsGroup.addActor(button);
            optionButtons.add(button);
            x++;
        }

        ScrollableGroup scrollableGroup = new ScrollableGroup();
        scrollableGroup.setWidth(optionsGroup.getWidth());
        scrollableGroup.setHeight(CharacterPartOptionButton.sideLength);
        scrollableGroup.setPosition(getWidth() - scrollableGroup.getWidth(), backButton.getY());
        scrollableGroup.setContentGroup(optionsGroup);


        addActor(scrollableGroup);
        rgbMenu.setSliders(color.r, color.g, color.b);
        optionButtons.forEach(i -> i.setColor(color));
    }

    public void onClose() {
        //pass
    }

    private void colorChanged(float r, float g, float b) {
        color = new Color(r, g, b, 1);
        optionButtons.forEach(i -> i.setColor(color));

        onChange(color, selectedOption != null? selectedOption.name : null);
    }

    public void onChange(Color color, String selection) {
        // pass
    }
}
