package com.kacstudios.game.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import java.nio.ByteBuffer;

public class ShapeGenerator {
    public static Pixmap createRoundedRectangle(int width, int height, int cornerRadius, Color color) {
        Pixmap shape = new Pixmap(width, height, Pixmap.Format.RGBA4444);

        shape.setBlending(Pixmap.Blending.None);
        shape.setColor(color);
        shape.fillCircle(cornerRadius, cornerRadius, cornerRadius);
        shape.fillCircle(width - cornerRadius, cornerRadius, cornerRadius);
        shape.fillCircle(cornerRadius, height - cornerRadius, cornerRadius);
        shape.fillCircle(width - cornerRadius, height - cornerRadius, cornerRadius);

        shape.fillRectangle(0, cornerRadius, width, height - 2 * cornerRadius);
        shape.fillRectangle(cornerRadius, 0, width - 2 * cornerRadius, height);

        return shape;
    }

    /**
     * Creates a circle texture of the give dimensions
     * @param radius circle's radius
     * @param color the color of the circle
     * @return A texture
     */
    public static Pixmap createCircle(int radius, Color color) {
        Pixmap shape = new Pixmap(2 * radius, 2 * radius, Pixmap.Format.RGBA4444);

        shape.setColor(color);
        shape.fillCircle(radius, radius, radius);

        return shape;
    }

    public static Pixmap createCloseButton(int radius, Color xColor, Color circleColor) {
        int length = 2 * radius;

        SpriteBatch spriteBatch = new SpriteBatch();
        Pixmap circle = createCircle(radius, circleColor);

        FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, length, length,false);
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        frameBuffer.begin();

        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Matrix4 m = new Matrix4();
        m.setToOrtho2D(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());

        spriteBatch.setProjectionMatrix(m);
        spriteBatch.begin();
        spriteBatch.draw(new Texture(circle),0,0);

        spriteBatch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(xColor);
        shapeRenderer.setProjectionMatrix(m);

        shapeRenderer.rectLine(new Vector2(radius / 2, radius / 2),new Vector2(length - radius / 2, length - radius / 2), 3);
        shapeRenderer.rectLine(new Vector2(radius / 2, length - radius / 2),new Vector2(length - radius / 2, radius / 2), 3);

        shapeRenderer.end();

        ByteBuffer buf;
        Pixmap pixmap = new Pixmap(length, length, Pixmap.Format.RGBA8888);
        buf = pixmap.getPixels();

        Gdx.gl.glReadPixels(0, 0, length, length, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, buf);

        frameBuffer.end();
        circle.dispose();

        return pixmap;
    }
}
