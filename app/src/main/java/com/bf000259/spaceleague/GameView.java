package com.bf000259.spaceleague;

// TODO: add high scores table
// TODO: change the way the player is controlled -> drag and hold
// TODO: add a pause between level changes

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private GameActivity activity;
    private boolean isPlaying;
    protected static int screenX, screenY;
    private int level, score = 0, replaceEnemies = 0;
    private static final int FRAMES_PER_SECOND = 60, BACKGROUND_SPEED = 2;
    protected static float screenRatioX, screenRatioY;
    private SharedPreferences prefs;
    private Paint paint;
    private Player player;
    private Enemy[] enemies;
    private Random random;
    private Background bg1, bg2;

    private void configurePaint() {
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);
    }

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

    private void createEnemies() {
        for (int i = 0; i < 3; i++) {
            Enemy enemy = createCorrectEnemy();

            // offset second enemy by a third of the screen
            if (i == 1) {
                enemy.x = (int) (enemy.x + (640 * screenRatioX));
            }

            // offset third enemy by two thirds of the screen
            if (i == 2) {
                enemy.x = (int) (enemy.x + (1280 * screenRatioX));
            }

            enemies[i] = enemy;
        }
    }

    public GameView(GameActivity activity, int screenX, int screenY, int level) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("spaceLeague", Context.MODE_PRIVATE);

        this.screenX = screenX;
        this.screenY = screenY;
        this.level = level;

        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        bg1 = new Background(screenX, screenY, getResources());
        bg2 = new Background(screenX, screenY, getResources());
        bg2.x = screenX;

        player = new Player(screenY, getResources());

        paint = new Paint();
        configurePaint();

        enemies = new Enemy[3];
        createEnemies();

        random = new Random();
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        isPlaying = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateBackground(Background bg) {
        bg.x -= BACKGROUND_SPEED;
    }

    private void updateBackgrounds() {
        updateBackground(bg1);
        updateBackground(bg2);
    }

    private void checkBackground(Background bg) {
        if (bg.x + bg.bg.getWidth() < 0) {
            bg.x = screenX - BACKGROUND_SPEED;
        }
    }

    private void checkBackgrounds() {
        checkBackground(bg1);
        checkBackground(bg2);
    }

    private void updatePlayer() {
        if (player.isGoingUp) {
            player.y -= player.speed * screenRatioY;
        }

        if (player.isGoingDown) {
            player.y += player.speed * screenRatioY;
        }
    }

    private void checkPlayer() {
        if (player.y < 0) {
            player.y = 0;
        }

        if (player.y >= screenY - player.height) {
            player.y = screenY - player.height;
        }
    }

    private void replaceEnemy(Enemy enemy) {
        int index = enemy.enemyId;  // get index of enemy
        enemies[index] = null;  // delete enemy
        enemy = createCorrectEnemy();  // create correct enemy
        enemy.enemyId = index;  // set the correct ID for the new enemy
        Enemy.enemyCounter--;  // decrement the enemy counter
        enemies[index] = enemy;  // insert the new enemy into the array of enemies
    }

    private void checkEnemyOffScreen(Enemy enemy) {
        if (enemy.x + enemy.width < 0) {
            score += enemy.score;

            if (replaceEnemies > 0) {
                replaceEnemy(enemy);
                replaceEnemies--;
            }

            enemy.x = screenX;
            enemy.y = random.nextInt(screenY - enemy.height);
        }
    }

    private void checkEnemyHitPlayer(Enemy enemy) {
        if (Rect.intersects(enemy.getRectangle(), player.getRectangle())) {
            isPlaying = false;
        }
    }

    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.x -= enemy.speed;
            checkEnemyOffScreen(enemy);
            checkEnemyHitPlayer(enemy);
        }
    }

    private void checkScore() {
        if (level == 1 && score > 15) {
            level = 2;
            replaceEnemies = 3;
        }

        if (level == 2 && score > 40) {
            level = 3;
            replaceEnemies = 3;
        }
    }

    private void update() {
        updateBackgrounds();
        checkBackgrounds();

        updatePlayer();
        checkPlayer();

        updateEnemies();

        checkScore();
    }

    private void drawBackground(Canvas canvas, Background bg) {
        canvas.drawBitmap(bg.bg, bg.x, bg.y, paint);
    }

    private void drawBackgrounds(Canvas canvas) {
        drawBackground(canvas, bg1);
        drawBackground(canvas, bg2);
    }

    private void drawScore(Canvas canvas) {
        canvas.drawText(Integer.toString(score), screenX / 2f, 164, paint);
    }

    private void drawPlayer(Canvas canvas) {
        canvas.drawBitmap(player.getBitmap(), player.x, player.y, paint);
    }

    private void drawEnemies(Canvas canvas) {
        for (Enemy enemy : enemies) {
            canvas.drawBitmap(enemy.getBitmap(), enemy.x, enemy.y, paint);
        }
    }

    private void updateHighScore() {
        if (prefs.getInt("highScore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highScore", score);
            editor.apply();
        }
    }

    private void waitBeforeExiting() {
        try {
            Thread.sleep(2000);
            activity.finish();
            activity.startActivity(new Intent(activity, MainActivity.class));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleEndgame() {
        updateHighScore();
        waitBeforeExiting();
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
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

    private boolean isPressedTopHalf(MotionEvent event) {
        if (event.getY() < screenY / 2) {
            return true;
        }
        return false;
    }

    private boolean isPressedBottomHalf(MotionEvent event) {
        if (event.getY() > screenY / 2) {
            return true;
        }
        return false;
    }

    private void checkPlayerDirection(MotionEvent event) {
        player.isGoingUp = isPressedTopHalf(event);
        player.isGoingDown = isPressedBottomHalf(event);
    }

    private void resetPlayerDirection() {
        player.isGoingUp = false;
        player.isGoingDown = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                checkPlayerDirection(event);
                break;
            case MotionEvent.ACTION_UP:
                resetPlayerDirection();
                break;
        }

        return true;
    }

    private void sleep() {
        try {
            Thread.sleep(1000 / FRAMES_PER_SECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void refreshThread() {
        Runnable r = () -> {
            sleep();
        };
        Thread refreshThread = new Thread(r);
        refreshThread.start();
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            refreshThread();
        }
    }
}
