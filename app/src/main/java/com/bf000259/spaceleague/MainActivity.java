package com.bf000259.spaceleague;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private int level;

    private void animateBackground() {
        final ImageView bg1 = (ImageView) findViewById(R.id.backgroundOne);
        final ImageView bg2 = (ImageView) findViewById(R.id.backgroundTwo);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);

        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = bg1.getWidth();
                final float translationX = width * progress;

                bg1.setTranslationX(-translationX);
                bg2.setTranslationX(-(translationX - width));
            }
        });
        animator.start();
    }

    private void launchGame() {
        Intent play = new Intent(MainActivity.this, GameActivity.class);
        play.putExtra("level", level);
        startActivity(play);
    }

    private void selectDifficultyDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("SELECT DIFFICULTY");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "EASY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                level = 1;
                launchGame();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "MEDIUM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                level = 2;
                launchGame();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "HARD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                level = 3;
                launchGame();
            }
        });

        alertDialog.show();

        Button easy = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        Button medium = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button hard = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) hard.getLayoutParams();
        layoutParams.weight = 10;
        easy.setLayoutParams(layoutParams);
        medium.setLayoutParams(layoutParams);
        hard.setLayoutParams(layoutParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        animateBackground();

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDifficultyDialog();
            }
        });

        TextView highScores = findViewById(R.id.highScores);
        SharedPreferences prefs = getSharedPreferences("spaceLeague", MODE_PRIVATE);
        highScores.setText("HIGH SCORE  " + prefs.getInt("highScore", 0));
    }
}