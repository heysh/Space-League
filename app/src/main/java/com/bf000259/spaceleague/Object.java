package com.spaceleague;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Abstract class responsible for holding variables, and providing access to these variables. A base
 * class from which the player, and enemies inherit.
 * @author Harshil Surendralal
 */
public abstract class Object {
    protected int x, y, width, height, speed;
    protected Bitmap bitmap;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Rect getRectangle() {
        return new Rect(x, y, x + width, y + height);
    }
}
