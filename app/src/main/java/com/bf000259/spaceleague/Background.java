package com.bf000259.spaceleague;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int x = 0, y = 0;
    Bitmap bg;

    Background(int screenX, int screenY, Resources res) {
        bg = BitmapFactory.decodeResource(res, R.drawable.background);
        bg = Bitmap.createScaledBitmap(bg, screenX, screenY, false);
    }
}
