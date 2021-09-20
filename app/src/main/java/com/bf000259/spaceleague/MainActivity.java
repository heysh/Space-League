package com.spaceleague;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Initial class - responsible for showing the main menu that is used to navigate through the game.
 * @author Harshil Surendralal
 */
public class MainActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private int level;
    private String name;

    /**
     * Create and launch the GameActivity.
     */
    private void launchGame() {
        Intent play = new Intent(MainActivity.this, GameActivity.class);
        play.putExtra("level", level);  // add the level the user selected
        play.putExtra("name", name);  // add the name of the user
        startActivity(play);
    }

    /**
     * Displays a dialog to the user, from which they can select the difficulty they would like to play.
     */
    private void selectDifficultyDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("SELECT DIFFICULTY");  // title of the dialog

        // create an easy button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "EASY", (dialog, which) -> {
            level = 1;
            launchGame();
        });

        // create a medium button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "MEDIUM", (dialog, which) -> {
            level = 2;
            launchGame();
        });

        // create a hard button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "HARD", (dialog, which) -> {
            level = 3;
            launchGame();
        });

        alertDialog.show();

        // get the buttons from the dialog
        Button easy = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        Button medium = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button hard = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

        // lay out the buttons in the dialog evenly
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) hard.getLayoutParams();
        layoutParams.weight = 10;
        easy.setLayoutParams(layoutParams);
        medium.setLayoutParams(layoutParams);
        hard.setLayoutParams(layoutParams);
    }

    /**
     * Update the "enter name" text to match the user's name.
     */
    private void updateEnterNameText() {
        TextView enterName = findViewById(R.id.enterName);
        enterName.setText(name);
    }

    /**
     * Save the user's name onto the device.
     */
    private void saveNameLocally() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", name);
        editor.apply();
    }

    /**
     * Displays a dialog to the user, in which they can enter their name, or exit the dialog.
     */
    private void enterNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter your name");  // title of the dialog

        // input field in which the user can enter their name
        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // create an OK button
        builder.setPositiveButton("OK", (dialog, which) -> {
            name = input.getText().toString();
            updateEnterNameText();
            saveNameLocally();
        });

        // create a cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Create and launch the HighScoresActivity.
     */
    private void showHighScores() {
        Intent highScores = new Intent(MainActivity.this, HighScoresActivity.class);
        startActivity(highScores);
    }

    /**
     * Create and launch the InformationActivity.
     */
    private void showInformation() {
        Intent information = new Intent(MainActivity.this, InformationActivity.class);
        startActivity(information);
    }

    /**
     * Display the view of the splash screen and set up the buttons.
     * @param savedInstanceState The data from this activity's previous initialisation.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Helper.hideNavigationBar(this);

        prefs = getSharedPreferences("spaceLeague", MODE_PRIVATE);
        name = prefs.getString("name", "Anon");

        setContentView(R.layout.activity_main);

        Helper.animateBackground(this);

        // create the play button
        findViewById(R.id.play).setOnClickListener(v -> selectDifficultyDialog());

        // create the enter name button
        findViewById(R.id.enterName).setOnClickListener(v -> enterNameDialog());

        // create the high scores button
        findViewById(R.id.highScores).setOnClickListener(v -> showHighScores());

        // create the information button
        findViewById(R.id.information).setOnClickListener(v -> showInformation());

        // set the name of the user if they have set it before
        TextView enterName = findViewById(R.id.enterName);
        enterName.setText(prefs.getString("name", "ENTER NAME"));

        // display the highest score the user has achieved, if they have played before
        // otherwise display 0
        TextView highScores = findViewById(R.id.highScores);
        String highScoresText = "HIGH SCORE  " + prefs.getInt("highScore", 0);
        highScores.setText(highScoresText);
    }
}