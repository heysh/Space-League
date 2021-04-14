package com.bf000259.spaceleague;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

import static com.bf000259.spaceleague.GameView.screenRatioX;
import static com.bf000259.spaceleague.GameView.screenRatioY;
import static com.bf000259.spaceleague.GameView.screenX;
import static com.bf000259.spaceleague.GameView.screenY;

public class Enemy extends Object {
    protected int score, enemyId;
    protected static int enemyCounter = 0;

    private int getRandomY() {
        Random r = new Random();
        return r.nextInt(screenY - this.height);
    }

    public Enemy(int speed, int score, Resources res, int id) {
        x = screenX;
        y = getRandomY();

        this.speed = speed;
        this.score = score;
        this.enemyId = enemyCounter++;

        bitmap = BitmapFactory.decodeResource(res, id);

        width = bitmap.getWidth() / 8;
        height = bitmap.getHeight() / 8;

        width = (int) (screenRatioX * width);
        height = (int) (screenRatioY * height);

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getEnemyId() {
        return enemyId;
    }

    public void setEnemyId(int enemyId) {
        this.enemyId = enemyId;
    }

    public int getEnemyCounter() {
        return enemyCounter;
    }

    public void setEnemyCounter(int enemyCounter) {
        Enemy.enemyCounter = enemyCounter;
    }
}
