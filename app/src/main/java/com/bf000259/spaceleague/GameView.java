package com.bf000259.spaceleague;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY;
    protected static float screenRatioX, screenRatioY;
    private Paint paint;
    private Player player;
    private int FRAMES_PER_SECOND = 60;
    private Background bg1, bg2;

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        this.screenX = screenX;
        this.screenY = screenY;

        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        bg1 = new Background(screenX, screenY, getResources());
        bg2 = new Background(screenX, screenY, getResources());

        player = new Player(screenY, getResources());

        bg2.x = screenX;

        paint = new Paint();
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
        bg.x -= 2 * screenRatioX;
    }

    private void updateBackgrounds() {
        updateBackground(bg1);
        updateBackground(bg2);
    }

    private void checkBackground(Background bg) {
        if (bg.x + bg.bg.getWidth() < 0) {
            bg.x = screenX;
        }
    }

    private void checkBackgrounds() {
        checkBackground(bg1);
        checkBackground(bg2);
    }

    private void updatePlayer() {
        if (player.isGoingUp) {
            player.y -= 30 * screenRatioY;
        }

        if (player.isGoingDown) {
            player.y += 30 * screenRatioY;
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

    private void update() {
        updateBackgrounds();
        checkBackgrounds();

        updatePlayer();
        checkPlayer();
    }

    private void drawBackground(Canvas canvas, Background bg) {
        canvas.drawBitmap(bg.bg, bg.x, bg.y, paint);
    }

    private void drawBackgrounds(Canvas canvas) {
        drawBackground(canvas, bg1);
        drawBackground(canvas, bg2);
    }

    private void drawPlayer(Canvas canvas) {
        canvas.drawBitmap(player.getPlayer(), player.x, player.y, paint);
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            drawBackgrounds(canvas);
            drawPlayer(canvas);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000 / FRAMES_PER_SECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }
}
