package com.assignment.quizapplication2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Quiz implements Parcelable {

    private List<Question> myQuestionBank;
    private boolean mQuizRunning;
    private int mCurrentIndex;

    public Quiz() {
        myQuestionBank = new ArrayList<>();
        mCurrentIndex = 0;
    }

    private Quiz(Parcel source) {
        myQuestionBank = new ArrayList<Question>();
        source.readList(myQuestionBank, Question.class.getClassLoader());
        this.mQuizRunning = source.readByte() != 0;
        this.mCurrentIndex = source.readInt();
    }

    public ArrayList<Question> getMyQuestionBank() {
        return (ArrayList<Question>) myQuestionBank;
    }

    public void setMyQuestionBank(ArrayList<Question> questionBank) {
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
