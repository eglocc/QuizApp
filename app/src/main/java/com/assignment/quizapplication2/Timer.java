package com.assignment.quizapplication2;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

public class Timer implements Runnable {

    private int mSeconds;
    private boolean mRunning;
    private boolean mRanOutOfTime;

    private Context mContext;
    private TextView mTimeView;
    private Handler mHandler;
    private Question mQuestion;
    private User mUser;

    public Timer(Context c, User user, Question question, TextView tv, Handler handler) {
        this.mSeconds = question.getmRemainingTime();
        this.mContext = c;
        this.mUser = user;
        this.mQuestion = question;
        this.mHandler = handler;
        this.mTimeView = tv;
    }

    final public int getmSeconds() {
        return mSeconds;
    }

    final public boolean getmRunning() {
        return mRunning;
    }

    public void setmSeconds(int s) {
        this.mSeconds = s;
    }

    public void setmRunning(boolean r) {
        this.mRunning = r;
    }

    public void decrementmSeconds() {
        this.mSeconds--;
    }

    public void incrementmSeconds() {
        this.mSeconds++;
    }

    @Override
    public void run() {
        int seconds = getmSeconds();
        boolean running = getmRunning();
        String time = String.format("00:%02d", seconds);
        mTimeView.setText(time);
        if (running && seconds > 0) {
            decrementmSeconds();
            mQuestion.decrementmRemainingTime();
        } else if (seconds <= 0) {
            mRanOutOfTime = true;
            setmRunning(false);
            mUser.answeredWrong(mQuestion.getmScore());
            //goToNextQuestion();
        }
        if (!mRanOutOfTime)
            mHandler.postDelayed(this, 1000);
    }
}

