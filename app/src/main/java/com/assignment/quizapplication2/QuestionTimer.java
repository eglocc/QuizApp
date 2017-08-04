package com.assignment.quizapplication2;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import static com.assignment.quizapplication2.LoginActivity.sUser;

public class QuestionTimer extends Timer {

    private Context mContext;
    private Handler mHandler;
    private TextView mRemainingTimeView;
    private Question mSelectedQuestion;
    private TextView mScoreView;
    private ListView mAnswerListView;

    public QuestionTimer(Context c, Question question, Handler handler) {
        this.mContext = c;
        this.mSelectedQuestion = question;
        this.mHandler = handler;
    }

    public void setmRemainingTimeView(TextView tv) {
        this.mRemainingTimeView = tv;
    }

    public void setmScoreView(TextView tv) {
        this.mScoreView = tv;
    }

    public void setmAnswerListView(ListView lv) {
        this.mAnswerListView = lv;
    }

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
            mRemainingTimeView.setText(mContext.getResources().getString(R.string.times_up));
            Button button = (Button) mAnswerListView.findViewWithTag("true_answer");
            if (button != null)
                button.setBackground(mContext.getDrawable(R.drawable.rounded_button_green));
            mSelectedQuestion.setmRanOutOfTime(true);
            mSelectedQuestion.setmHasBeenAnswered(true);
            setmRunning(false);
            mHandler.removeCallbacks(this);
        }
    }
}
