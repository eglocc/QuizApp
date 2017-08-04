package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import static com.assignment.quizapplication2.LoginActivity.sUser;

public class QuestionFragment extends Fragment {

    static interface AnswerListener {
        void answerClicked(View v, String clickedAnswer, boolean answerCorrect);
    }

    public class ActivityTimer extends Timer {

        @Override
        public void run() {
            int seconds = mSelectedQuestion.getmRemainingTime();
            boolean running = getmRunning();
            String time = String.format("00:%02d", seconds);
            if (running && seconds > 0) {
                Log.d("sec", String.valueOf(seconds));
                mRemainingTimeView.setText(time);
                decrementmSeconds();
                mSelectedQuestion.decrementmRemainingTime();
                mHandler.postDelayed(this, 1000);
            } else if (seconds <= 0) {
                if (!mSelectedQuestion.getmHasBeenAnswered()) {
                    sUser.answeredWrong(mSelectedQuestion.getmScore());
                    mScoreView.setText(String.valueOf(sUser.getmScore()));
                }
                mRemainingTimeView.setBackground(mContext.getDrawable(R.drawable.score_button_red));
                mRemainingTimeView.setText(getResources().getString(R.string.times_up));
                ListView answerList = (ListView) getView().findViewById(R.id.answer_list_view);
                Button button = (Button) answerList.findViewWithTag("true_answer");
                if (button != null)
                    button.setBackground(mContext.getDrawable(R.drawable.rounded_button_green));
                mSelectedQuestion.setmRanOutOfTime(true);
                mSelectedQuestion.setmHasBeenAnswered(true);
                setmRunning(false);
                mHandler.removeCallbacks(this);
            }
        }
    }

    private Context mContext;
    private AnswerListener mListener;
    private Timer mTimer;
    private Handler mHandler;

    private TextView mRemainingTimeView;
    private TextView mRemainingTimeLabel;
    private TextView mScoreView;
    private TextView mNicknameView;
    private TextView mQuestionTextView;
    private ListView mAnswerListView;

    private Question mSelectedQuestion;

    public void setmSelectedQuestion(Question question) {
        mSelectedQuestion = question;
    }

    public void setmTimer(Timer timer) {
        mTimer = timer;
    }

    public void setmHandler(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        this.mListener = (AnswerListener) mContext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {

            mRemainingTimeView = (TextView) view.findViewById(R.id.remaining_time);
            mRemainingTimeLabel = (TextView) view.findViewById(R.id.remaining_time_label);
            mScoreView = (TextView) view.findViewById(R.id.score);

            mNicknameView = (TextView) view.findViewById(R.id.nickname);
            mQuestionTextView = (TextView) view.findViewById(R.id.question_text);
            mAnswerListView = (ListView) view.findViewById(R.id.answer_list_view);

            mNicknameView.setText(sUser.getmNickname());
            mScoreView.setText(String.valueOf(sUser.getmScore()));
            mQuestionTextView.setText(mSelectedQuestion.getmText());

            mAnswerListView.setAdapter(new AnswerButtonAdapter(mContext, mSelectedQuestion, mListener));

            if (mSelectedQuestion.getmHasBeenAnswered()) {
                mRemainingTimeLabel.setVisibility(View.GONE);
                //mRemainingTimeView.settra
                if (mSelectedQuestion.getmAnsweredCorrectly()) {
                    mRemainingTimeView.setText("Correct");
                    mRemainingTimeView.setBackground(mContext.getDrawable(R.drawable.score_button_green));
                } else if (mSelectedQuestion.getmAnsweredWrong()) {
                    mRemainingTimeView.setText("Wrong");
                    mRemainingTimeView.setBackground(mContext.getDrawable(R.drawable.score_button_red));
                } else if (mSelectedQuestion.getmRanOutOfTime()) {
                    mRemainingTimeView.setBackground(mContext.getDrawable(R.drawable.score_button_red));
                    mRemainingTimeView.setText(getResources().getString(R.string.times_up));
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mSelectedQuestion.getmHasBeenAnswered()) {
            mTimer.setmRunning(true);
            mHandler.post(mTimer);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mTimer.setmRunning(false);
        mHandler.removeCallbacks(mTimer);
    }
}
