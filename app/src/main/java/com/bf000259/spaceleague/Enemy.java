package com.bf000259.spaceleague;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

import static com.bf000259.spaceleague.GameView.screenRatioX;
import static com.bf000259.spaceleague.GameView.screenRatioY;
import static com.bf000259.spaceleague.GameView.screenX;
import static com.bf000259.spaceleague.GameView.screenY;

/**
 * Class representing an enemy.
 * @author Harshil Surendralal bf000259
 */
public class Enemy extends Object {
    protected int score;

    /**
     * Get a y co-ordinate that would allow the enemy to hit the player at the current point in time.
     * @param player The player into which the enemy will be colliding.
     * @return The new y co-ordinate of the enemy.
     */
    protected int getTargetedY(Player player) {
        Random r = new Random();
        int y, enemyUpperBound, enemyLowerBound, enemyRange;

        // the highest the enemy can be while still colliding with the player
        enemyUpperBound = player.y - (int) (0.9 * height);

        // the lowest the enemy can be while still colliding with the player
        enemyLowerBound = player.y + player.getHeight() - (int) (0.1 * height);

        // upper bound is off the screen
        if (enemyUpperBound < 0) {
            enemyUpperBound = 0;
        }

        // lower bound is off the screen
        if (enemyLowerBound > screenY - height) {
            enemyLowerBound = screenY - height;
        }

        // the range of possible y values the enemy could be in
        enemyRange = enemyLowerBound - enemyUpperBound;

        // pick a random value in this range
        y = r.nextInt(enemyRange);

        // add the upper bound to the random value to make sure the enemy collides with the player
        y += enemyUpperBound;

        return y;
    }

    /**
     * Get a random y co-ordinate.
     * @return The y co-ordinate.
     */
    private int getRandomY() {
        Random r = new Random();
        return r.nextInt(screenY - this.height);
    }

    /**
     * Create an object of type Enemy that holds the enemy image, its co-ordinates, and its speed.
     * @param speed The speed of the enemy.
     * @param score The score the player will be awarded with if they successfully evade this enemy.
     * @param res The resources of the application.
     * @param id The bitmap graphic of the enemy.
     */
    public Enemy(int speed, int score, Resources res, int id) {
        this.speed = speed;
        this.score = score;

        bitmap = BitmapFactory.decodeResource(res, id);

        width = bitmap.getWidth() / 8;
        height = bitmap.getHeight() / 8;

        width = (int) (screenRatioX * width);
        height = (int) (screenRatioY * height);

        x = 2 * screenX;
        y = getRandomY();

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
