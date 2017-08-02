package com.assignment.quizapplication2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Question implements Parcelable {

    static final int mTimeLimit = 15;
    static final int mNumberOfAnswers = 4;

    private String mText;
    private int mNumber;
    private List<Answer> mAnswerSet;
    private Answer mAnswerTrue;
    private int mRemainingTime;
    private boolean mAnsweredCorrectly;
    private boolean mAnsweredWrong;

    public Question(String text, int no) {
        this.mText = text;
        this.mNumber = no;
        this.mAnswerSet = new ArrayList<>();
        this.mRemainingTime = mTimeLimit;
    }

    private Question() {
    }

    private Question(Parcel source) {
        this.mText = source.readString();
        this.mNumber = source.readInt();
        mAnswerSet = new ArrayList<Answer>();
        source.readList(mAnswerSet, Answer.class.getClassLoader());
        this.mAnswerTrue = source.readParcelable(Answer.class.getClassLoader());
        this.mRemainingTime = source.readInt();
        this.mAnsweredCorrectly = source.readByte() != 0;
        this.mAnsweredWrong = source.readByte() != 0;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public ArrayList<Answer> getmAnswerSet() {
        return (ArrayList<Answer>) mAnswerSet;
    }

    public void setmAnswerSet(ArrayList<Answer> mAnswerSet) {
        this.mAnswerSet = mAnswerSet;
    }

    public Answer getmAnswerTrue() {
        return mAnswerTrue;
    }

    public void setmAnswerTrue(Answer mAnswerTrue) {
        this.mAnswerTrue = mAnswerTrue;
    }

    public int getmNumber() {
        return mNumber;
    }

    public void setmNumber(int number) {
        this.mNumber = number;
    }

    public int getmRemainingTime() {
        return mRemainingTime;
    }

    public void setmRemainingTime(int time) {
        this.mRemainingTime = time;
    }

    public void decrementmRemainingTime() {
        this.mRemainingTime--;
    }

    public boolean getmAnsweredCorrectly() {
        return mAnsweredCorrectly;
    }

    public void setmAnsweredCorrectly(boolean correct) {
        this.mAnsweredCorrectly = correct;
    }

    public boolean getmAnsweredWrong() {
        return mAnsweredWrong;
    }

    public void setmAnsweredWrong(boolean wrong) {
        this.mAnsweredWrong = wrong;
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Question(source);
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
        dest.writeString(mText);
        dest.writeInt(mNumber);
        dest.writeList(mAnswerSet);
        dest.writeParcelable(mAnswerTrue, flags);
        dest.writeInt(mRemainingTime);
        dest.writeByte((byte) (mAnsweredCorrectly ? 1 : 0));
        dest.writeByte((byte) (mAnsweredWrong ? 1 : 0));
    }
}
