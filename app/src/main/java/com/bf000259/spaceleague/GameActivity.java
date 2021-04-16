package com.bf000259.spaceleague;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Class that is responsible for setting up the game.
 * @author Harshil Surendralal bf000259
 */
public class GameActivity extends AppCompatActivity {
    private GameView gameView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Helper.hideNavigationBar(this);

        // extract the level and name
        Bundle b = getIntent().getExtras();
        int level = b.getInt("level");
        String name = b.getString("name");

        // get the dimensions of the device's screen
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        gameView = new GameView(this, dm.widthPixels, dm.heightPixels, level, name);

        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}