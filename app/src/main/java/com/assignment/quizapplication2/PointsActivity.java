package com.assignment.quizapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PointsActivity extends AppCompatActivity implements PointsFragment.ScoreListListener {

    private PointsFragment mPointsFragment;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        mPointsFragment = (PointsFragment) getFragmentManager().findFragmentById(R.id.points_fragment);
        mBundle = mPointsFragment.getmBundle();
    }

    @Override
    public void itemClicked(int position) {
        Intent intent = new Intent(PointsActivity.this, QuestionActivity.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }
}
