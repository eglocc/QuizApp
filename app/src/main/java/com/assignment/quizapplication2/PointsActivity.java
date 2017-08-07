package com.assignment.quizapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static com.assignment.quizapplication2.QuizConstants.CLICKED_CATEGORY_POSITION;
import static com.assignment.quizapplication2.QuizConstants.CLICKED_QUESTION_POSITION;

public class PointsActivity extends AppCompatActivity implements PointsFragment.QuestionListListener {

    private PointsFragment mPointsFragment;
    private Bundle mBundle;

    private int mClickedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        mBundle = getIntent().getExtras();

        mPointsFragment = (PointsFragment) getFragmentManager().findFragmentById(R.id.points_fragment);
        mClickedCategory = mBundle.getInt(CLICKED_CATEGORY_POSITION);
        mPointsFragment.setmCategoryId(mClickedCategory);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    public void questionClicked(int position) {
        Intent intent = new Intent(PointsActivity.this, QuestionActivity.class);
        mBundle.putInt(CLICKED_QUESTION_POSITION, position);
        intent.putExtras(mBundle);
        //displayBundleOnLog(mBundle);
        startActivity(intent);
    }

    //For debugging purposes
    public static void displayBundleOnLog(Bundle b) {
        for (String s : b.keySet()) {
            Log.d(s, b.get(s).toString());
        }
    }
}
