package com.bf000259.spaceleague;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.bf000259.spaceleague.GameView.screenRatioX;
import static com.bf000259.spaceleague.GameView.screenRatioY;

public class Player extends Object {
    boolean isGoingUp, isGoingDown, isGoingLeft, isGoingRight;

    public Player(int screenY, Resources res) {
        bitmap = BitmapFactory.decodeResource(res, R.drawable.player);

        width = bitmap.getWidth() / 6;
        height = bitmap.getHeight() / 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        x = (int) (64 * screenRatioX);
        y = screenY / 2;

        speed = 30;
    }
}