package com.assignment.quizapplication2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Question implements Parcelable {

    static final int mTimeLimit = 15;
    static final int mNumberOfAnswers = 4;

    private String mCategory;
    private String mText;
    private int mId;
    private int mScore;
    private HashMap<String, Boolean> mAnswerMap;
    private int mRemainingTime;
    private boolean mAnsweredCorrectly;
    private boolean mAnsweredWrong;

    public Question(String text, int score, String category, HashMap<String, Boolean> answers) {
        this.mText = text;
        this.mScore = score;
        this.mCategory = category;
        this.mAnswerMap = answers;
        this.mRemainingTime = mTimeLimit;
    }

    private Question() {
    }

    private Question(Parcel source) {
        this.mText = source.readString();
        this.mId = source.readInt();
        this.mCategory = source.readString();
        this.mAnswerMap = source.readHashMap(null);
        this.mRemainingTime = source.readInt();
        this.mAnsweredCorrectly = source.readByte() != 0;
        this.mAnsweredWrong = source.readByte() != 0;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String category) {
        this.mCategory = category;
    }

    public int getmScore() {
        return mScore;
    }

    public void setmScore(int score) {
        this.mScore = score;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String text) {
        mText = text;
    }

    public HashMap<String, Boolean> getmAnswerMap() {
        return mAnswerMap;
    }

    public void setmAnswerMap(HashMap<String, Boolean> mAnswerMap) {
        this.mAnswerMap = mAnswerMap;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int id) {
        this.mId = id;
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
        dest.writeInt(mId);
        dest.writeMap(mAnswerMap);
        dest.writeInt(mRemainingTime);
        dest.writeByte((byte) (mAnsweredCorrectly ? 1 : 0));
        dest.writeByte((byte) (mAnsweredWrong ? 1 : 0));
    }
}
