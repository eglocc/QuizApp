package com.assignment.quizapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class QuestionActivity extends AppCompatActivity implements QuestionFragment.AnswerListener {

    private class ActivityTimer extends Timer {

        private boolean mRanOutOfTime;

        public ActivityTimer(int seconds) {
            super(seconds);
        }

        @Override
        public void run() {
            int seconds = getmSeconds();
            boolean running = getmRunning();
            String time = String.format("00:%02d", seconds);
            mRemainingTimeView.setText(time);
            if (running && seconds > 0) {
                Log.d("sec", String.valueOf(seconds));
                decrementmSeconds();
                mQuestion.decrementmRemainingTime();
            } else if (seconds <= 0) {
                mRanOutOfTime = true;
                setmRunning(false);
                msUser.answeredWrong(mQuestion.getmScore());
                mRemainingTimeView.setBackground(getDrawable(R.drawable.score_button_red));
                mRemainingTimeView.setText(getResources().getString(R.string.times_up));
                ListView answerList = (ListView) findViewById(R.id.answer_list_view);
                Button button = (Button) answerList.findViewWithTag("true_answer");
                button.setBackground(getDrawable(R.drawable.rounded_button_green));
            }
            if (!mRanOutOfTime)
                msHandler.postDelayed(this, 1000);
        }
    }

    private Bundle mBundle;
    private QuestionFragment mQuestionFragment;
    private TextView mRemainingTimeView;
    private Timer mTimer;
    private static Handler msHandler = new Handler();

    private int mCategoryId;
    private int mQuestionId;
    private Question mQuestion;
    private List<Question> mQuestionList;
    private static User msUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mBundle = getIntent().getExtras();

        if (savedInstanceState == null) {
            String nickname = mBundle.getString(LoginActivity.NICKNAME);
            msUser.setmNickname(nickname);
        }

        mQuestionFragment = (QuestionFragment) getFragmentManager().findFragmentById(R.id.question_fragment);

        mRemainingTimeView = (TextView) mQuestionFragment.getView().findViewById(R.id.remaining_time);

        mCategoryId = mBundle.getInt(CategoryActivity.CLICKED_CATEGORY_POSITION);
        mQuestionId = mBundle.getInt(PointsActivity.CLICKED_QUESTION_POSITION);
        mQuestionList = Category.mCategoryList.get(mCategoryId).getmQuestionList();
        mQuestion = mQuestionList.get(mQuestionId);
        mTimer = new ActivityTimer(mQuestion.getmRemainingTime());
        mQuestionFragment.setmSelectedQuestion(mQuestion);
        mQuestionFragment.setmUser(msUser);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(QuestionActivity.this, PointsActivity.class);
        mTimer.setmRunning(false);
        msHandler.removeCallbacks(mTimer);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mQuestion.getmHasBeenAnswered()) {
            mTimer.setmRunning(true);
            msHandler.post(mTimer);
        } else {
            TextView tv = (TextView) findViewById(R.id.remaining_time_label);
            tv.setVisibility(View.GONE);
            if (mQuestion.getmAnsweredCorrectly()) {
                mRemainingTimeView.setText("Correct");
                mRemainingTimeView.setBackground(getDrawable(R.drawable.score_button_green));
            } else if (mQuestion.getmAnsweredWrong()) {
                mRemainingTimeView.setText("Wrong");
                mRemainingTimeView.setBackground(getDrawable(R.drawable.score_button_red));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimer.setmRunning(false);
        msHandler.removeCallbacks(mTimer);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTimer.setmRunning(false);
        msHandler.removeCallbacks(mTimer);
    }

    @Override
    public void answerClicked(View v, String clickedAnswer, boolean answerCorrect) {
        mQuestion.setmHasBeenAnswered(true);
        mQuestion.setmClickedAnswer(clickedAnswer);
        Log.d("clicked_answer", clickedAnswer);
        mTimer.setmRunning(false);
        msHandler.removeCallbacks(mTimer);

        ListView answers = (ListView) v.getParent();
        int answer_button_count = answers.getChildCount();
        for (int i = 0; i < answer_button_count; i++) {
            answers.getChildAt(i).setOnClickListener(null);
        }

        if (answerCorrect) {
            mQuestion.setmAnsweredCorrectly(true);
            msUser.answeredCorrectly(mQuestion.getmScore());
            v.setBackground(getDrawable(R.drawable.rounded_button_green));
        } else {
            mQuestion.setmAnsweredWrong(true);
            msUser.answeredWrong(mQuestion.getmScore());
            v.setBackground(getDrawable(R.drawable.rounded_button_red));
        }

        goNext();
    }

    public void goNext() {
        int quizFinished = isAllQuestionsAnswered(mQuestionId);
        if (quizFinished != -1) {
            mQuestionId = quizFinished;
            Log.d("quiz finished", String.valueOf(quizFinished));
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
