package com.bf000259.spaceleague;

import android.content.res.Resources;

/**
 * Class representing an enemy of easy difficulty.
 * @author Harshil Surendralal bf000259
 */
public class EnemyEasy extends Enemy {

    /**
     * Create an object of type EnemyEasy, passing the speed, score, resources, and bitmap graphic to
     * the superclass' constructor.
     * @param res The resources of the application.
     */
    public EnemyEasy(Resources res) {
        super(15, 1, res, R.drawable.enemyeasy);
    }
}
