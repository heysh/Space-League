package com.bf000259.spaceleague;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;

public class PopUp extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * 0.7);
        int height = (int) (dm.heightPixels * 0.4);

        getWindow().setLayout(width, height);

        Intent i = new Intent(this, GameActivity.class);

        findViewById(R.id.easy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("level", 1);
                startActivity(i);
            }
        });

        findViewById(R.id.medium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("level", 2);
                startActivity(i);
            }
        });

        findViewById(R.id.hard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("level", 3);
                startActivity(i);
            }
        });
    }
}
