package com.assignment.quizapplication2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.concurrent.CopyOnWriteArrayList;

public class Quiz implements Parcelable {

    private CopyOnWriteArrayList<Question> myQuestionBank;
    private boolean mQuizRunning;
    private int mCurrentIndex;

    public Quiz() {
        myQuestionBank = new CopyOnWriteArrayList<>();
        mCurrentIndex = 0;
    }

    private Quiz(Parcel source) {
        myQuestionBank = new CopyOnWriteArrayList<>();
        source.readList(myQuestionBank, Question.class.getClassLoader());
        this.mQuizRunning = source.readByte() != 0;
        this.mCurrentIndex = source.readInt();
    }

    public CopyOnWriteArrayList<Question> getMyQuestionBank() {
        return myQuestionBank;
    }

    public void setMyQuestionBank(CopyOnWriteArrayList<Question> questionBank) {
        this.myQuestionBank = questionBank;
    }

    public boolean getmQuizRunning() {
        return mQuizRunning;
    }

    public void setmQuizRunning(boolean mQuizRunning) {
        this.mQuizRunning = mQuizRunning;
    }

    public int getmCurrentIndex() {
        return mCurrentIndex;
    }

    public void setmCurrentIndex(int index) {
        this.mCurrentIndex = index;
    }

    public void incrementmCurrentIndex() {
        mCurrentIndex++;
    }

    public void decrementmCurrentIndex() {
        mCurrentIndex--;
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Quiz(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(myQuestionBank);
        dest.writeInt((byte) (mQuizRunning ? 1 : 0));
        dest.writeInt(mCurrentIndex);
    }
}
