package com.assignment.quizapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import static com.assignment.quizapplication2.LoginActivity.sUser;

public class QuestionActivity extends AppCompatActivity implements QuestionFragment.AnswerListener {

    private Bundle mBundle;
    private QuestionFragment mQuestionFragment;
    private QuestionTimer mTimer;
    private Handler mHandler = new Handler();

    private int mCategoryId;
    private int mQuestionId;
    private Question mQuestion;
    private List<Question> mQuestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mBundle = getIntent().getExtras();

        mQuestionFragment = (QuestionFragment) getFragmentManager().findFragmentById(R.id.question_fragment);

        //Sets the question accordingly
        mCategoryId = mBundle.getInt(CategoryActivity.CLICKED_CATEGORY_POSITION);
        mQuestionId = mBundle.getInt(PointsActivity.CLICKED_QUESTION_POSITION);
        mQuestionList = Category.mCategoryList.get(mCategoryId).getmQuestionList();
        mQuestion = mQuestionList.get(mQuestionId);
        mTimer = new QuestionTimer(this, mHandler);

        //Transfer important data to the fragment
        mQuestionFragment.setmCategoryId(mCategoryId);
        mQuestionFragment.setmQuestionId(mQuestionId);
        mQuestionFragment.setmTimer(mTimer);
        mQuestionFragment.setmHandler(mHandler);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(QuestionActivity.this, PointsActivity.class);
        mTimer.setmRunning(false);
        mHandler.removeCallbacks(mTimer);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    public void answerClicked(View v, String clickedAnswer, boolean answerCorrect) {
        mQuestion.setmHasBeenAnswered(true);
        mQuestion.setmClickedAnswer(clickedAnswer);

        ListView answers = (ListView) findViewById(R.id.answer_list_view);
        int answer_button_count = answers.getChildCount();
        for (int i = 0; i < answer_button_count; i++) {
            answers.getChildAt(i).setOnClickListener(null);
        }

        if (answerCorrect) {
            mQuestion.setmAnsweredCorrectly(true);
            sUser.answeredCorrectly(mQuestion.getmScore());
            v.setBackground(getDrawable(R.drawable.rounded_button_green));
        } else {
            mQuestion.setmAnsweredWrong(true);
            sUser.answeredWrong(mQuestion.getmScore());
            v.setBackground(getDrawable(R.drawable.rounded_button_red));
        }

        goNext();
    }

    public void goNext() {
        int quizFinished = isAllQuestionsAnswered(mQuestionId);
        if (quizFinished != -1) {

            if (mQuestionId < mQuestionList.size() - 1)
                mQuestionId++;
            else
                mQuestionId = quizFinished;

            mBundle.putInt(PointsActivity.CLICKED_QUESTION_POSITION, mQuestionId);
            Intent intent = new Intent(QuestionActivity.this, QuestionActivity.class);
            intent.putExtras(mBundle);
            startActivity(intent);
        } else {
            Intent intent = new Intent(QuestionActivity.this, LoginActivity.class);
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }

    private int isAllQuestionsAnswered(int id) {
        for (Question q : mQuestionList) {
            if (!q.getmHasBeenAnswered() && q.getmRemainingTime() > 0) {
                id = q.getmQuestionId();
                return id;
            }
        }
        return -1;
    }
}
