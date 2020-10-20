package com.kacstudios.game.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public final class FarmaniaFonts {
    public static BitmapFont generateFont(String fontName, int size) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator( Gdx.files.internal(fontName) );
        BitmapFont font = generator.generateFont(parameter);

        generator.dispose();
        return font;
    }
}
