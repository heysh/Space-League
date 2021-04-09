package com.bf000259.spaceleague;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.bf000259.spaceleague.GameView.screenRatioX;
import static com.bf000259.spaceleague.GameView.screenRatioY;

public class Player {
    int x, y, width, height;
    boolean isGoingUp, isGoingDown;
    Bitmap player;

    Player(int screenY, Resources res) {
        player = BitmapFactory.decodeResource(res, R.drawable.player);

        width = player.getWidth() / 6;
        height = player.getHeight() / 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        player = Bitmap.createScaledBitmap(player, width, height, false);

        x = (int) (64 * screenRatioX);
        y = screenY / 2;
    }

    protected Bitmap getPlayer() {
        return player;
    }
}
