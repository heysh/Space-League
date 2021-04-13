package com.bf000259.spaceleague;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Object {
    protected int x, y, width, height, speed;
    protected Bitmap bitmap;

    protected Bitmap getBitmap() {
        return this.bitmap;
    }

    protected Rect getRectangle() {
        return new Rect(x, y, x + width, y + height);
    }
}
