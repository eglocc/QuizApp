package com.assignment.quizapplication2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import static com.assignment.quizapplication2.LoginActivity.sUser;

public class QuestionActivity extends AppCompatActivity implements QuestionFragment.AnswerListener, QuizConstants {

    public static boolean sQuizFinished;

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
        mCategoryId = mBundle.getInt(CLICKED_CATEGORY_POSITION);
        mQuestionId = mBundle.getInt(CLICKED_QUESTION_POSITION);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                goToPointsActivity();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void onBackPressed() {
        if (sQuizFinished) {
            if (mQuestionId > 0) {
                mQuestionId--;
                selfIntent();
            } else
                goToPointsActivity();
        }
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
        int unAnsweredQuestionIndex = findUnAnsweredQuestion(mQuestionId, mQuestionList);

        if (unAnsweredQuestionIndex != -1) {
            mQuestionId = unAnsweredQuestionIndex;
            selfIntent();
        } else {
            sQuizFinished = true;
            goToQuizFinishActivity();
        }
    }

    public void selfIntent() {
        mBundle.putInt(CLICKED_QUESTION_POSITION, mQuestionId);
        Intent intent = new Intent(QuestionActivity.this, QuestionActivity.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    private void goToQuizFinishActivity() {
        Intent intent = new Intent(this, QuizFinishActivity.class);
        intent.putExtras(mBundle);
        startActivityForResult(intent, 1);
    }

    private void goToCategoryActivity() {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    private void goToPointsActivity() {
        Intent intent = new Intent(this, PointsActivity.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    public int findUnAnsweredQuestion(int id, List<Question> list) {
        for (int i = id; i < list.size(); i++) {
            Question q = list.get(i);
            if (!q.getmHasBeenAnswered())
                return q.getmQuestionId();
        }
        for (int i = id; i >= 0; i--) {
            Question q = list.get(i);
            if (!q.getmHasBeenAnswered())
                return q.getmQuestionId();
        }
        return -1;
    }
}
