package com.assignment.quizapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PointsActivity extends AppCompatActivity implements PointsFragment.ScoreListListener {

    private PointsFragment mPointsFragment;
    private Bundle mBundle;
    private List<Category> mCategoryList;
    private Category mCurrentCategory;
    private int mCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        mBundle = getIntent().getExtras();

        mPointsFragment = (PointsFragment) getFragmentManager().findFragmentById(R.id.points_fragment);
        mCategoryId = mBundle.getInt(CategoryFragment.CLICKED_CATEGORY_POSITION);
        mCategoryList = mBundle.getParcelableArrayList(CategoryFragment.CATEGORY_LIST);
        mCurrentCategory = mCategoryList.get(mCategoryId);
        mPointsFragment.setmCurrentCategory(mCurrentCategory);
    }

    @Override
    public void itemClicked(int position) {
        Intent intent = new Intent(PointsActivity.this, QuestionActivity.class);
        mBundle.putInt(PointsFragment.CLICKED_QUESTION_POSITION, position);
        mBundle.putParcelable(PointsFragment.CURRENT_CATEGORY, mCurrentCategory);
        mBundle.putParcelableArrayList(PointsFragment.QUESTION_LIST, mCurrentCategory.getmQuiz().getMyQuestionBank());
        intent.putExtras(mBundle);
        displayBundleOnLog();
        startActivity(intent);
    }

    private void displayBundleOnLog() {
        for (String s : mBundle.keySet()) {
            switch (s) {
                case LoginActivity.USER:
                    Log.d(s, ((User) mBundle.get(s)).getmNickname());
                    break;
                case CategoryFragment.CATEGORY_LIST:
                    List<Category> categoryList = (ArrayList<Category>) mBundle.get(s);
                    Log.d(s, categoryList.toString());
                    break;
                case PointsFragment.QUESTION_LIST:
                    List<Question> questionList = (ArrayList<Question>) mBundle.get(s);
                    for (Question q : questionList) {
                        Log.d(s + ",Question_" + String.valueOf(q.getmQuestionId()), q.getmText());
                        Log.d(s + ",Answers", q.getmAnswerMap().toString());
                    }
                    break;
                default:
                    Log.d(s, mBundle.get(s).toString());
            }
        }
    }
}
