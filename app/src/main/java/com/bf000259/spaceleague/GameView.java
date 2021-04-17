package com.bf000259.spaceleague;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

/**
 * Main driver class of the game - responsible for the actual gameplay.
 * @author Harshil Surendralal bf000259
 */
public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private GameActivity activity;
    private Accelerometer accelerometer;
    private boolean isPlaying, levelChanged = false;
    protected static int screenX, screenY;
    private int level, score = 0;
    private static final int FRAMES_PER_SECOND = 60, BACKGROUND_SPEED = 2;
    protected static float screenRatioX, screenRatioY;
    private String name;
    private SharedPreferences prefs;
    private Paint paint;
    private Player player;
    private ArrayList<Enemy> activeEnemies, removeEnemies;
    private Random random;
    private Background bg1, bg2;

    /**
     * Configures the paint for drawing text onto the screen.
     */
    private void configurePaint() {
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);
    }

    /**
     * Create an object of type Enemy, depending on which level the user is currently playing.
     * @return An enemy with the correct difficulty for the level.
     */
    private Enemy createCorrectEnemy() {
        // easy
        if (level == 1) {
            return new EnemyEasy(getResources());
        }

        // medium
        if (level == 2) {
            return new EnemyMedium(getResources());
        }

        // hard
        return new EnemyHard(getResources());
    }

    /**
     * Create all three enemies.
     */
    private void createEnemies() {
        for (int i = 0; i < 3; i++) {
            Enemy enemy = createCorrectEnemy();

            // offset second enemy by a third of the screen
            if (i == 1) {
                enemy.x = (int) (enemy.x + ((screenX + enemy.width) / 3));
            }

            // offset third enemy by two thirds of the screen
            if (i == 2) {
                enemy.x = (int) (enemy.x + (2 * (screenX + enemy.width) / 3));
            }

            activeEnemies.add(enemy);
        }
    }

    /**
     * Set up all of the components that will be used during play.
     * @param activity The GameActivity.
     * @param screenX The width of the device's screen.
     * @param screenY The height of the device's screen.
     * @param level The level selected by the user.
     * @param name The name of the user.
     */
    public GameView(GameActivity activity, int screenX, int screenY, int level, String name) {
        super(activity);

        this.activity = activity;
        accelerometer = new Accelerometer(activity);

        prefs = activity.getSharedPreferences("spaceLeague", Context.MODE_PRIVATE);

        this.screenX = screenX;
        this.screenY = screenY;
        this.level = level;
        this.name = name;

        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        bg1 = new Background(screenX, screenY, getResources());
        bg2 = new Background(screenX, screenY, getResources());
        bg2.x = screenX;

        player = new Player(screenY, getResources());

        paint = new Paint();
        configurePaint();

        activeEnemies = new ArrayList<>();
        removeEnemies = new ArrayList<>();
        createEnemies();

        random = new Random();
    }

    /**
     * Resume the game.
     */
    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Pause the game.
     */
    public void pause() {
        isPlaying = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Move the x co-ordinate of the background.
     * @param bg The background that is to be updated.
     */
    private void updateBackground(Background bg) {
        bg.x -= BACKGROUND_SPEED;
    }

    /**
     * Update both backgrounds.
     */
    private void updateBackgrounds() {
        updateBackground(bg1);
        updateBackground(bg2);
    }

    /**
     * Check if the background has gone off the left-hand side of the screen.
     * @param bg The background that is to be checked.
     */
    private void checkBackground(Background bg) {
        if (bg.x + bg.bg.getWidth() < 0) {
            bg.x = screenX - BACKGROUND_SPEED;
        }
    }

    /**
     * Check both backgrounds.
     */
    private void checkBackgrounds() {
        checkBackground(bg1);
        checkBackground(bg2);
    }

    /**
     * Reset the values recorded by the accelerometer.
     */
    private void resetAccelerometerReadings() {
        player.isGoingLeft = false;
        player.isGoingRight = false;
    }

    /**
     * Check if the user is tilting their phone leftwards or rightwards.
     */
    private void checkAccelerometer() {
        // user is tilting to the left
        if (accelerometer.tilt <= -1) {
            player.isGoingLeft = true;
        }

        // user is tilting to the right
        if (accelerometer.tilt >= 1) {
            player.isGoingRight = true;
        }
    }

    /**
     * Update the co-ordinates of the player with the direction they are going.
     */
    private void updatePlayer() {
        // move the player up
        if (player.isGoingUp) {
            player.y -= player.speed * screenRatioY;
        }

        // move the player down
        if (player.isGoingDown) {
            player.y += player.speed * screenRatioY;
        }

        // move the player left
        if (player.isGoingLeft) {
            player.x -= player.speed * screenRatioY;
        }

        // move the player right
        if (player.isGoingRight) {
            player.x += player.speed * screenRatioY;
        }
    }

    /**
     * Ensure the player is not going off the sides of the screen.
     */
    private void checkPlayer() {
        // player is limited to the top of the screen
        if (player.y < 0) {
            player.y = 0;
        }

        // player is limited to the bottom of the screen
        if (player.y >= screenY - player.height) {
            player.y = screenY - player.height;
        }

        // player is limited to the left-hand side of the screen
        if (player.x < 0) {
            player.x = 0;
        }

        // player is limited to the right-hand side of the screen
        if (player.x >= screenX - player.width) {
            player.x = screenX - player.width;
        }
    }

    /**
     * Update the position of the enemies.
     */
    private void updateEnemyPositions() {
        for (Enemy enemy : activeEnemies) {
            enemy.x -= enemy.speed;
        }
    }

    /**
     * Increase difficulty if the player is doing well in their level.
     */
    private void checkScore() {
        // medium difficulty
        if (level == 1 && score >= 15) {
            level = 2;
            levelChanged = true;
        }

        // hard difficulty
        if (level == 2 && score >= 40) {
            level = 3;
            levelChanged = true;
        }
    }

    /**
     * Check if the enemies have gone off the left-hand side of the screen. The enemy will be reset
     * to the right-hand side of the screen if this is true.
     */
    private void checkEnemiesOffScreen() {
        for (Enemy enemy : activeEnemies) {
            if (enemy.x + enemy.width < 0) {
                score += enemy.score;  // add the points earned to the user's score
                checkScore();  // check if the user is eligible to move onto the next level

                // if the difficulty has increased, and there are still enemies on the screen,
                // add the enemy to the list of enemies that are to be removed, and move onto the
                // next enemy
                if (levelChanged && !activeEnemies.isEmpty()) {
                    removeEnemies.add(enemy);
                    continue;
                }

                enemy.x = screenX;
                enemy.y = enemy.getTargetedY(player);
            }
        }
    }

    /**
     * Remove all of the enemies that are no longer meant to be drawn onto screen.
     */
    private void checkEnemiesToBeRemoved() {
        for (Enemy enemy : removeEnemies) {
            activeEnemies.remove(enemy);
        }
        removeEnemies.clear();
    }

    /**
     * Check if the enemies have collided with the player. The game will end if this is true,
     * otherwise the user will continue playing.
     */
    private void checkEnemiesHitPlayer() {
        for (Enemy enemy : activeEnemies) {
            if (Rect.intersects(enemy.getRectangle(), player.getRectangle())) {
                isPlaying = false;
            }
        }
    }

    /**
     * Iterate through each enemy, and update their position on the screen. Check each enemy to see
     * whether they have gone off the screen, or hit the player.
     */
    private void updateEnemies() {
        if (activeEnemies.isEmpty()) {
            levelChanged = false;
            createEnemies();
        }

        updateEnemyPositions();
        checkEnemiesOffScreen();
        checkEnemiesToBeRemoved();
        checkEnemiesHitPlayer();
    }

    /**
     * Update the background, the player, and the enemies.
     */
    private void update() {
        updateBackgrounds();
        checkBackgrounds();

        resetAccelerometerReadings();
        checkAccelerometer();
        updatePlayer();
        checkPlayer();

        updateEnemies();
    }

    /**
     * Draw the background onto the canvas.
     * @param canvas The canvas on which the background will be drawn.
     * @param bg The background that is to be drawn.
     */
    private void drawBackground(Canvas canvas, Background bg) {
        canvas.drawBitmap(bg.bg, bg.x, bg.y, paint);
    }

    /**
     * Draw both backgrounds onto the canvas.
     * @param canvas The canvas on which the backgrounds will be drawn.
     */
    private void drawBackgrounds(Canvas canvas) {
        drawBackground(canvas, bg1);
        drawBackground(canvas, bg2);
    }

    /**
     * Draw the score onto the canvas.
     * @param canvas The canvas on which the score will be drawn.
     */
    private void drawScore(Canvas canvas) {
        canvas.drawText(Integer.toString(score), screenX / 2f, 164, paint);
    }

    /**
     * Draw the player onto the canvas.
     * @param canvas The canvas on which the player will be drawn.
     */
    private void drawPlayer(Canvas canvas) {
        canvas.drawBitmap(player.getBitmap(), player.x, player.y, paint);
    }

    /**
     * Draw the enemies onto the canvas.
     * @param canvas The canvas on which the enemies will be drawn.
     */
    private void drawEnemies(Canvas canvas) {
        for (Enemy enemy : activeEnemies) {
            canvas.drawBitmap(enemy.getBitmap(), enemy.x, enemy.y, paint);
        }
    }

    /**
     * Save the score locally if it is a new high score for the user.
     */
    private void updateLocalHighScore() {
        if (prefs.getInt("highScore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highScore", score);
            editor.apply();
        }
    }

    /**
     * Push the user's name and score to the online Firebase database.
     */
    private void uploadHighScoreToFirebase() {
        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference ref = root.getReference("High Scores");
        HighScore post = new HighScore(name, score);
        ref.push().setValue(post);
    }

    /**
     * Wait for two seconds before returning to the main menu.
     */
    private void waitBeforeExiting() {
        try {
            Thread.sleep(2000);
            activity.finish();
            activity.startActivity(new Intent(activity, MainActivity.class));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the high score locally if it is a personal best, upload the score to the Firebase
     * database, and wait a while before returning the user to the main menu.
     */
    private void handleEndgame() {
        updateLocalHighScore();
        uploadHighScoreToFirebase();
        waitBeforeExiting();
    }

    /**
     * Draw the background, the player, the enemies, and the score.
     */
    private void draw() {
        if (getHolder().getSurface().isValid()) {

            // player has hit an enemy
            if (!isPlaying) {
                handleEndgame();
                return;
            }

            Canvas canvas = getHolder().lockCanvas();

            drawBackgrounds(canvas);
            drawPlayer(canvas);
            drawEnemies(canvas);
            drawScore(canvas);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Checks whether the user has pressed on the top half of the screen.
     * @param event The event from which the press will be determined.
     * @return True if the user has pressed on the top half of the screen, otherwise false.
     */
    private boolean isPressedTopHalf(MotionEvent event) {
        return event.getY() < screenY / 2.0;
    }

    /**
     * Checks whether the user has pressed on the bottom half of the screen.
     * @param event The event from which the press will be determined.
     * @return True if the user has pressed on the bottom half of the screen, otherwise false.
     */
    private boolean isPressedBottomHalf(MotionEvent event) {
        return event.getY() > screenY / 2.0;
    }

    /**
     * Checks which vertical direction the player is moving, if any.
     * @param event The event from which the vertical direction will be determined.
     */
    private void checkPlayerDirection(MotionEvent event) {
        player.isGoingUp = isPressedTopHalf(event);
        player.isGoingDown = isPressedBottomHalf(event);
    }

    /**
     * Reset the vertical direction of the player.
     */
    private void resetPlayerDirection() {
        player.isGoingUp = false;
        player.isGoingDown = false;
    }

    /**
     * Determine which vertical direction the user wishes to move.
     * @param event The event from which the vertical direction will be determined.
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            // user pressed down
            case MotionEvent.ACTION_DOWN:
                checkPlayerDirection(event);
                break;

            // user released press
            case MotionEvent.ACTION_UP:
                resetPlayerDirection();
                break;
        }

        return true;
    }

    /**
     * Wait a small amount of time (~17 ms) before updating and drawing all of the objects again.
     */
    private void sleep() {
        try {
            Thread.sleep(1000 / FRAMES_PER_SECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Second thread that is responsible for sleeping. Greatly improves the performance of the game,
     * by producing a smoother and more responsive experience.
     */
    private void refreshThread() {
        Runnable r = () -> {
            sleep();
        };
        Thread refreshThread = new Thread(r);
        refreshThread.start();
    }

    /**
     * Main driver function of the game.
     */
    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            refreshThread();
        }
    }
}
