package com.bf000259.spaceleague;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InformationActivity extends AppCompatActivity {
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

    private void returnToMainActivity() {
        Intent main = new Intent(InformationActivity.this, MainActivity.class);
        startActivity(main);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        MainActivity.hideNavigationBar(this);
        MainActivity.animateBackground(this);
        animatePlayer(this);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMainActivity();
            }
        });
    }
}
