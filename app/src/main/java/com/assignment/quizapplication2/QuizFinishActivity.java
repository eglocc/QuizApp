package com.assignment.quizapplication2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static com.assignment.quizapplication2.LoginActivity.sUser;
import static com.assignment.quizapplication2.QuizConstants.CLICKED_CATEGORY_POSITION;
import static com.assignment.quizapplication2.QuizConstants.POINTS_ON;
import static com.assignment.quizapplication2.QuizConstants.QUESTION_ON;

public class QuizFinishActivity extends AppCompatActivity {

    private Bundle mBundle;
    private TextView mNicknameView;
    private TextView mScoreView;
    private TextView mCategoryView;

    private String mCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_finish);

        mBundle = getIntent().getExtras();

        int finishedCategoryPosition = mBundle.getInt(CLICKED_CATEGORY_POSITION);
        mCategoryName = Category.mCategoryList.get(finishedCategoryPosition).toString();

        mNicknameView = (TextView) findViewById(R.id.nickname);
        mScoreView = (TextView) findViewById(R.id.score);
        mCategoryView = (TextView) findViewById(R.id.category);

        mNicknameView.setText(sUser.getmNickname());
        mScoreView.setText(String.valueOf(sUser.getmScore()));
        mCategoryView.setText(mCategoryName);

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        mBundle.putBoolean(QUESTION_ON, false);
        mBundle.putBoolean(POINTS_ON, true);
        returnIntent.putExtras(mBundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
