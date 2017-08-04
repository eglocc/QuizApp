package com.assignment.quizapplication2;

public abstract class Timer implements Runnable {

    private int mSeconds;
    private boolean mRunning;
    private boolean mRanOutOfTime;

    public Timer(int seconds) {
        this.mSeconds = seconds;
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

    /*
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
            mTimeView.setBackground(mContext.getDrawable(R.drawable.score_button_red));
            mTimeView.setText(mContext.getResources().getString(R.string.times_up));
        }
        if (!mRanOutOfTime)
            mHandler.postDelayed(this, 1000);
    }*/
}

