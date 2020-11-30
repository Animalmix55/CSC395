package com.kacstudios.game.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.kacstudios.game.actors.Farmer.Farmer;
import com.kacstudios.game.inventoryItems.BasicTractorItem;
import com.kacstudios.game.overlays.ContextMenu.ContextMenu;
import com.kacstudios.game.screens.LevelScreen;

import java.awt.geom.Point2D;

public class Tractor extends PlayableActor {
    private static final int radius = 300;
    private Farmer farmer;
    private boolean hasResized = true;
    private boolean justMounted = false;

    ContextMenu menu = new ContextMenu(0, 0, new ContextMenu.ContextMenuOption[] {
            new ContextMenu.ContextMenuOption("Store", () -> { if(getDistanceFromFarmer() < radius) removeTractor(); }),
            new ContextMenu.ContextMenuOption("Mount", () -> { if(getDistanceFromFarmer() < radius) addFarmer(); })
    });

    public Tractor (float x, float y, LevelScreen s) {
        super(x,y,s, false);
        farmer = s.getFarmer();

        String[] leftMovementFiles = {"tractor-textures/tractor-left-1.png"};
        String[] rightMovementFiles = {"tractor-textures/tractor-right-1.png"};
        String[] upMovementFiles = {"tractor-textures/tractor-up-1.png"};
        String[] downMovementFiles = {"tractor-textures/tractor-down-1.png"};

        Animation<TextureRegion> leftAnimation = loadAnimationUnsetFromFiles(leftMovementFiles, 0.1f, true);
        Animation<TextureRegion> rightAnimation = loadAnimationUnsetFromFiles(rightMovementFiles,0.1f,true);
        Animation<TextureRegion> upAnimation = loadAnimationUnsetFromFiles(upMovementFiles,0.1f,true);
        Animation<TextureRegion> downAnimation = loadAnimationUnsetFromFiles(downMovementFiles,0.1f,true);
        setAcceleration(2000);
        setMaxSpeed(625);
        setDeceleration(5000);

        setDirectionalAnimations(leftAnimation, rightAnimation, upAnimation, downAnimation);
        setAnimationDirection(Direction.right);
        setBoundaryPolyCustom(new float[]{0,0, getWidth(),0, getWidth(),getHeight(), 0,getHeight()});

        menu.setVisible(false);
        menu.setPosition(Math.abs(menu.getWidth() - getWidth())/2, Math.abs(menu.getHeight() - getHeight())/2);
        addActor(menu);
    }

    @Override
    public void onClick(InputEvent e, float x, float y) {
        if(getDistanceFromFarmer() > radius) return;

        if(!menu.isOpen() && !justMounted) {
            if(getFocused()) removeFarmer();
            else menu.setOpen(true);
        }
    }

    public void act(float dt) {
        super.act(dt);
        if (farmer != null) {
            if(getFocused()){
                Direction currentDirection = getAnimationDirection();
                if(farmer.getAnimationDirection() != currentDirection) {
                    farmer.setAnimationDirection(currentDirection);
                    setBoundaryPolyCustom(new float[]{89, 0, getWidth() - 89, 0, getWidth() - 89, getHeight(), 89, getHeight()});
                }

                preventOverlapWithAddedActors();
                alignFarmer(currentDirection);
            }
            else {
                farmer.preventOverlap(this);
                if(!hasResized &&
                        (getAnimationDirection() == Direction.left || getAnimationDirection() == Direction.right)) {
                    setBoundaryPolyCustom(new float[]{0, 0, getWidth(), 0, getWidth(), getHeight(), 0, getHeight()});
                    hasResized = true;
                }

            }
        }

        if (justMounted) justMounted = false;
    }

    private void alignFarmer(Direction currentDirection) {
        // every frame
        switch (currentDirection) { // run once per direction
            case down:
            case up:
                farmer.setPosition(this.getX() + (getWidth() - farmer.getWidth()) / 2, this.getY() + 70);
                break;
            case right:
                farmer.setPosition(this.getX() + 35, this.getY() + 70);
                break;
            case left:
                farmer.setPosition(this.getX() + 115, this.getY() + 70);
                break;
        }
    }

    /**
     * Sets the tractor status as being actively used, which disables player movement, matches player speed to tractor speed, and mounts the player onto the tractor
     */
    private void addFarmer() {
        justMounted = true;
        this.setFocused(true);
        farmer.setMaxSpeed(625);
    }

    /**
     * Sets the tractor status as inactive, re-enabling player movement and resetting speed to normal
     * Also forces tractor speed to 0, so that it doesn't slide when getting back on tractor for second time usage
     */
    private void removeFarmer() {
        hasResized = false;
        farmer.setFocused(true);
        farmer.setMaxSpeed(200);
        this.setSpeed(0);
    }

    /**
     * @return Pixel distance between tractor and farmer
     */
    private double getDistanceFromFarmer() {
        return Point2D.distance(
                farmer.getX()+(farmer.getWidth()/2),
                farmer.getY()+(farmer.getHeight()/2),
                this.getX()+(this.getWidth()/2),
                this.getY()+(this.getHeight()/2)
        );
    }

    private void removeTractor() {
        screen.getHud().getInventoryViewer().addItem(new BasicTractorItem(1));
        screen.getAddedActors().remove(this);
        remove();
    }
}
