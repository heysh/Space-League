package com.spaceleague;

import android.content.res.Resources;

/**
 * Class representing an enemy of medium difficulty.
 * @author Harshil Surendralal
 */
public class EnemyMedium extends Enemy {

    /**
     * Create an object of type EnemyMedium, passing the speed, score, resources, and bitmap graphic to
     * the superclass' constructor.
     * @param res The resources of the application.
     */
    public EnemyMedium(Resources res) {
        super(25, 2, res, R.drawable.enemymedium);
    }
}
