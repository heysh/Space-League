package com.bf000259.spaceleague;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class that is responsible for showing high scores achieved by all players.
 * @author Harshil Surendralal bf000259
 */
public class HighScoresActivity extends AppCompatActivity {
    private Query ref;
    private ArrayList<String> highScores;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private String name, score;

    /**
     * Retrieve and store the high scores saved in the online realtime Firebase database.
     * @param snapshot The data that is stored in this Firebase database.
     */
    private void retrieveHighScores(DataSnapshot snapshot) {
        // for every high score entry, concatenate the name and the score, and add to a list
        for (DataSnapshot snap : snapshot.getChildren()) {
            name = snap.child("name").getValue().toString();
            score = snap.child("score").getValue().toString();
            highScores.add(name + ": " + score);
        }

        // reverse the order of the list so it is in descending order
        Collections.reverse(highScores);
    }

    /**
     * Display the view of the high scores screen.
     * @param savedInstanceState The data from this activity's previous initialisation.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        setTitle("High scores");  // title of the screen

        listView = (ListView) findViewById(R.id.highScoresList);
        highScores = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, highScores);
        listView.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference().child("High Scores").orderByChild("score");

        // if there are any changes in the database, get a fresh copy of all the data and display it
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                highScores.clear();
                retrieveHighScores(snapshot);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
