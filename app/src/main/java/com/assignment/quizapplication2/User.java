package com.assignment.quizapplication2;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String mName;
    private String mNickname;
    private int mScore;

    /*public User() {
        this.mNickname = "";
    }*/

    private User() {

    }

    public User(String name, String nickname) {
        this.mName = name;
        this.mNickname = nickname;
        this.mScore = 0;
    }

    public User(String name, String nickname, int score) {
        this.mName = name;
        this.mNickname = nickname;
        this.mScore = score;
    }

    private User(Parcel source) {
        this.mName = source.readString();
        this.mNickname = source.readString();
        this.mScore = source.readInt();
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String name) {
        this.mName = name;
    }

    public String getmNickname() {
        return mNickname;
    }

    public void setmNickname(String nickname) {
        this.mNickname = nickname;
    }

    public int getmScore() {
        return mScore;
    }

    public void setmScore(int score) {
        this.mScore = score;
    }

    public void answeredCorrectly(int won) {
        this.mScore += won;
    }

    public void answeredWrong(int lost) {
        this.mScore -= lost;
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
        dest.writeString(mName);
        dest.writeString(mNickname);
        dest.writeInt(mScore);
    }
}
