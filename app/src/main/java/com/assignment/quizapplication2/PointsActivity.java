package com.assignment.quizapplication2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class PointsActivity extends AppCompatActivity implements PointsFragment.ScoreListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
    }

    @Override
    public void itemClicked(int position) {
        Log.d("position", String.valueOf(position));
        Log.d("clicked", "true");
    }
}
