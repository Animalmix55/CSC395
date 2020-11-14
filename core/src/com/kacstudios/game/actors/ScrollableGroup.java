package com.kacstudios.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kacstudios.game.utilities.ShapeGenerator;

public class ScrollableGroup extends Group {
    private Group internalGroup;
    private Image scrollBarKnob;
    private Image scrollBar;
    private static final int scrollBarPadding = 5;
    boolean isClicked = false;

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 globalCursorPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 localCursorPos = screenToLocalCoordinates(globalCursorPos);

        if(scrollBarKnob != null && scrollBar != null) {
            if (localCursorPos.x > 0 && localCursorPos.y > 0 && localCursorPos.x < getWidth() && localCursorPos.y < getHeight()) {
                scrollBarKnob.setVisible(true);
                scrollBar.setVisible(true);
            } else if (!isClicked) {
                scrollBarKnob.setVisible(false);
                scrollBar.setVisible(false);
            }
        }

        if(isClicked == true) {
            float knobPos = localCursorPos.y - scrollBarKnob.getHeight()/2;
            float minPos = scrollBar.getY() + scrollBarPadding;
            float maxPos = scrollBar.getY() + scrollBar.getHeight() - scrollBarPadding - scrollBarKnob.getHeight();
            scrollBarKnob.setY(knobPos < minPos? minPos : (knobPos > maxPos? maxPos : knobPos));

            float bodyY = -((scrollBarKnob.getY() - minPos) / (maxPos - minPos)) *
                    (internalGroup.getHeight() - getHeight());

            internalGroup.setY(bodyY);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Vector2 globalPos = localToStageCoordinates(new Vector2(0, 0));

        Gdx.gl.glEnable(Gdx.gl20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor((int)globalPos.x, (int)globalPos.y, (int)getWidth(), (int)getHeight());
        batch.setTransformMatrix(computeTransform());
        drawChildren(batch, parentAlpha);
        Gdx.gl.glDisable(Gdx.gl20.GL_SCISSOR_TEST);
    }

    private void createScrollBar() {
        if(scrollBar != null) scrollBar.remove();
        if(scrollBarKnob != null) scrollBarKnob.remove();
        if(internalGroup.getHeight() <= getHeight()) return;

        int scrollBarHeight = (int)getHeight() - 50;
        int knobHeight = (int) ((getHeight() / internalGroup.getHeight()) * (scrollBarHeight - 2 * scrollBarPadding));

        scrollBar = new Image(new Texture(ShapeGenerator.createRoundedRectangle(10, scrollBarHeight, 5, Color.GRAY)));
        scrollBarKnob = new Image(new Texture(ShapeGenerator.createRoundedRectangle(6, knobHeight, 3, Color.WHITE)));

        scrollBar.setY((getHeight() - scrollBarHeight)/2);
        scrollBar.setX(getWidth() - scrollBar.getWidth() - 4);

        scrollBarKnob.setX(scrollBar.getX() + (scrollBar.getWidth() - scrollBarKnob.getWidth())/2);
        scrollBarKnob.setY(scrollBar.getY() + scrollBar.getHeight() - scrollBarKnob.getHeight() - scrollBarPadding);

        scrollBarKnob.addCaptureListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isClicked = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isClicked = false;
            }
        });

        addActor(scrollBar);
        addActor(scrollBarKnob);
    }

    public void setContentGroup(Group contents) {
        if(internalGroup != null) internalGroup.remove();
        internalGroup = contents;

        if(contents != null) {
            addActor(internalGroup);
            updateBounds();
        }
    }

    public Group getContentGroup() {
        return internalGroup;
    }

    /**
     * Call whenever the bounds of the content group are changed
     */
    public void updateBounds() {
        internalGroup.setPosition(0, getHeight() - internalGroup.getHeight()); // position at top
        internalGroup.setWidth(getWidth());
        createScrollBar();
    }
}
