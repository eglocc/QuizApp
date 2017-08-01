package com.assignment.quizapplication2;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    final private int mWrongAnswerScoreDeduction = 20;
    final private int mWrongAnswerScoreIncrease = 100;

    private String mNickname;
    private int mScore;

    public User(){
        this.mNickname = null;
        this.mScore = 0;
    }

    private User(Parcel source) {
        this.mNickname = source.readString();
        this.mScore = source.readInt();
    }

    public String getmNickname() {
        return mNickname;
    }

    public void setmNickname(String mNickname) {
        this.mNickname = mNickname;
    }

    public int getmScore() {
        return mScore;
    }

    public void setmScore(int mScore) {
        this.mScore = mScore;
    }

    public void answeredCorrectly() {
        this.mScore += mWrongAnswerScoreIncrease;
    }

    public void answeredWrong() {
        this.mScore -= mWrongAnswerScoreDeduction;
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mNickname);
        dest.writeInt(mScore);
    }
}
