package com.assignment.quizapplication2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static com.assignment.quizapplication2.LoginActivity.sUser;

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

        int finishedCategoryPosition = mBundle.getInt(CategoryActivity.CLICKED_CATEGORY_POSITION);
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
        //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        mBundle.putBoolean(CategoryActivity.QUESTION_ON, false);
        mBundle.putBoolean(CategoryActivity.POINTS_ON, true);
        mBundle.putInt(CategoryActivity.CLICKED_QUESTION_POSITION, 0);
        returnIntent.putExtras(mBundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
