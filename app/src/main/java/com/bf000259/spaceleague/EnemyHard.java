package com.bf000259.spaceleague;

import android.content.res.Resources;

/**
 * Class representing an enemy of hard difficulty.
 * @author Harshil Surendralal bf000259
 */
public class EnemyHard extends Enemy {

    /**
     * Create an object of type EnemyHard, passing the speed, score, resources, and bitmap graphic to
     * the superclass' constructor.
     * @param res The resources of the application.
     */
    public EnemyHard(Resources res) {
        super(35, 3, res, R.drawable.enemyhard);
    }
}
