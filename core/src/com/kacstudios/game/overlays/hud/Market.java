package com.kacstudios.game.overlays.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kacstudios.game.screens.LevelScreen;
import com.kacstudios.game.utilities.ShapeGenerator;

public class Market extends Group {
    LevelScreen screen;
    Image background;
    Image closeButton;

    public Market(LevelScreen inputScreen) {
        screen = inputScreen;
        Stage stage = screen.getUIStage();
        background = new Image(
                new Texture(ShapeGenerator.createRoundedRectangle(912, 332, 30, new Color(0, 0, 0, .7f))));
        setWidth(background.getWidth());
        setHeight(background.getHeight());
        addActor(background);
        closeButton =
                new Image(new Texture(ShapeGenerator.createCloseButton(20, Color.BLACK, Color.WHITE)));
        closeButton.setPosition(getWidth() - closeButton.getWidth() / 2, getHeight() - closeButton.getHeight() / 2);
        addActor(closeButton);

        setPosition((stage.getWidth() - getWidth())/2, (stage.getHeight() - getHeight())/ 2);
        stage.addActor(this); // add Market to screen
    }

    /*
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(Gdx.gl20.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setColor(new Color(0, 0, 0, .7f));
        shapeRenderer.rect(0, 0, screen.getUIStage().getWidth(), screen.getUIStage().getHeight());

        shapeRenderer.end();
        Gdx.gl.glDisable(Gdx.gl20.GL_BLEND);
        batch.begin();
        super.draw(batch, parentAlpha);
    }
     */
}
