package com.bf000259.spaceleague;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.bf000259.spaceleague.GameView.screenRatioX;
import static com.bf000259.spaceleague.GameView.screenRatioY;

public class EnemyMedium extends Enemy {
    public EnemyMedium(Resources res) {
        bitmap = BitmapFactory.decodeResource(res, R.drawable.enemymedium);

        width = bitmap.getWidth() / 8;
        height = bitmap.getHeight() / 8;

        width = (int) (screenRatioX * width);
        height = (int) (screenRatioY * height);

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        y = -height;

        speed = 25;
    }
}
