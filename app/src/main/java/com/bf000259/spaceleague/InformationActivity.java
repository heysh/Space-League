package com.bf000259.spaceleague;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Class that is responsible for showing the instructions on how to play the game.
 * @author Harshil Surendralal bf000259
 */
public class InformationActivity extends AppCompatActivity {

    /**
     * SLowly moves the player left and right on the screen.
     * @param activity The activity in which the player will be animated.
     */
    private void animatePlayer(Activity activity) {
        final ImageView player = (ImageView) activity.findViewById(R.id.player);
        player.setVisibility(View.VISIBLE);

        final Animation animation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT, 0.2f,
                TranslateAnimation.RELATIVE_TO_PARENT, -0.5f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f);
        animation.setDuration(20000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setInterpolator(new LinearInterpolator());
        player.setAnimation(animation);
    }

    /**
     * Create and launch the MainActivity.
     */
    private void returnToMainActivity() {
        Intent main = new Intent(InformationActivity.this, MainActivity.class);
        startActivity(main);
    }

    /**
     * Display the view of the information screen and set up the button.
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Helper.hideNavigationBar(this);
        Helper.animateBackground(this);
        animatePlayer(this);

        // create the back button
        findViewById(R.id.back).setOnClickListener(v -> returnToMainActivity());
    }
}
