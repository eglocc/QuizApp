package com.assignment.quizapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class PointsActivity extends AppCompatActivity implements FragmentItemListener {

    public static final String CLICKED_QUESTION_POSITION = "clicked_question_position";
    public static final String SELECTED_QUESTION = "selected_question";

    private PointsFragment mPointsFragment;
    private Bundle mBundle;

    int mClickedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        mBundle = getIntent().getExtras();

        mPointsFragment = (PointsFragment) getFragmentManager().findFragmentById(R.id.points_fragment);
        mClickedCategory = mBundle.getInt(CategoryActivity.CLICKED_CATEGORY_POSITION);
        mPointsFragment.setmCategoryId(mClickedCategory);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PointsActivity.this, CategoryActivity.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    public void itemClicked(int position) {
        Intent intent = new Intent(PointsActivity.this, QuestionActivity.class);
        Question selectedQuestion = Category.mCategoryList.get(mClickedCategory).getmQuestionList().get(position);
        mBundle.putInt(CLICKED_QUESTION_POSITION, position);
        //mBundle.putParcelable(SELECTED_QUESTION, selectedQuestion);
        intent.putExtras(mBundle);
        displayBundleOnLog();
        startActivity(intent);
    }

    private void displayBundleOnLog() {
        for (String s : mBundle.keySet()) {
            Log.d(s, mBundle.get(s).toString());
        }
    }
}
