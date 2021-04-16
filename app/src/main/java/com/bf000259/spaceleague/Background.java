package com.bf000259.spaceleague;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Class representing the background.
 * @author Harshil Surendralal bf000259
 */
public class Background {
    int x = 0, y = 0;
    Bitmap bg;

    /**
     * Create an object of type Background that holds the background image, and its co-ordinates.
     * @param screenX The width of the background image.
     * @param screenY The height of the background image.
     * @param res The resources of the application.
     */
    Background(int screenX, int screenY, Resources res) {
        bg = BitmapFactory.decodeResource(res, R.drawable.background);
        bg = Bitmap.createScaledBitmap(bg, screenX, screenY, false);
    }
}
