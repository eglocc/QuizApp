package com.assignment.quizapplication2;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Question implements Parcelable, Comparable {

    private static final int mTimeLimit = 15;

    private String mCategory;
    private String mText;
    private int mQuestionId;
    private int mScore;
    private HashMap<String, Boolean> mAnswerMap;
    private int mRemainingTime;
    private boolean mAnsweredCorrectly;
    private boolean mAnsweredWrong;
    private boolean mHasBeenAnswered;
    private boolean mRanOutOfTime;
    private String mClickedAnswer;


    public Question(String text, int score, String category, HashMap<String, Boolean> answers, int id) {
        this.mText = text;
        this.mScore = score;
        this.mCategory = category;
        this.mAnswerMap = answers;
        this.mRemainingTime = mTimeLimit;
        this.mQuestionId = id;
    }

    public Question(String text, int score, String category, HashMap<String, Boolean> answers) {
        this.mText = text;
        this.mScore = score;
        this.mCategory = category;
        this.mAnswerMap = answers;
        this.mRemainingTime = mTimeLimit;
    }

    public Question(String text, int score, String category, int id) {
        this.mText = text;
        this.mScore = score;
        this.mCategory = category;
        this.mRemainingTime = mTimeLimit;
        this.mQuestionId = id;
    }

    private Question() {
    }

    private Question(Parcel source) {
        this.mText = source.readString();
        this.mQuestionId = source.readInt();
        this.mCategory = source.readString();
        this.mRemainingTime = source.readInt();
        this.mAnsweredCorrectly = source.readByte() != 0;
        this.mAnsweredWrong = source.readByte() != 0;
        this.mHasBeenAnswered = source.readByte() != 0;
        this.mRanOutOfTime = source.readByte() != 0;
        final int size = source.readInt();
        mAnswerMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            final String key = source.readString();
            final boolean value = source.readByte() != 0;
            mAnswerMap.put(key, value);
        }
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

    public int getmQuestionId() {
        return mQuestionId;
    }

    public void setmId(int id) {
        this.mQuestionId = id;
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

    public boolean getmHasBeenAnswered() {
        return mHasBeenAnswered;
    }

    public void setmHasBeenAnswered(boolean answered) {
        mHasBeenAnswered = answered;
    }

    public String getmClickedAnswer() {
        return mClickedAnswer;
    }

    public void setmClickedAnswer(String clicked) {
        this.mClickedAnswer = clicked;
    }

    public boolean getmRanOutOfTime() {
        return mRanOutOfTime;
    }

    public void setmRanOutOfTime(boolean mRanOutOfTime) {
        this.mRanOutOfTime = mRanOutOfTime;
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
        dest.writeInt(mQuestionId);
        dest.writeInt(mRemainingTime);
        dest.writeByte((byte) (mAnsweredCorrectly ? 1 : 0));
        dest.writeByte((byte) (mAnsweredWrong ? 1 : 0));
        dest.writeByte((byte) (mHasBeenAnswered ? 1 : 0));
        dest.writeByte((byte) (mRanOutOfTime ? 1 : 0));
        dest.writeInt(mAnswerMap.size());
        for (Map.Entry<String, Boolean> entry : mAnswerMap.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeByte((byte) (entry.getValue() ? 1 : 0));
        }
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Question q = (Question) o;
        if (this.mQuestionId == q.mQuestionId)
            return 0;
        else
            return this.mQuestionId > q.mQuestionId ? 1 : -1;
    }
}
