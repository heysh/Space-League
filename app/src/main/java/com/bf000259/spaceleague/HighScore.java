package com.bf000259.spaceleague;

/**
 * Class that is used when pushing a high score to the Firebase database.
 * @author Harshil Surendralal bf000259
 */
public class HighScore {
    public String name;
    public int score;

    /**
     * Create an object of type HighScore that holds the user's name, and the score they achieved.
     * @param name The name of the user.
     * @param score The score the user achieved.
     */
    public HighScore(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
