package com.assignment.quizapplication2;

public abstract class Timer implements Runnable {

    private int mSeconds;
    private boolean mRunning;

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
}

