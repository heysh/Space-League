package com.bf000259.spaceleague;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY;
    private float screenRatioX, screenRatioY;
    private Paint paint;
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

    private void checkBackground(Background bg) {
        if (bg.x + bg.bg.getWidth() < 0) {
            bg.x = screenX;
        }
    }

    private void update() {
        bg1.x -= 2 * screenRatioX;
        bg2.x -= 1 * screenRatioX; // should be 2 but my driver issues make it look weird

        checkBackground(bg1);
        checkBackground(bg2);
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(bg1.bg, bg1.x, bg1.y, paint);
            canvas.drawBitmap(bg2.bg, bg2.x, bg2.y, paint);

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

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }
}
