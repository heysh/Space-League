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

public class HighScoresActivity extends AppCompatActivity {
    private Query ref;
    private ArrayList<String> highScores;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private String name, score;

    private void retrieveHighScores(DataSnapshot snapshot) {
        for (DataSnapshot snap : snapshot.getChildren()) {
            name = snap.child("name").getValue().toString();
            score = snap.child("score").getValue().toString();
            highScores.add(name + ": " + score);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        setTitle("High scores");

        listView = (ListView) findViewById(R.id.highScoresList);
        highScores = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, highScores);
        listView.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference().child("High Scores");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                highScores.clear();
                retrieveHighScores();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
